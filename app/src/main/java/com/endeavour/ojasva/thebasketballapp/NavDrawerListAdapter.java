package com.endeavour.ojasva.thebasketballapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ojasva on 10-Apr-16.
 */
public class NavDrawerListAdapter extends ArrayAdapter<String>
{
    private List<String> options;
    private Context context;

    public NavDrawerListAdapter(Context context,List<String> options)
    {
        super(context, R.layout.team_choose_custom_grid,options);
        this.options = options;
        this.context = context;
    }

    @Override
    public View getView(int position,View convertView, ViewGroup parent)
    {
        if(convertView==null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.nav_drawer_item, parent, false);
        }

        String team= getItem(position);
        TextView tv_name=(TextView)convertView.findViewById(R.id.tv_option);
        ImageView iv_logo=(ImageView)convertView.findViewById(R.id.iv_icon);

        tv_name.setText(team);
        if(team.equals("Teams"))
            iv_logo.setImageResource(R.drawable.team_icon);
        else if(team.equals("Standings"))
            iv_logo.setImageResource(R.drawable.standings_icon);
        else if(team.equals("Players"))
            iv_logo.setImageResource(R.drawable.players_icon);
        else if(team.equals("Upcoming Matches"))
            iv_logo.setImageResource(R.drawable.match_icon);
        return convertView;
    }

}
