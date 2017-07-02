package com.endeavour.ojasva.teamdetails_recyclerview;

/**
 * Created by Ojasva on 04-Apr-16.
 */
public class Player
{
    private String player_name;
    private String player_position;
    private int player_id;

    public Player(String player_name, String player_position,int player_id) {
        this.player_name = player_name;
        this.player_position = player_position;
        this.player_id = player_id;
    }


    public String getPlayer_name() {
        return player_name;
    }

    public String getPlayer_position()
    {
        return player_position;
    }

    public int getPlayer_id(){ return player_id; }


}
