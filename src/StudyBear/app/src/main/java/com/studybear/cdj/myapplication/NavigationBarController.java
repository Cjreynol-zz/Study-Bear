package com.studybear.cdj.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class NavigationBarController {

    private static NavigationBarController navBarInstance;

    private Activity currentActivity;
    private String username;

    private ImageButton matchButton;
    private ImageButton messageButton;
    private ImageButton classButton;
    private ImageButton profileButton;

    public NavigationBarController(Activity activity, String uname) {
        currentActivity = activity;
        username = uname;
        activateNavBar();
    }

    public void activateNavBar() {
        getButtons(currentActivity);
        addListeners(currentActivity, username);
    }

    private void getButtons(Activity activity) {
        // if the nav bar layout changes then these will have to be updated
        matchButton = (ImageButton) activity.findViewById(R.id.matchButton);
        messageButton = (ImageButton) activity.findViewById(R.id.messageButton);
        classButton = (ImageButton) activity.findViewById(R.id.classButton);
        profileButton = (ImageButton) activity.findViewById(R.id.profileButton);
    }

    private void addListeners(final Activity activity, final String username) {

        matchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MatchActivity.class);
                intent.putExtra("username",username);
                activity.startActivity(intent);
                activity.finish();
            }
        });

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, inboxActivity.class);
                intent.putExtra("username",username);
                activity.startActivity(intent);
                activity.finish();
            }
        });

        classButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EditClasses.class);
                intent.putExtra("username",username);
                activity.startActivity(intent);
                activity.finish();
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ProfileActivity.class);
                intent.putExtra("username",username);
                activity.startActivity(intent);
                activity.finish();
            }
        });

    }
}
