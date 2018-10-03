package com.example.jon.eventpro.java.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.jon.eventpro.R;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity
{
    // UI references.
    private EditText emailView, passwordView, confirmPasswordView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });

        // Set up the login form.
        emailView = findViewById(R.id.email);
        passwordView = findViewById(R.id.password);

        // Clicking done will attempt login
        confirmPasswordView = findViewById(R.id.confirm_password);
        confirmPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent)
            {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        // Clicking register button will attempt registration process
        Button btnRegister = findViewById(R.id.button_register);
        btnRegister.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                attemptRegister();
            }
        });
    }

    private void attemptRegister()
    {
        // Reset errors.
        emailView.setError(null);
        passwordView.setError(null);
        confirmPasswordView.setError(null);


        // Store values at the time of the login attempt.
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        String confirmPassword = confirmPasswordView.getText().toString();


        boolean cancel = false;
        View focusView = null;

        // Checking passwords match
        if (TextUtils.isEmpty(confirmPassword))
        {
            confirmPasswordView.setError(getString(R.string.error_field_required));
            focusView = confirmPasswordView;
            cancel = true;
        }
        else if(!confirmPassword.equals(password))
        {
            confirmPasswordView.setError(getString(R.string.error_password_mismatch));
            focusView = confirmPasswordView;
            cancel = true;
        }

        // Check for a valid password
        if (TextUtils.isEmpty(password))
        {
            passwordView.setError(getString(R.string.error_field_required));
            focusView = passwordView;
            cancel = true;
        }
        else if(!isPasswordValid(password))
        {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }

        // Check if email already exists
        //

        // Check for a valid email address.
        if (TextUtils.isEmpty(email))
        {
            emailView.setError(getString(R.string.error_field_required));
            focusView = emailView;
            cancel = true;
        }
        else if (!isEmailValid(email))
        {
            emailView.setError(getString(R.string.error_invalid_email));
            focusView = emailView;
            cancel = true;
        }

        if (cancel)
        {
            // There was an error; don't attempt register and focus the first form field with an error.
            focusView.requestFocus();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "successful register", Toast.LENGTH_LONG).show();
            finish();
        }
    }


    private boolean isEmailValid(String email)
    {
        return email.matches("[a-zA-Z][\\w]*@[a-z]+\\.[a-z]+");
    }

    private boolean isPasswordValid(String password)
    {
        return password.length() >= 8;
    }
}


