package com.endeavour.ojasva.match_performance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.endeavour.ojasva.thebasketballapp.R;

import java.util.List;


/**
 * Created by Ojasva on 11-Apr-16.
 */
public class PlayerStatsListAdapter extends ArrayAdapter<PlayerStats>
{

    Context context;
    List<PlayerStats> playerStatsList;

    public PlayerStatsListAdapter(Context context,List<PlayerStats> playerStatsList)
    {
        super(context, R.layout.player_stats_item,playerStatsList);
        this.context = context;
        this.playerStatsList = playerStatsList;
    }

    public View getView(int position,View convertView, ViewGroup parent)
    {
        if(convertView==null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.player_stats_item, parent, false);
        }

        PlayerStats item= getItem(position);

        ImageView iv_player_photo = (ImageView)convertView.findViewById(R.id.iv_player_photo);
        TextView tv_player_name = (TextView)convertView.findViewById(R.id.tv_player_name);
        TextView tv_player_position = (TextView)convertView.findViewById(R.id.tv_player_position);
        TextView tv_assists = (TextView)convertView.findViewById(R.id.tv_assists);
        TextView tv_rebounds = (TextView)convertView.findViewById(R.id.tv_rebounds);
        TextView tv_steals = (TextView)convertView.findViewById(R.id.tv_steals);
        TextView tv_points = (TextView)convertView.findViewById(R.id.tv_points);

        iv_player_photo.setImageResource(getResourceId(item.getName()));
        tv_player_name.setText(item.getName());
        tv_player_position.setText(item.getPosition());
        tv_assists.setText("AST:"+item.getAssists());
        tv_steals.setText("STL:"+item.getSteals()+"");
        tv_rebounds.setText("REB:"+item.getRebounds()+"");
        tv_points.setText(item.getPoints()+"");

        return convertView;
    }

    public void swapData(List<PlayerStats> playerStatsList)
    {
        this.playerStatsList.clear();
        this.playerStatsList.addAll(playerStatsList);
        notifyDataSetChanged();
    }

    public int getResourceId(String name) //replace spaces with underscore and everything in lower case
    {
        for (int i = 0; i < name.length(); i++) {
            if (!((name.charAt(i) >= 65 && name.charAt(i) <= 90) || (name.charAt(i) >= 97 && name.charAt(i) <= 122) || (name.charAt(i) >= '0' && name.charAt(i) <= '9')))  //not an alphabet or number
            {
                name = name.replace(name.charAt(i), '_');
            }
        }
        String res_name = name.toLowerCase();
        int resId = context.getResources().getIdentifier(res_name, "drawable", context.getPackageName());
        return resId;

    }


}
