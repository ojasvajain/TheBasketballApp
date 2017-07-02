package com.endeavour.ojasva.playerdetails_recyclerview;

/**
 * Created by Ojasva on 18-Apr-16.
 */
public class Stats {
    private double points, assists, rebounds, steals;

    public Stats(double points, double assists, double rebounds, double steals) {
        this.points = roundOffTo1Dec(points);
        this.assists = roundOffTo1Dec(assists);
        this.rebounds = roundOffTo1Dec(rebounds);
        this.steals = roundOffTo1Dec(steals);

    }

    public double getPoints() {

        return points;
    }

    public double getAssists() {
        return assists;
    }

    public double getRebounds() {
        return rebounds;
    }

    public double getSteals() {
        return steals;
    }


    public double roundOffTo1Dec(double value) {
        double temp = value * 10;
        temp = (int) temp;
        temp = temp / 10;
        return temp;

    }

}
