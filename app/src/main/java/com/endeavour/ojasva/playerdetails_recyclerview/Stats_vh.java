package com.endeavour.ojasva.playerdetails_recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.endeavour.ojasva.thebasketballapp.R;

/**
 * Created by Ojasva on 18-Apr-16.
 */
public class Stats_vh extends RecyclerView.ViewHolder
{
    private TextView tv_points,tv_assists,tv_steals,tv_rebounds;

    public Stats_vh(View itemView)
    {
        super(itemView);

        tv_points = (TextView)itemView.findViewById(R.id.tv_points);
        tv_assists = (TextView)itemView.findViewById(R.id.tv_assists);
        tv_rebounds = (TextView)itemView.findViewById(R.id.tv_rebounds);
        tv_steals = (TextView)itemView.findViewById(R.id.tv_steals);

    }

    public TextView getTv_points() {
        return tv_points;
    }

    public TextView getTv_assists() {
        return tv_assists;
    }

    public TextView getTv_steals() {
        return tv_steals;
    }

    public TextView getTv_rebounds() {
        return tv_rebounds;
    }
}
