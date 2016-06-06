package com.makingdevs.mybarista.ui.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.ui.activity.ShowCheckinActivity

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
        TextView mmoment

        void bindCheckin(Checkin checkin) {
            itemView.onClickListener = {
                Intent intent = ShowCheckinActivity.newIntentWithContext(mContext,mCheckin.id)
                mContext.startActivity(intent)
            }
            mCheckin = checkin
            mtitle.text = checkin.method
            morigin.text = checkin.origin
            //mmoment.text = checkin.created_at.format("dd - MM - yy")
            //mmoment.text = (DateUtils.getRelativeTimeSpanString(checkin.created_at.time)).toString()
            mprice.text = checkin.price
        }

        BrewViewHolder(View itemView) {
            super(itemView)
            mtitle = (TextView) itemView.findViewById(R.id.label_method)
            morigin = (TextView) itemView.findViewById(R.id.label_origin)
            mprice = (TextView) itemView.findViewById(R.id.label_price)
            mmoment = (TextView) itemView.findViewById(R.id.label_moment)
        }
    }

}

