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
public class CheckDetailsValidity extends AsyncTaskLoader<List<Object>> {
    
    private List<Object> ans;   //flag 1 - fav_team_id - flag2
    private List<String> queries;
    private boolean flag1,flag2;
    private String fav_team_name;
    private int fav_team_id;
    private String email;

    public CheckDetailsValidity(Context context, List<String> queries,String fav_team_name,String email) {
        
        super(context);
        this.queries = queries;
        flag1=flag2=true;
        this.email = email;
        this.fav_team_name=fav_team_name;
        ans = new ArrayList<>();
        fav_team_id=-1;
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
        return ans;
    }

    @Override
    public void deliverResult(List<Object> data) {
        super.deliverResult(ans);
    }


    public void getFromResultSets(ResultSet resultSets[]) {
        
        ResultSet rs = resultSets[0];   //id and name of teams
        try {
            while(rs.next()) {
                String team_name = rs.getString("NAME");
                if (team_name.toLowerCase().equals(fav_team_name.toLowerCase().trim()))
                {
                    fav_team_id = rs.getInt("ID");
                    Log.i("xyz",fav_team_id+"");
                    break;
                }
            }

            rs.close();

            if(fav_team_id==-1)
            {
                flag1=false;  //no match found
            }

            Log.i("xyz",flag1+"");
            Log.i("xyz",fav_team_id+"");

        }
        catch (SQLException e1)
        {
            e1.printStackTrace();
        }

        rs = resultSets[1];

        try {
            while(rs.next()) {

                String email_id = rs.getString("EMAIL");
                if (email_id.equals(email))
                {
                    flag2=false;
                    Log.i("xyz", flag2 + "");
                    break;
                }
            }
            rs.close();
        }
        catch (SQLException e2)
        {
            e2.printStackTrace();
        }

        ans.add(new Boolean(flag1));
        ans.add(new Integer(fav_team_id));
        ans.add(new Boolean(flag2));


    }
    }



