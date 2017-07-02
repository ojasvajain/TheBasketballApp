package com.endeavour.ojasva.standings_listview;

import android.util.Log;

/**
 * Created by Ojasva on 04-Apr-16.
 */
public class StandingsItem
{
    private String position;
    private String team_name;
    private String w;
    private String l;
    private String pct;
    private String avgp;
    private String id;

    public String getAvgp() {
        return avgp;
    }

    public String getPct() {
        return pct;
    }

    public String getL() {
        return l;
    }

    public String getW() {
        return w;
    }

    public String getTeam_name() {
        return team_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setL(String l) {
        this.l = l;
    }

    public void setW(String w) {
        this.w = w;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public void setPosition(String position)
    {
        this.position = position;
    }

    public void setAvgp(String avgp) {
        this.avgp = avgp;
    }

    public void calculatePCT() {

        //calculate
        double won = Double.parseDouble(w);
        double played = won + Double.parseDouble(l);
        if(played==0) {
            pct = "0.0";
            return;
        }
        double PCT = (won / played) * 100;
        PCT*=100;
        PCT=(int)PCT;
        PCT/=100;
        pct = ""+PCT;
    }

    public void setPct(String pct) {
        this.pct = pct;
    }
}
