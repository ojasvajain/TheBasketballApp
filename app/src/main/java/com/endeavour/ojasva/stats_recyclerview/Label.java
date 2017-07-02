package com.endeavour.ojasva.stats_recyclerview;

/**
 * Created by Ojasva on 15-Apr-16.
 */
public class Label
{
    private String label;
    private boolean isTopLabel;

    public Label(String label, boolean isTopLabel) {
        this.label = label;
        this.isTopLabel = isTopLabel;
    }

    public String getLabel() {
        return label;
    }

    public boolean isTopLabel()
    {
        return isTopLabel;
    }


}
