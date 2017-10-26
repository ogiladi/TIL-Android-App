package com.og.tilappopen;


import android.text.Html;
import android.text.Spanned;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class Post {

    String title;
    String permalink;
    String linkUrl;
    int numComments;

    @SuppressWarnings("deprecation")
    private static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }


    public String getPermalink() {
        return "http://www.reddit.com" + permalink;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public boolean isGood() {

        if (numComments < 10) return false;

        List<String> strList = new ArrayList(Arrays.asList(title.split(" ")));

        if (strList.get(1).toLowerCase().equals("about") || strList.get(1).toLowerCase().equals("of")) return false;

        return true;

    }

    public String getCleanTitle() {

        List<String> strList = new ArrayList(Arrays.asList(title.split(" ")));

        List<String> tilList = new ArrayList(Arrays.asList("TIL", "TIL:", "TIL,", "TIL;", "TIL-", "TIL--"));

        List<String> startList = new ArrayList(Arrays.asList("that", "that,", "-", "--"));

        List<String> qmList = new ArrayList(Arrays.asList("\"", "\'"));

        String lastWord = strList.get(strList.size()-1);

        int lastWordLength = lastWord.length();

        if (tilList.contains(strList.get(0).toUpperCase())) {

            strList.remove(0);

        }

        // in case someone starts a post with "TIL I learned..."

        if (strList.get(0).toLowerCase().equals("i") && strList.get(1).toLowerCase().equals("learned")) {

            strList.subList(0,2).clear();
        }

        // remove the TIL part from the title

        if (startList.contains(strList.get(0).toLowerCase())) {

            strList.remove(0);

        }

        // remove other unnecessary words from the title

        if (qmList.contains(strList.get(0).substring(0,1))) {

            strList.set(0, strList.get(0).substring(0,1)
                    + strList.get(0).substring(1,2).toUpperCase()
                    + strList.get(0).substring(2));

        }

        else {

            strList.set(0, strList.get(0).substring(0,1).toUpperCase() + strList.get(0).substring(1));

        }

        // remove the period at the end of the last sentence

        if (lastWord.substring(lastWordLength-1).equals(".")) {

            lastWord = lastWord.substring(0, lastWordLength-1);
            strList.set(strList.size()-1, lastWord);

        }

        String result = "";

        for (String str : strList) result += fromHtml(str).toString() + " ";

        return result;

    }

}

