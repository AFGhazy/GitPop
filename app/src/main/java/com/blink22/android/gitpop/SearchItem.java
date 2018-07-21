package com.blink22.android.gitpop;

import io.realm.RealmObject;

/**
 * Created by ahmedghazy on 7/19/18.
 */

public class SearchItem extends RealmObject {
    private String searchString;

    public SearchItem() {}

    public SearchItem(String searchString) {
        this.searchString = searchString;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }




}
