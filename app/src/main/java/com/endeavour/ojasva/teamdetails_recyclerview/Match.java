package com.endeavour.ojasva.teamdetails_recyclerview;

/**
 * Created by Ojasva on 02-Apr-16.
 */
public class Match
{

    private String label;
    private String home_team;
    private String away_team;
    private int home_score;
    private int away_score;
    private String venue;
    private String city;
    private String date;
    private String id_home_team;
    private String id_away_team;
    private String id_game;

    public Match(String date, String venue, String city,int away_score, int home_score, String away_team, String home_team,String label,String id_home_team,String id_away_team,String id_game) {
        this.date = date;
        this.venue = venue;
        this.away_score = away_score;
        this.home_score = home_score;
        this.away_team = away_team;
        this.home_team = home_team;
        this.city = city;
        this.label=label;
        this.id_away_team=id_away_team;
        this.id_home_team=id_home_team;
        this.id_game = id_game;
    }

    public String getHome_team() {
        return home_team;
    }


    public String getAway_team() {
        return away_team;
    }


    public int getHome_score() {
        return home_score;
    }


    public int getAway_score() {
        return away_score;
    }


    public String getDate() {
        return date;
    }


    public String getVenue() {
        return venue;
    }

    public String getCity()
    {
        return city;
    }

    public String getId_home_team() {
        return id_home_team;
    }

    public String getId_away_team() {
        return id_away_team;
    }

    public String getLabel()
    {
        return label;
    }

    public String getId_game() {
        return id_game;
    }
}
