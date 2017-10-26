package com.og.tilappopen;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    ArrayList<Post> pList = new ArrayList<>();
    Post p = new Post();
    int pListSize;
    int chosenPostIndex;
    TextView postTitleText;
    RelativeLayout textLayout;
    Button tellMeButton;
    Button learnMoreButton;
    Button redditPostButton;
    Button backToMainButton;
    Button goToSiteButton;
    Button goToGitHubButton;

    Animation fadeIn = new AlphaAnimation(0f, 1f);
    Animation fadeOut = new AlphaAnimation(1f, 0f);

    private void toastMsg(String msg) {

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    private void goToUrl(String url) {

        Uri uriUrl = Uri.parse(url);

        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);

        startActivity(launchBrowser);

    }

    public void goBack(View view) {

        fadeIn.setDuration(800);
        fadeOut.setDuration(800);

        textLayout.startAnimation(fadeOut);

        backToMainButton.setVisibility(View.GONE);

        goToSiteButton.setVisibility(View.GONE);

        goToGitHubButton.setVisibility(View.GONE);

        postTitleText.setVisibility(View.GONE);

        tellMeButton.setText("Click Here");

        tellMeButton.setVisibility(View.VISIBLE);

        textLayout.startAnimation(fadeIn);

    }

    public void tellMe(View view) {


        fadeIn.setDuration(800);
        fadeOut.setDuration(800);

        Random r = new Random();
        chosenPostIndex = r.nextInt(pListSize);
        p = pList.get(chosenPostIndex);

        while (!p.isGood()) {

            r = new Random();
            chosenPostIndex = r.nextInt(pListSize);
            p = pList.get(chosenPostIndex);

        }

        textLayout.startAnimation(fadeOut);

        postTitleText.setText(p.getCleanTitle());

        tellMeButton.setText("Tell Me Something Else");

        postTitleText.setVisibility(View.VISIBLE);
        learnMoreButton.setVisibility(View.VISIBLE);
        redditPostButton.setVisibility(View.VISIBLE);

        textLayout.startAnimation(fadeIn);

    }

    public void learnMore(View view) {

        goToUrl(p.getLinkUrl());

    }

    public void goToRedditPost(View view) {

        goToUrl(p.getPermalink());

    }

    public void goToSite(View view) {

        goToUrl("http://www.ohadgiladi.com");

    }

    public void goToGitHub(View view) {

        goToUrl("https://github.com/ogiladi");

    }


    public class DownloadTask extends AsyncTask<Void, Void, ArrayList<Post>> {

        @Override
        protected ArrayList<Post> doInBackground(Void... voids) {

            PostList postList = new PostList();

            postList.addPages(20);

            return postList.getPostList();

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fadeIn.setDuration(800);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        postTitleText = (TextView) findViewById(R.id.postTitleText);

        tellMeButton = (Button) findViewById(R.id.tellMeButton);

        learnMoreButton = (Button) findViewById(R.id.learnMoreButton);

        redditPostButton = (Button) findViewById(R.id.redditPostButton);

        backToMainButton = (Button) findViewById(R.id.backToMainButton);

        goToSiteButton = (Button) findViewById(R.id.goToSiteButton);

        goToGitHubButton = (Button) findViewById(R.id.goToGithubButton);

        textLayout = (RelativeLayout) findViewById(R.id.textLayout);

        textLayout.setVisibility(View.INVISIBLE);

        textLayout.startAnimation(fadeIn);

        textLayout.setVisibility(View.VISIBLE);

        DownloadTask task = new DownloadTask();

        try {

            pList = task.execute().get();
            pListSize = pList.size();

        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();

        }

        if (pListSize == 0) {

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("An error has occurred. Please try again later");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }

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

            textLayout.startAnimation(fadeIn);

            postTitleText.setText("This app takes the first 500 posts from Reddit's Today I Learned (TIL) " +
                    "subreddit and chooses randomly a post with at least 10 comments");
            postTitleText.setVisibility(View.VISIBLE);
            backToMainButton.setVisibility(View.VISIBLE);
            goToSiteButton.setVisibility(View.VISIBLE);
            goToGitHubButton.setVisibility(View.VISIBLE);
            tellMeButton.setVisibility(View.GONE);
            learnMoreButton.setVisibility(View.GONE);
            redditPostButton.setVisibility(View.GONE);


        }

        return super.onOptionsItemSelected(item);
    }
}
