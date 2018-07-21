package com.blink22.android.gitpop;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by ahmedghazy on 7/17/18.
 */

public class GitPopAdapter extends RecyclerView.Adapter<GitPopHolder> {
    Context mContext;
    List<Repo> mRepos;

    public GitPopAdapter(Context context) {
        mContext = context;
    }

    @Override
    public GitPopHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new GitPopHolder(inflater, parent, mContext);
    }

    public void setRepos(List<Repo> repos) {
        mRepos = repos;
    }

    @Override
    public void onBindViewHolder(GitPopHolder holder, int position) {
        holder.bind(mRepos.get(position));
    }

    @Override
    public int getItemCount() {
        return mRepos.size();
    }
}
