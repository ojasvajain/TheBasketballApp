package com.endeavour.ojasva.thebasketballapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.endeavour.ojasva.aceqlapi.AceQLDBManager;
import com.endeavour.ojasva.aceqlapi.BackendConnection;
import com.endeavour.ojasva.aceqlapi.OnGetResultSetListener;
import com.endeavour.ojasva.aceqlapi.OnPrepareStatements;
import com.endeavour.ojasva.teamdetails_recyclerview.Match;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ojasva on 10-Apr-16.
 */
public class ScheduleLoader extends AsyncTaskLoader<List<Match>>
{
    private String query;
    private List<Match> schedule;

    public ScheduleLoader(String query,Context context)
    {
        super(context);
        this.query=query;
        schedule = new ArrayList<>();
    }

    @Override
    protected void onStartLoading()
    {
        forceLoad();
    }

    @Override
    public List<Match> loadInBackground() {
        final OnPrepareStatements onPreparedStatementsListener = new OnPrepareStatements() {
            @Override
            public PreparedStatement[] onGetPreparedStatementListener(BackendConnection remoteConnection) {

                try {

                    PreparedStatement[] preparedStatements = new PreparedStatement[1];
                    PreparedStatement preparedStatement = remoteConnection.prepareStatement(query);
                    preparedStatements[0] = preparedStatement;
                    return preparedStatements;
                } catch (SQLException e) {
                    //Log and display any error that occurs
                    e.printStackTrace();
                    return null;
                }
            }
        };


        //This listener tells the database manager what to do when we receive the result of the query execution
        //We will be using this listener when the execute button is clicked
        final OnGetResultSetListener onGetResultSetListener = new OnGetResultSetListener() {
            @Override
            public void onGetResultSets(ResultSet[] resultSets, SQLException e) {

                if (e != null) {
                    //Log and display any error that occurs
                    e.printStackTrace();
                } else if (resultSets.length > 0) {

                    Log.i("abc","here");
                    ResultSet rs = null;
                    rs = resultSets[0];
                    int c=1;
                    try
                    {
                        while(rs.next())
                        {
                            Log.i("abc","here");
                           schedule.add(new Match(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("DATE_PLAYED")).toString(),rs.getString("VENUE"),rs.getString("CITY"),rs.getInt("AWAY_SCORE"),rs.getInt("HOME_SCORE"),rs.getString("AWAY_TEAM"),rs.getString("HOME_TEAM"),"Matchday "+(c++),rs.getInt("ID_HOME_TEAM")+"",rs.getInt("ID_AWAY_TEAM")+"",rs.getInt("ID")+""));
                        }
                        rs.close();
                    }
                    catch (SQLException e1) {
                        Log.i("abc","there");
                        e1.printStackTrace();
                    }

                } else {
                    //This should never happen but if it does,
                    //log and display it
                    Log.i("abc", "Received no result sets from query");
                }
            }
        };

        AceQLDBManager.executePreparedStatements(onPreparedStatementsListener, onGetResultSetListener);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return schedule;
    }

    @Override
    public void deliverResult(List<Match> data) {
        super.deliverResult(data);
    }



}

