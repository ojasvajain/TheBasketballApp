package com.endeavour.ojasva.thebasketballapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.endeavour.ojasva.aceqlapi.AceQLDBManager;
import com.endeavour.ojasva.aceqlapi.BackendConnection;
import com.endeavour.ojasva.aceqlapi.OnGetResultSetListener;
import com.endeavour.ojasva.aceqlapi.OnPrepareStatements;
import com.endeavour.ojasva.standings_listview.StandingsItem;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ojasva on 10-Apr-16.
 */
public class StandingsLoader extends AsyncTaskLoader<List<StandingsItem>>
{
    private List<String> queries;
    private List<StandingsItem> standingsItems;
    
    public StandingsLoader(Context context,List<String> queries)
    {
        super(context);
        this.queries=queries;
        standingsItems = new ArrayList<>();
    }

    @Override
    protected void onStartLoading()
    {
        forceLoad();
    }

    @Override
    public List<StandingsItem> loadInBackground()
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
        return standingsItems;
    }

    @Override
    public void deliverResult(List<StandingsItem> data) {
        super.deliverResult(standingsItems);
    }


    public void getFromResultSets( ResultSet resultSets[]) {
        ResultSet rs = null;
        //getting team name, venue and coach
        rs = resultSets[0];
        try {
            //Build the output
            while (rs.next()) {
                StandingsItem temp =new StandingsItem();
                temp.setId("" + rs.getInt("ID"));
                temp.setTeam_name(rs.getString("NAME"));
                temp.setW("" + rs.getInt("WON"));
                standingsItems.add(temp);

                Log.i("ltq1",temp.getId());
            }
            //Always close the Result set when your done
            rs.close();
        } catch (SQLException e1) {
            //Log and display any error that occurs
            e1.printStackTrace();
        }


        //getting player list
        rs = resultSets[1];
        int i=0;
        try {
            while (rs.next()) {  //note: the items were inserted in increasing order of their IDs

                StandingsItem temp = standingsItems.get(i);
                temp.setL(String.valueOf(rs.getInt("PLAYED") - Integer.parseInt(temp.getW())));
                temp.calculatePCT();
                standingsItems.set(i, temp);
                i++;
                Log.i("ltq2",temp.getId());
            }
            rs.close();
        }
        catch (SQLException e1)
        {
            e1.printStackTrace();
        }

        rs = resultSets[2];
        i=0;
        try
        {
            while(rs.next())
            {
                Log.i("ltq3",standingsItems.get(i).getId());
                standingsItems.get(i++).setAvgp("" + rs.getDouble("AVG"));
            }
            rs.close();
        }
        catch (SQLException e1)
        {
            e1.printStackTrace();
        }



    }    

}
