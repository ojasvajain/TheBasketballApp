package com.endeavour.ojasva.thebasketballapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ojasva on 19-Mar-16.
 */
public class CustomGridAdapter extends ArrayAdapter<String>
{
    LruCache<String,Bitmap> cache;
    List<String> team;
    Context context;   //for using getResources and getPackageName

    CustomGridAdapter(Context context, List<String> team)
    {
        super(context, R.layout.team_choose_custom_grid, team);
        this.team=team;
        this.context = context;

        //caching images
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        cache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getRowBytes() / 1024;
            }
        };
    }

    @Override
    public View getView(int position,View convertView, ViewGroup parent)
    {
        if(convertView==null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.team_choose_custom_grid, parent, false);
        }

        String team= getItem(position);
        TextView tv_name=(TextView)convertView.findViewById(R.id.tv_name);
        ImageView iv_logo=(ImageView)convertView.findViewById(R.id.iv_logo);

        tv_name.setText(team);
        iv_logo.setImageResource(getResourceId(team));
        return convertView;
    }

    public void swapData(List<String> team)
    {
        this.team.clear();
        this.team.addAll(team);
        notifyDataSetChanged();
    }

    public int getResourceId(String name) //replace spaces with underscore and everything in lower case
    {
        String res_name = name.replace(' ','_');
        res_name = res_name.toLowerCase();
        int resId = context.getResources().getIdentifier(res_name+"_small", "drawable", context.getPackageName());
        return resId;
    }


}
