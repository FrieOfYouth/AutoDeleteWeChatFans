package com.clearlee.autosendwechatmsg;

import android.accessibilityservice.AccessibilityService;
import com.clearlee.autosendwechatmsg.WeChatConstantPool.ResourceID;

/**
 * Created by 胡山文 on 11/17/2018.
 */
public class UserInfoCapturer
{

    private UserInfoPool userInfoPool;
    private AccessibilityService accessibilityService;

    public UserInfoCapturer(AccessibilityService accessibilityService,UserInfoPool userInfoPool)
    {
        this.userInfoPool=userInfoPool;
        this.accessibilityService = accessibilityService;
    }

    public void captureProfileInfo()
    {
        captureUsername();
    }

    private void captureUsername()
    {
        String username = WechatUtils.findTextById(accessibilityService, ResourceID.PROFILE_USERNAME);
        if (username != null && !username.isEmpty())
            this.userInfoPool.setUsername(username);

    }


}
