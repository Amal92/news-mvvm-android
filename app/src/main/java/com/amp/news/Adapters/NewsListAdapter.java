package com.amp.news.Adapters;

import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amp.news.Models.News.NewsDetail;
import com.amp.news.R;
import com.amp.news.ViewModels.NewsViewModel;
import com.bumptech.glide.Glide;

import org.joda.time.DateTime;

import java.util.ListIterator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by amal on 14/12/18.
 */

public class NewsListAdapter extends PagedListAdapter<NewsDetail, NewsListAdapter.MyViewHolder> {

    private static DiffUtil.ItemCallback<NewsDetail> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<NewsDetail>() {
                @Override
                public boolean areItemsTheSame(NewsDetail oldItem, NewsDetail newItem) {
                    return oldItem.getUrl().equals(newItem.getUrl());
                }

                @Override
                public boolean areContentsTheSame(NewsDetail oldItem, NewsDetail newItem) {
                    return oldItem.equals(newItem);
                }
            };
    private LayoutInflater mInflater;
    private Context context;
    private NewsViewModel newsViewModel;

    public NewsListAdapter(Context mContext) {
        super(DIFF_CALLBACK);
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
    public void onBindViewHolder(final NewsListAdapter.MyViewHolder holder, int position) {
        final NewsDetail newsDetail = getItem(position); //newsDetails.get(position);
        if (newsDetail != null) {
            holder.title_tv.setText(newsDetail.getTitle());
            holder.description_tv.setText(newsDetail.getDescription());
            holder.source_tv.setText(newsDetail.getSource().getName());
            DateTime dateTime = new DateTime(newsDetail.getPublishedAt());
            String datetimeString = (String) DateUtils.getRelativeTimeSpanString(dateTime.getMillis(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
            holder.time_tv.setText(datetimeString);
            Glide.with(context).load(newsDetail.getUrlToImage()).thumbnail(0.1f).into(holder.image_iv);
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    if (newsViewModel.checkIfNewsExists(newsDetail))
                        holder.bookmark_button.setImageResource(R.drawable.bookmark);
                    else holder.bookmark_button.setImageResource(R.drawable.bookmark_outline);
                }
            });
        }
    }

    public void removeBookmark(NewsDetail newsDetail) {

        if (getCurrentList() != null) {
            ListIterator iterator = getCurrentList().listIterator();
            while (iterator.hasNext()) {
                NewsDetail newsDetail1 = (NewsDetail) iterator.next();
                if (newsDetail1.getUrl().equals(newsDetail.getUrl())) {
                    int index = iterator.nextIndex();
                    notifyItemChanged(index - 1);
                    break;
                }
            }
        }
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
                    if (bookmark_button.getDrawable().getConstantState() == context.getResources().getDrawable(R.drawable.bookmark).getConstantState()) {
                        // delete
                        bookmark_button.setImageResource(R.drawable.bookmark_outline);
                        newsViewModel.deleteArticle(getItem(getAdapterPosition()));
                    } else {
                        //insert
                        bookmark_button.setImageResource(R.drawable.bookmark);
                        newsViewModel.insertArticle(getItem(getAdapterPosition()));
                    }

                }
            });
        }
    }
}
