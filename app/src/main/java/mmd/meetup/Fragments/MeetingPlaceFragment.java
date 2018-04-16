package mmd.meetup.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;

import mmd.meetup.Activities.MapActivity;
import mmd.meetup.Adapters.PlaceAdapter;
import mmd.meetup.Models.Meeting;
import mmd.meetup.Models.MeetingPlace;
import mmd.meetup.R;


public class MeetingPlaceFragment extends Fragment implements NullFieldAsserter{
    private final String LOG_TAG = this.getClass().getSimpleName();

    private PlacePickerHandler mListener;

    private Meeting meeting;
    private FloatingActionButton addPlaceButton;
    private RecyclerView mRecyclerView;
    private PlaceAdapter mAdapter;

    public MeetingPlaceFragment() {
        // Required empty public constructor
    }

    public static MeetingPlaceFragment newInstance(Meeting meeting) {
        MeetingPlaceFragment fragment = new MeetingPlaceFragment();
        fragment.meeting = meeting;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meeting_place, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addPlaceButton = view.findViewById(R.id.addPlace);

        addPlaceButton.setOnClickListener(v -> mListener.makePlacePicker());

        mRecyclerView = view.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new PlaceAdapter();
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PlacePickerHandler) {
            mListener = (PlacePickerHandler) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public ArrayList<MeetingPlace> getMeetingPlaceList(){
        return (ArrayList<MeetingPlace>) mAdapter.getDataSet();
    }

    public void addPickedPlace(Place place) {
        MeetingPlace meetingPlace = new MeetingPlace();

        meetingPlace.setName(place.getName().toString());
        meetingPlace.setAddress(place.getAddress().toString());
        meetingPlace.setLatitude(place.getLatLng().latitude);
        meetingPlace.setLongitude(place.getLatLng().longitude);

        mAdapter.insertMeetingPlace(meetingPlace);
    }

    @Override
    public boolean hasNullFields() {
        return mAdapter.getItemCount() == 0;
    }

    public interface PlacePickerHandler {
        void makePlacePicker();
        void handlePlaceOption(Place place);
    }
}
