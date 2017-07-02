package com.endeavour.ojasva.teamdetails_recyclerview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.endeavour.ojasva.thebasketballapp.MatchPerformanceActivity;
import com.endeavour.ojasva.thebasketballapp.R;

/**
 * Created by Ojasva on 02-Apr-16.
 */
public class Match_vh extends RecyclerView.ViewHolder implements View.OnClickListener
{

    private ImageView home_logo,away_logo;
    private TextView home_team,away_team,home_score,away_score,venue,date,city,label,id_home_team,id_away_team,id_game;  //0-home_team, 1-away_team, 2-home score,3-away score,4 -venue,5-date,6-city,7-home id,8-away id
    private Context context;

    public Match_vh(View itemView)
    {
        super(itemView);
        home_logo=(ImageView)itemView.findViewById(R.id.iv_home_logo);
        away_logo=(ImageView)itemView.findViewById(R.id.iv_away_logo);
        home_team=(TextView)itemView.findViewById(R.id.tv_home_team);
        away_team=(TextView)itemView.findViewById(R.id.tv_away_team);
        home_score=(TextView)itemView.findViewById(R.id.tv_home_score);
        away_score=(TextView)itemView.findViewById(R.id.tv_away_score);
        venue=(TextView)itemView.findViewById(R.id.tv_venue);
        date=(TextView)itemView.findViewById(R.id.tv_date);
        city=(TextView)itemView.findViewById(R.id.tv_city);
        label = (TextView)itemView.findViewById(R.id.tv_label);
        id_away_team = (TextView)itemView.findViewById(R.id.tv_id_away_team);
        id_home_team = (TextView)itemView.findViewById(R.id.tv_id_home_team);
        id_game = (TextView)itemView.findViewById(R.id.tv_game_id);

        context=itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public TextView getDate() {
        return date;
    }


    public TextView getVenue() {
        return venue;
    }


    public TextView getAway_score() {
        return away_score;
    }


    public TextView getHome_score() {
        return home_score;
    }

    public TextView getId_home_team() {
        return id_home_team;
    }

    public TextView getId_away_team() {
        return id_away_team;
    }

    public TextView getAway_team() {
        return away_team;
    }


    public TextView getHome_team() {
        return home_team;
    }


    public ImageView getAway_logo() {
        return away_logo;
    }


    public ImageView getHome_logo() {
        return home_logo;
    }

    public TextView getCity()
    {
        return city;
    }

    public TextView getLabel()
    {
        return label;
    }

    public TextView getId_game() {
        return id_game;
    }

    @Override
    public void onClick(View view)
    {
        if((!(label.getText().toString().equals("")) && !(home_score.getText().toString().equals(""))))   // if it is not an unplayed match or if there is label
        {
            String[] parcel = new String[]{home_team.getText().toString(), away_team.getText().toString(), home_score.getText().toString(), away_score.getText().toString()
                    , venue.getText().toString(), date.getText().toString(), city.getText().toString(), id_home_team.getText().toString(), id_away_team.getText().toString(),id_game.getText().toString()};

            Intent intent = new Intent(context, MatchPerformanceActivity.class);
            intent.putExtra("Match Info", parcel);
            context.startActivity(intent);
        }
    }

}
