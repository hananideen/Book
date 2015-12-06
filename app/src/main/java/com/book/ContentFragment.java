package com.book;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Hananideen on 4/12/2015.
 */
public class ContentFragment extends android.support.v4.app.Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        String url;
        WebView view = (WebView) rootView.findViewById(R.id.webView);
        view.getSettings().setJavaScriptEnabled(true);

        try {
            Bundle bundle = this.getArguments();
            url = bundle.getString(MainActivity.URL_TAG);
            view.loadUrl("file:///android_asset/" + url);
            }
        catch (Exception e) {
            url  = "chapter1.html";
            view.loadUrl("file:///android_asset/" + url);
        }

        return rootView;
    }

}

