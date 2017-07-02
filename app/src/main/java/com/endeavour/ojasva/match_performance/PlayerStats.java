package com.endeavour.ojasva.match_performance;

/**
 * Created by Ojasva on 11-Apr-16.
 */
public class PlayerStats
{
    private int ID;
    private String name;
    private int points,rebounds,steals,assists;
    private String position;

    public PlayerStats(String position, String name, int points, int rebounds, int steals, int assists,int ID) {
        this.position = position;
        this.name = name;
        this.points = points;
        this.rebounds = rebounds;
        this.steals = steals;
        this.assists = assists;
        this.ID = ID;
    }

    public String getPosition() {
        return position;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public int getRebounds() {
        return rebounds;
    }

    public int getSteals() {
        return steals;
    }

    public int getAssists() {
        return assists;
    }
}
