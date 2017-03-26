package com.example.ariel.boodal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import java.util.HashMap;
import android.view.View;
import android.content.Intent;
import android.view.Gravity;
import com.example.ariel.boodal.helper.SessionManager;
import com.example.ariel.boodal.helper.SQLiteHandler;

public class Driver_Home extends AppCompatActivity {

    private Drawer navigationDrawerLeft;
    private AccountHeader headerNavigationLeft;
    private SQLiteHandler db;
    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver__home);

        db = new SQLiteHandler(getApplicationContext());

        // session manager


        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");
        String id = user.get("id_user");
        headerNavigationLeft = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(false)
                .withSavedInstance(savedInstanceState)
                .withThreeSmallProfileImages(false)
                .addProfiles(
                        new ProfileDrawerItem().withName(name).withEmail(email)
                )
                .withProfileImagesVisible(false)
                .build();

        navigationDrawerLeft = new DrawerBuilder()
                .withActivity(this)
                .withDrawerGravity(Gravity.LEFT)
                .withSavedInstance(savedInstanceState)
                .withAccountHeader(headerNavigationLeft)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home").withIdentifier(0),
                        new PrimaryDrawerItem().withName("Top Up").withIdentifier(1),
                        new PrimaryDrawerItem().withName("Saldo").withIdentifier(2),
                        new PrimaryDrawerItem().withName("Setting").withIdentifier(3),
                        new PrimaryDrawerItem().withName("Logout").withIdentifier(4)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            Intent i = null;
                            if (drawerItem.getIdentifier() == 1) {
                                i = new Intent(Driver_Home.this,TopUp.class);
                                startActivity(i);
                            }
                            if (drawerItem.getIdentifier() == 2) {
                                i = new Intent(Driver_Home.this, Saldo.class);
                                startActivity(i);
                            }
                            if (drawerItem.getIdentifier() == 3) {
                                //i = new Intent(Driver_Home.this, .class);
                                //startActivity(i);
                            }
                            if (drawerItem.getIdentifier() == 4) {
                                logoutUser();
                            }

                        }
                        return false;
                    }
                })
                .build();
    }
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(Driver_Home.this, Login.class);
        startActivity(intent);
        finish();
    }
}
