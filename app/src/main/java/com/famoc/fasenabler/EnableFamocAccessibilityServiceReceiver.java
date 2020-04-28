package com.famoc.fasenabler;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.util.Log;

public class EnableFamocAccessibilityServiceReceiver extends BroadcastReceiver {

    private static final String TAG = EnableFamocAccessibilityServiceReceiver.class.getSimpleName();
    private static final String FAMOC_ACCESSIBILITY_SERVICE_COMPONENT = "com.fancyfon.baseAgent.services.FamocAccessibilityService";
    private static final String PACKAGE_NAME_KEY = "package_name";
    private static final String FAS_ENABLER_ACTION = "com.famoc.fasenabler.ACTION_ENABLE_FAS_RESULT";
    private static final String RESULT_KEY = "result";
    private static final String PERMISSION = "com.famoc.baseAgent.permission.fasenablepermission";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        String packageName = intent.getStringExtra(PACKAGE_NAME_KEY);
        if (context.checkSelfPermission(Manifest.permission.WRITE_SECURE_SETTINGS) != PackageManager.PERMISSION_GRANTED
                || isPackageNameEmpty(packageName)) {
            Log.e(TAG, "can't set accessibility service");
            context.sendBroadcast(getResultIntent(false), PERMISSION);
            return;
        }
        Settings.Secure.putString(context.getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
                String.format("%s/%s", packageName, FAMOC_ACCESSIBILITY_SERVICE_COMPONENT));
        context.sendBroadcast(getResultIntent(true), PERMISSION);
    }


    private boolean isPackageNameEmpty(String packageName) {
        return packageName == null || packageName.isEmpty();
    }

    private Intent getResultIntent(boolean success) {
        Intent intent = new Intent(FAS_ENABLER_ACTION);
        intent.putExtra(RESULT_KEY, success);
        return intent;
    }
}
