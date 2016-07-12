package com.example.jacob.avachallenge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScrollingActivity extends AppCompatActivity {

    public static final String LOG_TAG = ScrollingActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<TranscriptionInfo> myDataset = new ArrayList<>();
    String mRunningTranscript = "";
    String mBlocCheck = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        IntentFilter filter = new IntentFilter();
        filter.addAction(getResources().getString(R.string.NEW_BLOC));
        filter.addAction(getResources().getString(R.string.NEW_TRANSCRIPT));

        registerReceiver(myReceiver, filter);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fab.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_hearing_white_24dp).getConstantState()) {
                    startService(view);
                    fab.setImageResource(R.drawable.ic_stop_white_24dp);
                } else {
                    stopService(view);
                    fab.setImageResource(R.drawable.ic_hearing_white_24dp);
                }

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Method to start the subscribe service
    public void startService(View view) {
        startService(new Intent(getBaseContext(), SubService.class));
    }

    // Method to stop the subscribe service
    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), SubService.class));
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

//            Log.v(LOG_TAG, "BlocCheck is: " + String.valueOf(mRunningTranscript)
//                    + ". And incoming blocId is: " + intent.getStringExtra("blocId")
//                    + ". And incoming requestCommand is: " + intent.getStringExtra("requestCommand")
//                    + ". And incoming speakerId is: " + intent.getStringExtra("speakerId")
//                    + ". And incoming transcript is: " + intent.getStringExtra("transcript"));
            String blocId = intent.getStringExtra("blocId");
            String requestCommand = intent.getStringExtra("requestCommand");
            String speakerId = intent.getStringExtra("speakerId");
            String transcript = intent.getStringExtra("transcript");
            TranscriptionInfo transcriptionInfo = new TranscriptionInfo();



            switch (requestCommand) {
                case "room broadcast":
                    mRunningTranscript = transcript;
                    break;
                case "drop bloc":
                    mRunningTranscript = "";
                    break;
            }



//            if (!mRunningTranscript.equals("")) {
                if (myDataset.isEmpty() || !mBlocCheck.equals(blocId)) {
                    Log.v(LOG_TAG, "myDataset is empty! mBlocCheck is " + mBlocCheck);
                    SimpleDateFormat s = new SimpleDateFormat("h:mm aa", Locale.US);
                    String format = s.format(new Date());
                    transcriptionInfo.speaker = speakerId;
                    transcriptionInfo.transcription = mRunningTranscript;
                    mRunningTranscript = transcript;
                    transcriptionInfo.time = format;
                    Log.v(LOG_TAG, "Dataset is empty, mRunningTranscript is: " + mRunningTranscript);
                    myDataset.add(transcriptionInfo);
                    mAdapter.notifyDataSetChanged();
                    mBlocCheck = blocId;
                } else {

                    SimpleDateFormat s = new SimpleDateFormat("h:mm aa", Locale.US);
                    String format = s.format(new Date());
                    transcriptionInfo.speaker = speakerId;
                    mRunningTranscript = transcript;
                    transcriptionInfo.time = format;

                    View view = mLayoutManager.findViewByPosition(myDataset.size()-1);
                    TextView transcriptView =  (TextView) view.findViewById(R.id.transcript_text_view);
                    transcriptView.setText(mRunningTranscript);
                    Log.v(LOG_TAG, "Dataset is not empty, mRunningTranscript is: " + mRunningTranscript);
                    mAdapter.notifyItemChanged(myDataset.size()-1);
                }

//            }
//            do {
//                SimpleDateFormat s = new SimpleDateFormat("h:mm aa", Locale.US);
//                String format = s.format(new Date());
//
//                transcriptionInfo.speaker = speakerId
//                transcriptionInfo.transcription = transcript;
//                transcriptionInfo.time = format;
//
//                myDataset.add(transcriptionInfo);
//                mAdapter.notifyDataSetChanged();
//            } while (!requestCommand.equals("drop bloc"));
//
//            mRunningTranscript = blocId;
//            boolean continueBloc = true;
//
//            if (!requestCommand.equals("drop bloc")) {
////                if (myDataset != null) {
////                    mAdapter.getItemId(myDataset.size()-1)
////                }
//                SimpleDateFormat s = new SimpleDateFormat("h:mm aa", Locale.US);
//                String format = s.format(new Date());
//
//                transcriptionInfo.speaker = speakerId;
//                transcriptionInfo.transcription = transcript;
//                transcriptionInfo.time = format;
//
//                myDataset.add(transcriptionInfo);
//                mAdapter.notifyItemChanged(myDataset.size()-1);
////                mAdapter.notifyItemInserted(myDataset.size()-1);
////                mAdapter.notifyDataSetChanged();
//            }


            //Check if the bloc is set to the initial value, if so, start a first bloc and
            //prepare to check for a change in blocId
//            if (mRunningTranscript.equals("") && intent != null) {
//                mRunningTranscript = intent.getStringExtra("blocId");
//
//            //Initial value has changed, which mean I need to verify that the incoming value is
//            // greater than the stored value, if yes - new bloc, if no, same bloc.
//            } else if (!mRunningTranscript.equals("") && intent != null) {
//                if (!mRunningTranscript.equals(intent.getStringExtra("blocId")) ) {
//                    //start a new bloc
//                    System.out.println("NEW");
//
////                    Long timeStampLong = System.currentTimeMillis()/1000;
////                    String timeStamp = timeStampLong.toString();
//
//                    SimpleDateFormat s = new SimpleDateFormat("h:mm aa", Locale.US);
//                    String format = s.format(new Date());
//
//
//                    transcriptionInfo.speaker = intent.getStringExtra("speakerId");
//                    transcriptionInfo.transcription = intent.getStringExtra("transcript");
//                    Log.v(LOG_TAG, "This is the transcription in the new bloc: " + intent.getStringExtra("transcript"));
//                    transcriptionInfo.time = format;
//
//                    myDataset.add(transcriptionInfo);
//                    mAdapter.notifyDataSetChanged();
//
//
//
//                    mRunningTranscript = intent.getStringExtra("blocId");
//                } else {
//                    //continue with the same bloc
//                    Log.v(LOG_TAG, "This is the transcription in the same bloc: " + intent.getStringExtra("transcript"));
//                    mAdapter.notifyDataSetChanged();
//                    System.out.println("SAME");
//                }
//            }

        }

    };

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }
}
