package com.endeavour.ojasva.stats_recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.endeavour.ojasva.thebasketballapp.R;

/**
 * Created by Ojasva on 15-Apr-16.
 */
public class Label_vh extends RecyclerView.ViewHolder
{
    private TextView label;

    public Label_vh(View itemView)
    {
        super(itemView);

        label = (TextView)itemView.findViewById(R.id.tv_stats_label);
    }

    public TextView getLabel() {
        return label;
    }

}
