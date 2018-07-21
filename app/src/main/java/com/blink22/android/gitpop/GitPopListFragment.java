package com.blink22.android.gitpop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ahmedghazy on 7/17/18.
 */

public class GitPopListFragment extends Fragment implements Callback<List<Repo>> {
    @BindView(R.id.git_pop_recycler_view) RecyclerView mGitPopRecyclerView;
    @BindView(R.id.empty_view)
    TextView mEmptyView;
    GitPopAdapter mGitPopAdapter;
    List<Repo> mRepos;
    Realm mRealm;
    private static final String TAG = "GitPopListFragment";

    public static GitPopListFragment newInstance() {

        Bundle args = new Bundle();

        GitPopListFragment fragment = new GitPopListFragment();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Realm.init(getActivity());

        mRepos = new ArrayList<Repo>();

        mRealm = Realm.getDefaultInstance();

        RealmResults<SearchItem> results = mRealm.where(SearchItem.class).findAll();

        if(results != null && results.size() > 0) {
            new GitPopFetcher().getReposForUser(results.get(0).getSearchString(), this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_git_pop_list, container, false);

        ButterKnife.bind(this, v);

        mGitPopRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void updateUI() {
        if(mGitPopAdapter == null) {
            mGitPopAdapter = new GitPopAdapter(getActivity());
            mGitPopRecyclerView.setAdapter(mGitPopAdapter);
        }

        if (mRepos != null && mRepos.size() > 0) {
            mGitPopRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
            mGitPopAdapter.setRepos(mRepos);
        } else {
            mGitPopRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }

        mGitPopAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResponse(Call<List<Repo>> call, final Response<List<Repo>> response) {

        if(response.body() != null && response.body().size() > 0 && mRealm.where(Repo.class).equalTo("id", response.body().get(0).getId()).findAll().size() == 0) {
            Log.i(TAG, "found new items");

            mRealm.executeTransaction(new Realm.Transaction() {
                public void execute(Realm realm) {
                    realm.delete(Repo.class);

                    for(Repo repo: response.body()){
                        realm.insert(repo);
                    }
                }
            });

        }


        mRepos = mRealm.where(Repo.class).findAll();

        updateUI();
    }

    @Override
    public void onFailure(Call<List<Repo>> call, Throwable t) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_git_pop_list, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        final SearchItem searchItem = new SearchItem(query);
                        mRealm.executeTransaction(new Realm.Transaction() {

                            public void execute(Realm realm) {
                                realm.delete(SearchItem.class);

                                realm.insert(searchItem);
                            }
                        });

                        RealmResults<SearchItem> results = mRealm.where(SearchItem.class).findAll();

                        if(results.size() > 0) {
                            new GitPopFetcher().getReposForUser(results.get(0).getSearchString(), GitPopListFragment.this);
                        }

                        updateUI();


                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                }
        );

        searchView.setOnSearchClickListener(new SearchView.OnClickListener(){

            @Override
            public void onClick(View v) {
                RealmResults<SearchItem> results = mRealm.where(SearchItem.class).findAll();

                if(results.size() > 0) {
                    searchView.setQuery(results.get(0).getSearchString(), false);
                }
            }
        });
    }
}
