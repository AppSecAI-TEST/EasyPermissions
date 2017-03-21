package com.j7arsen;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.WindowManager;

import com.j7arsen.event.ObservableController;
import com.j7arsen.event.PermissionEvent;

import java.util.ArrayList;

/**
 * Created by arsen on 21.03.17.
 */

public class TransparentPermissionActivity extends AppCompatActivity {

    public static final int REQ_CODE_PERMISSION_REQUEST = 101;
    public static final int REQ_CODE_REQUEST_SETTING = 102;
    public static final int REQ_CODE_SYSTEM_ALERT_WINDOW_PERMISSION_REQUEST = 103;
    public static final int REQ_CODE_SYSTEM_ALERT_WINDOW_PERMISSION_REQUEST_SETTING = 104;

    private static final String EXTRA_PERMISSIONS = "TransparentPermissionActivity.EXTRA_PERMISSIONS";
    private static final String EXTRA_USER_MESSAGE = "TransparentPermissionActivity.EXTRA_USER_MESSAGE";
    private static final String EXTRA_DENY_MESSAGE = "TransparentPermissionActivity.EXTRA_DENY_MESSAGE";
    private static final String EXTRA_IS_PRESENT_SETTING_BUTTON = "TransparentPermissionActivity.EXTRA_IS_PRESENT_SETTING_BUTTON";
    private static final String EXTRA_SETTINGS_TEXT = "TransparentPermissionActivity.EXTRA_SETTINGS_TEXT";
    private static final String EXTRA_CLOSE_TEXT = "TransparentPermissionActivity.EXTRA_CLOSE_TEXT";
    private static final String EXTRA_USER_MESSAGE_CONFIRM_TEXT = "TransparentPermissionActivity.EXTRA_USER_MESSAGE_CONFIRM_TEXT";
    private static final String EXTRA_PACKAGE = "TransparentPermissionActivity.EXTRA_PACKAGE";

    private static final String SAVE_EXTRA_PERMISSIONS = "TransparentPermissionActivity.SAVE_EXTRA_PERMISSIONS";
    private static final String SAVE_EXTRA_USER_MESSAGE = "TransparentPermissionActivity.SAVE_EXTRA_USER_MESSAGE";
    private static final String SAVE_EXTRA_DENY_MESSAGE = "TransparentPermissionActivity.SAVE_EXTRA_DENY_MESSAGE";
    private static final String SAVE_EXTRA_IS_PRESENT_SETTING_BUTTON = "TransparentPermissionActivity.SAVE_EXTRA_IS_PRESENT_SETTING_BUTTON";
    private static final String SAVE_EXTRA_SETTINGS_TEXT = "TransparentPermissionActivity.SAVE_EXTRA_SETTINGS_TEXT";
    private static final String SAVE_EXTRA_CLOSE_TEXT = "TransparentPermissionActivity.SAVE_EXTRA_CLOSE_TEXT";
    private static final String SAVE_EXTRA_USER_MESSAGE_CONFIRM_TEXT = "TransparentPermissionActivity.SAVE_EXTRA_USER_MESSAGE_CONFIRM_TEXT";
    private static final String SAVE_EXTRA_PACKAGE = "TransparentPermissionActivity.EXTRA_PACKAGE";

    private String[] mPermissions;
    private String mUserMessage;
    private String mDenyMessage;
    private boolean isPresentSettingButton;
    private String mSettingText;
    private String mCloseText;
    private String mUserMessageConfirmText;
    private String mPackageName;

    boolean isShownRationaleDialog;

    public static void startActivity(Context context, String[] permissions, String userMessage, String denyMessage, boolean isPresentSettingsButton, String settingText, String closeText, String userMessageConfirmText, String packageName) {
        Intent intent = new Intent(context, TransparentPermissionActivity.class);

        intent.putExtra(EXTRA_PERMISSIONS, permissions);
        intent.putExtra(EXTRA_USER_MESSAGE, userMessage);
        intent.putExtra(EXTRA_DENY_MESSAGE, denyMessage);
        intent.putExtra(EXTRA_IS_PRESENT_SETTING_BUTTON, isPresentSettingsButton);
        intent.putExtra(EXTRA_SETTINGS_TEXT, settingText);
        intent.putExtra(EXTRA_CLOSE_TEXT, closeText);
        intent.putExtra(EXTRA_USER_MESSAGE_CONFIRM_TEXT, userMessageConfirmText);
        intent.putExtra(EXTRA_PACKAGE, packageName);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        getExtras();
        if (savedInstanceState != null) {
            mPermissions = savedInstanceState.getStringArray(SAVE_EXTRA_PERMISSIONS);
            mUserMessage = savedInstanceState.getString(SAVE_EXTRA_USER_MESSAGE);
            mDenyMessage = savedInstanceState.getString(SAVE_EXTRA_DENY_MESSAGE);
            isPresentSettingButton = savedInstanceState.getBoolean(SAVE_EXTRA_IS_PRESENT_SETTING_BUTTON);
            mSettingText = savedInstanceState.getString(SAVE_EXTRA_SETTINGS_TEXT);
            mCloseText = savedInstanceState.getString(SAVE_EXTRA_CLOSE_TEXT);
            mUserMessageConfirmText = savedInstanceState.getString(SAVE_EXTRA_USER_MESSAGE_CONFIRM_TEXT);
            mPackageName = savedInstanceState.getString(SAVE_EXTRA_PACKAGE);
        }
        if (needWindowPermission()) {
            requestWindowPermission();
        } else {
            checkPermissions(false);
        }
    }

