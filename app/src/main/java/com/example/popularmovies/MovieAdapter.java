package com.example.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> mPosters;
    private String[] poster_paths;
    String main_url = "http://image.tmdb.org/t/p/w185";
    String LOG_TAG = "MovieAdapter";

    public MovieAdapter (Context context,ArrayList<String> posters){
        mContext=context;
        mPosters=posters;
        try {
            poster_paths = new String[mPosters.size()];
            for (int i = 0; i < mPosters.size(); i++) {
                poster_paths[i] = main_url + mPosters.get(i);
            }
        }catch (NullPointerException e) {
            Log.e(LOG_TAG, "Sorry, There is an error.", e);
        }
    }

    @Override
    public int getCount() {
        return poster_paths.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView==null){
            imageView=new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(190,270));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(5,5,5,5);
        }else {
            imageView = (ImageView) convertView;
        }
        Picasso.with(mContext).load(poster_paths[position]).into(imageView);
        return null;
    }
}
