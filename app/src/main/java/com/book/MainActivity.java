package com.book;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static String URL_TAG = "url";
    public static String POSITION_TAG = "position";
    public static String PAGE_TAG = "page";

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private TextView tvName;
    private ImageView ivIcon;
    private ListView lvChapter;
    private List<Chapter> ChapterList;
    private ChapterAdapter chapAdapter;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new ContentFragment()).commit();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferences = getSharedPreferences("Preference", Context.MODE_PRIVATE);
        editor = preferences.edit();

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        mDrawer.setDrawerListener(drawerToggle);

        nvDrawer = (NavigationView) findViewById(R.id.nvView);

        View header = LayoutInflater.from(this).inflate(R.layout.nav_header, null);
        nvDrawer.addHeaderView(header);

        tvName = (TextView) header.findViewById(R.id.tvName);
        ivIcon = (ImageView) header.findViewById(R.id.ivIcon);
        nvDrawer.inflateHeaderView(R.layout.nav_header2);

        ChapterList = new ArrayList<Chapter>();
        chapAdapter = new ChapterAdapter(this, ChapterList);
        lvChapter = (ListView) findViewById(R.id.lvChapter);
        lvChapter.setAdapter(chapAdapter);

        lvChapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectDrawerItem(i);
            }
        });

        loadData();
        loadChapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_previous:

                String currentPage = preferences.getString(PAGE_TAG, "");
                int page = 0;
                try {
                    page = Integer.parseInt(currentPage) - 1;
                    if(page<1) {
                        //do nothing
                    } else {
                        String url = "chapter" +page +".html";
                        Fragment fragment = null;
                        Bundle bundle = new Bundle();
                        fragment = new ContentFragment();
                        bundle.putString(URL_TAG, url);
                        bundle.putInt(POSITION_TAG, page);
                        fragment.setArguments(bundle);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                    }
                } catch(NumberFormatException nfe) {
                    System.out.println("Could not parse " + nfe);
                }
                return true;

            case R.id.action_next:
                Toast.makeText(this, "Next page", Toast.LENGTH_SHORT).show();
                return true;
        }
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    public void selectDrawerItem(int  i) {
        Fragment fragment = null;
        Bundle bundle=new Bundle();
        Chapter chapter = chapAdapter.getChapter(i);
        fragment = new ContentFragment();
        bundle.putInt(POSITION_TAG, i+1);
        bundle.putString(URL_TAG, chapter.getUrl());
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        mDrawer.closeDrawers();
    }

    public void loadData() {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            String appName = obj.getString("title");
            String icon = obj.getString("icon");
            Log.d("json object: ", appName +", " +icon);

            tvName.setText(appName);
            try {
                InputStream ims = getAssets().open(icon);
                Drawable d = Drawable.createFromStream(ims, null);
                ivIcon.setImageDrawable(d);
            }
            catch(IOException ex) {
                return;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void loadChapter() {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray jsonArray = obj.getJSONArray("toc");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Chapter chapter = new Chapter(new Json2Chapter(jsonObject));
                ChapterList.add(chapter);
                chapAdapter.notifyDataSetChanged();
                Log.d("json object: ", jsonObject.getString("url"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("book.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
