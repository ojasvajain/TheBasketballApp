package com.endeavour.ojasva.thebasketballapp;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.endeavour.ojasva.aceqlapi.AceQLDBManager;
import com.endeavour.ojasva.teamdetails_recyclerview.Match;
import com.endeavour.ojasva.teamdetails_recyclerview.SpacesItemDecoration;
import com.endeavour.ojasva.teamschedule_recyclerview.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class ScheduleActivity extends AppCompatActivity {

    private List<Match> schedule;
    private RecyclerViewAdapter recyclerViewAdapter;
    private String query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        String ur="jdbc:aceql:http://"+"192.168.157.1"+":9090/ServerSqlManager";
        AceQLDBManager.initialize(ur, getString(R.string.username), getString(R.string.password));

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_36dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });

        int team_id = getIntent().getIntExtra("Team ID", 1);
        query = "with match(id,id_home_team,id_away_team,home_team,away_team,home_score,away_score,venue,city,date_played) as (select game.id as id,team1.id as id_home_team,team2.id as id_away_team,team1.name as home_team,team2.name as away_team,home_score,away_score,venue.name,city,date_played from team team1,game,team team2,venue,location where id_home_team=team1.id and id_away_team=team2.id and (id_home_team="+team_id+" or id_away_team="+team_id+" ) and team1.id_venue=venue.id and venue.zip_location=zip order by date_played) select * from match";

        schedule = new ArrayList<>();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_schedule);
        recyclerViewAdapter = new RecyclerViewAdapter(schedule, this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SpacesItemDecoration(30));   //to provide space between details

        getLoaderManager().initLoader(0, null, loaderCallbacks);      //loading in background
    }

        private LoaderManager.LoaderCallbacks<List<Match>> loaderCallbacks = new LoaderManager.LoaderCallbacks<List<Match>>()
        {
            @Override
            public Loader<List<Match>> onCreateLoader(int id, Bundle args) {
                Log.i("abc", "creating loader object");
                return new ScheduleLoader(query,getApplicationContext());
            }

            @Override
            public void onLoadFinished(Loader<List<Match>> loader, List<Match> schedule) {

                removeThrobber();
                Log.i("abc", "" + schedule.size());;
                recyclerViewAdapter.swapData(schedule); //There was nothing in the adapter previously. It will be swapped by data fetched from db.
                getLoaderManager().destroyLoader(0);
            }

            @Override
            public void onLoaderReset(Loader<List<Match>> loader) {

                recyclerViewAdapter.swapData(schedule);

            }
        };

    public void removeThrobber() {
        GifImageView throbber = (GifImageView) findViewById(R.id.throbber);
        RelativeLayout this_layout = (RelativeLayout) findViewById(R.id.activity_schedule);
        this_layout.removeView(throbber);
    }




}
