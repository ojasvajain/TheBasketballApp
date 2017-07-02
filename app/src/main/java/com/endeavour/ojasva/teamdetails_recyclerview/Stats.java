package com.endeavour.ojasva.teamdetails_recyclerview;

/**
 * Created by Ojasva on 03-Apr-16.
 */
public class Stats
{
    private double pct;
    private int position;
    private int played;
    private int won;
    private int lost;

    public Stats(double pct, int position, int played, int won,int lost) {
        this.pct = pct;
        this.position = position;
        this.played = played;
        this.won = won;
        this.lost = lost;
    }

    public int getLost() {

        return lost;
    }

    public int getWon() {
        return won;
    }

    public int getPlayed() {
        return played;
    }


    public double getPct() {
        return pct;
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
