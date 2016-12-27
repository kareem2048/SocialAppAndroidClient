package com.example.kemo.socializer.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.kemo.socializer.Control.ClientLoggedUser;
import com.example.kemo.socializer.R;
import com.example.kemo.socializer.View.Adapters.ProfileAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProfileActivityFragment extends Fragment {
    private ProfileAdapter profileAdapter;
    public ProfileActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        profileAdapter = new ProfileAdapter(getActivity());
        Intent intent = getActivity().getIntent();
        String id = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (id.equals(ClientLoggedUser.id))
        {
            profileAdapter.getPosts().add(null);
        }
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ListView listView = (ListView) view.findViewById(R.id.profile_ListView);
        listView.setAdapter(profileAdapter);
        profileAdapter.notifyDataSetChanged();
        //TODO
        return view;
    }
}