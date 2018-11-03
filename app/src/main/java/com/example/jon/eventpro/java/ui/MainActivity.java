package com.example.jon.eventpro.java.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.jon.eventpro.R;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity
{
    private FirebaseAuth auth;
    private static final int RC_SIGN_IN = 200;

    private BottomNavigationView mainNav;
    private FrameLayout mainFrame;

    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private MessagesFragment messagesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainNav = findViewById(R.id.main_nav);
        mainFrame = findViewById(R.id.main_frame);


        //fragments
        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        messagesFragment = new MessagesFragment();

        //start with the home fragment
        setFragment(homeFragment);

        //set fragment for whichever item is selected
        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch(item.getItemId())
                {
                    case R.id.nav_home:
                        setFragment(homeFragment);
                        return true;

                    case R.id.nav_search:
                        setFragment(searchFragment);
                        return true;

                    case R.id.nav_messages:
                        auth = FirebaseAuth.getInstance();
                        if(auth.getCurrentUser() == null)   //TODO: fix the nav bar bug
                        {
                            signIn();
                            mainNav.setSelectedItemId(R.id.nav_home);
                        }

                        else
                            setFragment(messagesFragment);
                        return true;

                    default: return false;
                }
            }
        });

    }

    private void setFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

    private void signIn()
    {
        if(auth.getCurrentUser() == null)
        {   //you have to be signed out to access sign in page
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .build(),
                    RC_SIGN_IN);
        }
        else
            displayMessage("already signed in");
    }

    private void displayMessage(String message)
    {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }

}
