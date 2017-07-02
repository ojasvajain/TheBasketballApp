package com.endeavour.ojasva.playerlist_recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.endeavour.ojasva.teamdetails_recyclerview.Player;
import com.endeavour.ojasva.teamdetails_recyclerview.Player_vh;

import com.endeavour.ojasva.thebasketballapp.R;

import java.util.List;

/**
 * Created by Ojasva on 14-Apr-16.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Context context;
    private List<Player> playerList;
    final private int PLAYER=0;
    
    public RecyclerViewAdapter(Context context,List<Player> playerList)
    {
        this.context = context;
        this.playerList = playerList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case PLAYER:
                View v6 = inflater.inflate(R.layout.player_vh, parent, false);
                viewHolder = new Player_vh(v6);
                break;
        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {

            case PLAYER:
                Player_vh vh6 = (Player_vh) holder;
                configurePlayer_vh(vh6, position);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return this.playerList.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (playerList.get(position) instanceof Player)
            return PLAYER;
        return -1;
    }



    private void configurePlayer_vh(Player_vh vh6, int position) {
        Player player = playerList.get(position);
        if (player != null) {
            vh6.getPlayer_name().setText(player.getPlayer_name());
            vh6.getPlayer_photo().setImageResource(getResourceId(player.getPlayer_name(), false));
            vh6.getPlayer_position().setText(player.getPlayer_position());
            vh6.getPlayer_id().setText(player.getPlayer_id() + "");

        }
    }

    public int getResourceId(String name, boolean isSmall) //replace spaces with underscore and everything in lower case
    {
        for (int i = 0; i < name.length(); i++) {
            if (!((name.charAt(i) >= 65 && name.charAt(i) <= 90) || (name.charAt(i) >= 97 && name.charAt(i) <= 122) || (name.charAt(i) >= '0' && name.charAt(i) <= '9')))  //not an alphabet or number
            {
                name = name.replace(name.charAt(i), '_');
            }
        }
        String res_name = name.toLowerCase();

        if (isSmall)
            res_name += "_small";
        int resId = context.getResources().getIdentifier(res_name, "drawable", context.getPackageName());
        return resId;

    }

    public void swapData(List<Player> playerList)
    {
        this.playerList.clear();
        this.playerList.addAll(playerList);
        notifyDataSetChanged();
    }

}

