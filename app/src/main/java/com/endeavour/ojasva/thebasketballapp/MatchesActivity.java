package com.endeavour.ojasva.thebasketballapp;

import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.endeavour.ojasva.aceqlapi.AceQLDBManager;
import com.endeavour.ojasva.schedule_recyclerview.RecyclerViewAdapter;
import com.endeavour.ojasva.teamdetails_recyclerview.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class MatchesActivity extends AppCompatActivity {
    
    
    private List<Object> items;
    final private String query="with match(id,id_home_team,id_away_team,home_team,away_team,home_score,away_score,venue,city,date_played) as (select game.id as id,team1.id as id_home_team,team2.id as id_away_team,team1.name as home_team,team2.name as away_team,home_score,away_score,venue.name,city,date_played from team team1,game,team team2,venue,location where id_home_team=team1.id and id_away_team=team2.id and team1.id_venue=venue.id and venue.zip_location=zip and date_played-sysdate>0 order by date_played) select * from match";
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        String ur="jdbc:aceql:http://"+"192.168.157.1"+":9090/ServerSqlManager";
        AceQLDBManager.initialize(ur, getString(R.string.username), getString(R.string.password));
        
        items = new ArrayList<>();

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


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_schedule);
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
            Log.i("abc", "creating loader object");
            return new MatchesLoader(getApplicationContext(),query);
        }

        @Override
        public void onLoadFinished(Loader<List<Object>> loader, List<Object> items) {

            removeThrobber();
            Log.i("abc", "" + items.size());;
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
        RelativeLayout this_layout = (RelativeLayout) findViewById(R.id.rl_schedule);
        this_layout.removeView(throbber);
    }
}
