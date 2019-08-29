package com.example.misdaqia.View;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.misdaqia.Helper.MainFontButton;
import com.example.misdaqia.Helper.MainFontEdittext;
import com.example.misdaqia.Helper.MainFontTextview;
import com.example.misdaqia.Model.User;
import com.example.misdaqia.Model.registerUserResponse;
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
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private CircleImageView profileProfileImage;
    private MainFontEdittext edtname;
    private MainFontEdittext edtemail;
    private MainFontEdittext edtpassword;
    private MainFontEdittext edtPasswordVerified;
    private MainFontButton signBtn;
    private ImageView google;
    private LinearLayout facebookbtn;
    private LoginButton facebookloginbtn;
    ProgressDialog progressDialog;

    //for  sign with googlebtn
    int RC_SIGN_IN = 0;
    GoogleSignInClient mGoogleSignInClient;

    //for sign with facebookbtn
    private CallbackManager callbackManager;


    JsonPlaceHolderApi jsonPlaceHolderApi;

    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initView();


        jsonPlaceHolderApi = ApiClient.getApiClient().create(JsonPlaceHolderApi.class);

        //for googlebtn login
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(SignUpActivity.this);
        if (acct != null) {
            String personName = acct.getDisplayName();
//            String personGivenName = acct.getGivenName();
//            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();

            edtname.setText(personName);
            edtemail.setText(personEmail);
        }

        //for facebookbtn login
        callbackManager = CallbackManager.Factory.create();
        facebookloginbtn.setReadPermissions(Arrays.asList("email", "public_profile"));
        checkLoginStatus();


        initActions();

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    private void sign() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    private void initActions() {

        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = edtname.getText().toString();
                String email = edtemail.getText().toString();
                String password = edtpassword.getText().toString();
                String re_password = edtPasswordVerified.getText().toString();

                if (checkValidation() == false) {
                    return;
                } else {
                    createUser(name, email, password, re_password);

                }

            }
        });

        google.setOnClickListener(new View.OnClickListener() {
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

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    private void createUser(String name, String email, String password, String re_password) {

        progressDialog.show();

        Call<registerUserResponse> call = jsonPlaceHolderApi.createUser(name, email, password, re_password);

        call.enqueue(new Callback<registerUserResponse>() {
            @Override
            public void onResponse(Call<registerUserResponse> call, Response<registerUserResponse> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }


                if (response.code() == 500) {
                    Toast.makeText(SignUpActivity.this, "exist", Toast.LENGTH_SHORT).show();
                }

                registerUserResponse registerUserResponse = response.body();

                User users = registerUserResponse.getUserInfo();

                SharedPreferences.Editor edit = getSharedPreferences("data", Context.MODE_PRIVATE).edit();
                edit.putString("email", users.getEmail());
                edit.putString("password", users.getPassword());
                edit.commit();

                progressDialog.dismiss();

                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                finish();
            }


            @Override
            public void onFailure(Call<registerUserResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(SignUpActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(SignUpActivity.this, "خطأ فى اتصال بالانترنت !", Toast.LENGTH_SHORT).show();
            }
        });

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    public void backLogin(View view) {
        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    private void initView() {
        profileProfileImage = (CircleImageView) findViewById(R.id.profile_profile_image);
        edtname = (MainFontEdittext) findViewById(R.id.edtname);
        edtemail = (MainFontEdittext) findViewById(R.id.edtemail);
        edtpassword = (MainFontEdittext) findViewById(R.id.edtpassword);
        edtPasswordVerified = (MainFontEdittext) findViewById(R.id.edtverifiedpassword);
        signBtn = (MainFontButton) findViewById(R.id.signBtn);
        facebookbtn = (LinearLayout) findViewById(R.id.facebook);
        facebookloginbtn = (LoginButton) findViewById(R.id.loginbtn);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("انتظر ...");

        google = (ImageView) findViewById(R.id.google);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean checkValidation() {

        boolean flag = false;

        if (edtname.getText().toString().length() == 0) {
            edtname.setError("الاسم فارغ");
            edtname.requestFocus();
            flag = false;

        } else if (edtemail.getText().toString().length() == 0) {
            edtemail.setError("البريد الالكتروني فارغ");
            edtemail.requestFocus();
            flag = false;

        } else if (edtpassword.getText().toString().length() == 0) {
            edtpassword.setError("كلمه المرور فارغه");
            edtpassword.requestFocus();
            flag = false;

        } else if (edtPasswordVerified.getText().toString().length() == 0) {
            edtPasswordVerified.setError("كلمه المرور فارغه");
            edtPasswordVerified.requestFocus();
            flag = false;

        } else if (!edtPasswordVerified.getText().toString().equals(edtpassword.getText().toString())) {
            edtPasswordVerified.setError("كلمه المرور غير مطابقه");
            edtPasswordVerified.requestFocus();
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

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, SignInActivity.class));
        finish();
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

    /////////////////////////////////
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            startActivity(new Intent(SignUpActivity.this, SignUpActivity.class));
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //facebook login
    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                edtemail.setText("");
                Toast.makeText(SignUpActivity.this, "تم تسجيل الخروج", Toast.LENGTH_LONG).show();
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

