package com.endeavour.ojasva.teamdetails_recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.endeavour.ojasva.thebasketballapp.R;

/**
 * Created by Ojasva on 02-Apr-16.
 */
public class Emblem_Tname_vh extends RecyclerView.ViewHolder
{

    private ImageView logo;   //image_id = "team" + team_id
    private TextView name;

    public Emblem_Tname_vh(View v) {
        super(v);
        logo = (ImageView)v.findViewById(R.id.iv_logo);
        name = (TextView)v.findViewById(R.id.tv_teamname);
    }

    public ImageView getLogo() {
        return logo;
    }

    public void setLogo(ImageView logo) {
        this.logo = logo;
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }
}
