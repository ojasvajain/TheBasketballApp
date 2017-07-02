package com.endeavour.ojasva.teamdetails_recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.endeavour.ojasva.thebasketballapp.R;

/**
 * Created by Ojasva on 02-Apr-16.
 */
public class Coach_vh extends RecyclerView.ViewHolder
{
    private TextView coach;
    private TextView label;

    public Coach_vh(View itemView) {
        super(itemView);
        coach = (TextView)itemView.findViewById(R.id.coach);
        label = (TextView)itemView.findViewById(R.id.coach_label);
    }

    public TextView getCoach() {
        return coach;
    }
    public TextView getLabel()
    {
        return label;
    }


}
