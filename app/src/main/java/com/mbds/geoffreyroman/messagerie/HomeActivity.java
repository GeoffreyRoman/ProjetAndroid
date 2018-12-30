package com.mbds.geoffreyroman.messagerie;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity implements iCallable{
    View frag1;
    View frag2;
    ContactFragment contactFragment;
    MessageFragment messageFragment;
    FragmentTransaction fragmentTransaction;

    int currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        frag1 = (View) findViewById(R.id.frag1);
        frag2 = (View) findViewById(R.id.frag2);
        contactFragment = new ContactFragment();
        messageFragment = new MessageFragment();
        fragmentTransaction = getFragmentManager().beginTransaction();

        int display_mode = getResources().getConfiguration().orientation;
        fragmentTransaction.replace(frag1.getId(), contactFragment);
        currentFragment = 0;


        if (display_mode == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentTransaction.replace(frag2.getId(), messageFragment);
            currentFragment = 2;
        }

        fragmentTransaction.commit();



        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            }
        }
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
            messageFragment.setMessage(sharedText);
        }
    }


        @Override
    public void transferData(String s) {
        messageFragment.setText(s);

    }

    @Override
    public void afficheMessage(String name) {
        currentFragment = 1;
        frag2 = (View)findViewById(R.id.frag2);
        fragmentTransaction =  getFragmentManager().beginTransaction();
        messageFragment = new MessageFragment();
        fragmentTransaction.replace(frag2.getId(), messageFragment).addToBackStack( "tag" );
        fragmentTransaction.commit();
        transferData(name);
    }
}
