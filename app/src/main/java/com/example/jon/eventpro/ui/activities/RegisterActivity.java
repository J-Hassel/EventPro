package com.example.jon.eventpro.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jon.eventpro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity
{
    private EditText etDisplayName, etEmail, etPassword;
    private FirebaseAuth auth;
    private DatabaseReference database;
    private ProgressDialog registerProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });

        registerProgress = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();

        etDisplayName = findViewById(R.id.et_display_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);


        Button btnRegister = findViewById(R.id.button_register);
        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String displayName = etDisplayName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if(!TextUtils.isEmpty(displayName) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)
                        && isDisplayNameValid(displayName) && isEmailValid(email) && isPasswordValid(password))
                {
                    registerProgress.setTitle("Registering User");
                    registerProgress.setMessage("Please wait while we create your account!");
                    registerProgress.setCanceledOnTouchOutside(false);
                    registerProgress.show();

                    registerUser(displayName, email, password);
                }
                else
                {   //checking displayName is valid
                    if(TextUtils.isEmpty(displayName))
                        etDisplayName.setError("You must complete this field");
                    else if(!isDisplayNameValid(displayName))
                        etDisplayName.setError("Not a valid display name");

                    //checking email is valid
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
    }

    private void registerUser(final String displayName, String email, String password)
    {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String currentUid = currentUser.getUid();

                    database = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid);

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("image", "default");
                    userMap.put("name", displayName);
                    userMap.put("location", "United States");
                    userMap.put("about", "Nothing here yet!");
                    userMap.put("status", "Nothing here yet!");
                    userMap.put("thumb_image", "default");



                    database.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if(task.isSuccessful())
                            {
                                registerProgress.dismiss();

                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                finish();
                            }
                        }
                    });
                }
                else
                {
                    registerProgress.hide();
                    Toast.makeText(RegisterActivity.this, "Registration failed. Please check forms and try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isDisplayNameValid(String name)
    {
        return name.matches("[a-zA-Z]+ ?[a-zA-Z]* ?[a-zA-Z]*");
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
