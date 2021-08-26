package com.example.steganography.login;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.example.steganography.R;
import com.example.steganography.base.BaseViewModel;
import com.example.steganography.database.User;
import com.example.steganography.database.UserDao;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static android.service.controls.ControlsProviderService.TAG;
import static com.facebook.FacebookSdk.getApplicationContext;

public class LoginViewModel extends BaseViewModel<LoginNavigator> {
    private static int RC_SIGN_IN = 1;
    public ObservableField<String> userPassword = new ObservableField<>("");
    public ObservableField<String> userEmail = new ObservableField<>("");
    public ObservableField<String> textPasswordError = new ObservableField<>("");
    public ObservableField<String> textEmailError = new ObservableField<>("");

    public MutableLiveData<FirebaseUser> authUser = new MutableLiveData<>();
    public ObservableField<Boolean> progress_bar = new ObservableField<>(false);
    public FirebaseAuth auth;
    public LoginManager loginManger;
    public CallbackManager callbackManger;
    public GoogleSignInClient googleSingInClient;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    FirebaseException e;
    String error_message;


    public LoginViewModel(@NonNull Application application) {
        super(application);


        auth = FirebaseAuth.getInstance();
        preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = preferences.edit();






        loginManger = LoginManager.getInstance();
        callbackManger = CallbackManager.Factory.create();
        //  holdData();
        //pref = getApplicationContext().getSharedPreferences("DataHolder", 0);
        //editor = pref.edit();
    }


    public void login() {
        Log.e("reg", "in login");
        Log.e("reg", "in login" + userEmail.get());
        Log.e("reg", "in login" + userPassword.get());

        if (validDataToLogin()) {
            progress_bar.set(true);

            auth.signInWithEmailAndPassword(userEmail.get(), userPassword.get())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            if (task.isSuccessful()) {
                                FirebaseUser user = auth.getCurrentUser();

                                holdData();
                                progress_bar.set(false);
                                goToHomeActivity();
                            }
                            if (!task.isSuccessful()) {
                                progress_bar.set(false);
                                try {
                                    throw task.getException();
                                } catch (FirebaseNetworkException e) {
                                    error_message = "network error";
                                    showMessage();

                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    textPasswordError.set("invalid email or password");

                                    showMessage();

                                } catch (FirebaseAuthUserCollisionException e) {
                                    error_message = "in valid user";
                                    //mTxtEmail.setError(getString(R.string.error_user_exists));
                                    showMessage();

                                } catch (Exception e) {
                                    error_message = e.getMessage();
                                    showMessage();
                                }


                            }


                        }


                    });


        } else {


        }
    }


    public void restPassword() {
        if (!userEmail.get().toString().isEmpty()) {
            auth.sendPasswordResetEmail(userEmail.get().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email sent.");
                            }
                            if (!task.isSuccessful()) {
                                try {
                                    throw task.getException();
                                } catch (FirebaseNetworkException e) {
                                    error_message = "network error";
                                    showMessage();

                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    textPasswordError.set("invalid email or password");

                                    showMessage();

                                } catch (FirebaseAuthUserCollisionException e) {
                                    error_message = "in valid user";
                                    //mTxtEmail.setError(getString(R.string.error_user_exists));
                                    showMessage();

                                } catch (Exception e) {
                                    error_message = e.getMessage();
                                    showMessage();
                                }


                            }
                        }
                    });

        } else {

            error_message = "enter your email to rest password";
            showMessage();
        }


    }

    private void showMessage() {
        navigator.showDialogMessage();
    }

    private boolean validDataToLogin() {

        boolean isValid = true;
        if (validateEmail() == false) {
            isValid = false;

        }
        if (isValidPassword() == false) {
            isValid = false;


        }
        return isValid;
    }

    private boolean validateEmail() {
        if (userEmail.get().isEmpty()) {
            textEmailError.set("Email cant be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail.get()).matches()) {
            textEmailError.set("Please enter a valid email address");

            return false;
        } else {
            textEmailError.set("");
            return true;
        }
    }

    public boolean isValidPassword() {


        if (userPassword.get().isEmpty()) {
            textPasswordError.set("password can not be empty");
            return false;
        } else {
            textPasswordError.set("");
            return true;
        }


    }
    public void holdData() {

        UserDao.getUser(auth.getCurrentUser().getUid(), new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> documentSnapshot) {
                if (documentSnapshot.isSuccessful()) {
                    Log.e("message", " login susssssss");
                    FirebaseUser user = auth.getCurrentUser();

                    User dataBaseUser = documentSnapshot.getResult().toObject(User.class);
                    Log.e("message", "Zxcvbnm,./" + dataBaseUser.getUser_email());

                    editor.putString("userId", dataBaseUser.getId());
                    editor.putString("user_name", dataBaseUser.getUser_name());
                    editor.putString("user_email", dataBaseUser.getUser_email());
                    editor.apply();

                } else {
                    Log.e("message", "error" + documentSnapshot.getException().getLocalizedMessage());

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
                        //  LoginManager.getInstance().logOut();
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

    public void logout() {

        auth.signOut();
        // googleSingInClient.signOut();
        // loginManger.logOut();


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

                        editor.putString("userId", user.getUid());
                        editor.putString("user_name", user.getDisplayName());
                        editor.putString("user_email", user.getEmail());
                        editor.apply();
                        goToHomeActivity();
                        googleSingInClient.signOut();

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


                        editor.putString("userId", fbUserID);
                        editor.putString("user_name", name);
                        editor.putString("user_email", email);
                        editor.apply();

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

