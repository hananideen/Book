package com.book;

import org.json.JSONObject;

/**
 * Created by Hananideen on 5/12/2015.
 */
public class Json2Chapter {

    public JSONObject jsonObject;
    public String chap, url;

    public Json2Chapter(JSONObject json){
        jsonObject = json;
        if (json!=null)
        {
            chap= json.optString("title");
            url = json.optString("url");
        }
        else{
            chap = "";
            url = "";
        }
    }
}