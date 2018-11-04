package com.example.jon.eventpro.java.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jon.eventpro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firestore.admin.v1beta1.Progress;

public class LoginActivity extends AppCompatActivity
{
    private EditText etEmail, etPassword;
    private ProgressDialog loginProgress;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        loginProgress = new ProgressDialog(this);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);


        Button btnLogin = findViewById(R.id.button_login);
        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && isEmailValid(email) && isPasswordValid(password))
                {
                    loginProgress.setTitle("Logging In");
                    loginProgress.setMessage("Please wait while we verify your credentials!");
                    loginProgress.setCanceledOnTouchOutside(false);
                    loginProgress.show();

                    loginUser(email, password);
                }
                else
                {   //checking email is valid
                    if(TextUtils.isEmpty(email))
                        etEmail.setError("You must complete this field");
                    else if(!isEmailValid(email))
                        etEmail.setError("Invalid Email");

                    //checking password is valid
                    if(TextUtils.isEmpty(password))
                        etPassword.setError("You must complete this field");
                    else if(!isPasswordValid(password))
                        etPassword.setError("Password must be at least 6 characters long");
                }
            }
        });

        TextView btnRegister = findViewById(R.id.button_register);
        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void loginUser(String email, String password)
    {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    loginProgress.dismiss();

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
                else
                {
                    loginProgress.hide();
                    Toast.makeText(LoginActivity.this, "Login failed. Please check forms and try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private boolean isEmailValid(String email)
    {
        return email.matches("[a-zA-Z][\\w]*@[a-z]+\\.[a-z]+");
    }
    private boolean isPasswordValid(String password)
    {
        return password.length() > 5;
    }
}
