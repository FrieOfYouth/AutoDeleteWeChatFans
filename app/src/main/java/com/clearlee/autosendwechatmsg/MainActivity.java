package com.clearlee.autosendwechatmsg;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.accessibility.AccessibilityManager;
import android.widget.TextView;
import android.widget.Toast;
import com.clearlee.autosendwechatmsg.WeChatConstantPool.WechatUIClassName;

import java.util.Random;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.clearlee.autosendwechatmsg.AutoSendMsgService.SEND_STATUS;
import static com.clearlee.autosendwechatmsg.AutoSendMsgService.hasSend;
import static com.clearlee.autosendwechatmsg.WechatUtils.CONTENT;
import static com.clearlee.autosendwechatmsg.WechatUtils.NAME;

/**
 * Created by Clearlee
 * 2017/12/22.
 */
public class MainActivity extends AppCompatActivity
{

    private TextView start, sendStatus;
//    private EditText sendName, sendContent;
    private AccessibilityManager accessibilityManager;
//    private String name, content;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init()
    {

        start = findViewById(R.id.testWechat);
//        sendName = findViewById(R.id.sendName);
//        sendContent = findViewById(R.id.sendContent);
//        sendStatus = findViewById(R.id.sendStatus);
        start.setOnClickListener(v -> checkAndStartService());
    }

    private int goWeChat()
    {
        try
        {
            Thread.sleep(new Random().nextInt(800)+500);
//            setValue(name, content);
            hasSend = false;
            Intent intent = new Intent();
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.setClassName(WeChatConstantPool.WECHAT_PACKAGE_NAME, WechatUIClassName.HOME);
            startActivity(intent);

            while (true)
            {
                if (hasSend)
                {
                    return SEND_STATUS;
                }
                else
                {
                    try
                    {
                        Thread.sleep(new Random().nextInt(800)+500);

                    }
                    catch (Exception e)
                    {
                        openService();
                        e.printStackTrace();
                    }
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return SEND_STATUS;
        }
    }


    private void openService()
    {
        try
        {
            //打开系统设置中辅助功能
            Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
            Toast.makeText(MainActivity.this, "找到微信神器，然后开启服务即可", Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void checkAndStartService()
    {
        accessibilityManager = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);

//        name = sendName.getText().toString();
//        content = sendContent.getText().toString();
//
//        if (TextUtils.isEmpty(name))
//        {
//            Toast.makeText(MainActivity.this, "联系人不能为空", Toast.LENGTH_SHORT).show();
//        }
//        if (TextUtils.isEmpty(content))
//        {
//            Toast.makeText(MainActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
//        }

        if (!accessibilityManager.isEnabled())
        {
            openService();
        }
        else
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    statusHandler.sendEmptyMessage(goWeChat());
                }
            }).start();
        }
    }

    Handler statusHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            setSendStatusText(msg.what);
        }
    };

    private void setSendStatusText(int status)
    {
//        if (status == SEND_SUCCESS)
//        {
//            sendStatus.setText("微信发送成功");
//        }
//        else
//        {
//            sendStatus.setText("微信发送失败");
//        }
    }

    public void setValue(String name, String content)
    {
        NAME = name;
        CONTENT = content;
        hasSend = false;
    }

}
