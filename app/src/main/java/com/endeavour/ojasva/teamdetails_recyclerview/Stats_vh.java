package com.endeavour.ojasva.teamdetails_recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.endeavour.ojasva.thebasketballapp.R;

/**
 * Created by Ojasva on 03-Apr-16.
 */
public class Stats_vh extends RecyclerView.ViewHolder
{
    private TextView pct;
    private TextView position;
    private TextView played;
    private TextView won;
    private TextView lost;

    public Stats_vh(View viewHolder)
    {
        super(viewHolder);
        pct = (TextView)viewHolder.findViewById(R.id.tv_pct);
        position = (TextView)viewHolder.findViewById(R.id.tv_position);
        played = (TextView)viewHolder.findViewById(R.id.tv_played);
        won = (TextView)viewHolder.findViewById(R.id.tv_won);
        lost = (TextView)viewHolder.findViewById(R.id.tv_lost);
    }

    public TextView getWon() {
        return won;
    }


    public TextView getLost() {
        return lost;
    }

    public TextView getPos() {   //getPosition is not accepted
        return position;
    }

    public TextView getPlayed() {
        return played;
    }

    public TextView getPct() {
        return pct;
    }
}
