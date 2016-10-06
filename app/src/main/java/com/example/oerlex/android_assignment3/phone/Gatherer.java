package com.example.oerlex.android_assignment3.phone;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.Bundle;
import android.telephony.TelephonyManager;

/**
 * Created by oerlex on 06.10.2016.
 */
public class Gatherer extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if(bundle == null){
            return;
        }else{
            String telephoneState = bundle.getString(TelephonyManager.EXTRA_STATE);
            if( telephoneState.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)){
                String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                String time = new SimpleDateFormat("dd.MM. HH:mm:ss").format(Calendar.getInstance().getTime());
                WriteReadCalls writeReadCalls = new WriteReadCalls();
                writeReadCalls.saveCalls(context, time + " " + number);
            }
        }
    }
}
