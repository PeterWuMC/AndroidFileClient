package com.wu.androidfileclient.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AutoStart extends BroadcastReceiver 
{
    public void onReceive(Context arg0, Intent arg1) 
    {
        Intent intent = new Intent(arg0, MonitorCameraService.class);
        arg0.startService(intent);
        Log.i("Autostart", "started");
    }
}