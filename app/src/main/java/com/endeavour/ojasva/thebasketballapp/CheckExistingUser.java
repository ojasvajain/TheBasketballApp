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
 * Created by Ojasva on 20-Apr-16.
 */
public class CheckExistingUser extends AsyncTaskLoader<List<Object>>
{
    private String query;
    private List<Object> ans;

    public CheckExistingUser(Context context,String query)
    {
        super(context);
        this.query = query;
        ans = new ArrayList<>();
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

                    PreparedStatement[] preparedStatements = new PreparedStatement[1];

                    for (int i = 0; i < 1; i++) {
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
        return ans;
    }

    @Override
    public void deliverResult(List<Object> data) {
        super.deliverResult(ans);
    }

    public  void getFromResultSets(ResultSet[] resultSets) {
        ResultSet rs = resultSets[0];
        String password="",name="";
        int fav_team_id=-1;
        try {
            if (rs.next()) {
                password=rs.getString("PASSWORD");
                name=rs.getString("NAME");
                fav_team_id=rs.getInt("FAV_TEAM_ID");
            }
            rs.close();

            ans.add(new String(password));
            ans.add(new String(name));
            ans.add(new Integer(fav_team_id));
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

}
