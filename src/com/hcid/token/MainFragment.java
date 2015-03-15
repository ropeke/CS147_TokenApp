package com.hcid.token;

import com.facebook.widget.LoginButton;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainFragment extends Fragment {

	private MainFragment mainFragment;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	  if (savedInstanceState == null) {
	    // Add the fragment on initial activity setup
	    mainFragment = new MainFragment();
	        getActivity().getSupportFragmentManager()
	        .beginTransaction()
	      .add(android.R.id.content, mainFragment)
	      .commit();
	   } else {
	      // Or set the fragment from restored state info
	      mainFragment = (MainFragment) getActivity().getSupportFragmentManager()
	      .findFragmentById(android.R.id.content);
	    }
	  
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, 
            ViewGroup container, 
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);

        return view;
    }
	
	
}
