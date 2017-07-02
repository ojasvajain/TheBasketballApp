package com.endeavour.ojasva.thebasketballapp;



import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.endeavour.ojasva.aceqlapi.AceQLDBManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pl.droidsonroids.gif.GifImageView;

public class ChooseTeamActivity extends AppCompatActivity {


    final private String query="SELECT NAME FROM TEAM";

    private List<String> team_initial=new ArrayList<>();
    private GridView gridView;
    private String voice_result;
    private int resultCode=-1;
    CustomGridAdapter gridAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_team);

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

        // String ip=getIntent().getStringExtra("IP Address");    //last correct ip entered by user
        gridAdapter=new CustomGridAdapter(this,team_initial);
        gridView=(GridView)findViewById(R.id.grid_team);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(),TeamDetailsActivity.class);
                intent.putExtra("Team ID",position+1);
                intent.putExtra("Are we coming from ChooseTeamActivity?",true);
              //  intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                //saveTeamDetails(position+1);   //saving ID for future reference
                startActivity(intent);

            }
        });

        getLoaderManager().initLoader(0, null, loaderCallbacks);      //loading in background

    }


   private LoaderManager.LoaderCallbacks<List<String>> loaderCallbacks = new LoaderManager.LoaderCallbacks<List<String>>()
    {
        @Override
        public Loader<List<String>> onCreateLoader(int id, Bundle args) {
            return new TeamListLoader(query,getApplicationContext());
        }

        @Override
        public void onLoadFinished(Loader<List<String>> loader, List<String> team) {

            removeThrobber();
            team_initial = team;
            gridAdapter.swapData(team); //There was nothing in the adapter previously. It will be swapped by data fetched from db.
            getLoaderManager().destroyLoader(0);
        }

        @Override
        public void onLoaderReset(Loader<List<String>> loader) {

            gridAdapter.swapData(team_initial);

        }
    };

    public void removeThrobber()
    {
        GifImageView throbber=(GifImageView)findViewById(R.id.throbber);
        RelativeLayout this_layout=(RelativeLayout)findViewById(R.id.choose_team);
        this_layout.removeView(throbber);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.choose_team_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.ab_voice:
            {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Please start speaking");
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
                startActivityForResult(intent,1);
            }
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==1)
        {
            ArrayList<String> results;
            if(data!=null)
            {
                results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                voice_result = results.get(0).replace("'", "").toLowerCase();
                Toast.makeText(getApplicationContext(), voice_result, Toast.LENGTH_SHORT).show();
                Log.i("voice", voice_result);
                for(int i=0;i<team_initial.size();i++) {
                    if(team_initial.get(i).toLowerCase().contains(voice_result))
                    {
                        Intent intent = new Intent(this,TeamDetailsActivity.class);
                        intent.putExtra("Team ID", i + 1);
                        startActivity(intent);
                        break;
                    }
                }
            }


        }

    }




}
