package com.farina.circleprogressdemo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.farina.circleprogressdemo.R;
import com.farina.circleprogressdemo.component.DrawHookView;
import com.farina.circleprogressdemo.message.MessageConst;

public class MainActivity extends AppCompatActivity {
    Handler handler;
    private DrawHookView hooKView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initHandler();
        hooKView= (DrawHookView)findViewById(R.id.progress2);
        hooKView.setHandler(handler);
    }
    private void initHandler(){
         handler=new Handler(){
            @Override
            public void handleMessage(Message msg){
                switch(msg.what){
                    case MessageConst.CLIENT_ACTION_SINGN_ANIM_CLEAR:{
                        hooKView.clearMyAnim();
                        break;
                    }
                    case MessageConst.CLIENT_ACTION_SINGN_ANIM_INVALIDATE:{
                        hooKView.invalidate();
                        break;
                    }
                }
            }
        };
    }
}
