package com.book;

import org.json.JSONObject;

/**
 * Created by Hananideen on 5/12/2015.
 */
public class Chapter {

    public String Chapter, Url;

    public Chapter(Json2Chapter json2Chapter){
        Chapter = json2Chapter.chap;
        Url = json2Chapter.url;
    }

    public String getChapter () {
        return Chapter;
    }

    public String getUrl () {
        return Url;
    }
}
