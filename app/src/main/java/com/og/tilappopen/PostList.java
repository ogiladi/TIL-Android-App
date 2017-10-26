package com.og.tilappopen;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class PostList {

    String url;
    String after;
    ArrayList<Post> postList;

    private final String pageURL = "https://www.reddit.com/r/todayilearned/.json" + "?after=AFTER";

    PostList() {
        after = "";
        getURL();
        postList = new ArrayList<>();
    }

    private void getURL() {

        url = pageURL.replace("AFTER", after);

    }

    private void addPagePosts(String urlSt) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        URL url;

        try {

            url = new URL(urlSt);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            InputStream in = urlConnection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(in));

            StringBuffer buffer = new StringBuffer();

            String line = "";

            while ((line = reader.readLine()) != null) {

                buffer.append(line);

            }

            JSONObject data = new JSONObject(buffer.toString()).getJSONObject("data");

            JSONArray children = data.getJSONArray("children");

            after = data.getString("after");

            for (int i = 0; i < children.length(); i++) {

                JSONObject current = children.getJSONObject(i).getJSONObject("data");

                Post p = new Post();

                p.title = current.optString("title");
                p.permalink = current.optString("permalink");
                p.linkUrl = current.optString("url");
                p.numComments = current.optInt("num_comments");

                if (p.title != null) {

                    postList.add(p);

                }
            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public ArrayList<Post> getPostList() {

        return postList;

    }


    public void addPages(int numPages) {

        for (int i = 0; i < numPages; i++) {

            getURL();

            addPagePosts(url);

        }
    }

}

