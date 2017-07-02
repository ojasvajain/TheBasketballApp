 package com.endeavour.ojasva.thebasketballapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.endeavour.ojasva.aceqlapi.AceQLDBManager;

import java.util.ArrayList;
import java.util.List;


 /**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {

    public static final String PREF_FILE_NAME="textpref";
    public static final String KEY_USER_LEARNED_DRAWER="user_learned_drawer";

    private boolean userLearnedDrawer;
    private boolean fromSavedInstanceState;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private NavDrawerListAdapter navDrawerListAdapter;


    private  View containerView;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLearnedDrawer = Boolean.valueOf(readFromPreferences(getContext(), KEY_USER_LEARNED_DRAWER, "false"));
        if (savedInstanceState != null) {
            fromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        List<String> options = new ArrayList<>();
        options.add("Teams");
        options.add("Standings");
        options.add("Upcoming Matches");
        options.add("Players");

        navDrawerListAdapter = new NavDrawerListAdapter(getActivity(),options);
        ListView listView = (ListView)rootView.findViewById(R.id.lv_nav_drawer);
        listView.setAdapter(navDrawerListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String item = String.valueOf(parent.getItemAtPosition(position));
                if(item.equals("Standings"))
                {
                    startActivity(new Intent(getActivity(),StandingsActivity.class));
                }
                else if(item.equals("Teams"))
                {
                    startActivity(new Intent(getActivity(),ChooseTeamActivity.class));
                }
                else if(item.equals("Players"))
                {
                    startActivity(new Intent(getActivity(),PlayerListActivity.class));
                }
                else if(item.equals("Upcoming Matches"))
                {
                    startActivity(new Intent(getActivity(),MatchesActivity.class));
                }

            }
        });
        return rootView;
    }



    public void setUp(DrawerLayout drawerLayout,Toolbar toolbar, int fragmentID) {

        containerView = getActivity().findViewById(fragmentID);
        this.drawerLayout = drawerLayout;
        drawerToggle = new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
        @Override
        public void onDrawerOpened(View drawerView)
        {
            super.onDrawerOpened(drawerView);
            if(userLearnedDrawer)
            {
                userLearnedDrawer=true;
                saveToPreferences(getContext(),KEY_USER_LEARNED_DRAWER,userLearnedDrawer+"");
            }

            getActivity().invalidateOptionsMenu(); //draw action bar again
        }
        @Override
        public void onDrawerClosed(View drawerView)
        {
            super.onDrawerClosed(drawerView);
            getActivity().invalidateOptionsMenu();
        }};
        if(userLearnedDrawer && !fromSavedInstanceState)
        {
            drawerLayout.openDrawer(containerView);
        }

        drawerLayout.setDrawerListener(drawerToggle);
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {

                drawerToggle.syncState();
            }
        });
    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName,preferenceValue);
        editor.commit();
    }

    public static String readFromPreferences(Context context, String preferenceName, String defaultValue)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName,defaultValue);
    }
}
