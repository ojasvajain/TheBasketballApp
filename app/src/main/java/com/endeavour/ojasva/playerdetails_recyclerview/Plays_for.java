package com.endeavour.ojasva.playerdetails_recyclerview;

/**
 * Created by Ojasva on 09-Apr-16.
 */
public class Plays_for
{
    private String plays_for;
    private int team_id;

    public Plays_for(String plays_for,int team_id)
    {
        this.plays_for = plays_for;
        this.team_id = team_id;
    }

    public String getPlays_for() {
        return plays_for;
    }

    public int getTeam_id() {
        return team_id;
    }
}
