package com.example.steganography.register;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.example.steganography.base.BaseViewModel;
import com.example.steganography.database.User;
import com.example.steganography.database.UserDao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterViewModel extends BaseViewModel<RegisterNavigator> {

    public ObservableField<String> userName = new ObservableField<>("");

    public ObservableField<Boolean> invalidUserName = new ObservableField<>(false);
    public ObservableField<String> userEmail = new ObservableField<>("");
    public ObservableField<Boolean> invalidUserEmail = new ObservableField<>(false);
    public ObservableField<String> userPassword = new ObservableField<>("");
    public ObservableField<Boolean> invalidUserPassword = new ObservableField<>();
    public ObservableField<String> confirmPassword = new ObservableField<>("");
    public ObservableField<Boolean> errorConfirmPassword = new ObservableField<>(false);
    public ObservableField<Boolean> progress_bar = new ObservableField<>(false);



   /*public ObservableField<String> getUserName() {
        return userName;


   }

    public void setUserName(ObservableField<String> userName) {
        this.userName = userName;
    }

    public ObservableField<Boolean> getInvalidUserName() {
        return invalidUserName;
    }

    public void setInvalidUserName(ObservableField<Boolean> invalidUserName) {
        this.invalidUserName = invalidUserName;
    }

    public ObservableField<String> getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(ObservableField<String> userEmail) {
        this.userEmail = userEmail;
    }

    public ObservableField<Boolean> getInvalidUserEmail() {
        return invalidUserEmail;
    }

    public void setInvalidUserEmail(ObservableField<Boolean> invalidUserEmail) {
        this.invalidUserEmail = invalidUserEmail;
    }

    public ObservableField<String> getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(ObservableField<String> userPassword) {
        this.userPassword = userPassword;
    }

    public ObservableField<Boolean> getInvalidUserPassword() {
        return invalidUserPassword;
    }

    public void setInvalidUserPassword(ObservableField<Boolean> invalidUserPassword) {
        this.invalidUserPassword = invalidUserPassword;
    }

    public ObservableField<String> getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(ObservableField<String> confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public ObservableField<Boolean> getErrorConfirmPassword() {
        return errorConfirmPassword;
    }

    public void setErrorConfirmPassword(ObservableField<Boolean> errorConfirmPassword) {
        this.errorConfirmPassword = errorConfirmPassword; }*/


 /*  public RegisterViewModel(ObservableField<String> userName, ObservableField<Boolean> invalidUserName,
                             ObservableField<String> userEmail, ObservableField<Boolean> invalidUserEmail,
                             ObservableField<String> userPassword, ObservableField<Boolean> invalidUserPassword,
                             ObservableField<String> confirmPassword, ObservableField<Boolean> errorConfirmPassword,
                             FirebaseAuth auth) {
        this.userName = userName;
        this.invalidUserName = invalidUserName;
        this.userEmail = userEmail;
        this.invalidUserEmail = invalidUserEmail;
        this.userPassword = userPassword;
        this.invalidUserPassword = invalidUserPassword;
        this.confirmPassword = confirmPassword;
        this.errorConfirmPassword = errorConfirmPassword;
        this.auth = auth;
    }*/

    public FirebaseAuth auth;

    public RegisterViewModel() {

        auth = FirebaseAuth.getInstance();
    }

    public static boolean isValidEmail(String str) {
        boolean isValid = false;
        if (Build.VERSION.SDK_INT >= 8) {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches();
        }
        String expression = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        CharSequence inputStr = str;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public void register() {
        Log.e("re", " in register function");


        if (validDataRegister() == true) {
            progress_bar.set(true);
            Log.e("reg", " valid ");

            auth.createUserWithEmailAndPassword(userEmail.get(), userPassword.get())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progress_bar.set(false);
                                Log.e("task", "create succsseful");
                                auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            Log.e("nnnnn", "email verhication");

                                            //  Toast.makeText(this, "please check email for verification.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            //Toast.makeText(this, task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                                            Log.e("nnnnn", "cant" + task.getException().getMessage());

                                        }
                                    }
                                });

                               /* if (auth.getCurrentUser().isEmailVerified()) {
                                    Log.e("nnnnn","email verhication");

                                    auth.getCurrentUser().sendEmailVerification();
                                    Log.e("nnnnn","email send");
                                } else {
                                    Log.e("invalid", "cant verification email");

                                }*/

                                User newUser = new User();
                                newUser.setId(task.getResult().getUser().getUid());
                                newUser.setUser_name(userName.get());
                                newUser.setUser_email(userEmail.get());
                                UserDao.addUser(newUser, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            openMain();
                                            Log.e("nnnnn", "use add to database");

                                        } else {
                                            Log.e("invalid", "not add");

                                        }
                                    }
                                });

                            } else {
                                Log.e("", "cant create method");
                            }

                        }
                    });

        } else {
            Log.e("invalid", "not valid");
        }


        Log.e("jjjjjjjj", "eeee");


    }

    public Boolean validDataRegister() {
        Log.e("reg", "usrName" + userName.get());

        Log.e("reg", "email" + userEmail.get());

        Log.e("reg", "password" + userPassword.get());

        Log.e("reg", "password" + confirmPassword.get());


        Boolean isValid = true;
        if (isValidUsername(userName.get())) {
            isValid = false;

            invalidUserName.set(true);
        }

        if (isValidEmail(userEmail.get())) {
            isValid = false;
            invalidUserEmail.set(true);
        }
        if (userPassword.get().isEmpty()) {
            isValid = false;

            invalidUserPassword.set(true);

        }
        if (!confirmPassword.get().equals(userPassword.get())) {
            isValid = false;
            errorConfirmPassword.set(true);
        }
        Log.e("reg", "validation==" + isValid);

        return isValid;

    }

    public boolean isValidUsername(String name) {
        String regex = "^[A-Za-z]\\w{5,10}$";
        //String regex="^[a-zA-Z0-9]+([_ -]?[a-zA-Z0-9])*$";
        Pattern p = Pattern.compile(regex);
        if (name == null) {
            return false;
        }
        Matcher m = p.matcher(name);
        return m.matches();
    }

    public boolean isValidPassword(final String password) {

        final String PASSWORD_PATTERN = "String = \"(?=.*[0-9a-zA-Z]).{6,}\"";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);

        if (password == null) {
            return false;
        }
        return matcher.matches();

    }


    public void openMain() {

        navigator.openHomeActivity();
    }


}









