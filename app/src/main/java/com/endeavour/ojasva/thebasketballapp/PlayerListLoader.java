package com.endeavour.ojasva.thebasketballapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.endeavour.ojasva.aceqlapi.AceQLDBManager;
import com.endeavour.ojasva.aceqlapi.BackendConnection;
import com.endeavour.ojasva.aceqlapi.OnGetResultSetListener;
import com.endeavour.ojasva.aceqlapi.OnPrepareStatements;
import com.endeavour.ojasva.teamdetails_recyclerview.Coach;
import com.endeavour.ojasva.teamdetails_recyclerview.Emblem_Tname;
import com.endeavour.ojasva.teamdetails_recyclerview.Player;
import com.endeavour.ojasva.teamdetails_recyclerview.Venue;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ojasva on 14-Apr-16.
 */
public class PlayerListLoader extends AsyncTaskLoader<List<Player>>
{ 
    private  List<Player> playerList;
    private String query;
    
    
    public PlayerListLoader(Context context,String query)
    {
        super(context);
        this.query = query;
        playerList = new ArrayList<>();
    }

    @Override
    protected void onStartLoading()
    {
        forceLoad();
    }

    @Override
    public List<Player> loadInBackground()
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
        return playerList;
    }

    @Override
    public void deliverResult(List<Player> data) {
        super.deliverResult(data);
    }


    public void getFromResultSets( ResultSet resultSets[]) {
        ResultSet rs = null;
        //getting team name, venue and coach
        rs = resultSets[0];
        try {
            //Build the output
            while (rs.next()) {//While there are rows and we still haven't displayed the first 5 rows

                playerList.add(new Player(rs.getString("FNAME")+" "+rs.getString("LNAME"),rs.getString("POSITION"),rs.getInt("ID")));
            }
            //Always close the Result set when your done
            rs.close();
        } catch (SQLException e1) {
            //Log and display any error that occurs
            e1.printStackTrace();
        }
    }
    
}
