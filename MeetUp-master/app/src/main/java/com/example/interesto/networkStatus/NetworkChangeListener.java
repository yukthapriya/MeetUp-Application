package com.example.interesto.networkStatus;

import static java.lang.Thread.sleep;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

public class NetworkChangeListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(!Common.isConnectedToInternet(context))
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setMessage("Please check the internet connection!!!")
                    .setCancelable(false)
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            onReceive(context,intent);
                        }
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            alert.setTitle("Network Issue");
            alert.show();
        }
    }
}
