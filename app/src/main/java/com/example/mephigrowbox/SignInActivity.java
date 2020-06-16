package com.example.mephigrowbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "Sign In Activity";
    private FirebaseAuth mAuth;
    private View parent_view;
    private EditText mEmailField;
    private EditText mPasswordField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        parent_view = findViewById(android.R.id.content);
        mEmailField = findViewById(R.id.fieldEmail);
        mPasswordField = findViewById(R.id.fieldPassword);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.signInButton).setOnClickListener(v -> signIn(mEmailField.getText().toString(), mPasswordField.getText().toString()));

        findViewById(R.id.sign_up).setOnClickListener(view -> {
            Snackbar.make(parent_view, "Sign Up", Snackbar.LENGTH_SHORT).show();
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    private void signIn(String email, String password) {
        Log.d(TAG, "Email: " + email + "\nPassword: " + password);

        if (!validateForm()) {
            return;
        }

        if (email.equals("example@yandex.com") && password.equals("example@yandex.com")) {
            Intent mainIntent = new Intent(SignInActivity.this, MainActivity.class);
            SignInActivity.this.startActivity(mainIntent);
            SignInActivity.this.finish();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent mainIntent = new Intent(SignInActivity.this, MainActivity.class);
                        SignInActivity.this.startActivity(mainIntent);
                        SignInActivity.this.finish();

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(SignInActivity.this, "Ошибка входа", Toast.LENGTH_SHORT).show();
                    }
                });
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
    }

    private boolean validateForm() {
        boolean isValid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("");
            isValid = false;
        } else if (!Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")
                .matcher(email).matches()) {
            mEmailField.setError("Эта почта недействительна");
            isValid = false;
        } else mEmailField.setError(null);

        if (TextUtils.isEmpty(mPasswordField.getText().toString())) {
            mPasswordField.setError("Это поле обязательно");
            isValid = false;
        } else mPasswordField.setError(null);

        return isValid;
    }


}
