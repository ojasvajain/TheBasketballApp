package com.endeavour.ojasva.schedule_recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.endeavour.ojasva.stats_recyclerview.Label;
import com.endeavour.ojasva.stats_recyclerview.Label_vh;
import com.endeavour.ojasva.teamdetails_recyclerview.Match;
import com.endeavour.ojasva.teamdetails_recyclerview.Match_vh;
import com.endeavour.ojasva.thebasketballapp.R;

import java.util.List;

/**
 * Created by Ojasva on 18-Apr-16.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Object> items;
    final private int LABEL = 0, MATCH = 1,TOP_LABEL=2;

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

            case MATCH:
                View v2 = inflater.inflate(R.layout.match_vh, parent, false);
                viewHolder = new Match_vh(v2);
                break;

            case TOP_LABEL:
                View v3 = inflater.inflate(R.layout.top_label_vh,parent,false);
                viewHolder = new Label_vh(v3);
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

            case MATCH:
                Match_vh vh2 = (Match_vh) holder;
                configureMatch_vh(vh2, position);
                break;
            case TOP_LABEL:
                Label_vh vh3 = (Label_vh)holder;
                configureLabel_vh(vh3,position);
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
            Label label = (Label) items.get(position);
            if (label.isTopLabel())
                return TOP_LABEL;
            else
                return LABEL;
        }
        if (items.get(position) instanceof Match)
            return MATCH;
        return -1;
    }


    private void configureMatch_vh(Match_vh vh3, int position) {
        Match match = (Match) items.get(position);
        if (match != null) {
            vh3.getHome_logo().setImageResource(getResourceId(match.getHome_team(), true));
            vh3.getAway_logo().setImageResource(getResourceId(match.getAway_team(), true));
            vh3.getHome_team().setText(match.getHome_team());
            vh3.getAway_team().setText(match.getAway_team());
            vh3.getHome_score().setText("" + (match.getHome_score() == -1 ? "" : match.getHome_score()));
            vh3.getAway_score().setText("" + (match.getAway_score() == -1 ? "" : match.getAway_score()));
            vh3.getVenue().setText(match.getVenue());
            vh3.getDate().setText(match.getDate());
            vh3.getCity().setText(match.getCity());
            vh3.getLabel().setText(match.getLabel());
            vh3.getId_home_team().setText(match.getId_home_team());
            vh3.getId_away_team().setText(match.getId_away_team());
            vh3.getId_game().setText(match.getId_game());
        }
    }

    private void configureLabel_vh(Label_vh vh1, int position) {
        Label label = (Label) items.get(position);

        if (label != null) {
            vh1.getLabel().setText(label.getLabel());
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

    public void swapData(List<Object> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }
}