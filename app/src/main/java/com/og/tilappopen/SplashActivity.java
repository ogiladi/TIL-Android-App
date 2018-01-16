package com.og.tilappopen;

// A class for the splash activity -- the splash screen that appears when
// the app opens

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;


public class SplashActivity extends AppCompatActivity{

    protected static TILGlobalClass global;

    public static class BuildList extends AsyncTask<Void, Void, Void> {
        private WeakReference<SplashActivity> weakRef;

        private BuildList(SplashActivity context) {
            weakRef = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            global.buildListPosts();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            SplashActivity context = weakRef.get();
            context.startActivity(new Intent(context, MainActivity.class));
            context.finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        global = (TILGlobalClass) getApplication();
        new BuildList(this).execute();
    }

}

