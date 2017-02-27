package ru.pp.mita.lesson01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;


import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static ru.pp.mita.lesson01.R.layout.activity_main;

//logging library



public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "mita";
    TextView mtxtView;
    TextView mtxtViewService;
    Button mBtn,mBtnReset;
    CheckBox mChkbox;
    EditText mEdTxt;
    Integer i;
    String sss;
    public View returnDataView;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Logger.init().methodCount(1).hideThreadInfo();
        //Logger.init().methodCount(0).hideThreadInfo();
        Log.d(LOG_TAG,"hello");
        setContentView(activity_main);

        mtxtView = (TextView) findViewById(R.id.textView);
        mtxtViewService = (TextView) findViewById(R.id.textView2);
        mBtn = (Button) findViewById(R.id.button);
        mBtnReset = (Button) findViewById(R.id.bntReset);
        mChkbox = (CheckBox) findViewById(R.id.checkBox);
        mEdTxt = (EditText) findViewById(R.id.editText);
        mtxtViewService.setText("From service with love...");
        mBtn.setText(R.string.myButtonText1);

        OnClickListener oclBtn = new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button:
                        Log.d(LOG_TAG,"Button clicked!");
                        mBtn.setText(R.string.myButtonText1);
                        mtxtView.setText(R.string.myBtn1Str);
                        mChkbox.setChecked(true);

                        try {
                            i = Integer.parseInt(mEdTxt.getText().toString());
                        } catch (NumberFormatException e) {
                            i = 0;
                        }
                        startService(new Intent(v.getContext(), MyFirstService.class).putExtra("time", i));

                        i++;
                        Log.d(LOG_TAG,"Increment");

                        try {
                            sss = i.toString();
                        } catch (NumberFormatException e) {
                            sss = "0";
                        }
                        mEdTxt.setText(sss);
                        break;
                    case R.id.bntReset:
                        Log.d(LOG_TAG,"Reset");
                        //startService(new Intent(v.getContext(), MyFirstService.class).putExtra("cmd", "stop"));
                        mtxtView.setText(R.string.myStr1);
                        mChkbox.setChecked(false);
                        mEdTxt.setText(R.string.str_times_clicked);
                }

            }
        };

        mBtn.setOnClickListener(oclBtn);
        mBtnReset.setOnClickListener(oclBtn);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    void OnSvcMessage(MessageEvent event) {
        mtxtViewService.setText(event.message + "\n" + mtxtViewService.getText());
    }
}
