package com.amp.news.UI.Fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amp.news.Models.News.NewsDetail;
import com.amp.news.R;
import com.amp.news.UI.Adapters.NewsListAdapter;
import com.amp.news.ViewModels.NewsViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavouritesFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.empty_tv)
    TextView empty_tv;

    private NewsViewModel newsViewModel;
    private NewsListAdapter newsListAdapter;
    private RecyclerView.AdapterDataObserver dataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            if (newsListAdapter.getItemCount() > 0)
                empty_tv.setVisibility(View.GONE);

        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            if (newsListAdapter.getItemCount() == 0)
                empty_tv.setVisibility(View.VISIBLE);
        }
    };

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
        newsViewModel.getSavedNews().observe(this, new Observer<PagedList<NewsDetail>>() {
            @Override
            public void onChanged(@Nullable PagedList<NewsDetail> newsDetails) {
                newsListAdapter.submitList(newsDetails);
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
        newsListAdapter.registerAdapterDataObserver(dataObserver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        newsListAdapter.unregisterAdapterDataObserver(dataObserver);
    }
}
