package com.endeavour.ojasva.thebasketballapp;

import android.app.LoaderManager;
import android.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.endeavour.ojasva.aceqlapi.AceQLDBManager;
import com.endeavour.ojasva.stats_recyclerview.RecyclerViewAdapter;
import com.endeavour.ojasva.teamdetails_recyclerview.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class HomeActivity extends AppCompatActivity {

    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Object> items;
    private List<String> queries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeQueries();

        items = new ArrayList<>();

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


        NavigationDrawerFragment navigationDrawerFragment = (NavigationDrawerFragment)
        getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        navigationDrawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.id.fragment_navigation_drawer);



        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv_stats);
        recyclerViewAdapter = new RecyclerViewAdapter(this,items);
        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SpacesItemDecoration(30));   //to provide space between details

        getLoaderManager().initLoader(0, null, loaderCallbacks);      //loading in background

    }


    private LoaderManager.LoaderCallbacks<List<Object>> loaderCallbacks = new LoaderManager.LoaderCallbacks<List<Object>>()
    {
        @Override
        public Loader<List<Object>> onCreateLoader(int id, Bundle args) {
            return new HomeLoader(getApplicationContext(),queries);
        }

        @Override public void onLoadFinished(Loader<List<Object>> loader, List<Object> items) {

            removeThrobber();
            recyclerViewAdapter.swapData(items); //There was nothing in the adapter previously. It will be swapped by data fetched from db.
            getLoaderManager().destroyLoader(0);
        }

        @Override
        public void onLoaderReset(Loader<List<Object>> loader) {

            recyclerViewAdapter.swapData(items);

        }
    };

    public void removeThrobber() {
        GifImageView throbber = (GifImageView) findViewById(R.id.throbber);
        RelativeLayout this_layout = (RelativeLayout) findViewById(R.id.home_layout);
        this_layout.removeView(throbber);
    }



    public void initializeQueries()
    {
        queries = new ArrayList<>();
        //points
        queries.add("with stats(id,avg_points) as (select id_player,avg(points) from game_performance  group by id_player order by avg(points) desc ),stats1(id,avg_points) as(select id,avg_points from stats where rownum<6) select stats1.id,fname,lname,avg_points,team.name as team from stats1,player,players_teams,team where player.id=stats1.id and player.id=id_player and id_team=team.id order by avg_points desc");
        //assists
        queries.add("with stats(id,avg_assists) as (select id_player,avg(assists) from game_performance  group by id_player order by avg(assists) desc ),stats1(id,avg_assists) as(select id,avg_assists from stats where rownum<6) select stats1.id,fname,lname,avg_assists,team.name as team from stats1,player,players_teams,team where player.id=stats1.id and player.id=id_player and id_team=team.id order by avg_assists desc");
        //rebounds
        queries.add("with stats(id,avg_rebounds) as (select id_player,avg(rebounds) from game_performance  group by id_player order by avg(rebounds) desc ),stats1(id,avg_rebounds) as(select id,avg_rebounds from stats where rownum<6) select stats1.id,fname,lname,avg_rebounds,team.name as team from stats1,player,players_teams,team where player.id=stats1.id and player.id=id_player and id_team=team.id order by avg_rebounds desc");
        //steals
        queries.add("with stats(id,avg_steals) as (select id_player,avg(steals) from game_performance  group by id_player order by avg(steals) desc ),stats1(id,avg_steals) as(select id,avg_steals from stats where rownum<6) select stats1.id,fname,lname,avg_steals,team.name as team from stats1,player,players_teams,team where player.id=stats1.id and player.id=id_player and id_team=team.id order by avg_steals desc");
    }




}
