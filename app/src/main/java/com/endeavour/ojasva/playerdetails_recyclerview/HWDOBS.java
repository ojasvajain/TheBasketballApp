package com.endeavour.ojasva.playerdetails_recyclerview;

/**
 * Created by Ojasva on 09-Apr-16.
 */
public class HWDOBS
{
    private String label;
    private String content;

    public HWDOBS(String content,String label) {
        this.content = content;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getContent() {
        return content;
    }
}
