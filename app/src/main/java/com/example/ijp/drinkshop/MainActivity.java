package com.example.ijp.drinkshop;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.PatternMatcher;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ijp.drinkshop.Model.CheckUserResponse;
import com.example.ijp.drinkshop.Model.User;
import com.example.ijp.drinkshop.Retrofit.IDrinkShopAPI;
import com.example.ijp.drinkshop.Utils.Common;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.szagurskii.patternedtextwatcher.PatternedTextWatcher;

import java.security.MessageDigest;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1000;
    private static final int REQUEST_PERMISSION = 1001;
    Button btn_continue;

    IDrinkShopAPI mService;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case REQUEST_PERMISSION:
            {
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            },REQUEST_PERMISSION);

        mService= Common.getAPI();

        btn_continue=findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginPage(LoginType.PHONE);
            }
        });

        // Check Session
        if(AccountKit.getCurrentAccessToken()!=null)
        {
            final android.app.AlertDialog alertDialog=new SpotsDialog(MainActivity.this);
            alertDialog.show();
            alertDialog.setMessage("Please Wait...");
            // Auto Login


            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(final Account account) {

                    mService.checkUserExists(account.getPhoneNumber().toString())
                            .enqueue(new Callback<CheckUserResponse>() {

                                @Override
                                public void onResponse(Call<CheckUserResponse> call, Response<CheckUserResponse> response) {
                                    CheckUserResponse userResponse=response.body();
                                    if(userResponse.isExists()){
                                        // Fetch Information
                                        mService.getUserInformation(account.getPhoneNumber().toString())
                                                .enqueue(new Callback<User>() {
                                                    @Override
                                                    public void onResponse(Call<User> call, Response<User> response) {
                                                        // If user already exists just start new Activity
                                                        alertDialog.dismiss();

                                                        Common.currentUser=response.body();

                                                        startActivity(new Intent(MainActivity.this,HomeActivity.class));
                                                        finish();  // Closes MainActivity
                                                    }

                                                    @Override
                                                    public void onFailure(Call<User> call, Throwable t) {
                                                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                                                    }
                                                });


                                    }else {
                                        // need register
                                        alertDialog.dismiss();

                                        showRegisterDialog(account.getPhoneNumber().toString());
                                    }
                                }

                                @Override
                                public void onFailure(Call<CheckUserResponse> call, Throwable t) {

                                }
                            });
                }

                @Override
                public void onError(AccountKitError accountKitError) {

                    Log.i("ERROR",accountKitError.getErrorType().getMessage());

                }
            });
        }

    }

    private void startLoginPage(LoginType loginType) {

        Intent intent=new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder builder=
                new AccountKitConfiguration.AccountKitConfigurationBuilder(loginType,
                        AccountKitActivity.ResponseType.TOKEN);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,builder.build());
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_CODE){
            AccountKitLoginResult result=data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);

            if(result.getError()!=null){
                Toast.makeText(this, ""+result.getError().getErrorType(), Toast.LENGTH_SHORT).show();
            }else if(result.wasCancelled()){
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            }
            else {
                if(result.getAccessToken()!=null){
                    final android.app.AlertDialog alertDialog=new SpotsDialog(MainActivity.this);
                    alertDialog.show();
                    alertDialog.setMessage("Please Wait...");

                    //Get user phone and check it exists on server

                    AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                        @Override
                        public void onSuccess(final Account account) {

                            mService.checkUserExists(account.getPhoneNumber().toString())
                                    .enqueue(new Callback<CheckUserResponse>() {

                                        @Override
                                        public void onResponse(Call<CheckUserResponse> call, Response<CheckUserResponse> response) {
                                            CheckUserResponse userResponse=response.body();
                                            if(userResponse.isExists()){
                                                // Fetch Information
                                                mService.getUserInformation(account.getPhoneNumber().toString())
                                                        .enqueue(new Callback<User>() {
                                                            @Override
                                                            public void onResponse(Call<User> call, Response<User> response) {
                                                                // If user already exists just start new Activity
                                                                alertDialog.dismiss();

                                                                Common.currentUser=response.body();

                                                                startActivity(new Intent(MainActivity.this,HomeActivity.class));
                                                                finish();  // Closes MainActivity
                                                            }

                                                            @Override
                                                            public void onFailure(Call<User> call, Throwable t) {
                                                                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                                                            }
                                                        });


                                            }else {
                                                // need register
                                                alertDialog.dismiss();

                                                showRegisterDialog(account.getPhoneNumber().toString());
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<CheckUserResponse> call, Throwable t) {

                                        }
                                    });
                        }

                        @Override
                        public void onError(AccountKitError accountKitError) {

                            Log.i("ERROR",accountKitError.getErrorType().getMessage());

                        }
                    });
                }
            }
        }
    }

    private void showRegisterDialog(final String phone) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("REGISTER");


        LayoutInflater inflater=this.getLayoutInflater();
        View register_layout=inflater.inflate(R.layout.register_layout,null);

        final MaterialEditText edt_name=register_layout.findViewById(R.id.edt_name);
        final MaterialEditText edt_add=register_layout.findViewById(R.id.edt_address);
        final MaterialEditText edt_dob=register_layout.findViewById(R.id.edt_birthdate);

        Button btn_reg= register_layout.findViewById(R.id.btn_register);

        edt_dob.addTextChangedListener(new PatternedTextWatcher("####-##-##"));

        builder.setView(register_layout);
        final AlertDialog dialog=builder.create();


        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                if(TextUtils.isEmpty(edt_add.getText().toString())){
                    Toast.makeText(MainActivity.this, "Please Enter Your Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(edt_dob.getText().toString())){
                    Toast.makeText(MainActivity.this, "Please Enter Your DOB", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(edt_name.getText().toString())){
                    Toast.makeText(MainActivity.this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                final android.app.AlertDialog waitingDialog=new SpotsDialog(MainActivity.this);
                waitingDialog.show();
                waitingDialog.setMessage("Please wait...");

                mService.registerNewUser(phone,
                        edt_name.getText().toString(),
                        edt_add.getText().toString(),
                        edt_dob.getText().toString())
                        .enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                waitingDialog.dismiss();
                                User user=response.body();

                                if(TextUtils.isEmpty(user.getErr_msg())){
                                    Toast.makeText(MainActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                    Common.currentUser=response.body();
                                    // Start new Activity
                                    startActivity(new Intent(MainActivity.this,HomeActivity.class));
                                    finish();

                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                waitingDialog.dismiss();

                            }
                        });
            }
        });

        dialog.show();
    }

    // Provides Key Hash
    private void printKeyHash() {
        try{
            PackageInfo info=getPackageManager().getPackageInfo("com.example.ijp.drinkshop", PackageManager.GET_SIGNATURES);
            for(Signature signature:info.signatures){
                MessageDigest md=MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KEYHASH", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // Exit App wen back button pressed
    boolean isBackButtonClicked=false;

    @Override
    public void onBackPressed()
    {

        if (isBackButtonClicked) {
            super.onBackPressed();
            return;
        }
        this.isBackButtonClicked=true;
        Toast.makeText(this, "Please Click Again To Exit", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        isBackButtonClicked=false;
        super.onResume();
    }
}
