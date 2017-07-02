package com.endeavour.ojasva.thebasketballapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.endeavour.ojasva.standings_listview.StandingsItem;
import com.endeavour.ojasva.standings_listview.Standings_ListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class StandingsActivity extends AppCompatActivity {

    private List<String> keys;
    private List<StandingsItem> standingsItems;
    private List<String> queries;
    private Standings_ListAdapter standings_ListAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_standings);
            initializeQueries();


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

        standingsItems = new ArrayList<>();

        standings_ListAdapter = new Standings_ListAdapter(this,standingsItems);
        ListView listView = (ListView)findViewById(R.id.lv_standings);
        listView.setAdapter(standings_ListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                StandingsItem item = (StandingsItem) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), TeamDetailsActivity.class);
                intent.putExtra("Team ID", Integer.parseInt(item.getId()));
                startActivity(intent);

            }
        });

        getLoaderManager().initLoader(0, null, loaderCallbacks);      //loading in background



        }

    private LoaderManager.LoaderCallbacks<List<StandingsItem>> loaderCallbacks = new LoaderManager.LoaderCallbacks<List<StandingsItem>>()
    {
        @Override
        public Loader<List<StandingsItem>> onCreateLoader(int id, Bundle args) {
            Log.i("abc", "creating loader object");
            return new StandingsLoader(getApplicationContext(),queries);
        }

        @Override
        public void onLoadFinished(Loader<List<StandingsItem>> loader, List<StandingsItem> standingsItems) {

            standingsItems = additionalWork(standingsItems);
            removeThrobber();
            standings_ListAdapter.swapData(standingsItems); //There was nothing in the adapter previously. It will be swapped by data fetched from db.
            getLoaderManager().destroyLoader(0);

        }

        @Override
        public void onLoaderReset(Loader<List<StandingsItem>> loader) {

            standings_ListAdapter.swapData(standingsItems);

        }
    };

    public void removeThrobber() {
        GifImageView throbber = (GifImageView) findViewById(R.id.throbber);
        RelativeLayout this_layout = (RelativeLayout) findViewById(R.id.rl_standings);
        this_layout.removeView(throbber);
    }

    public void initializeQueries()
    {
        queries = new ArrayList<>();
        queries.add("with mytable as(select id_winning_team as id,count(*) as won from game where id_winning_team is not null group by id_winning_team)select id,name,won from mytable natural join team order by id");
        queries.add("select t3.id,count(*) as played from team t1,game,team t2,team t3 where (id_home_team=t1.id and id_away_team=t2.id and id_winning_team is not null) and (t3.id=id_away_team or t3.id=id_home_team) group by t3.id order by t3.id");
        queries.add("with match(id,name,score,venue,city,date_played) as (select t.id,t.name,home_score as score,venue.name,city,date_played from team team1,game,team team2,venue,location,team t where id_home_team=team1.id and id_away_team=team2.id and id_home_team=t.id and team1.id_venue=venue.id and venue.zip_location=zip union select t.id,t.name,away_score as score,venue.name,city,date_played from team team1,game,team team2,venue,location,team t where id_home_team=team1.id and id_away_team=team2.id and id_away_team=t.id and team1.id_venue=venue.id and venue.zip_location=zip ) select id,avg(score) as avg from match group by id order by id");
    }

    public List<StandingsItem> additionalWork(List<StandingsItem> standingsItems)  //1-sort according to wins,sort according to avgp.2- Add Label as first element
    {
        Collections.sort(standingsItems, new Comparator<StandingsItem>() {
            @Override
            public int compare(StandingsItem lhs, StandingsItem rhs) {

                int wlhs = Integer.parseInt(lhs.getW());
                int wrhs = Integer.parseInt(rhs.getW());
                if (wlhs == wrhs) {

                    double avg_lhs = Double.parseDouble(lhs.getAvgp());
                    double avg_rhs = Double.parseDouble(rhs.getAvgp());
                    if (avg_lhs == avg_rhs) {
                        String name_lhs = lhs.getTeam_name();
                        String name_rhs = rhs.getTeam_name();
                        if (name_lhs.equals(name_rhs))
                            return 0;
                        else if (name_lhs.compareTo(name_rhs) < 0)
                            return -1;
                        else
                            return 1;
                    }

                    if (avg_lhs < avg_rhs)
                        return 1;
                    else
                        return -1;
                }
                if (wlhs < wrhs)
                    return 1;
                else
                    return -1;

            }
        });

        for(int i=0;i<standingsItems.size();i++)
        {
            standingsItems.get(i).setPosition(""+(i+1));
        }
        saveToPreferences(standingsItems);

        //insert label item at beginning
        StandingsItem temp = new StandingsItem();
        temp.setAvgp("AVGP");
        temp.setW("W");
        temp.setTeam_name("");
        temp.setL("L");
        temp.setId("");
        temp.setPosition("");
        temp.setPct("PCT");
        standingsItems.add(0, temp);


        return standingsItems;
    }


    public void saveToPreferences(List<StandingsItem> standingsItems)
    {
        keys = new ArrayList<>();
        Log.i("123",standingsItems.size()+"");
        for(int i=0;i<standingsItems.size();i++)
        {
            keys.add(standingsItems.get(i).getId());
        }


        SharedPreferences sp = getSharedPreferences("MyPreferences",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        for(int i=0;i<keys.size();i++)
        {
            StandingsItem temp = standingsItems.get(i);
            editor.putString(keys.get(i),temp.getPosition()+" "+temp.getW()+" "+temp.getL()+
                    " "+temp.getPct()).apply();  //order = position wins loses pct
            Log.i("123",keys.get(i));
        }

    }





}
