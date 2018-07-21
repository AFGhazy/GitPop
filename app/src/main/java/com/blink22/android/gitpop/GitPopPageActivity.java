package com.blink22.android.gitpop;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by ahmedghazy on 7/18/18.
 */

public class GitPopPageActivity extends SingleFragmentActivity {
    private static final String EXTRA_LINK = "com.blink22.android.gitpop.link";

    public static Intent newIntent(Context context, String link) {
        Intent intent = new Intent(context, GitPopPageActivity.class);
        intent.putExtra(EXTRA_LINK, link);
        Log.i("GitPopPageActivity", link);
        Log.i("GitPopPageActivity", context.toString());
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return GitPopPageFragment.newInstance(getIntent().getStringExtra(EXTRA_LINK));
    }
}
