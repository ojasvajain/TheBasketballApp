package com.endeavour.ojasva.stats_recyclerview;

import android.app.ActionBar;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.endeavour.ojasva.teamdetails_recyclerview.Match;
import com.endeavour.ojasva.teamdetails_recyclerview.Match_vh;
import com.endeavour.ojasva.thebasketballapp.R;

import java.util.List;

/**
 * Created by Ojasva on 15-Apr-16.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Object> items;    //both labels and stats
    final private int LABEL=0,STAT_LEADER=1,STAT_OTHER=2,TOP_LABEL=3;

    public RecyclerViewAdapter(Context context, List<Object> items) {
        this.context = context;
        this.items = items;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case LABEL:
                View v1 = inflater.inflate(R.layout.stats_label_vh, parent, false);
                viewHolder = new Label_vh(v1);
                break;
            case STAT_LEADER:
                View v2 = inflater.inflate(R.layout.stats_lead_player_vh, parent, false);
                viewHolder = new StatsPlayer_vh(v2);
                break;
            case STAT_OTHER:
                View v3 = inflater.inflate(R.layout.stats_other_players_vh,parent,false);
                viewHolder = new StatsPlayer_vh(v3);
                break;
            case TOP_LABEL:
                View v4 = inflater.inflate(R.layout.top_label_vh,parent,false);
                viewHolder = new Label_vh(v4);
                break;
        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case LABEL:
                Label_vh vh1 = (Label_vh) holder;
                configureLabel_vh(vh1, position);
                break;
            case STAT_LEADER:
                StatsPlayer_vh vh2 = (StatsPlayer_vh) holder;
                configureStatsPlayer_vh(vh2, position);
                break;
            case STAT_OTHER:
                StatsPlayer_vh vh3 = (StatsPlayer_vh) holder;
                configureStatsPlayer_vh(vh3, position);
                break;
            case TOP_LABEL:
                Label_vh vh4 = (Label_vh) holder;
                configureLabel_vh(vh4,position);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (items.get(position) instanceof Label) {
            {
                Label label = (Label)items.get(position);
                if(label.isTopLabel())
                    return TOP_LABEL;
                else
                    return LABEL;

            }
        } else if (items.get(position) instanceof StatsPlayer) {

            StatsPlayer temp = (StatsPlayer)items.get(position);

            if(temp.isOtherPlayer())
                return STAT_OTHER;
            else
                return STAT_LEADER;

        }

        return -1;
    }

    private void configureLabel_vh(Label_vh vh1,int position)
    {
        Label label = (Label) items.get(position);

        if(label!=null)
        {
            vh1.getLabel().setText(label.getLabel());
        }
    }



    private void configureStatsPlayer_vh(StatsPlayer_vh vh3, int position) {
        StatsPlayer statsPlayer = (StatsPlayer) items.get(position);

        int choice = statsPlayer.isOtherPlayer()?3:2;  //if he is an other player, then use smaller pic for club logo

        if (statsPlayer != null) {
            vh3.getTv_player_name().setText(statsPlayer.getPlayer_name());
            vh3.getTv_player_id().setText(statsPlayer.getPlayer_id() + "");
            vh3.getIv_player_photo().setImageResource(getResourceId(statsPlayer.getPlayer_name(), 1));
            vh3.getIv_team_logo().setImageResource(getResourceId(statsPlayer.getClub_name(), choice));
            vh3.getTv_stat().setText(statsPlayer.getStat() + "");
            vh3.getTv_stat_label().setText(statsPlayer.getStat_label());
        }


    }



    public int getResourceId(String name, int choice) //replace spaces with underscore and everything in lower case
    {
        for (int i = 0; i < name.length(); i++) {
            if (!((name.charAt(i) >= 65 && name.charAt(i) <= 90) || (name.charAt(i) >= 97 && name.charAt(i) <= 122) || (name.charAt(i) >= '0' && name.charAt(i) <= '9')))  //not an alphabet or number
            {
                name = name.replace(name.charAt(i), '_');
            }
        }
        String res_name = name.toLowerCase();

        if (choice==2)
            res_name += "_small";
        else if(choice==3)
            res_name+="_smaller";
        int resId = context.getResources().getIdentifier(res_name, "drawable", context.getPackageName());
        return resId;

    }

    public void swapData(List<Object> items) {
        this.items = items;
        notifyDataSetChanged();

    }
}
