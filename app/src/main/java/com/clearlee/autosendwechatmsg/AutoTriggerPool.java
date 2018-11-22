package com.clearlee.autosendwechatmsg;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import com.clearlee.autosendwechatmsg.WeChatConstantPool.ResourceID;
import com.clearlee.autosendwechatmsg.WeChatConstantPool.Text;
import com.clearlee.autosendwechatmsg.WeChatConstantPool.WechatUIClassName;

/**
 * Created by 胡山文 on 11/12/2018.
 */
public class AutoTriggerPool
{

    public static void clickContacts(AccessibilityService accessibilityService)
    {
        Log.d("message", "+++++++++++++++++++++++");

        WechatUtils.findTextAndClick(accessibilityService, Text.CONTACTS);
    }

    public static void clickSendingMessage(AccessibilityService accessibilityService)
    {

        WechatUtils.findTextAndClick(accessibilityService, Text.SENDING_MESSAGE);
    }

    public static void clickChattingInfo(AccessibilityService accessibilityService)
    {
        WechatUtils.findTextAndClick(accessibilityService, Text.CHATTING_INFO);
    }

    public static void clickChattingInfoUIHeadView(AccessibilityService accessibilityService)
    {
        WechatUtils.findViewIdAndClick(accessibilityService, ResourceID.CHATTING_INFO_UI_HEAD_IMAGE);
    }

    public static void clickMore(AccessibilityService accessibilityService)
    {
            WechatUtils.findViewIdAndClick(accessibilityService, ResourceID.MORE);
    }
    public static void clickDelete(AccessibilityService accessibilityService)
    {
        try{

            WechatUtils.findTextAndClick(accessibilityService,Text.DELETE);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void clickBack(AccessibilityService accessibilityService)
    {
        accessibilityService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }


}

class AutoTriggerHelper
{

    private String currentActivity;


    public final boolean isLaunchUI = this.isLaunchUI();

    public AutoTriggerHelper(AccessibilityEvent event)
    {

        this.currentActivity = event.getClassName().toString();

    }

    private boolean isLaunchUI()
    {
        return (isNotEmpty() && currentActivity.equals(WechatUIClassName.HOME));
    }

    private boolean isNotEmpty()
    {
        return currentActivity != null && !currentActivity.isEmpty();

    }
}
