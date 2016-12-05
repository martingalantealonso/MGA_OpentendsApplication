package com.galante.martin.opentendsapplication.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.galante.martin.opentendsapplication.R;
import com.galante.martin.opentendsapplication.data.Event;

import java.util.List;

/**
 * Created by Martin on 04/12/2016.
 *
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;


    public EventAdapter(List<Event> events) {
        this.eventList = events;
    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView eventTitle;
        TextView eventDescription;
        NetworkImageView eventImage;
        ImageLoader mImageLoader;

        EventViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.eventCard);
            eventTitle = (TextView) itemView.findViewById(R.id.text_view_event_title);
            eventDescription = (TextView) itemView.findViewById(R.id.text_view_event_description);
            eventImage = (NetworkImageView) itemView.findViewById(R.id.network_image_view_event);
            mImageLoader = VolleySingleton.getInstance(itemView.getContext()).getImageLoader();
        }


        @Override
        public void onClick(View view) {

        }
    }

    @Override
    public EventAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_events, parent, false);
        EventAdapter.EventViewHolder mEventViewHolder = new EventAdapter.EventViewHolder(v);
        return mEventViewHolder;
    }

    @Override
    public void onBindViewHolder(EventAdapter.EventViewHolder holder, int position) {
        holder.eventTitle.setText(eventList.get(position).eventTitle);
        holder.eventDescription.setText(eventList.get(position).eventDescription);

        if (eventList.get(position).eventImage != null) {
            if (!eventList.get(position).eventImage.isEmpty()) {
                holder.eventImage.setImageUrl(eventList.get(position).eventImage, holder.mImageLoader);
            }
        }
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }


}
