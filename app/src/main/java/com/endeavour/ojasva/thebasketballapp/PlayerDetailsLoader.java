package com.endeavour.ojasva.thebasketballapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.endeavour.ojasva.aceqlapi.AceQLDBManager;
import com.endeavour.ojasva.aceqlapi.BackendConnection;
import com.endeavour.ojasva.aceqlapi.OnGetResultSetListener;
import com.endeavour.ojasva.aceqlapi.OnPrepareStatements;
import com.endeavour.ojasva.playerdetails_recyclerview.HWDOBS;
import com.endeavour.ojasva.playerdetails_recyclerview.Plays_for;
import com.endeavour.ojasva.playerdetails_recyclerview.Stats;
import com.endeavour.ojasva.teamdetails_recyclerview.Coach;
import com.endeavour.ojasva.teamdetails_recyclerview.Emblem_Tname;
import com.endeavour.ojasva.teamdetails_recyclerview.Player;
import com.endeavour.ojasva.teamdetails_recyclerview.Venue;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Ojasva on 09-Apr-16.
 */
public class PlayerDetailsLoader extends AsyncTaskLoader<List<Object>>
{
    private List<String> queries;
    private List<Object> details=new ArrayList<>();

    public PlayerDetailsLoader(List<String> queries,Context context)
    {
        super(context);
        this.queries=queries;

    }

    @Override
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

                    PreparedStatement[] preparedStatements = new PreparedStatement[queries.size()];

                    for(int i=0;i<queries.size();i++)
                    {
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
        return details;
    }

    @Override
    public void deliverResult(List<Object> data) {
        super.deliverResult(data);
    }


    public void getFromResultSets( ResultSet resultSets[]) {
        ResultSet rs = null;
        //getting team name, venue and coach
        rs = resultSets[0];
        try {

            while (rs.next()) {
                details.add(new Emblem_Tname(rs.getString("FNAME") + " " + rs.getString("LNAME")));  //player name
                details.add(new HWDOBS(rs.getString("POSITION"),"Position"));
                details.add(new Plays_for(rs.getString("PLAYS_FOR"),rs.getInt("PLAYS_FOR_ID")));
                details.add(new HWDOBS(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("DOB")).toString() + " (Age: " + getAge(rs.getDate("DOB")) + ")", "Born On"));   //date of birth
                details.add(new HWDOBS(rs.getString("HEIGHT") + " cm", "Height"));   //height
                details.add(new HWDOBS(rs.getString("WEIGHT") + " kg", "Weight"));  //weight
                details.add(new HWDOBS(String.valueOf(rs.getInt("SALARY")) + " USD", "Salary"));  //salary
                details.add(new HWDOBS(rs.getString("COLLEGE"),"College"));

            }
            rs.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        //stats
        rs = resultSets[1];
        try {
            while (rs.next())
            {
                details.add(new Stats(rs.getDouble("POINTS"),rs.getDouble("ASSISTS"),rs.getDouble("REBOUNDS"),rs.getDouble("STEALS")));
            }
            rs.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }
        public int getAge(Date dateOfBirth) {

            Calendar dob = Calendar.getInstance();
            dob.setTime(dateOfBirth);
            Calendar today = Calendar.getInstance();
            int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) {
                age--;
            } else if (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH)
                    && today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
                age--;
            }
            return age;
        }







}

