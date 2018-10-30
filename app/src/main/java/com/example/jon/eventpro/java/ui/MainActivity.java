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

public class MainActivity extends AppCompatActivity {

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
}
