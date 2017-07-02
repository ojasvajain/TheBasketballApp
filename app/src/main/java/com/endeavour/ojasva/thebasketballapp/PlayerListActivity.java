package com.endeavour.ojasva.thebasketballapp;

import android.app.LoaderManager;
;
import android.content.Loader;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.endeavour.ojasva.aceqlapi.AceQLDBManager;
import com.endeavour.ojasva.playerlist_recyclerview.RecyclerViewAdapter;
import com.endeavour.ojasva.teamdetails_recyclerview.Player;
import com.endeavour.ojasva.teamdetails_recyclerview.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class PlayerListActivity extends AppCompatActivity {
    
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    List<Player> playerList = new ArrayList<>();
    final private String query  = "SELECT FNAME,LNAME,POSITION,ID FROM PLAYER ORDER BY FNAME,LNAME";
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_list);

        searchView = (SearchView)findViewById(R.id.search);
        searchView.setOnQueryTextListener(listener);

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

        recyclerView = (RecyclerView)findViewById(R.id.rv_players_list);
        recyclerViewAdapter  = new RecyclerViewAdapter(this,playerList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SpacesItemDecoration(30));   //to provide space between details

        getLoaderManager().initLoader(0, null, loaderCallbacks);      //loading in background


    }

    private LoaderManager.LoaderCallbacks<List<Player>> loaderCallbacks = new LoaderManager.LoaderCallbacks<List<Player>>()
    {
        @Override
        public Loader<List<Player>> onCreateLoader(int id, Bundle args) {
            return new PlayerListLoader(getApplicationContext(),query);
        }

        @Override
        public void onLoadFinished(Loader<List<Player>> loader, List<Player>playerList) {

            removeThrobber();
            recyclerViewAdapter.swapData(playerList); //There was nothing in the adapter previously. It will be swapped by data fetched from db.
            getLoaderManager().destroyLoader(0);
        }

        @Override
        public void onLoaderReset(Loader<List<Player>> loader) {

            recyclerViewAdapter.swapData(playerList);

        }
    };

    public void removeThrobber() {
        GifImageView throbber = (GifImageView) findViewById(R.id.throbber);
        RelativeLayout this_layout = (RelativeLayout) findViewById(R.id.rl_playerlist);
        this_layout.removeView(throbber);
    }




    //search related
    SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextChange(String query) {
            query = query.toLowerCase();

            final List<Player> filteredList = new ArrayList<>();

            for (int i = 0; i < playerList.size(); i++) {

                final String text = playerList.get(i).getPlayer_name().toLowerCase();
                if (text.contains(query)) {

                    filteredList.add(playerList.get(i));
                }
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(PlayerListActivity.this));
            recyclerViewAdapter = new RecyclerViewAdapter(PlayerListActivity.this,filteredList);
            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerViewAdapter.notifyDataSetChanged();
            return true;

        }
        public boolean onQueryTextSubmit(String query) {
            return false;
        }
    };




}

    
    

