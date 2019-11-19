package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecipeJSONAdapter extends BaseAdapter {
    private List<MyRecipe> data;
    private LayoutInflater inflater;
    private String title;
    private TextView recipe_list;

    public RecipeJSONAdapter(Context context, List<MyRecipe> data){
        super();
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if(view == null){
            view = inflater.inflate(R.layout.recipe_list_view, null);
            view.findViewById(R.id.recipelist);
            recipe_list = view.findViewById(R.id.recipelist);
            recipe_list.setText(title);
        }

        return view;
    }
}
