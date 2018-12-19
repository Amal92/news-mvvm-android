package com.amp.news.Adapters;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amp.news.Models.NewsDetail;
import com.amp.news.R;
import com.amp.news.ViewModels.NewsViewModel;
import com.bumptech.glide.Glide;

import org.joda.time.DateTime;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by amal on 14/12/18.
 */

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.MyViewHolder> {

    private LayoutInflater mInflater;
    private List<NewsDetail> newsDetails;
    private Context context;
    private NewsViewModel newsViewModel;

    public NewsListAdapter(Context mContext) {
        mInflater = LayoutInflater.from(mContext);
        this.context = mContext;
        newsViewModel = ViewModelProviders.of((FragmentActivity) context).get(NewsViewModel.class);
    }

    @Override
    public NewsListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.row_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NewsListAdapter.MyViewHolder holder, int position) {
        NewsDetail newsDetail = newsDetails.get(position);
        holder.title_tv.setText(newsDetail.getTitle());
        holder.description_tv.setText(newsDetail.getDescription());
        holder.source_tv.setText(newsDetail.getSource().getName());
        DateTime dateTime = new DateTime(newsDetail.getPublishedAt());
        String datetimeString = (String) DateUtils.getRelativeTimeSpanString(dateTime.getMillis(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
        holder.time_tv.setText(datetimeString);
        Glide.with(context).load(newsDetail.getUrlToImage()).into(holder.image_iv);
    }

    @Override
    public int getItemCount() {
        if (newsDetails != null)
            return newsDetails.size();
        else return 0;
    }

    public void setDataList(List<NewsDetail> newsDetails) {
        this.newsDetails = newsDetails;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.poster_iv)
        ImageView image_iv;

        @BindView(R.id.title_tv)
        TextView title_tv;

        @BindView(R.id.description_tv)
        TextView description_tv;

        @BindView(R.id.source_tv)
        TextView source_tv;

        @BindView(R.id.time_tv)
        TextView time_tv;

        @BindView(R.id.bookmark_button)
        ImageView bookmark_button;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            bookmark_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newsViewModel.insertArticle(newsDetails.get(getAdapterPosition()));
                }
            });
        }
    }
}
