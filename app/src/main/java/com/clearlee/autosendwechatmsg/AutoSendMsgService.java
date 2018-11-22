package com.clearlee.autosendwechatmsg;

import android.accessibilityservice.AccessibilityService;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.clearlee.autosendwechatmsg.WeChatConstantPool.ResourceID;
import com.clearlee.autosendwechatmsg.WeChatConstantPool.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Clearlee
 * 2017/12/22.
 */
public class AutoSendMsgService extends AccessibilityService
{

    private static final String TAG = "AutoSendMsgService";
    private List<String> allNameList = new ArrayList<>();
    private int mRepeatCount;
    private UserInfoPool userInfoPool = new UserInfoPool();
    static boolean hasSend;
    public static final int SEND_FAIL = 0;
    static final int SEND_SUCCESS = 1;
    static int SEND_STATUS;
    private ArrayList<String> usernameList = new ArrayList<>();

    /**
     * 必须重写的方法，响应各种事件。
     *
     * @param event
     */
    @Override
    public void onAccessibilityEvent(final AccessibilityEvent event)
    {
        int eventType = event.getEventType();
        switch (eventType)
        {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
            {

                boot();
            }
            break;
        }
    }


    /**
     * 从头至尾遍历寻找联系人
     */

    //all exe
    private void boot()
    {
        try
        {


            List<AccessibilityNodeInfo> listView = WechatUtils.getNodeInfo(this, ResourceID.CONTACT_LIST_VIEW);
            if (listView != null && !listView.isEmpty())
            {
//                while (true)
                {
                    checkingSingleContact();
                    listView.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }




    }

    private void checkingSingleContact()
    {
        try
        {
            final List<AccessibilityNodeInfo> listView = WechatUtils.getNodeInfo(this, ResourceID.CONTACT_LIST_VIEW);
            if (listView != null && !listView.isEmpty())
            {
                List<AccessibilityNodeInfo> nameList = WechatUtils.getNodeInfo(this, ResourceID.CONTACT);

                if (nameList != null && !nameList.isEmpty())
                {
                    for (int i = nameList.size() - 1; i >= 0; i--)
                    {
                        AccessibilityNodeInfo itemInfo = nameList.get(i).getParent();

                        String text = nameList.get(i).getText().toString();
                        if (!usernameList.contains(text))
                        {

                            int STEP = 1;
                            switch (STEP)
                            {
                                case 1:
                                    Thread.sleep(500);
                                    itemInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                    STEP = 2;
                                case 2:
                                    new UserInfoCapturer(this, this.userInfoPool).captureProfileInfo();
                                    STEP = 3;
                                case 3:
                                    AutoTriggerPool.clickSendingMessage(this);
                                    STEP = 4;
                                case 4:
//                                    slideToWeChattingTop();
                                    STEP = 5;
                                case 5:
                                    delFanOrChangeName();
                                    default:
                                        Log.d("default", "entering default");
                                        AutoTriggerPool.clickBack(this);
                            }

                            usernameList.add(text);

                        }

                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void delFanOrChangeName()
    {

        try
        {
            if (isRobot())
                deleteFan();
            else
            {
                Thread.sleep(800);
                AutoTriggerPool.clickBack(this);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private void deleteFan()
    {
        AutoTriggerPool.clickChattingInfo(this);
        AutoTriggerPool.clickChattingInfoUIHeadView(this);
        AutoTriggerPool.clickMore(this);
        AutoTriggerPool.clickDelete(this);
        AutoTriggerPool.clickDelete(this);
    }


    private boolean isRobot()
    {
        try
        {
            List<AccessibilityNodeInfo> nodeInfoList = WechatUtils.getNodeInfo(this, ResourceID.CHATTING_DIALOG);
            if (nodeInfoList != null && !nodeInfoList.isEmpty())
            {


                for (AccessibilityNodeInfo nodeInfo : nodeInfoList)
                {

                    String username = userInfoPool.getUsername();
                    String originText = nodeInfo.getText().toString();
                    boolean isPrefix=originText.contains(Text.PREFIX);
                    boolean isPostfix = originText.contains(Text.POSTFIX);
                    Log.d("text", nodeInfo.getText().toString());
                    if (isPrefix&&isPostfix)
                    {
                        Log.d("isRobot", isPrefix + "-" + isPostfix);
                        return true;
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }


    private boolean isHuman()
    {
        try
        {


            List<AccessibilityNodeInfo> nodeInfoList = WechatUtils.getNodeInfo(this, "com.tencent.mm:id/mq");
            if (nodeInfoList != null && !nodeInfoList.isEmpty())
            {
                Log.d(TAG, nodeInfoList.get(0).getText().toString());
                return nodeInfoList.get(0).getText().equals("我通过了你的朋友验证请求，现在我们可以开始聊天了");
            }
            return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }


    private void slideToWeChattingTop()
    {
        try
        {


            AccessibilityNodeInfo rootNode = getRootInActiveWindow();
            final List<AccessibilityNodeInfo> listView = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/mo");
            ArrayList<String> timeList = new ArrayList<>();
            int flag = 0;
            if (listView != null && !listView.isEmpty())
            {

                while (true)
                {
                    List<AccessibilityNodeInfo> timePointList = rootNode.findAccessibilityNodeInfosByViewId(WeChatConstantPool.ResourceID.TIME_POINT);

                    Log.d("***********----------", String.valueOf(flag));
                    if (timePointList != null && !timePointList.isEmpty())
                    {
                        for (int i = timePointList.size() - 1; i >= 0; i--)
                        {
                            String each = timePointList.get(i).getText().toString();
                            if (!timeList.contains(each))
                            {
                                Log.d("+++++++", each);
                                timeList.add(each);
                            }
                            else flag++;

                        }
//                        listView.get(0).getParent().getParent().performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
                    }


                    if (flag >= 3)
                    {
                        Log.d("*********", String.valueOf(flag));
                        flag = 0;
                        return;
                    }

                    try
                    {
                        Thread.sleep(new Random().nextInt(800) + 500);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    Log.d("Message", "********not jump out while loop");

                }

            }
            Log.d("Message", "********already jump out while loop");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void slideDownToBottom()
    {

        try
        {


            AccessibilityNodeInfo rootNode = getRootInActiveWindow();
            final List<AccessibilityNodeInfo> listView = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/cpj");

            boolean scrollToBottom = false;
            if (listView != null && !listView.isEmpty())
            {
                while (true)
                {
                    List<AccessibilityNodeInfo> nameList = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/azl");
                    List<AccessibilityNodeInfo> itemList = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/azj");


                    if (nameList != null && !nameList.isEmpty())
                    {
                        for (int i = 0; i < nameList.size(); i++)
                        {
                            AccessibilityNodeInfo nodeInfo = nameList.get(i);
                            String nickname = nodeInfo.getText().toString();
                            if (!allNameList.contains(nickname))
                            {
                                allNameList.add(nickname);
                            }
                            else if (allNameList.contains(nickname))
                            {
                                if (mRepeatCount == 3)
                                {
                                    //表示已经滑动到顶部了
                                    if (scrollToBottom)
                                    {
                                        return;
                                    }
                                    scrollToBottom = true;
                                }
                                mRepeatCount++;
                            }

                        }
                        if (!scrollToBottom)
                        {
                            //向下滚动
                            listView.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
                        }
                    }


                    else return;

                    try
                    {
                        Thread.sleep(new Random().nextInt(800) + 500);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void changeAlias()
    {

    }

    private void resetAndReturnApp()
    {
        hasSend = true;
        ActivityManager activtyManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activtyManager.getRunningTasks(3);
        for (ActivityManager.RunningTaskInfo runningTaskInfo : runningTaskInfos)
        {
            if (this.getPackageName().equals(runningTaskInfo.topActivity.getPackageName()))
            {
                activtyManager.moveTaskToFront(runningTaskInfo.id, ActivityManager.MOVE_TASK_WITH_HOME);
                return;
            }
        }
    }

    @Override
    public void onInterrupt()
    {

    }


}
