package com.og.tilappopen;

// A that contains the list of Reddit posts as a global object,
// and a method to choose a random post

import android.app.Application;
import java.util.ArrayList;

public class TILGlobalClass extends Application {

    private ArrayList<Post> listPosts;
    private int listPostsSize;
    private int numClicks;
    private ArrayList<Integer> listInd;

    public boolean isListEmpty() {
        return listPostsSize == 0;
    }

//    Build a list of posts using 20 pages (500 posts)
    public void buildListPosts() {
        PostList pList = new PostList();
        pList.addPages(20);
        listPosts = pList.getPostList();
        numClicks = 0;
        listPostsSize = listPosts.size();
        listInd = new ArrayList<>(listPostsSize);
        for (int i = 0; i < listPostsSize; i++) listInd.add(i);
        java.util.Collections.shuffle(listInd);
    }

    public Post getRandomPost() {
        try {
            int chosenInd = listInd.get(numClicks++);
            Post post = listPosts.get(chosenInd % listPostsSize);
            while (!post.isGood()) {
                chosenInd = listInd.get(numClicks++);
                post = listPosts.get(chosenInd % listPostsSize);
            }
            return listPosts.get(chosenInd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void onCreate() {
        super.onCreate();
    }

}
