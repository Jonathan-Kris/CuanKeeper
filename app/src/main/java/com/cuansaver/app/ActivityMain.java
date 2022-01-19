package com.cuansaver.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.cuansaver.app.auth.ActivityLogin;
import com.cuansaver.app.auth.ActivityRegister;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.cuansaver.app.databinding.ActivityMainBinding;
import com.huawei.hms.support.account.result.AuthAccount;

import java.io.InputStream;

public class ActivityMain extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private Intent recvIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.appBarActivityMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Insert New Data", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                intendedAction("insert");
            }
        });
        recvIntent = getIntent();

        DrawerLayout drawer = binding.drawerLayout;
        TextView navUsername = (TextView) headerView.findViewById(R.id.username);
        TextView navEmail = (TextView) headerView.findViewById(R.id.email);
        navUsername.setText(recvIntent.getStringExtra("username"));
        navEmail.setText(recvIntent.getStringExtra("email"));
        String photo = recvIntent.getStringExtra("photo");
        if (photo != null && !photo.isEmpty())
            new DownloadImageTask((ImageView) headerView.findViewById(R.id.imageView))
                    .execute(recvIntent.getStringExtra("photo"));
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                        .setOpenableLayout(drawer)
                        .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_activity_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    protected void intendedAction(String page) {
        Intent intent = null;
        switch (page) {
            case "insert":
                intent = new Intent(this, ActivityInsert.class);
                intent.putExtra("uid", getIntent().getStringExtra("uid"));
                startActivity(intent);
                break;
            default:
                intent = new Intent(this, ActivityMain.class);
                startActivity(intent);
                return;
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}