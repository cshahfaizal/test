package app.airtouchmedia.candidate.com.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.util.Log;

/**
 * NetworkChangeEventListener.
 */

public class NetworkChangeEventListener {

    /**
     * TAG : class name
     */
    private final String TAG = "NetworkStateHandler";

    /**
     * Network change handler
     */
    private Handler networkChangeHandler = new Handler();

    /**
     * Network state Listener
     */
    private NetworkStateListener networkStateListener;

    public void registerNetWorkChangeBroadCast(Context context) {
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        context.registerReceiver(networkStateChangeReceiver, intentFilter);
    }

    public void setNetworkStateListener(NetworkStateListener networkStateListener) {
        this.networkStateListener = networkStateListener;
    }

    public void unRegisterNetWorkChangeBroadCast(Context context) {
        try {
            context.unregisterReceiver(networkStateChangeReceiver);
        } catch (Exception ex) {
            Log.d(TAG, "Exception :" + ex.getMessage());
        }
    }

    private BroadcastReceiver networkStateChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            if ("android.net.conn.CONNECTIVITY_CHANGE".equalsIgnoreCase(intent.getAction())) {
                boolean state = !(intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE));
                    networkChangeHandler.post(() -> {
                        if (networkStateListener != null) {
                            networkStateListener.onStateReceived(state);
                        }
                    });

                }
        }
    };

    public interface NetworkStateListener {
        void onStateReceived(boolean state);
    }
}
