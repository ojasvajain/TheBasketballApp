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
import com.endeavour.ojasva.teamdetails_recyclerview.Form;
import com.endeavour.ojasva.teamdetails_recyclerview.Match;
import com.endeavour.ojasva.teamdetails_recyclerview.Player;
import com.endeavour.ojasva.teamdetails_recyclerview.Venue;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ojasva on 02-Apr-16.
 */
public class TeamDetailsLoader extends AsyncTaskLoader<List<Object>>
{


    private List<String> queries;
    private List<Object> details=new ArrayList<>();
    int team_id;

    public TeamDetailsLoader(List<String> queries,Context context,int team_id)
    {
        super(context);
        this.queries=queries;
        this.team_id=team_id;

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

                PreparedStatement[] preparedStatements = new PreparedStatement[queries.size()];  //2 queries

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
            //Build the output
            while (rs.next()) {//While there are rows and we still haven't displayed the first 5 rows
                details.add(new Emblem_Tname(rs.getString("T_NAME")));  //team name
                details.add(new Venue(rs.getString("V_NAME")));   //venue
                details.add(new Coach(rs.getString("FNAME") + " " + rs.getString("LNAME")));   //name of coach

            }
            //Always close the Result set when your done
            rs.close();
        } catch (SQLException e1) {
            //Log and display any error that occurs
            e1.printStackTrace();
        }


        //getting player list
       rs = resultSets[1];
        try {
            while (rs.next()) {
                //getting player's full name and his position
                details.add(new Player(rs.getString("FNAME") + " " + rs.getString("LNAME"),rs.getString("POSITION"),rs.getInt("ID")));
            }
            rs.close();
        }

        catch (SQLException e1)
        {
            e1.printStackTrace();
        }

        rs = resultSets[2];   //getting next match (1st record)
        try{

                if(rs.next())
                    details.add(new Match(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("DATE_PLAYED")).toString(),rs.getString("VENUE"),rs.getString("CITY"),-1,-1,
                            rs.getString("AWAY_TEAM"),rs.getString("HOME_TEAM"),"Next Match",rs.getInt("HOME_ID")+"",rs.getInt("AWAY_ID")+"",rs.getInt("GAME_ID")+""));
                rs.close();
        }

        catch (SQLException e)
        {
            e.printStackTrace();
        }

        rs = resultSets[3];   //getting previous match and last five matches form
        try{
            Form form=new Form();
            int c=0;
            while (rs.next() && c<5)
            {
                if(c==0)   //prev match
                {
                    details.add(new Match(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("DATE_PLAYED")).toString(),rs.getString("VENUE"),rs.getString("CITY"),rs.getInt("AWAY_SCORE"),rs.getInt("HOME_SCORE"),
                            rs.getString("AWAY_TEAM"),rs.getString("HOME_TEAM"),"Last Match",rs.getInt("HOME_ID")+"",rs.getInt("AWAY_ID")+"",rs.getInt("GAME_ID")+""));
                }
                form.f[4-c] = rs.getInt("ID_WINNING_TEAM")==team_id?true:false;
                c++;
            }
            details.add(form);
            rs.close();
        }

        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

}

