package com.endeavour.ojasva.thebasketballapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.endeavour.ojasva.aceqlapi.AceQLDBManager;
import com.endeavour.ojasva.aceqlapi.BackendConnection;
import com.endeavour.ojasva.aceqlapi.OnGetResultSetListener;
import com.endeavour.ojasva.aceqlapi.OnPrepareStatements;
import com.endeavour.ojasva.stats_recyclerview.Label;
import com.endeavour.ojasva.stats_recyclerview.StatsPlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ojasva on 16-Apr-16.
 */
public class HomeLoader extends AsyncTaskLoader<List<Object>> {

    private List<String> queries;
    private List<Object> items;

    public HomeLoader(Context context, List<String> queries) {

        super(context);
        this.queries = queries;
        items = new ArrayList<>();

    }

    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Object> loadInBackground() {

        final OnPrepareStatements onPreparedStatementsListener = new OnPrepareStatements() {
            @Override
            public PreparedStatement[] onGetPreparedStatementListener(BackendConnection remoteConnection) {

                try {

                    PreparedStatement[] preparedStatements = new PreparedStatement[queries.size()];

                    for (int i = 0; i < queries.size(); i++) {
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
        return items;
    }

    @Override
    public void deliverResult(List<Object> data) {
        super.deliverResult(items);
    }


    public void getFromResultSets(ResultSet resultSets[]) {
        ResultSet rs = null;
        boolean isOtherPlayer=false;
        //getting points
        items.add(new Label("Most Points",true));
        rs = resultSets[0];
        boolean isFirstPlayer=true;
        try {
            //Build the output
            while (rs.next()) {
                if(isFirstPlayer) {
                    isOtherPlayer = false;
                    isFirstPlayer=false;
                }
                else
                    isOtherPlayer=true;
                items.add(new StatsPlayer(rs.getString("FNAME")+ " "+rs.getString("LNAME"),rs.getDouble("AVG_POINTS"),"PTS",rs.getInt("ID"),isOtherPlayer,rs.getString("TEAM")));

            }

            //Always close the Result set when your done
            rs.close();
        } catch (SQLException e1) {
            //Log and display any error that occurs
            e1.printStackTrace();
        }

        //getting assists
        items.add(new Label("Most Assists",false));
        rs = resultSets[1];
        isFirstPlayer=true;
        try {
            //Build the output
            while (rs.next()) {
                if(isFirstPlayer) {
                    isOtherPlayer = false;
                    isFirstPlayer = false;
                }
                else
                    isOtherPlayer=true;
                items.add(new StatsPlayer(rs.getString("FNAME")+ " "+rs.getString("LNAME"),rs.getDouble("AVG_ASSISTS"),"AST",rs.getInt("ID"),isOtherPlayer,rs.getString("TEAM")));
            }

            //Always close the Result set when your done
            rs.close();
        } catch (SQLException e1) {
            //Log and display any error that occurs
            e1.printStackTrace();
        }

        //getting rebounds
        items.add(new Label("Most Rebounds",false));
        rs = resultSets[2];
        isFirstPlayer=true;
        try {
            //Build the output
            while (rs.next()) {
                if(isFirstPlayer) {
                    isOtherPlayer = false;
                    isFirstPlayer = false;
                }

                else
                    isOtherPlayer=true;
                items.add(new StatsPlayer(rs.getString("FNAME")+ " "+rs.getString("LNAME"),rs.getDouble("AVG_REBOUNDS"),"REB",rs.getInt("ID"),isOtherPlayer,rs.getString("TEAM")));
            }

            //Always close the Result set when your done
            rs.close();
        } catch (SQLException e1) {
            //Log and display any error that occurs
            e1.printStackTrace();
        }

        //getting steals
        items.add(new Label("Most Steals",false));
        isFirstPlayer=true;
        rs = resultSets[3];
        try {
            //Build the output
            while (rs.next()) {
                if(isFirstPlayer) {
                    isOtherPlayer = false;
                    isFirstPlayer = false;
                }
                else
                    isOtherPlayer=true;
                items.add(new StatsPlayer(rs.getString("FNAME")+ " "+rs.getString("LNAME"),rs.getDouble("AVG_STEALS"),"STE",rs.getInt("ID"),isOtherPlayer,rs.getString("TEAM")));
            }

            //Always close the Result set when your done
            rs.close();
        } catch (SQLException e1) {
            //Log and display any error that occurs
            e1.printStackTrace();
        }

    }
}
