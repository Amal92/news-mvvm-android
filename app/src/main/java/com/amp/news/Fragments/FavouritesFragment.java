package com.amp.news.Fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amp.news.Adapters.NewsListAdapter;
import com.amp.news.Models.News.NewsDetail;
import com.amp.news.R;
import com.amp.news.ViewModels.NewsViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavouritesFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private NewsViewModel newsViewModel;
    private NewsListAdapter newsListAdapter;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    public static FavouritesFragment newInstance() {

        return new FavouritesFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        ButterKnife.bind(this, view);
        setUpRecyclerView();

        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        newsViewModel.getSavedNews().observe(this, new Observer<List<NewsDetail>>() {
            @Override
            public void onChanged(@Nullable List<NewsDetail> newsDetails) {
                newsListAdapter.setDataList(newsDetails);
            }
        });


        return view;
    }

    private void setUpRecyclerView() {
        newsListAdapter = new NewsListAdapter(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(newsListAdapter);
    }

}
