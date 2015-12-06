package com.book;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * Created by Hananideen on 4/12/2015.
 */
public class ContentFragment extends android.support.v4.app.Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        SharedPreferences preferences = this.getActivity().getSharedPreferences("Preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String url;
        int position;
        WebView view = (WebView) rootView.findViewById(R.id.webView);

        view.getSettings().setJavaScriptEnabled(true);

        try {
            Bundle bundle = this.getArguments();
            url = bundle.getString(MainActivity.URL_TAG);
            position = bundle.getInt(MainActivity.POSITION_TAG);
            view.loadUrl("file:///android_asset/" + url);
            editor.putString(MainActivity.PAGE_TAG, String.valueOf(position));
            editor.commit();
        }
        catch (Exception e) {
            url  = "chapter1.html";
            position = 1;
            view.loadUrl("file:///android_asset/" + url);
            editor.putString(MainActivity.PAGE_TAG, String.valueOf(position));
            editor.commit();
        }

        return rootView;
    }

}