    private void getExtras() {
        if (getIntent().getExtras() != null) {
            mPermissions = getIntent().getExtras().getStringArray(EXTRA_PERMISSIONS);
            mUserMessage = getIntent().getExtras().getString(EXTRA_USER_MESSAGE);
            mDenyMessage = getIntent().getExtras().getString(EXTRA_DENY_MESSAGE);
            isPresentSettingButton = getIntent().getExtras().getBoolean(EXTRA_IS_PRESENT_SETTING_BUTTON);
            mSettingText = getIntent().getExtras().getString(EXTRA_SETTINGS_TEXT);
            mCloseText = getIntent().getExtras().getString(EXTRA_CLOSE_TEXT);
            mUserMessageConfirmText = getIntent().getExtras().getString(EXTRA_USER_MESSAGE_CONFIRM_TEXT);
            mPackageName = getIntent().getExtras().getString(EXTRA_PACKAGE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArray(SAVE_EXTRA_PERMISSIONS, mPermissions);
        outState.putString(SAVE_EXTRA_USER_MESSAGE, mUserMessage);
        outState.putString(SAVE_EXTRA_DENY_MESSAGE, mDenyMessage);
        outState.putBoolean(SAVE_EXTRA_IS_PRESENT_SETTING_BUTTON, isPresentSettingButton);
        outState.putString(SAVE_EXTRA_SETTINGS_TEXT, mSettingText);
        outState.putString(SAVE_EXTRA_CLOSE_TEXT, mCloseText);
        outState.putString(SAVE_EXTRA_USER_MESSAGE_CONFIRM_TEXT, mUserMessageConfirmText);
        outState.putString(SAVE_EXTRA_PACKAGE, mPackageName);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPermissions = savedInstanceState.getStringArray(SAVE_EXTRA_PERMISSIONS);
        mUserMessage = savedInstanceState.getString(SAVE_EXTRA_USER_MESSAGE);
        mDenyMessage = savedInstanceState.getString(SAVE_EXTRA_DENY_MESSAGE);
        isPresentSettingButton = savedInstanceState.getBoolean(SAVE_EXTRA_IS_PRESENT_SETTING_BUTTON);
        mSettingText = savedInstanceState.getString(SAVE_EXTRA_SETTINGS_TEXT);
        mCloseText = savedInstanceState.getString(SAVE_EXTRA_CLOSE_TEXT);
        mUserMessageConfirmText = savedInstanceState.getString(SAVE_EXTRA_USER_MESSAGE_CONFIRM_TEXT);
        mPackageName = savedInstanceState.getString(SAVE_EXTRA_PACKAGE);
    }

    private void permissionGranted() {
        ObservableController.getInstance().notifyEvent(new PermissionEvent(true, null));
        finish();
        overridePendingTransition(0, 0);
    }

    private void permissionDenied(ArrayList<String> deniedpermissions) {
        ObservableController.getInstance().notifyEvent(new PermissionEvent(false, deniedpermissions));
        finish();
        overridePendingTransition(0, 0);
    }

    private boolean needWindowPermission() {
        for (String permission : mPermissions) {
            if (permission.equals(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
                return !hasWindowPermission();
            }
        }
        return false;
    }

    private boolean hasWindowPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(getApplicationContext());
        } else {
            return false;
        }

    }

    private void requestWindowPermission() {
        Uri uri = Uri.fromParts("package", mPackageName, null);
        final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri);

        if (!TextUtils.isEmpty(mUserMessage)) {
            if (TextUtils.isEmpty(mUserMessageConfirmText)) {
                mUserMessageConfirmText = getString(R.string.permission_confirm);
            }
            new AlertDialog.Builder(this)
                    .setMessage(mUserMessage)
                    .setCancelable(false)

                    .setNegativeButton(mUserMessageConfirmText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivityForResult(intent, REQ_CODE_SYSTEM_ALERT_WINDOW_PERMISSION_REQUEST);
                        }
                    })
                    .show();
            isShownRationaleDialog = true;
        } else {
            startActivityForResult(intent, REQ_CODE_SYSTEM_ALERT_WINDOW_PERMISSION_REQUEST);
        }
    }

    private void checkPermissions(boolean fromOnActivityResult) {
        ArrayList<String> needPermissions = new ArrayList<>();
        for (String permission : mPermissions) {
            if (permission.equals(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
                if (!hasWindowPermission()) {
                    needPermissions.add(permission);
                }
            } else {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    needPermissions.add(permission);
                }
            }
        }

        if (needPermissions.isEmpty()) {
            permissionGranted();
        } else if (fromOnActivityResult) {
            permissionDenied(needPermissions);
        } else if (needPermissions.size() == 1 && needPermissions.contains(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
            permissionDenied(needPermissions);
        } else if (!isShownRationaleDialog && !TextUtils.isEmpty(mUserMessage)) {
            showRationaleDialog(needPermissions);
        } else {
            requestPermissions(needPermissions);
        }
    }

    public void requestPermissions(ArrayList<String> needPermissions) {
        ActivityCompat.requestPermissions(this, needPermissions.toArray(new String[needPermissions.size()]), REQ_CODE_PERMISSION_REQUEST);
    }

    private void showRationaleDialog(final ArrayList<String> needPermissions) {
        if (TextUtils.isEmpty(mUserMessageConfirmText)) {
            mUserMessageConfirmText = getString(R.string.permission_confirm);
        }
        new AlertDialog.Builder(this)
                .setMessage(mUserMessage)
                .setCancelable(false)

                .setNegativeButton(mUserMessageConfirmText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPermissions(needPermissions);

                    }
                })
                .show();
        isShownRationaleDialog = true;
    }

    public void showWindowPermissionDenyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (TextUtils.isEmpty(mCloseText)) {
            mCloseText = getString(R.string.permission_close);
        }
        builder.setMessage(mDenyMessage)
                .setCancelable(false)
                .setNegativeButton(mCloseText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkPermissions(false);
                    }
                });

        if (isPresentSettingButton) {
            if (TextUtils.isEmpty(mSettingText)) {
                mSettingText = getString(R.string.permission_setting);
            }

            builder.setPositiveButton(mSettingText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Uri uri = Uri.fromParts("package", mPackageName, null);
                    final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri);
                    startActivityForResult(intent, REQ_CODE_SYSTEM_ALERT_WINDOW_PERMISSION_REQUEST_SETTING);
                }
            });

        }
        builder.show();
    }

    public void showPermissionDenyDialog(final ArrayList<String> deniedPermissions) {
        if (TextUtils.isEmpty(mDenyMessage)) {
            permissionDenied(deniedPermissions);
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (TextUtils.isEmpty(mCloseText)) {
            mCloseText = getString(R.string.permission_close);
        }
        builder.setMessage(mDenyMessage)
                .setCancelable(false)

                .setNegativeButton(mCloseText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        permissionDenied(deniedPermissions);
                    }
                });

        if (isPresentSettingButton) {


            if (TextUtils.isEmpty(mSettingText)) {
                mSettingText = getString(R.string.permission_setting);
            }

            builder.setPositiveButton(mSettingText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    try {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                .setData(Uri.parse("package:" + mPackageName));
                        startActivityForResult(intent, REQ_CODE_REQUEST_SETTING);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                        startActivityForResult(intent, REQ_CODE_REQUEST_SETTING);
                    }

                }
            });

        }
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ArrayList<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permission);
            }
        }
        if (deniedPermissions.isEmpty()) {
            permissionGranted();
        } else {
            showPermissionDenyDialog(deniedPermissions);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_CODE_REQUEST_SETTING:
                checkPermissions(true);
                break;
            case REQ_CODE_SYSTEM_ALERT_WINDOW_PERMISSION_REQUEST:
                if (!hasWindowPermission() && !TextUtils.isEmpty(mDenyMessage)) {
                    showWindowPermissionDenyDialog();
                } else {
                    checkPermissions(false);
                }
                break;
            case REQ_CODE_SYSTEM_ALERT_WINDOW_PERMISSION_REQUEST_SETTING:
                checkPermissions(false);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }

    }

}
