package com.example.steganography.login;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.example.steganography.R;
import com.example.steganography.base.BaseViewModel;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginViewModel extends BaseViewModel<LoginNavigator> {
    private static int RC_SIGN_IN = 1;
    public ObservableField<String> userPassword = new ObservableField<>("");
    public ObservableField<Boolean> invalidUserName = new ObservableField<>(false);
    public ObservableField<String> userEmail = new ObservableField<>("");
    public ObservableField<Boolean> invalidUserEmail = new ObservableField<>(false);
    public MutableLiveData<FirebaseUser> authUser = new MutableLiveData<>();
    public ObservableField<Boolean> progress_bar = new ObservableField<>(false);
    public FirebaseAuth auth;
    public LoginManager loginManger;
    public CallbackManager callbackManger;
    public GoogleSignInClient googleSingInClient;

    public LoginViewModel(@NonNull Application application) {
        super(application);


        auth = FirebaseAuth.getInstance();

        loginManger = LoginManager.getInstance();
        callbackManger = CallbackManager.Factory.create();
    }

    public void login() {
        Log.e("reg", "in login");
        Log.e("reg", "in login" + userEmail.get());
        Log.e("reg", "in login" + userPassword.get());

        progress_bar.set(true);
        auth.signInWithEmailAndPassword(userEmail.get(), userPassword.get())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progress_bar.set(false);
                            goToHomeActivity();
                        } else {
                            Log.e("reg", "not  " + task.getException().getLocalizedMessage());
                        }
                    }


                });


    }


    private void goToHomeActivity() {
        navigator.openHomeActivity();
    }


    public void loginWithFacebook(Activity activity) {
        Log.e("dddddd", "in face login");


        loginManger.logInWithReadPermissions(activity, Arrays.asList("email", "public_profile"));

        loginManger.registerCallback(callbackManger, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("dddddd", "in success");

                handelFacebookToken(loginResult.getAccessToken());


            }

            @Override
            public void onCancel() {
                progress_bar.set(false);
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("LoginScreen", "----onError: "
                        + error.getMessage());

            }
        });


    }

    public void disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/permissions/",
                null,
                HttpMethod.DELETE,
                new GraphRequest
                        .Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {
                        LoginManager.getInstance().logOut();
                    }
                })
                .executeAsync();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data, Activity activity) {
        callbackManger.onActivityResult(
                requestCode,
                resultCode,
                data);


        if (requestCode == RC_SIGN_IN) {
            Task task = GoogleSignIn.getSignedInAccountFromIntent(data);

            firebaseAuthWithGoogle(task, activity);

        }


    }


    public void loginWithGoogle(Activity activity) {
        progress_bar.set(true);
        GoogleSignInOptions googleSignInOption = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)

                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSingInClient = GoogleSignIn.getClient(activity, googleSignInOption);

        Intent intent = googleSingInClient.getSignInIntent();
        activity.startActivityForResult(intent, RC_SIGN_IN);

    }


    private void firebaseAuthWithGoogle(Task<GoogleSignInAccount> task, Activity activity) {


        try {

            GoogleSignInAccount account = task.getResult(ApiException.class);
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

            auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progress_bar.set(false);

                    if (task.isSuccessful()) {

                        FirebaseUser user = auth.getCurrentUser();
                        goToHomeActivity();
                        googleSingInClient.signOut();
                        //updateUI(user);
                    } else {

                        Log.e("dddd", "not");
                    }


                }
            });

        } catch (ApiException e) {
            // Google Sign In failed, update UI appropriately
            //Log.w(TAG, "Google sign in failed", e);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }


    }


    public void handelFacebookToken(AccessToken token) {


        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (object != null) {
                    try {

                        String name = object.getString("name");
                        String email = object.getString("email");
                        String fbUserID = object.getString("id");

                        goToHomeActivity();
                        disconnectFromFacebook();
                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString(
                "fields",
                "id, name, email, gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }


}

