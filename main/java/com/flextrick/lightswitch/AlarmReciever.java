package com.flextrick.lightswitch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReciever extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        new SendCommands(context).execute("sudo sh one_twoON.sh");

    }

}