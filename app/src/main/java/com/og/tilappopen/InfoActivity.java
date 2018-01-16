package com.og.tilappopen;

// A class for the activity that includes information about this app

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class InfoActivity extends AppCompatActivity {

    Button backToMainButton;
    Button goToSiteButton;
    Button goToGithubButton;

    private void goToUrl(String s) {
        Uri uri = Uri.parse(s);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(launchBrowser);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        onStartActivity();
    }

    private void onStartActivity() {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        backToMainButton = (Button) findViewById(R.id.backToMainButton);
        goToSiteButton = (Button) findViewById(R.id.goToSiteButton);
        goToGithubButton = (Button) findViewById(R.id.goToGithubButton);

        backToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        goToSiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUrl("http://www.ohadgiladi.com");
            }
        });

        goToGithubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUrl("https://github.com/ogiladi");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.aboutBackground) {
            String url = "https://en.wikipedia.org/wiki/The_School_of_Athens";
            goToUrl(url);
            return true;
        }

        if (id == R.id.aboutThisApp) {
            Intent intent = new Intent(InfoActivity.this, InfoActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
