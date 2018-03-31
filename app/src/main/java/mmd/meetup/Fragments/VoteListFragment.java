package mmd.meetup.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mmd.meetup.Adapters.VoteAdapter;
import mmd.meetup.Firebase.FirebaseClient;
import mmd.meetup.Firebase.FirebaseDB;
import mmd.meetup.Models.FinalizedMeeting;
import mmd.meetup.Models.Meeting;
import mmd.meetup.Models.PendingMeeting;
import mmd.meetup.R;


public class VoteListFragment extends Fragment implements FirebaseListFragment{

    OnListFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private VoteAdapter mAdapter;
    private ChildEventListener mChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            if (!mAdapter.containsItem(dataSnapshot.getKey()))
            FirebaseDatabase.getInstance().getReference()
                    .child(FirebaseDB.PendingMeetings.path)
                    .child(dataSnapshot.getKey())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            PendingMeeting meeting = dataSnapshot.getValue(PendingMeeting.class);
                            meeting.setId(dataSnapshot.getKey());
                            mAdapter.onInsert(meeting);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public VoteListFragment() {
    }

    public static VoteListFragment newInstance(int columnCount) {
        VoteListFragment fragment = new VoteListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vote_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            mAdapter = new VoteAdapter(mListener);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            mRecyclerView.setAdapter(mAdapter);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopListening();
    }

    @Override
    public void startListening() {
        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseDB.Users.path)
                .child(FirebaseClient.getInstance().getUserID())
                .child(FirebaseDB.Users.Entries.pendingMeetings)
                .addChildEventListener(mChildEventListener);
    }

    @Override
    public void stopListening() {
        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseDB.Users.path)
                .child(FirebaseClient.getInstance().getUserID())
                .child(FirebaseDB.Users.Entries.pendingMeetings)
                .removeEventListener(mChildEventListener);
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction();
    }
}
