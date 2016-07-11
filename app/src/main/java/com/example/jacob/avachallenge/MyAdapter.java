package com.example.jacob.avachallenge;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jacob on 7/9/2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<TranscriptionInfo> myDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mSpeaker;
        public TextView mTime;
        public TextView mTranscription;

        public ViewHolder(View v) {
            super(v);
            mSpeaker = (TextView) v.findViewById(R.id.speaker_text_view);
            mTime = (TextView) v.findViewById(R.id.timestamp_text_view);
            mTranscription = (TextView) v.findViewById(R.id.transcript_text_view);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<TranscriptionInfo> myDataset) {
        this.myDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        //
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transcription_view_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
//        ViewHolder vh = new ViewHolder((TextView) v);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        TranscriptionInfo info = myDataset.get(position);
        holder.mTranscription.setText(info.transcription);
        holder.mTime.setText(info.time);
        holder.mSpeaker.setText(info.speaker);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (myDataset != null) {
            return myDataset.size();
        } else {
            return 0;
        }
    }

//    public void insertAt(int position) {
//        myDataset.add(position);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, mDataSet.size());
//    }
}