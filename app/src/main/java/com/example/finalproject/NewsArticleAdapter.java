package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.view.View.GONE;

/**
 * this class is a adapter for news
 */
public class NewsArticleAdapter extends BaseAdapter {

    private ArrayList<NewsArticleObject> newsArticleList;

    private Context mContext;
    private int layoutResourceId;

    /**
     * constructor with 3 parameter
     * @param mContext
     * @param layoutResourceId
     * @param newsArticleList
     */
    public NewsArticleAdapter(Context mContext, int layoutResourceId, ArrayList<NewsArticleObject> newsArticleList) {
        super();
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.newsArticleList = newsArticleList;

    }


    @Override
    public int getCount() {
        return newsArticleList.size();
    }

    /**
     * gettor of the item index
     *
     * @param i
     */
    @Override
    public NewsArticleObject getItem(int i) {
        return newsArticleList.get(i);
    }


    @Override
    public long getItemId(int i) {
        return (getItem(i)).getId();
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.text = row.findViewById(R.id.row_title);


            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        NewsArticleObject item = newsArticleList.get(position);

        if (!TextUtils.isEmpty(Html.fromHtml(item.getTitle()))) {

            holder.text.setText(Html.fromHtml(item.getTitle()));

        } else {

            holder.text.setVisibility(GONE);
        }


        return row;

    }


    public void setListData(ArrayList<NewsArticleObject> mListData) {
        this.newsArticleList = mListData;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView text;
        ImageView imageView;
        TextView descriptionTextView;
    }
}
