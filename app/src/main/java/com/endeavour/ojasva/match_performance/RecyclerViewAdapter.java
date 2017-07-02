package com.endeavour.ojasva.match_performance;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.endeavour.ojasva.teamdetails_recyclerview.Match;
import com.endeavour.ojasva.teamdetails_recyclerview.Match_vh;
import com.endeavour.ojasva.thebasketballapp.R;

/**
 * Created by Ojasva on 11-Apr-16.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Context context;
    private Match match;
    final private int MATCH=0;


    public RecyclerViewAdapter(Context context,Match match)
    {
            this.context = context;
            this.match = match;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder=null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case MATCH:
                View v1 = inflater.inflate(R.layout.match_vh, parent, false);
                viewHolder = new Match_vh(v1);
                break;
        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case MATCH:
                Match_vh vh1 = (Match_vh) holder;
                configureMatch_vh(vh1, position);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {

            return MATCH;
    }

    private void configureMatch_vh(Match_vh vh3, int position) {
        Match match = this.match;
        if (match != null) {
            vh3.getHome_logo().setImageResource(getResourceId(match.getHome_team(),true));
            vh3.getAway_logo().setImageResource(getResourceId(match.getAway_team(),true));
            vh3.getHome_team().setText(match.getHome_team());
            vh3.getAway_team().setText(match.getAway_team());
            vh3.getHome_score().setText("" + match.getHome_score());
            vh3.getAway_score().setText("" + match.getAway_score());
            vh3.getVenue().setText(match.getVenue());
            vh3.getDate().setText(match.getDate());
            vh3.getCity().setText(match.getCity());
            vh3.getLabel().setText(match.getLabel());
            vh3.getId_home_team().setText(match.getId_home_team());
            vh3.getId_away_team().setText(match.getId_away_team());
            vh3.getId_game().setText(match.getId_game());
        }
    }

    public int getResourceId(String name,boolean isSmall) //replace spaces with underscore and everything in lower case
    {
        for(int i=0;i<name.length();i++)
        {
            if(!((name.charAt(i) >=65 && name.charAt(i) <=90) || (name.charAt(i)>=97 && name.charAt(i)<=122) || (name.charAt(i)>='0' && name.charAt(i)<='9')))  //not an alphabet or a number
            {
                name=name.replace(name.charAt(i),'_');
            }
        }
        String res_name = name.toLowerCase();
        if(isSmall)   //for loading logo of team
            res_name+="_small";
        int resId = context.getResources().getIdentifier(res_name, "drawable", context.getPackageName());
        return resId;
    }

}
