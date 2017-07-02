package com.endeavour.ojasva.standings_listview;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.endeavour.ojasva.thebasketballapp.R;

import java.util.List;

/**
 * Created by Ojasva on 04-Apr-16.
 */
public class Standings_ListAdapter extends ArrayAdapter<StandingsItem>
{
    private List<StandingsItem> list;
    private Context context;


    public Standings_ListAdapter (Context context,List <StandingsItem> list)
    {
        super(context, R.layout.standings_list_item,list);
        this.list=list;
        this.context = context;
    }

    @Override
    public View getView(int position,View convertView, ViewGroup parent)
    {
        LayoutInflater inflater=LayoutInflater.from(getContext());
        View customView=inflater.inflate(R.layout.standings_list_item, parent, false);

        StandingsItem item= getItem(position);
        TextView tv_position = (TextView)customView.findViewById(R.id.tv_position);
        TextView tv_team_name = (TextView)customView.findViewById(R.id.tv_team_name);
        TextView tv_won = (TextView)customView.findViewById(R.id.tv_won);
        TextView tv_lost = (TextView)customView.findViewById(R.id.tv_lost);
        TextView tv_pct = (TextView)customView.findViewById(R.id.tv_pct);
        TextView tv_avgp  = (TextView)customView.findViewById(R.id.tv_avgp);
        ImageView iv_team = (ImageView)customView.findViewById(R.id.iv_team);

        tv_position.setText(item.getPosition());
        tv_team_name.setText(item.getTeam_name());
        tv_won.setText(item.getW());
        tv_lost.setText(item.getL());
        tv_pct.setText(item.getPct());
        tv_avgp.setText(item.getAvgp());

        if(position == 0)   //text in bold
        {
            tv_won.setTypeface(null, Typeface.BOLD);
            tv_lost.setTypeface(null, Typeface.BOLD);
            tv_pct.setTypeface(null, Typeface.BOLD);
            tv_avgp.setTypeface(null, Typeface.BOLD);
        }

        if(position>0)    //not the first row
        {
           // iv_team.setImageResource(getResourceId(item.getTeam_name(),true));
            iv_team.setImageResource(getResourceId(item.getTeam_name(),true));
        }


        return customView;
    }

    public void swapData(List<StandingsItem> list)
    {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public int getResourceId(String name,boolean isSmaller) //replace spaces with underscore and everything in lower case
    {
        for(int i=0;i<name.length();i++)
        {
            if(!((name.charAt(i) >=65 && name.charAt(i) <=90) || (name.charAt(i)>=97 && name.charAt(i)<=122) || (name.charAt(i)>='0' && name.charAt(i)<='9')))  //not an alphabet or a number
            {
                name=name.replace(name.charAt(i),'_');
            }
        }
        String res_name = name.toLowerCase();
        if(isSmaller)   //for loading logo of team of smalller size
            res_name+="_smaller";
        int resId = context.getResources().getIdentifier(res_name, "drawable", context.getPackageName());
        return resId;
    }



}
