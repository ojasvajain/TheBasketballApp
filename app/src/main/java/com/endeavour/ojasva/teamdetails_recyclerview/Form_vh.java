package com.endeavour.ojasva.teamdetails_recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.endeavour.ojasva.thebasketballapp.R;

/**
 * Created by Ojasva on 03-Apr-16.
 */
public class Form_vh extends RecyclerView.ViewHolder
{
    private ImageView form1,form2,form3,form4,form5;

    public ImageView getForm5() {
        return form5;
    }

    public void setForm5(ImageView form5) {
        this.form5 = form5;
    }

    public ImageView getForm4() {
        return form4;
    }

    public void setForm4(ImageView form4) {
        this.form4 = form4;
    }

    public ImageView getForm2() {
        return form2;
    }

    public void setForm2(ImageView form2) {
        this.form2 = form2;
    }

    public ImageView getForm3() {
        return form3;
    }

    public void setForm3(ImageView form3) {
        this.form3 = form3;
    }

    public ImageView getForm1() {
        return form1;
    }

    public void setForm1(ImageView form1) {
        this.form1 = form1;
    }

    public Form_vh(View itemView)
    {
        super(itemView);
        form1=(ImageView)itemView.findViewById(R.id.form1);
        form2=(ImageView)itemView.findViewById(R.id.form2);
        form3=(ImageView)itemView.findViewById(R.id.form3);
        form4=(ImageView)itemView.findViewById(R.id.form4);
        form5=(ImageView)itemView.findViewById(R.id.form5);
    }


}
