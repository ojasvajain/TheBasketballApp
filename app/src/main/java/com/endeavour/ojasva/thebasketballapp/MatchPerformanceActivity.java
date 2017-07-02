package com.endeavour.ojasva.thebasketballapp;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.endeavour.ojasva.aceqlapi.AceQLDBManager;
import com.endeavour.ojasva.match_performance.PlayerStats;
import com.endeavour.ojasva.match_performance.PlayerStatsListAdapter;
import com.endeavour.ojasva.match_performance.RecyclerViewAdapter;
import com.endeavour.ojasva.teamdetails_recyclerview.Match;
import com.endeavour.ojasva.teamdetails_recyclerview.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class MatchPerformanceActivity extends AppCompatActivity {

    private Match match;
    private ListView homePlayersLv,awayPlayersLv;
    private PlayerStatsListAdapter homeAdapter,awayAdapter;
    private List<PlayerStats> homePlayersList,awayPlayersList;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<String> queries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_performance);
        String parcel[] = getIntent().getStringArrayExtra("Match Info");

        String ur="jdbc:aceql:http://"+"192.168.157.1"+":9090/ServerSqlManager";
        AceQLDBManager.initialize(ur, getString(R.string.username), getString(R.string.password));

        initializeQueries(parcel[9]);  //parcel[9] = match id

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

        match = new Match(parcel[5],parcel[4],parcel[6],Integer.valueOf(parcel[3]),Integer.valueOf(parcel[2]),parcel[1],parcel[0],"",parcel[7],parcel[8],parcel[9]);
        Log.i("Match","Match id "+match.getId_game());
        Log.i("Match","home team id:"+match.getId_home_team());
        Log.i("Match","away team id: "+match.getId_away_team());
        Log.i("Match",match.getHome_score()+"");
        Log.i("Match",match.getAway_score()+"");
        Log.i("Match",match.getDate());
        Log.i("Match",match.getHome_team());
        Log.i("Match",match.getAway_team());

        homePlayersLv = (ListView)findViewById(R.id.lv_home_players);
        awayPlayersLv = (ListView)findViewById(R.id.lv_away_players);

        homePlayersList = new ArrayList<>();
        awayPlayersList = new ArrayList<>();




        //Recycler View stuff
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv_match);
        recyclerViewAdapter = new RecyclerViewAdapter(this,match);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SpacesItemDecoration(30));   //to provide space between details


        //setting up listeners
        homePlayersLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),PlayerDetailsActivity.class);
                PlayerStats playerStats = (PlayerStats)parent.getItemAtPosition(position);
                intent.putExtra("Player ID",playerStats.getID()+"");
                startActivity(intent);
            }
        });


        awayPlayersLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),PlayerDetailsActivity.class);
                PlayerStats playerStats = (PlayerStats)parent.getItemAtPosition(position);
                intent.putExtra("Player ID",playerStats.getID()+"");
                startActivity(intent);
            }
        });
        //loader stuff
        getLoaderManager().initLoader(0, null, loaderCallbacks);      //loading in background
    }

    private LoaderManager.LoaderCallbacks<List<PlayerStats>> loaderCallbacks = new LoaderManager.LoaderCallbacks<List<PlayerStats>>()
    {
        @Override
        public Loader<List<PlayerStats>> onCreateLoader(int id, Bundle args) {
            return new MatchPerformanceLoader(getApplicationContext(),queries);
        }

        @Override
        public void onLoadFinished(Loader<List<PlayerStats>> loader, List<PlayerStats> playerStatsList) {

            removeThrobber();
            segregrateData(playerStatsList);
            getLoaderManager().destroyLoader(0);

        }

        @Override
        public void onLoaderReset(Loader<List<PlayerStats>> loader) {

            homeAdapter.swapData(homePlayersList);
            awayAdapter.swapData(awayPlayersList);

        }
    };

    public void removeThrobber() {
        GifImageView throbber = (GifImageView) findViewById(R.id.throbber);
        RelativeLayout this_layout = (RelativeLayout) findViewById(R.id.rv_match_performance);
        this_layout.removeView(throbber);
    }

    public void initializeQueries(String game_id)
    {
       queries = new ArrayList<>();
        //home team player performances
       queries.add("select p.id,p.fname,p.lname,p.position,points,assists,rebounds,steals from player p,game,game_performance gp,players_teams pt where p.id=pt.id_player and game.id=gp.id_game and game.id_home_team=pt.id_team and game.id="+game_id+" and gp.id_player=p.id");
        //away team player performances
        queries.add("select p.id,p.fname,p.lname,p.position,points,assists,rebounds,steals from player p,game,game_performance gp,players_teams pt where p.id=pt.id_player and game.id=gp.id_game and game.id_away_team=pt.id_team and game.id="+game_id+" and gp.id_player=p.id");
    }

    public void segregrateData(List<PlayerStats> playerStatsList)
    {
        //last object stores counts
        int count_home = playerStatsList.get(playerStatsList.size()-1).getPoints();
        int count_away = playerStatsList.get(playerStatsList.size()-1).getRebounds();   //didn't need to use it

        for(int i=0;i<count_home;i++)   //home players
        {
            homePlayersList.add(playerStatsList.get(i));
        }
        for(int i=count_home;i<playerStatsList.size()-1;i++)   //away players
        {
            awayPlayersList.add(playerStatsList.get(i));
        }

        homeAdapter  = new PlayerStatsListAdapter(this,homePlayersList);
        awayAdapter = new PlayerStatsListAdapter(this,awayPlayersList);

        homePlayersLv.setAdapter(homeAdapter);
        awayPlayersLv.setAdapter(awayAdapter);
    }

}
