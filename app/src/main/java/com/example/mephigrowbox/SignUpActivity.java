package com.example.mephigrowbox;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    @VisibleForTesting
    private ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mUsernameField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mEmailField = findViewById(R.id.fieldEmailSignUp);
        mPasswordField = findViewById(R.id.fieldPasswordSignUp);
        mUsernameField = findViewById(R.id.fieldUsername);
        Button btnSign = findViewById(R.id.signedInButtonsSignUp);
        btnSign.setOnClickListener(v -> createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString()));

        mAuth = FirebaseAuth.getInstance();

    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                        SignUpActivity.this.startActivity(intent);

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.d("--------------------------------", "signInWithEmail:failure", task.getException());
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(SignUpActivity.this, "Ошибка регистрации",
                                Toast.LENGTH_SHORT).show();

                    }
                    hideProgressDialog();
                });
    }

    private boolean validateForm() {
        boolean isValid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Это поле обязательно");
            isValid = false;
        } else if (!Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")
                .matcher(email).matches()) {
            mEmailField.setError("Эта почта недействительна");
            isValid = false;
        } else mEmailField.setError(null);

        String username = mUsernameField.getText().toString();
        if (TextUtils.isEmpty(username)) {
            mUsernameField.setError("Это поле обязательно");
            isValid = false;
        } else if (username.length() < 5) {
            mUsernameField.setError("Этот логин слишком короткий");
            isValid = false;
        } else mUsernameField.setError(null);

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Это поле обязательно");
            isValid = false;
        } else if (password.length() < 6) {
            mPasswordField.setError("Этот пароль слишком короткий");
            isValid = false;
        } else mPasswordField.setError(null);

        return isValid;
    }


    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Загрузка…");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }
}
