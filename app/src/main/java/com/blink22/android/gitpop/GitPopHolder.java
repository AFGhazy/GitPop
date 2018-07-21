package com.blink22.android.gitpop;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ahmedghazy on 7/17/18.
 */

public class GitPopHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.list_item_title) TextView mTitle;
    @BindView(R.id.list_item_technology) TextView mLanguage;
    Context mContext;

    private static final Map<String, Integer> colors = new HashMap<String, Integer>(){{
        put("Java", Color.BLUE);
        put("Ruby", Color.RED);
        put("JavaScript", Color.YELLOW);
    }};

    public GitPopHolder(LayoutInflater inflater, ViewGroup parent, Context context) {
        super(inflater.inflate(R.layout.list_item_git_pop, parent, false));
        itemView.setOnClickListener(this);
        ButterKnife.bind(this, itemView);
        mContext = context;
    }

    public void bind(Repo repo) {
        mTitle.setText(repo.getFull_name());
        mLanguage.setText(repo.getLanguage());
        if(colors.get(repo.getLanguage()) != null)
            Log.i("GitPopHolder", colors.get(repo.getLanguage()).toString());
        mLanguage.setTextColor(colors.get(repo.getLanguage()) != null ? colors.get(repo.getLanguage()) : Color.BLACK);
    }

    @Override
    public void onClick(View view) {
        mContext.startActivity(GitPopPageActivity.newIntent(mContext, mTitle.getText().toString()));
    }
}
