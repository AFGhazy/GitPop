package com.blink22.android.gitpop;

import io.realm.RealmObject;

/**
 * Created by ahmedghazy on 7/18/18.
 */

public class User extends RealmObject {
    private String avatar_url;
    private String html_url;
    private String contributions;
    private String login;

    public String getContributions() {
        return contributions;
    }

    public void setContributions(String contributions) {
        this.contributions = contributions;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
}
