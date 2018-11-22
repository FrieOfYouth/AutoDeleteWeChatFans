package com.clearlee.autosendwechatmsg;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;
import java.util.Random;


/**
 * Created by Clearlee
 * 2017/12/22.
 */
public class WechatUtils
{

    public static String NAME;
    public static String CONTENT;

    /**
     * 在当前页面查找文字内容并点击
     */

    public static void slideDown(AccessibilityService accessibilityService)
    {
        AccessibilityNodeInfo rootNode = accessibilityService.getRootInActiveWindow();
        List<AccessibilityNodeInfo> listView = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/cpj");


//        try
//        {
//
//            for (int i = 0; i < 30; i++)
//            {
//
//                listView.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
//                Log.d(WechatUtils.class.getName(), String.valueOf(i));
//                Thread.sleep(12000);
//                WechatUtils.findTextAndClick(accessibilityService,"海阔天空");
//                if (currentActivity.equals(WeChatConstantPool.WechatUIClassName.WECHAT_CLASS_LAUNCH_UI))
//                {
//
//
//                }
//
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
    }


    public static void findTextAndClick(AccessibilityService accessibilityService, String text)
    {

        try
        {
            int time=new Random().nextInt(800) + 500;
            Log.d("findTextAndClick--"+text, String.valueOf(time));
            Thread.sleep(time);
            AccessibilityNodeInfo accessibilityNodeInfo = accessibilityService
                    .getRootInActiveWindow();

            if (accessibilityNodeInfo == null)
            {
                return;
            }

            List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text);
            if (nodeInfoList != null && !nodeInfoList.isEmpty())
            {

                for (AccessibilityNodeInfo nodeInfo : nodeInfoList)
                {
                    if (nodeInfo != null && (text.equals(nodeInfo.getText()) || text.equals(nodeInfo.getContentDescription())))
                    {
                        performClick(nodeInfo);
                        break;
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 检查viewId进行点击
     *
     * @param accessibilityService
     */


    public static void findViewIdAndClick(AccessibilityService accessibilityService, String id)
    {

        try
        {

            int time=new Random().nextInt(800) + 500;
            Log.d("findViewIDAndClick--"+id, String.valueOf(time));
            Thread.sleep(time);
            AccessibilityNodeInfo accessibilityNodeInfo = accessibilityService.getRootInActiveWindow();
            if (accessibilityNodeInfo == null)
            {
                return;
            }

            List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(id);
            if (nodeInfoList != null && !nodeInfoList.isEmpty())
            {
                for (AccessibilityNodeInfo nodeInfo : nodeInfoList)
                {
                    if (nodeInfo != null)
                    {
                        performClick(nodeInfo);
                        break;
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = VERSION_CODES.LOLLIPOP)
    public static boolean findViewByIdAndPasteContent(AccessibilityService accessibilityService, String id, String content)
    {
        AccessibilityNodeInfo rootNode = accessibilityService.getRootInActiveWindow();
        if (rootNode != null)
        {
            List<AccessibilityNodeInfo> editInfo = rootNode.findAccessibilityNodeInfosByViewId(id);
            if (editInfo != null && !editInfo.isEmpty())
            {
                Bundle arguments = new Bundle();
                arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, content);
                editInfo.get(0).performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                return true;
            }
            return false;
        }
        return false;
    }

    public static String findTextById(AccessibilityService accessibilityService, String id)
    {
        try
        {
            int time=new Random().nextInt(800) + 500;
            Log.d("findTextByID--"+id, String.valueOf(time));
            Thread.sleep(time);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        AccessibilityNodeInfo rootInfo = accessibilityService.getRootInActiveWindow();
        if (rootInfo != null)
        {

            List<AccessibilityNodeInfo> userNames = rootInfo.findAccessibilityNodeInfosByViewId(id);
            if (userNames != null && userNames.size() > 0)
                return userNames.get(0).getText().toString();
        }
        else
        {
            Log.d("message","this is null rootInfo");
            return null;
        }
        return null;
    }


    /**
     * 在当前页面查找对话框文字内容并点击
     *
     * @param text1 默认点击text1
     * @param text2
     */
    public static void findDialogAndClick(AccessibilityService accessibilityService, String text1, String text2)
    {

        AccessibilityNodeInfo accessibilityNodeInfo = accessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo == null)
        {
            return;
        }

        List<AccessibilityNodeInfo> dialogWait = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text1);
        List<AccessibilityNodeInfo> dialogConfirm = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text2);
        if (!dialogWait.isEmpty() && !dialogConfirm.isEmpty())
        {
            for (AccessibilityNodeInfo nodeInfo : dialogWait)
            {
                if (nodeInfo != null && text1.equals(nodeInfo.getText()))
                {
                    performClick(nodeInfo);
                    break;
                }
            }
        }

    }

    public static List<AccessibilityNodeInfo> getNodeInfo(AccessibilityService accessibilityService, String ID)
    {
        try
        {

            Thread.sleep(500);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        AccessibilityNodeInfo accessibilityNodeInfo = accessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo == null)
        {
            return null;
        }

        return accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(ID);

    }


    //模拟点击事件
    public static void performClick(AccessibilityNodeInfo nodeInfo)
    {
        if (nodeInfo == null)
        {
            return;
        }
        if (nodeInfo.isClickable())
        {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
        else
        {
            performClick(nodeInfo.getParent());
        }
    }

    //模拟返回事件
    public static void performBack(AccessibilityService service)
    {
        if (service == null)
        {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            try
            {
                Thread.sleep(5000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        }
    }
}
