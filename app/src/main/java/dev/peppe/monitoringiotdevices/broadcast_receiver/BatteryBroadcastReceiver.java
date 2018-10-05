package dev.peppe.monitoringiotdevices.broadcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

public class BatteryBroadcastReceiver extends BroadcastReceiver {

    private int mProgressStatus = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the battery scale
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);
        // get the battery level
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);

        // Calculate the battery charged percentage
        float percentage = level/ (float) scale;
        mProgressStatus = (int)((percentage)*100);
    }

    public int getmProgressStatus() {
        return mProgressStatus;
    }
}
