package com.studybear.cdj.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by chadreynolds on 4/25/15.
 */
public class NavigationBarController {

    private static NavigationBarController navBarInstance;

    private Activity currentActivity;
    private String username;

    private TextView matchButton;
    private TextView inboxButton;
    private TextView profileButton;
    private TextView logoutButton;


    private NavigationBarController(Activity activity, String uname) {
        currentActivity = activity;
        username = uname;
    }

    public static synchronized NavigationBarController getInstance(Activity activity, String username){
        if(navBarInstance == null){
            navBarInstance = new NavigationBarController(activity, username);
        }
        return navBarInstance;
    }

    public void activateNavBar() {
        getButtons(currentActivity);
        addListeners(currentActivity, username);
    }

    private void getButtons(Activity activity) {
        // if the nav bar layout changes then these will have to be updated
        matchButton = (TextView) activity.findViewById(R.id.findMatchButton);
        inboxButton = (TextView) activity.findViewById(R.id.inboxButton);
        profileButton = (TextView) activity.findViewById(R.id.profileButton);
        logoutButton = (TextView) activity.findViewById(R.id.logoutButton);
    }

    private void addListeners(final Activity activity, final String username) {
        matchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(activity, MatchActivity.class);
                intent.putExtra("username", username);
                activity.startActivity(intent);
                activity.finish();
            }
        });

        inboxButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(activity, inboxActivity.class);
                intent.putExtra("username", username);
                activity.startActivity(intent);
                activity.finish();
            }
        });

        // only go to EditProfile if you are on the profile activity
        // otherwise, go to the profile activity
        if (activity instanceof  ProfileActivity)
            profileButton.setText("Edit Profile");
        profileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (activity instanceof ProfileActivity) {
                    ((ProfileActivity) activity).EditProfile(v);
                }
                else {
                    Intent intent = new Intent(activity, ProfileActivity.class);
                    intent.putExtra("username", username);
                    activity.startActivity(intent);
                    activity.finish();
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(activity, LoginActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }
}
