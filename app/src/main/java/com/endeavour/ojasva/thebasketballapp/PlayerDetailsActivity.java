package com.endeavour.ojasva.thebasketballapp;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.endeavour.ojasva.aceqlapi.AceQLDBManager;
import com.endeavour.ojasva.playerdetails_recyclerview.Plays_for_vh;
import com.endeavour.ojasva.playerdetails_recyclerview.RecyclerViewAdapter;
import com.endeavour.ojasva.teamdetails_recyclerview.SpacesItemDecoration;


import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class PlayerDetailsActivity extends AppCompatActivity {

    private RecyclerViewAdapter recyclerViewAdapter;
    private List<String> queries;
    private List<Object> details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_details);
        details = new ArrayList<>();

        String ur="jdbc:aceql:http://"+"192.168.157.1"+":9090/ServerSqlManager";
        AceQLDBManager.initialize(ur, getString(R.string.username), getString(R.string.password));

        //toolbar stuff
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


        String player_id = getIntent().getStringExtra("Player ID");


        initializeQueries(player_id);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv_playerdetails);
        recyclerViewAdapter = new RecyclerViewAdapter(details,this,PlayerDetailsActivity.this);
        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SpacesItemDecoration(30));   //to provide space between details

        getLoaderManager().initLoader(0, null, loaderCallbacks);      //loading in background


    }

    private LoaderManager.LoaderCallbacks<List<Object>> loaderCallbacks = new LoaderManager.LoaderCallbacks<List<Object>>()
    {
        @Override
        public Loader<List<Object>> onCreateLoader(int id, Bundle args) {
            return new PlayerDetailsLoader(queries,getApplicationContext());
        }

        @Override
        public void onLoadFinished(Loader<List<Object>> loader, List<Object> details) {

            removeThrobber();
            recyclerViewAdapter.swapData(details); //There was nothing in the adapter previously. It will be swapped by data fetched from db.
            getLoaderManager().destroyLoader(0);
        }

        @Override
        public void onLoaderReset(Loader<List<Object>> loader) {

            recyclerViewAdapter.swapData(details);

        }
    };

    public void removeThrobber() {
        GifImageView throbber = (GifImageView) findViewById(R.id.throbber);
        RelativeLayout this_layout = (RelativeLayout) findViewById(R.id.layout_player_details);
        this_layout.removeView(throbber);
    }

    public void initializeQueries(String player_id)
    {
        queries = new ArrayList<>();

        //get player details
        queries.add("select fname,lname,position,college.name as college,dob,height,weight,salary,team.name as plays_for,team.id as plays_for_id from player,team,college where player.id = " + player_id + " and team.id in(select id_team from players_teams where id_player = " + player_id + ") and college.id = player.id_college");
        //get stats
        queries.add("select avg(points) as points,avg(assists) as assists,avg(rebounds) as rebounds,avg(steals) as steals from game_performance where id_player="+player_id);
    }










}





