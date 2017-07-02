package com.endeavour.ojasva.playerdetails_recyclerview;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.endeavour.ojasva.teamdetails_recyclerview.Coach_vh;
import com.endeavour.ojasva.teamdetails_recyclerview.Emblem_Tname;
import com.endeavour.ojasva.teamdetails_recyclerview.Emblem_Tname_vh;

import com.endeavour.ojasva.thebasketballapp.R;

import java.util.List;

/**
 * Created by Ojasva on 08-Apr-16.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Activity activity;
    private Context context; //Required for getting resource ids
    private List<Object> items;
    final private int PHOTO_PNAME=0,HWDOBS=1,PLAYSFOR=2,STATS=3;
    public RecyclerViewAdapter(List<Object> items,Context context,Activity activity)
    {
        this.items=items;
        this.context=context;
        this.activity=activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder=null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case PHOTO_PNAME:
                View v1 = inflater.inflate(R.layout.emblem_tname_vh, parent, false);
                viewHolder = new Emblem_Tname_vh(v1);
                break;
            case HWDOBS:
                View v2 = inflater.inflate(R.layout.coach_vh, parent, false);
                viewHolder = new Coach_vh(v2);
                break;
            case PLAYSFOR:
                View v3 = inflater.inflate(R.layout.plays_for_vh,parent,false);
                viewHolder = new Plays_for_vh(v3,activity);
                break;
            case STATS:
                View v4=inflater.inflate(R.layout.player_details_stats_vh,parent,false);
                viewHolder = new Stats_vh(v4);
                break;
        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case PHOTO_PNAME:
                Emblem_Tname_vh vh1 = (Emblem_Tname_vh) holder;
                configureEmblem_Tname_vh(vh1, position);
                break;
            case HWDOBS:
                Coach_vh vh2 = (Coach_vh) holder;
                configureCoach_vh(vh2, position);
                break;
            case PLAYSFOR:
                Plays_for_vh vh3 = (Plays_for_vh) holder;
                configurePlays_for_vh(vh3,position);
                break;
            case STATS:
                Stats_vh vh4 = (Stats_vh) holder;
                configureStats_vh(vh4,position);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public int getItemViewType(int position) {

        if(items.get(position) instanceof Emblem_Tname)
        {
            return PHOTO_PNAME;
        }
        else if(items.get(position) instanceof HWDOBS)
        {
            return HWDOBS;
        }
        else if(items.get(position) instanceof Plays_for)
        {
            return PLAYSFOR;
        }
        else if(items.get(position) instanceof Stats)
        {
            return STATS;
        }
        return -1;
    }

    private void configureEmblem_Tname_vh(Emblem_Tname_vh vh1, int position) {
        Emblem_Tname et = (Emblem_Tname) items.get(position);
        if (et != null) {
            vh1.getLogo().setImageResource(getResourceId(et.getTeam(),false));
            vh1.getName().setText(et.getTeam());
        }
    }

    private void configureCoach_vh(Coach_vh vh2,int position) {
        HWDOBS hwdobs= (HWDOBS) items.get(position);
        if(hwdobs!=null)
        {
            vh2.getCoach().setText(hwdobs.getContent());
            vh2.getLabel().setText(hwdobs.getLabel());
        }
    }

    private void configurePlays_for_vh(Plays_for_vh vh3,int position)
    {
        Plays_for plays_for = (Plays_for) items.get(position);
        if(plays_for!=null)
        {
            vh3.getTv_plays_for().setText(plays_for.getPlays_for());
            vh3.getIv_plays_for().setImageResource(getResourceId(plays_for.getPlays_for(), true));
            vh3.getTv_team_id().setText(plays_for.getTeam_id()+"");
        }
    }

    private void configureStats_vh(Stats_vh vh4,int position)
    {
        Stats stats = (Stats) items.get(position);
        if(stats!=null)
        {
            vh4.getTv_points().setText(stats.getPoints()+"");
            vh4.getTv_assists().setText(stats.getAssists()+"");
            vh4.getTv_rebounds().setText(stats.getRebounds()+"");
            vh4.getTv_steals().setText(stats.getSteals()+"");
        }
    }



    public void swapData(List<Object> items)
    {
        this.items=items;
        notifyDataSetChanged();

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
        if(isSmall)   //for laoding logo of team
            res_name+="_small";
        int resId = context.getResources().getIdentifier(res_name, "drawable", context.getPackageName());
        return resId;
    }


}
