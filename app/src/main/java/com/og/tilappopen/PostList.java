package com.og.tilappopen;

// A class for creating a list of Reddit posts. Such a list is created as an app global object
// and used to retrieve a random post.
// The class uses the OAuth authentication mechanism to connect to Reddit's API and retrieve data

import org.apache.commons.codec.binary.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;


class PostList {

    private String url;
    private String after;
    private ArrayList<Post> postList;
    private final String pageURL;
    private final String accessToken;

    PostList() {
        after = "";
        pageURL = "https://oauth.reddit.com/r/todayilearned/" + "?after=AFTER";
        getURL();
        postList = new ArrayList<>();
        accessToken = getToken();
    }

    private void getURL() {

        url = pageURL.replace("AFTER", after);

    }

//  A methed to retrieve the OAuth authentication token
    private String getToken() {
        String url = "https://www.reddit.com/api/v1/access_token";
        String username = ENTER_TOKEN_HERE;
        String password = "";
        String accessToken;

        try {
            String encoded = new String(new Base64().encode((username+":"+password).getBytes()));

            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Basic "+encoded);
            con.setDoOutput(true);

            String DEVICE_ID = UUID.randomUUID().toString();
            String urlParameters = "grant_type=https://oauth.reddit.com/grants/installed_client&device_id="+DEVICE_ID;

            DataOutputStream wr;
            wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonObj = new JSONObject(response.toString());
            accessToken = jsonObj.getString("access_token");
            return accessToken;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

//    Connect to Reddit's API and retrieve data
    private void addPagePosts(String url) {
        HttpURLConnection con = null;
        BufferedReader reader = null;
        URL obj;

        try {
            obj = new URL(url);

            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + accessToken);

            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            JSONObject data = new JSONObject(builder.toString()).getJSONObject("data");
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
            if (con != null) {
                con.disconnect();
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

    ArrayList<Post> getPostList() {
        return postList;
    }

//    Build a posts list reading posts from a given number of pages
    void addPages(int numPages) {
        for (int i = 0; i < numPages; i++) {
            getURL();
            addPagePosts(url);
        }
    }

}

