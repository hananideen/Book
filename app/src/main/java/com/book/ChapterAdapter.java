package com.book;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Hananideen on 5/12/2015.
 */
public class ChapterAdapter extends BaseAdapter {

    private Activity mActivity;
    private List<Chapter> ChapterList;
    private LayoutInflater inflater;
    private Chapter mChapter;

    public ChapterAdapter (Activity mActivity, List<Chapter> ChapterList){
        this.mActivity = mActivity;
        this.ChapterList = ChapterList;
    }

    @Override
    public int getCount() {
        return ChapterList.size();
    }

    @Override
    public Object getItem(int i) {
        return ChapterList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater == null) {

            inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null){
            view = inflater.inflate(R.layout.chap_row, viewGroup, false);
        }

        TextView chapName = (TextView) view.findViewById(R.id.tvChap);

        mChapter = ChapterList.get(i);

        chapName.setText(mChapter.getChapter());

        return view;
    }

    public Chapter getChapter (int position) {
        return ChapterList.get(position);
    }
}
