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
import com.amp.news.ApiResponsePojo.NewsApiResponse;
import com.amp.news.Models.NewsDetail;
import com.amp.news.R;
import com.amp.news.ViewModels.NewsViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsTabFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private NewsListAdapter newsListAdapter;
    private NewsViewModel newsViewModel;

    public NewsTabFragment() {
        // Required empty public constructor
    }

    public static NewsTabFragment newInstance(String category) {

        NewsTabFragment newsTabFragment = new NewsTabFragment();
        Bundle args = new Bundle();
        args.putString("category", category);
        newsTabFragment.setArguments(args);
        return newsTabFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_tab, container, false);
        ButterKnife.bind(this, view);
        setUpRecyclerView();
        String category = getArguments().getString("category");
        newsViewModel = ViewModelProviders.of(getActivity()).get(NewsViewModel.class);
        if (category.equals("latest"))
            category = null;
        newsViewModel.getNews(category).observe(this, new Observer<NewsApiResponse>() {
            @Override
            public void onChanged(@Nullable NewsApiResponse newsApiResponse) {
                if (newsApiResponse != null)
                    newsListAdapter.setDataList(newsApiResponse.getNewsDetails());
            }
        });

        newsViewModel.getDeletedNewsArticle().observe(this, new Observer<NewsDetail>() {
            @Override
            public void onChanged(@Nullable NewsDetail newsDetail) {
                newsListAdapter.removeBookmark(newsDetail);
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
