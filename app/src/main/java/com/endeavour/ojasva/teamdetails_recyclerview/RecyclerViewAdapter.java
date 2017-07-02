package com.endeavour.ojasva.teamdetails_recyclerview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.endeavour.ojasva.thebasketballapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by Ojasva on 02-Apr-16.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context; //Required for getting resource ids
    private List<Object> items;
    final private int EMBLEM_TNAME = 0, COACH = 1, MATCH = 2, FORM = 3, STATS = 4, PLAYER = 5, VENUE = 6;

    public RecyclerViewAdapter(List<Object> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case EMBLEM_TNAME:
                View v1 = inflater.inflate(R.layout.emblem_tname_vh, parent, false);
                viewHolder = new Emblem_Tname_vh(v1);
                break;
            case COACH:
                View v2 = inflater.inflate(R.layout.coach_vh, parent, false);
                viewHolder = new Coach_vh(v2);
                break;
            case MATCH:
                View v3 = inflater.inflate(R.layout.match_vh, parent, false);
                viewHolder = new Match_vh(v3);
                break;
            case FORM:
                View v4 = inflater.inflate(R.layout.form_vh, parent, false);
                viewHolder = new Form_vh(v4);
                break;
            case STATS:
                View v5 = inflater.inflate(R.layout.stats_vh, parent, false);
                viewHolder = new Stats_vh(v5);
                break;
            case PLAYER:
                View v6 = inflater.inflate(R.layout.player_vh, parent, false);
                viewHolder = new Player_vh(v6);
                break;
            case VENUE:
                View v7 = inflater.inflate(R.layout.coach_vh, parent, false);   //using the same layout for coach and venue
                viewHolder = new Venue_vh(v7);
                break;
        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case EMBLEM_TNAME:
                Emblem_Tname_vh vh1 = (Emblem_Tname_vh) holder;
                configureEmblem_Tname_vh(vh1, position);
                break;
            case COACH:
                Coach_vh vh2 = (Coach_vh) holder;
                configureCoach_vh(vh2, position);
                break;
            case MATCH:
                Match_vh vh3 = (Match_vh) holder;
                configureMatch_vh(vh3, position);
                break;
            case FORM:
                Form_vh vh4 = (Form_vh) holder;
                configureForm_vh(vh4, position);
                break;
            case STATS:
                Stats_vh vh5 = (Stats_vh) holder;
                configureStats_vh(vh5, position);
                break;
            case PLAYER:
                Player_vh vh6 = (Player_vh) holder;
                configurePlayer_vh(vh6, position);
                break;
            case VENUE:
                Venue_vh vh7 = (Venue_vh) holder;
                configureVenueCoach_vh(vh7, position);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (items.get(position) instanceof Emblem_Tname) {
            return EMBLEM_TNAME;
        } else if (items.get(position) instanceof Coach) {
            return COACH;
        } else if (items.get(position) instanceof Match) {
            return MATCH;
        } else if (items.get(position) instanceof Form) {
            return FORM;
        } else if (items.get(position) instanceof Stats) {
            return STATS;
        } else if (items.get(position) instanceof Player) {
            return PLAYER;
        } else if (items.get(position) instanceof Venue) {
            return VENUE;
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

    private void configureCoach_vh(Coach_vh vh2, int position) {
        Coach coach = (Coach) items.get(position);
        if (coach != null) {
            vh2.getCoach().setText(coach.getCoach());
            vh2.getLabel().setText("Coach");
        }
    }

    private void configureMatch_vh(Match_vh vh3, int position) {
        Match match = (Match) items.get(position);
        if (match != null) {
            vh3.getHome_logo().setImageResource(getResourceId(match.getHome_team(),true));
            vh3.getAway_logo().setImageResource(getResourceId(match.getAway_team(),true));
            vh3.getHome_team().setText(match.getHome_team());
            vh3.getAway_team().setText(match.getAway_team());
            vh3.getHome_score().setText("" + (match.getHome_score()==-1?"":match.getHome_score()));
            vh3.getAway_score().setText("" + (match.getAway_score()==-1?"":match.getAway_score()));
            vh3.getVenue().setText(match.getVenue());
            vh3.getDate().setText(match.getDate());
            vh3.getCity().setText(match.getCity());
            vh3.getLabel().setText(match.getLabel());
            vh3.getId_home_team().setText(match.getId_home_team());
            vh3.getId_away_team().setText(match.getId_away_team());
            vh3.getId_game().setText(match.getId_game());
        }
    }

    private void configureForm_vh(Form_vh vh4, int position) {
        Form form = (Form) items.get(position);
        if (form != null) {
            vh4.getForm1().setImageResource(form.f[0] == true ? R.drawable.won : R.drawable.lost);
            vh4.getForm2().setImageResource(form.f[1] == true ? R.drawable.won : R.drawable.lost);
            vh4.getForm3().setImageResource(form.f[2] == true ? R.drawable.won : R.drawable.lost);
            vh4.getForm4().setImageResource(form.f[3] == true ? R.drawable.won : R.drawable.lost);
            vh4.getForm5().setImageResource(form.f[4] == true ? R.drawable.won : R.drawable.lost);
        }
    }

    private void configureStats_vh(Stats_vh vh5, int position) {
        Stats stats = (Stats) items.get(position);
        if (stats != null) {
            vh5.getPct().setText(stats.getPct() + "");
            vh5.getPos().setText("Position:" + stats.getPosition());
            vh5.getPlayed().setText("P:" + stats.getPlayed() + "");
            vh5.getWon().setText("W:" + stats.getWon() + "");
            vh5.getLost().setText("L:" + stats.getLost() + "");
        }
    }

    private void configurePlayer_vh(Player_vh vh6, int position) {
        Player player = (Player) items.get(position);
        if (player != null) {
            vh6.getPlayer_name().setText(player.getPlayer_name());
            vh6.getPlayer_photo().setImageResource(getResourceId(player.getPlayer_name(),false));
            vh6.getPlayer_position().setText(player.getPlayer_position());
            vh6.getPlayer_id().setText(player.getPlayer_id() + "");
            ;

        }
    }

    private void configureVenueCoach_vh(Venue_vh vh7, int position) {
        Venue venue = (Venue) items.get(position);
        if (venue != null) {
            vh7.getTv_venue().setText(venue.getVenue());
            vh7.getLabel().setText("Home");
        }
    }


    public void swapData(List<Object> items) {
        this.items = items;
        notifyDataSetChanged();

    }

    public int getResourceId(String name,boolean isSmall) //replace spaces with underscore and everything in lower case
    {
        for (int i = 0; i < name.length(); i++) {
            if (!((name.charAt(i) >= 65 && name.charAt(i) <= 90) || (name.charAt(i) >= 97 && name.charAt(i) <= 122) || (name.charAt(i) >= '0' && name.charAt(i) <= '9')))  //not an alphabet or number
            {
                name = name.replace(name.charAt(i), '_');
            }
        }
        String res_name = name.toLowerCase();

        if(isSmall)
            res_name+="_small";
        int resId = context.getResources().getIdentifier(res_name, "drawable", context.getPackageName());
        return resId;

    }
}

