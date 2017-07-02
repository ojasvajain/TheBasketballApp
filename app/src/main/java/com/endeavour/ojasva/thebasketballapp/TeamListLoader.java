package com.endeavour.ojasva.thebasketballapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.endeavour.ojasva.aceqlapi.AceQLDBManager;
import com.endeavour.ojasva.aceqlapi.BackendConnection;
import com.endeavour.ojasva.aceqlapi.OnGetResultSetListener;
import com.endeavour.ojasva.aceqlapi.OnPrepareStatements;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ojasva on 30-Mar-16.
 */
public class TeamListLoader extends AsyncTaskLoader<List<String>>
{


    private String query;
    private List<String> team=new ArrayList<>();

    public TeamListLoader(String query,Context context)
    {
        super(context);
        this.query=query;

    }

    @Override
    protected void onStartLoading()
    {
        forceLoad();
    }

    @Override
    public List<String> loadInBackground()
    {

        final OnPrepareStatements onPreparedStatementsListener = new OnPrepareStatements() {
            @Override
            public PreparedStatement[] onGetPreparedStatementListener(BackendConnection remoteConnection) {

                try {
                    //Prepare it to an executable statement
                    PreparedStatement preparedStatement = remoteConnection.prepareStatement(query);

                    PreparedStatement[] preparedStatements = new PreparedStatement[1];
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
                    //Since we executed only one query, the result will show up in index 0 of the array
                    ResultSet rs = resultSets[0];

                    try {
                        //Build the output
                        while (rs.next()) {//While there are rows and we still haven't displayed the first 5 rows
                            team.add(rs.getString("NAME"));  //storing team names in list, "NAME" is the column name
                        }
                        //Always close the Result set when your done
                        rs.close();
                    } catch (SQLException e1) {
                        //Log and display any error that occurs
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
        return team;
    }

    @Override
    public void deliverResult(List<String> data) {
        super.deliverResult(data);
    }

}
