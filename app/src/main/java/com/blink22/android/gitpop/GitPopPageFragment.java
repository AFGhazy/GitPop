package com.blink22.android.gitpop;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ahmedghazy on 7/18/18.
 */

public class GitPopPageFragment extends Fragment implements Callback<List<User>> {
    private static final String TAG = "GitPopPageFragment";

    private static final String ARG_LINK = "link";
    private static final String ARG_CONTRIBUTERS = "contributers";
    @BindView(R.id.contributers_recycler_view) RecyclerView mContributersRecyclerView;
    ContributerAdapter mContributerAdapter;
    List<User> mUsers;

    @BindView(R.id.page_title) TextView mTitle;

    public static GitPopPageFragment newInstance(String link) {

        Bundle args = new Bundle();
        args.putString(ARG_LINK, link);

        GitPopPageFragment fragment = new GitPopPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new GitPopFetcher().getContributers(getArguments().getString(ARG_LINK), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_git_pop_page, container, false);
        ButterKnife.bind(this, v);
        mTitle.setText(getArguments().getString(ARG_LINK));
        mContributersRecyclerView = (RecyclerView) v.findViewById(R.id.contributers_recycler_view);
        mContributersRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        Log.i(TAG, getArguments().getString(ARG_LINK));
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(getArguments().getString(ARG_LINK));
        return v;
    }

    @Override
    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
        mUsers = response.body();
        Log.i(TAG, response.body() + "");
        updateUI();
    }

    @Override
    public void onFailure(Call<List<User>> call, Throwable t) {

    }

    private void updateUI() {
        if(mContributerAdapter == null) {
            mContributerAdapter = new ContributerAdapter(getActivity(), "https://github.com/" + getArguments().getString(ARG_LINK));
            mContributersRecyclerView.setAdapter(mContributerAdapter);
        }

        if (mUsers != null) {
            mContributerAdapter.setUsers(mUsers);
        }

        mContributerAdapter.notifyDataSetChanged();
    }

    private class ContributerAdapter extends RecyclerView.Adapter<ContributerHolder> {
        List<User> mUsers = new ArrayList<>();
        Context mContext;
        String mLink;

        public ContributerAdapter(Context context, String link) {
            mContext = context;
            mLink = link;
        }

        public void setUsers(List<User> users) {
            mUsers = users;
        }

        @Override

        public ContributerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            return new ContributerHolder(inflater, parent, mContext);
        }

        @Override
        public void onBindViewHolder(ContributerHolder holder, int position) {
            if(position == mUsers.size()) {
                Log.i(TAG, "The repo link is : " + mLink);
                holder.bind(null, mLink, "Find more about the repo.", null);
                return;
            }
            User currentUser = mUsers.get(position);
            holder.bind(currentUser.getAvatar_url(), currentUser.getHtml_url(), currentUser.getLogin(), currentUser.getContributions());
        }

        @Override
        public int getItemCount() {
            return mUsers.size() + 1;
        }
    }

    public class ContributerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final Context mContext;
        @BindView(R.id.contributer_image)
        CircleImageView mImageView;
        @BindView(R.id.contributer_name)
        TextView mName;
        @BindView(R.id.contributer_number)
        TextView mNumber;
        String mHtmlUrl;

        public ContributerHolder(LayoutInflater inflater, ViewGroup parent, Context context) {
            super(inflater.inflate(R.layout.list_item_contributer, parent, false));
            ButterKnife.bind(this, itemView);
            mContext = context;
            itemView.setOnClickListener(this);
        }

        public void bind(String url, String html_url, String name, String number) {
            mName.setText(name);
            mHtmlUrl = html_url;
            if(url == null) {
                mImageView.setImageDrawable(getResources().getDrawable( android.R.drawable.ic_menu_search));
                return;
            }
            Glide.with(mContext)
                    .load(url)
                    .into(mImageView);
            mNumber.setText("Contrib.: " + number);
        }

        @Override
        public void onClick(View v) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mHtmlUrl));
            Intent browserChooserIntent = Intent.createChooser(browserIntent , "Choose browser of your choice");
            if(mContext != null) {
                mContext.startActivity(browserChooserIntent);
            }
        }
    }
}
