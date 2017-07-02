package com.endeavour.ojasva.teamdetails_recyclerview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.endeavour.ojasva.thebasketballapp.PlayerDetailsActivity;
import com.endeavour.ojasva.thebasketballapp.R;
import com.endeavour.ojasva.thebasketballapp.StandingsActivity;

/**
 * Created by Ojasva on 04-Apr-16.
 */
public class Player_vh extends RecyclerView.ViewHolder implements View.OnClickListener
{
    private ImageView player_photo;
    private TextView player_name;
    private TextView player_id;
    private TextView player_position;
    private Context context;

    public Player_vh (View viewHolder)
    {
        super(viewHolder);
        player_name = (TextView)viewHolder.findViewById(R.id.tv_player_name);
        player_photo = (ImageView)viewHolder.findViewById(R.id.iv_player_photo);
        player_position = (TextView)viewHolder.findViewById(R.id.tv_player_position);
        player_id = (TextView)viewHolder.findViewById(R.id.tv_player_id);

        context = viewHolder.getContext();

        viewHolder.setOnClickListener(this);
    }

    public ImageView getPlayer_photo() {
        return player_photo;
    }


    public TextView getPlayer_name() {
        return player_name;
    }

    public TextView getPlayer_position()
    {
        return player_position;
    }

    public TextView getPlayer_id(){
        return player_id;
    }

    @Override
    public void onClick(View view)
    {
        Intent intent = new Intent(context,PlayerDetailsActivity.class);
        intent.putExtra("Player ID",player_id.getText().toString());
        context.startActivity(intent);
    }

}
