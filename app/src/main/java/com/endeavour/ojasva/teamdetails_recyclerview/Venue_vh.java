package com.endeavour.ojasva.teamdetails_recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.endeavour.ojasva.thebasketballapp.R;

/**
 * Created by Ojasva on 06-Apr-16.
 */
public class Venue_vh extends RecyclerView.ViewHolder
{
    private TextView tv_venue;
    private TextView label;

    public Venue_vh(View itemView)
    {
        super(itemView);
        tv_venue = (TextView)itemView.findViewById(R.id.coach);
        label = (TextView)itemView.findViewById(R.id.coach_label);
    }

    public TextView getTv_venue() {
        return tv_venue;
    }

    public TextView getLabel()
    {
        return label;
    }
}
