package com.example.jon.eventpro;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment
{

    private SearchView searchBar;

    public SearchFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchBar = view.findViewById(R.id.search_bar);

        return view;
    }
}
