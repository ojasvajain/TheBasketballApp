package com.endeavour.ojasva.thebasketballapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.endeavour.ojasva.aceqlapi.AceQLDBManager;
import com.endeavour.ojasva.aceqlapi.BackendConnection;
import com.endeavour.ojasva.aceqlapi.OnGetResultSetListener;
import com.endeavour.ojasva.aceqlapi.OnPrepareStatements;
import com.endeavour.ojasva.match_performance.PlayerStats;
import com.endeavour.ojasva.standings_listview.StandingsItem;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ojasva on 11-Apr-16.
 */
public class MatchPerformanceLoader extends AsyncTaskLoader<List<PlayerStats>>
{
    private List<PlayerStats> matchPerformanceList;
    private List<String> queries;

    public MatchPerformanceLoader(Context context,List<String> queries)
    {
        super(context);
        this.queries = queries;
        matchPerformanceList  = new ArrayList<>();
    }

    @Override
    protected void onStartLoading()
    {
        forceLoad();
    }
    public List<PlayerStats> loadInBackground()
    {

        final OnPrepareStatements onPreparedStatementsListener = new OnPrepareStatements() {
            @Override
            public PreparedStatement[] onGetPreparedStatementListener(BackendConnection remoteConnection) {

                try {

                    PreparedStatement[] preparedStatements = new PreparedStatement[queries.size()];

                    for(int i=0;i<queries.size();i++)
                    {
                        //CallableStatement
                        //Prepare it to an executable statement
                        PreparedStatement preparedStatement = remoteConnection.prepareStatement(queries.get(i));
                        preparedStatements[i] = preparedStatement;
                    }

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

                    getFromResultSets(resultSets);

                } else {
                    //This should never happen but if it does,
                    //log and display it
                    Log.e("Result", "Received no result sets from query");
                }
            }
        };

        AceQLDBManager.executePreparedStatements(onPreparedStatementsListener, onGetResultSetListener);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return matchPerformanceList;
    }

    @Override
    public void deliverResult(List<PlayerStats> data) {
        super.deliverResult(matchPerformanceList);
    }


    public void getFromResultSets( ResultSet resultSets[]) {

        int count_home=0,count_away=0;
        ResultSet rs = null;
        //getting home team player performances
        rs = resultSets[0];
        try {
            while (rs.next()) {

                    matchPerformanceList.add(new PlayerStats(rs.getString("POSITION"),rs.getString("FNAME")+" "+rs.getString("LNAME"),rs.getInt("POINTS"),rs.getInt("REBOUNDS"),rs.getInt("STEALS"),rs.getInt("ASSISTS"),rs.getInt("ID")));
                    count_home++;
            }
            rs.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        rs = resultSets[1];
        try
        {
            while(rs.next()) {

                matchPerformanceList.add(new PlayerStats(rs.getString("POSITION"),rs.getString("FNAME")+" "+rs.getString("LNAME"),rs.getInt("POINTS"),rs.getInt("REBOUNDS"),rs.getInt("STEALS"),rs.getInt("ASSISTS"),rs.getInt("ID")));
                count_away++;
            }
            rs.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        matchPerformanceList.add(new PlayerStats("","",count_home,count_away,-1,-1,-1));    //storing count of home records in points and count of away records in rebounds


    }


}
