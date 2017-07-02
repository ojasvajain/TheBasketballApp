package com.endeavour.ojasva.stats_recyclerview;

/**
 * Created by Ojasva on 15-Apr-16.
 */
public class StatsPlayer
{
    private String player_name,stat_label,club_name;
    private double stat;
    private boolean isOtherPlayer;
    private int player_id;

    public StatsPlayer(String player_name,double stat,String stat_label,int player_id,boolean isOtherPlayer,String club_name)
    {
        this.player_id  = player_id;
        this.stat = stat;
        this.stat_label = stat_label;
        this.isOtherPlayer = isOtherPlayer;
        this.player_name  = player_name;
        this.club_name = club_name;
    }

    public String getPlayer_name() {
        return player_name;
    }

    public String getStat_label() {
        return stat_label;
    }

    public double getStat() {
        return stat;
    }

    public int getPlayer_id() {
        return player_id;
    }

    public String getClub_name()
    {
        return club_name;
    }

    public  boolean isOtherPlayer()
    {
        return  isOtherPlayer;
    }


}
