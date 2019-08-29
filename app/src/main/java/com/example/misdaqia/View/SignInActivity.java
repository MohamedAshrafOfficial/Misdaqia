package com.example.misdaqia.View;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.misdaqia.Helper.MainFontButton;
import com.example.misdaqia.Helper.MainFontEdittext;
import com.example.misdaqia.Model.LoginUserResponse;
import com.example.misdaqia.R;
import com.example.misdaqia.Services.ApiClient;
import com.example.misdaqia.Services.JsonPlaceHolderApi;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {


    private MainFontEdittext edtemail;
    private MainFontEdittext edtpassword;
    private MainFontButton btnlogin;
    private ImageView googlebtn;
    private LinearLayout facebookbtn;
    private LoginButton facebookloginbtn;
    ProgressDialog progressDialog;


    JsonPlaceHolderApi jsonPlaceHolderApi;

    //for  sign with googlebtn
    int RC_SIGN_IN = 0;
    GoogleSignInClient mGoogleSignInClient;

    //for sign with facebookbtn
    private CallbackManager callbackManager;

    private static final String TAG = "SignInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        initView();


        jsonPlaceHolderApi = ApiClient.getApiClient().create(JsonPlaceHolderApi.class);


        //for googlebtn login
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(SignInActivity.this);
        if (acct != null) {
            String personEmail = acct.getEmail();

            edtemail.setText(personEmail);
        }

        //for facebookbtn login
        callbackManager = CallbackManager.Factory.create();
        facebookloginbtn.setReadPermissions(Arrays.asList("email", "public_profile"));
        checkLoginStatus();



        initActios();

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    private void sign() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    private void initActios() {

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = edtemail.getText().toString();
                String password = edtpassword.getText().toString();

                if (checkValidation() == false) {
                    return;
                } else {
                    loginUser(email, password);

                }
            }
        });

        googlebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sign();
            }
        });

        facebookloginbtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        facebookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebookloginbtn.performClick();
            }
        });

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    private void loginUser(String email, final String password) {

        progressDialog.show();

        Call<LoginUserResponse> call = jsonPlaceHolderApi.loginUser(email, password);


        call.enqueue(new Callback<LoginUserResponse>() {
            @Override
            public void onResponse(Call<LoginUserResponse> call, Response<LoginUserResponse> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(SignInActivity.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                    if (response.code() == 401) {
                        Toast.makeText(SignInActivity.this, "البريد الكتروني او الرقم سري غير صحيح", Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {


                    LoginUserResponse loginUserResponse = response.body();

                    SharedPreferences.Editor edit = getSharedPreferences("data", Context.MODE_PRIVATE).edit();
                    edit.putString("email", loginUserResponse.getEmail());
                    edit.putString("password", loginUserResponse.getPassword());
                    edit.commit();

                    progressDialog.dismiss();


                    startActivity(new Intent(SignInActivity.this, MainActivity.class));


                }


            }


            @Override
            public void onFailure(Call<LoginUserResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(SignInActivity.this, "خطأ فى اتصال بالانترنت !", Toast.LENGTH_SHORT).show();

            }
        });
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    public void goSignup(View view) {
        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    private void initView() {
        edtemail = (MainFontEdittext) findViewById(R.id.edtemail);
        edtpassword = (MainFontEdittext) findViewById(R.id.edtpassword);
        btnlogin = (MainFontButton) findViewById(R.id.btnlogin);
        googlebtn = (ImageView) findViewById(R.id.google);
        facebookbtn = (LinearLayout) findViewById(R.id.facebook);
        facebookloginbtn = (LoginButton) findViewById(R.id.loginbtn);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("انتظر ...");
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean checkValidation() {

        boolean flag = false;

        if (edtemail.getText().toString().length() == 0) {
            edtemail.setError("البريد الالكتروني فارغ");
            edtemail.requestFocus();
            flag = false;

        } else if (edtpassword.getText().toString().length() == 0) {
            edtpassword.setError("كلمه المرور فارغه");
            edtpassword.requestFocus();
            flag = false;

        } else if (!Patterns.EMAIL_ADDRESS.matcher(edtemail.getText().toString().trim()).matches()) {
            edtemail.setError("لبريد الالكتروني غير صحيح");
            edtemail.requestFocus();
            flag = false;
        } else {
            flag = true;
        }

        return flag;

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("خروج")
                .setMessage("هل انت متاكد من الخروج!")
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                    }

                })
                .setNegativeButton("لا", null)
                .show();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //facebooklogin
        callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    ///////////////////////////////////////////////
    //for google login
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            startActivity(new Intent(SignInActivity.this, SignInActivity.class));

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(SignInActivity.this, "Failed", Toast.LENGTH_LONG).show();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //facebook login
    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                edtemail.setText("");
                Toast.makeText(SignInActivity.this, "تم تسجيل الخروج", Toast.LENGTH_LONG).show();
            } else
                loadUserProfile(currentAccessToken);
        }
    };

    ////////////////////////////////
    private void loadUserProfile(AccessToken newAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
//                    String first_name = object.getString("first_name");
//                    String last_name = object.getString("last_name");
                    String email = object.getString("email");
//                    String id = object.getString("id");
//                    String image_url = "https://graph.facebook.com/"+id+ "/picture?type=normal";

                    edtemail.setText(email);
//                    txtName.setText(first_name +" "+last_name);

//                    RequestOptions requestOptions = new RequestOptions();
//                    requestOptions.dontAnimate();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }

    ///////////////////
    private void checkLoginStatus() {
        if (AccessToken.getCurrentAccessToken() != null) {
            loadUserProfile(AccessToken.getCurrentAccessToken());
        }
    }


}
