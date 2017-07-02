package com.endeavour.ojasva.stats_recyclerview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.endeavour.ojasva.thebasketballapp.PlayerDetailsActivity;
import com.endeavour.ojasva.thebasketballapp.R;

/**
 * Created by Ojasva on 15-Apr-16.
 */
public class StatsPlayer_vh extends RecyclerView.ViewHolder implements View.OnClickListener
{
    private TextView tv_player_name,tv_stat,tv_stat_label,tv_player_id;
    private ImageView iv_player_photo,iv_team_logo;
    private Context context;

    public StatsPlayer_vh(View itemView)
    {
        super(itemView);

        tv_player_name =(TextView) itemView.findViewById(R.id.tv_player_name);
        tv_stat = (TextView) itemView.findViewById(R.id.tv_stat);
        tv_stat_label = (TextView)itemView.findViewById(R.id.tv_stat_label);
        tv_player_id =  (TextView)itemView.findViewById(R.id.tv_player_id);
        iv_player_photo = (ImageView) itemView.findViewById(R.id.iv_player_photo);
        iv_team_logo = (ImageView) itemView.findViewById(R.id.iv_logo);

        context = itemView.getContext();
        itemView.setOnClickListener(this);
    }


    public TextView getTv_player_name() {
        return tv_player_name;
    }

    public TextView getTv_stat() {
        return tv_stat;
    }

    public TextView getTv_stat_label() {
        return tv_stat_label;
    }

    public TextView getTv_player_id() {
        return tv_player_id;
    }

    public ImageView getIv_player_photo() {
        return iv_player_photo;
    }

    public ImageView getIv_team_logo() {
        return iv_team_logo;
    }

    @Override
    public void onClick(View v)
    {
        Intent intent = new Intent(context, PlayerDetailsActivity.class);
        intent.putExtra("Player ID",tv_player_id.getText().toString());
        context.startActivity(intent);
    }
}
