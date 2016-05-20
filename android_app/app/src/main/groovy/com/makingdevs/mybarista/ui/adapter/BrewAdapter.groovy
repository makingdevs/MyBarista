package com.makingdevs.mybarista.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.R

class BrewAdapter extends RecyclerView.Adapter<BrewViewHolder>{

    Context mContext
    List<Checkin> mCheckins

    BrewAdapter(Context context, List<Checkin> checkinList){
        mContext = context
        mCheckins = checkinList
    }

    @Override
    BrewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_checkin, parent, false)
        new BrewViewHolder(view)
    }

    @Override
    void onBindViewHolder(BrewViewHolder holder, int position) {
        Checkin checkin = mCheckins[position]
        holder.bindCheckin(checkin)
    }

    @Override
    int getItemCount() {
        mCheckins.size()
    }


    class BrewViewHolder extends RecyclerView.ViewHolder{

        Checkin mCheckin
        TextView mtitle
        TextView morigin
        TextView mprice

        void bindCheckin(Checkin checkin) {
            itemView.onClickListener = {
                Toast.makeText(mContext,"${mCheckin.properties}",Toast.LENGTH_SHORT).show();
            }
            mCheckin = checkin
            mtitle.text = checkin.method
            morigin.text = checkin.origin
            mprice.text = checkin.price

        }

        BrewViewHolder(View itemView) {
            super(itemView)
            mtitle = (TextView) itemView.findViewById(R.id.label_method)
            morigin = (TextView) itemView.findViewById(R.id.label_origin)
            mprice = (TextView) itemView.findViewById(R.id.label_price)
        }
    }

}

