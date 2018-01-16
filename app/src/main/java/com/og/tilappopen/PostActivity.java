package com.og.tilappopen;

// A class for the post activity -- where content from Reddit's TIL subreddit is presented

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PostActivity extends AppCompatActivity {

    protected static TILGlobalClass global;

    Button tellMeButton;
    Button redditPostButton;
    Button learnMoreButton;

    TextView postTitleText;

    Post post;

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
        setContentView(R.layout.activity_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        global = (TILGlobalClass) getApplication();
        post = global.getRandomPost();

        tellMeButton = (Button) findViewById(R.id.tellMeButton);
        redditPostButton = (Button) findViewById(R.id.redditPostButton);
        learnMoreButton = (Button) findViewById(R.id.learnMoreButton);

        postTitleText = (TextView) findViewById(R.id.postTitleText);
        postTitleText.setText(post.getCleanTitle());

        tellMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostActivity.this, PostActivity.class);
                startActivity(intent);
            }
        });

        redditPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUrl(post.getPermalink());
            }
        });

        learnMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUrl(post.getLinkUrl());
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
            Intent intent = new Intent(PostActivity.this, InfoActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
