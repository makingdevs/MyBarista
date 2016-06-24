package com.makingdevs.mybarista.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.model.Venue
import com.makingdevs.mybarista.model.VenueDetailBindable

class VenueAdapter extends RecyclerView.Adapter<VenueViewHolder>{

    Context mContext
    List<Venue> mVenues
    VenueDetailBindable mVenueDetailBindable

    VenueAdapter(Context context, List<Venue> venueList, VenueDetailBindable venueDetailBindable){
        mContext = context
        mVenues = venueList
        mVenueDetailBindable = venueDetailBindable
    }

    @Override
    VenueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_venue, parent, false)
        new VenueViewHolder(view)
    }

    @Override
    void onBindViewHolder(VenueViewHolder holder, int position) {
        Venue venue = mVenues[position]
        holder.bindVenue(venue)
    }

    @Override
    int getItemCount() {
        mVenues.size()
    }

    class VenueViewHolder extends RecyclerView.ViewHolder{

        TextView venueItemLabel

        VenueViewHolder(View itemView) {
            super(itemView)
            venueItemLabel = (TextView) itemView.findViewById(R.id.item_venue_label)
        }

        void bindVenue(Venue venue) {
            itemView.onClickListener = {
                mVenueDetailBindable.venueID = venue.id
                mVenueDetailBindable.venueName = venue.name
            }
            venueItemLabel.text = venue.name+"\n"+venue.location.formattedAddress.join("")+"\n"
        }


    }

}

