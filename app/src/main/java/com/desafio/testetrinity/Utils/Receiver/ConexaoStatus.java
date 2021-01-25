package com.desafio.testetrinity.Utils.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class ConexaoStatus extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        try
        {
            if (isOnline(context)) {
                Toast.makeText(context, "Feed atualizado com sucesso.", Toast.LENGTH_SHORT).show();
                Log.e("status", "Online ");
            } else {
                Toast.makeText(context, "Não foi possível atualizar o feed. Sem conexão.", Toast.LENGTH_SHORT).show();
                Log.e("status", "Offline ");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    private boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            assert cm != null;
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
}
