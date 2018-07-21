package com.blink22.android.gitpop;

import android.support.v4.app.Fragment;

/**
 * Created by ahmedghazy on 7/17/18.
 */

public class GitPopListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return GitPopListFragment.newInstance();
    }
}
