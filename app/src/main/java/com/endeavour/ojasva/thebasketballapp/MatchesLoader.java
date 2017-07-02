package com.endeavour.ojasva.thebasketballapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.endeavour.ojasva.aceqlapi.AceQLDBManager;
import com.endeavour.ojasva.aceqlapi.BackendConnection;
import com.endeavour.ojasva.aceqlapi.OnGetResultSetListener;
import com.endeavour.ojasva.aceqlapi.OnPrepareStatements;
import com.endeavour.ojasva.stats_recyclerview.Label;
import com.endeavour.ojasva.teamdetails_recyclerview.Match;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ojasva on 18-Apr-16.
 */
public class MatchesLoader extends AsyncTaskLoader<List<Object>>
{
    private List<Object> items;
    private String query;
    
    public MatchesLoader(Context context,String query)
    {
        super(context);
        this.query = query;
        items = new ArrayList<>();
    }

    protected void onStartLoading()
    {
        forceLoad();
    }

    @Override
    public List<Object> loadInBackground()
    {

        final OnPrepareStatements onPreparedStatementsListener = new OnPrepareStatements() {
            @Override
            public PreparedStatement[] onGetPreparedStatementListener(BackendConnection remoteConnection) {

                try {

                    PreparedStatement[] preparedStatements = new PreparedStatement[1];  //2 queries

                    for(int i=0;i<1;i++)
                    {
                        //CallableStatement
                        //Prepare it to an executable statement
                        PreparedStatement preparedStatement = remoteConnection.prepareStatement(query);
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

                    ResultSet rs = resultSets[0];
                    boolean isFirstLabel=true;
                    try {
                            String prev_date = null;
                            while (rs.next())
                            {
                                String date_played = new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate("DATE_PLAYED"));;
                                if(!date_played.equals(prev_date))   //change in date detected
                                {
                                    items.add(new Label(date_played,isFirstLabel));
                                    isFirstLabel = false;
                                }
                                items.add(new Match("",rs.getString("VENUE"),rs.getString("CITY"),-1,-1, rs.getString("AWAY_TEAM"),rs.getString("HOME_TEAM"),"",rs.getInt("ID_HOME_TEAM")+"",rs.getInt("ID_AWAY_TEAM")+"",rs.getInt("ID")+""));


                                prev_date = date_played;

                            }
                        rs.close();


                    }
                    catch (SQLException e1)
                    {
                        e1.printStackTrace();
                    }


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
        return items;
    }

    @Override
    public void deliverResult(List<Object> data) {
        super.deliverResult(data);
    }

}
