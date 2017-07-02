package com.endeavour.ojasva.playerdetails_recyclerview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.endeavour.ojasva.thebasketballapp.PlayerDetailsActivity;
import com.endeavour.ojasva.thebasketballapp.R;
import com.endeavour.ojasva.thebasketballapp.TeamDetailsActivity;

/**
 * Created by Ojasva on 09-Apr-16.
 */
public class Plays_for_vh extends RecyclerView.ViewHolder
{
    private TextView tv_plays_for;
    private ImageView iv_plays_for;
    private TextView tv_team_id;
    private Context context;
    private Activity activity;

    public Plays_for_vh(View viewHolder,Activity activity)
    {
        super(viewHolder);
        tv_plays_for = (TextView)viewHolder.findViewById(R.id.tv_plays_for);
        iv_plays_for  = (ImageView)viewHolder.findViewById(R.id.iv_plays_for);
        tv_team_id = (TextView)viewHolder.findViewById(R.id.tv_team_id);

        context = viewHolder.getContext();

        this.activity=activity;

    }

    public TextView getTv_plays_for() {
        return tv_plays_for;
    }

    public ImageView getIv_plays_for() {
        return iv_plays_for;
    }

    public TextView getTv_team_id() {
        return tv_team_id;
    }




}
