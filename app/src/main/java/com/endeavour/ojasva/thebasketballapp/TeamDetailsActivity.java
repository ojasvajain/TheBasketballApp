package com.endeavour.ojasva.thebasketballapp;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.endeavour.ojasva.aceqlapi.AceQLDBManager;
import com.endeavour.ojasva.teamdetails_recyclerview.Form;
import com.endeavour.ojasva.teamdetails_recyclerview.Match;
import com.endeavour.ojasva.teamdetails_recyclerview.Player;
import com.endeavour.ojasva.teamdetails_recyclerview.RecyclerViewAdapter;
import com.endeavour.ojasva.teamdetails_recyclerview.SpacesItemDecoration;
import com.endeavour.ojasva.teamdetails_recyclerview.Stats;
import com.endeavour.ojasva.teamdetails_recyclerview.Venue;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;


public class TeamDetailsActivity extends AppCompatActivity {

    final private String KEY_FAV_TEAM_NAME = "om.endeavour.ojasva.thebasketballapp.FavTeamName";  //for sp
    final private String KEY_FAV_TEAM_ID  = "om.endeavour.ojasva.thebasketballapp.FavTeamID";   //for shared preferences

    private List<String> queries;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Object> details;
    private Stats stats;
    private int team_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_details);

        String ur="jdbc:aceql:http://"+"192.168.157.1"+":9090/ServerSqlManager";
        AceQLDBManager.initialize(ur, getString(R.string.username), getString(R.string.password));

        //toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_36dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }

        });


        team_id = getIntent().getIntExtra("Team ID", 1);

        getTeamStandingsData(team_id);

        queries = new ArrayList<>();
        initializeQueries(team_id);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_teamdetails);

        details = new ArrayList<>();

        recyclerViewAdapter = new RecyclerViewAdapter(details,this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SpacesItemDecoration(30));   //to provide space between details
        Log.i("abc","in on create");
        getLoaderManager().initLoader(0, null, loaderCallbacks);      //loading in background


    }

        private LoaderManager.LoaderCallbacks<List<Object>> loaderCallbacks = new LoaderManager.LoaderCallbacks<List<Object>>()
        {
            @Override
            public Loader<List<Object>> onCreateLoader(int id, Bundle args) {
                Log.i("abc","creating loader object");
                return new TeamDetailsLoader(queries,getApplicationContext(),team_id);
            }

            @Override
            public void onLoadFinished(Loader<List<Object>> loader, List<Object> details) {

                 removeThrobber();
             //   details.add(new Match("29/01/2016", "Rose Bowl Stadium", "California", 87, 102, "Chicago Bulls", "LA Lakers", "Next Match", "1", "2", "3"));
              //  details.add(new Form(false, true, false, true, false));
                details.add(stats);
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
        RelativeLayout this_layout = (RelativeLayout) findViewById(R.id.team_details);
        this_layout.removeView(throbber);
    }

    public void initializeQueries(int team_id)
    {
        //to fetch team name,coach name and home stadium name
        queries.add("SELECT TEAM.NAME AS T_NAME,VENUE.NAME AS V_NAME,FNAME,LNAME FROM TEAM,COACH,VENUE WHERE TEAM.ID_COACH=COACH.ID AND TEAM.ID_VENUE=VENUE.ID AND TEAM.ID = " + team_id);
        //to fetch list of players of the team
       queries.add("SELECT ID,FNAME,LNAME,POSITION FROM PLAYER WHERE ID IN( SELECT ID_PLAYER FROM PLAYERS_TEAMS WHERE ID_TEAM =" + team_id + ")");
        //to fetch next match
        queries.add("select g.id as game_id,t1.id home_id,t1.name as home_team,t2.id away_id,t2.name as away_team,venue.name as venue,city,date_played from team t1,game g,team t2,venue,location where t1.id=id_home_team and t2.id=id_away_team and t1.id_venue=venue.id and zip_location=zip and (t1.id="+team_id+" or t2.id="+team_id+")  and (date_played-sysdate)>=1 order by date_played");
        //to fetch previous match and form
        queries.add("select g.id as game_id,id_winning_team,t1.id home_id,t1.name as home_team,home_score ,t2.id away_id,t2.name as away_team,away_score,venue.name as venue,city,date_played from team t1,game g,team t2,venue,location where t1.id=id_home_team and t2.id=id_away_team and t1.id_venue=venue.id and zip_location=zip and (t1.id="+team_id+" or t2.id="+team_id+")  and (date_played-sysdate)<0 order by date_played desc");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.team_details_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.ab_schedule:
            {

                Intent intent = new Intent(this,ScheduleActivity.class);
                intent.putExtra("Team ID",team_id);
                startActivity(intent);
            }
        }

        return true;
    }


    public void getTeamStandingsData(int team_id)
    {
        String temp = "";
        SharedPreferences sp = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String standing_details =  sp.getString(team_id + "", "-1");   //key = team ID
        Log.i("123",standing_details);

        int wins=0,loses=0,position=0;
        double pct=0;

        for(int i=0,j=1;i<standing_details.length();i++)   //extracting details
        {
            if(standing_details.charAt(i)==' ')
            {
                switch (j)   //position wins loses pct
                {
                    case 1://position
                            position = Integer.parseInt(temp);
                            break;
                    case 2:  //wins
                            wins = Integer.parseInt(temp);
                            break;
                    case 3://loses
                            loses = Integer.parseInt(temp);
                            break;
                }

                temp="";
                j++;
            }
            else
            {
                temp+=standing_details.charAt(i);
            }
        }

        pct = Double.parseDouble(temp);

        stats  = new Stats(pct,position,wins+loses,wins,loses);
    }

}















