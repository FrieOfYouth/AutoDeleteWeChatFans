package com.clearlee.autosendwechatmsg;

import android.util.Log;

/**
 * Created by 胡山文 on 11/14/2018.
 */
public class UserInfoPool
{

    private String username;
    private String UID;
    private String gender;


    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        Log.d("UserInfoPool", username);
        this.username=username;
    }

    public String getUID()
    {
        return UID;
    }

    public void setUID(String UID)
    {
        this.UID = UID;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }
}
