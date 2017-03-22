package com.j7arsen;

import android.content.Context;
import android.os.Build;

import com.j7arsen.event.IPermissionObserver;
import com.j7arsen.event.PermissionObservableController;
import com.j7arsen.event.PermissionEvent;

/**
 * Created by arsen on 21.03.17.
 */

public class Permission implements IPermissionObserver {

    private PermissionListener mPermissionListener;
    private String[] mPermissions;
    private String mUserMessage;
    private String mDenyMessage;
    private boolean isPresentSettingButton;
    private String mSettingText;
    private String mCloseText;
    private String mUserMessageConfirmText;

    private Permission() {
    }

    public PermissionListener getPermissionListener() {
        return mPermissionListener;
    }

    public String[] getPermissions() {
        return mPermissions;
    }

    public String getUserMessage() {
        return mUserMessage;
    }

    public String getDenyMessage() {
        return mDenyMessage;
    }

    public boolean isPresentSettingButton() {
        return isPresentSettingButton;
    }

    public String getSettingText() {
        return mSettingText;
    }

    public String getCloseText() {
        return mCloseText;
    }

    public String getUserMessageText() {
        return mUserMessageConfirmText;
    }

    public void checkPermissions(Context context) {
        if (mPermissionListener == null) {
            throw new NullPointerException("You must setPermissionListener()");
        }
        if (isEmptyPermissionsList(mPermissions)) {
            throw new NullPointerException("You must setPermissions()");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            startCheckPermissionsActivity(context);
        }

    }

    private void startCheckPermissionsActivity(Context context) {
        TransparentPermissionActivity.startActivity(context, mPermissions, mUserMessage, mDenyMessage, isPresentSettingButton, mSettingText, mCloseText, mUserMessageConfirmText, context.getPackageName());
    }

    private boolean isEmptyPermissionsList(Object s) {
        if (s == null) {
            return true;
        }
        if ((s instanceof String) && (((String) s).trim().length() == 0)) {
            return true;
        }
        return false;
    }

    public static Builder newBuilder() {
        return new Permission().new Builder();
    }


    public class Builder {
        private Builder() {
            PermissionObservableController.getInstance().addObserver(Permission.this);
        }

        public Builder setPermissionListener(PermissionListener permissionListener) {
            Permission.this.mPermissionListener = permissionListener;
            return this;
        }

        public Builder setPermissions(String... permissions) {
            Permission.this.mPermissions = permissions;
            return this;
        }

        public Builder setUserMessage(String mUserMessage) {
            Permission.this.mUserMessage = mUserMessage;
            return this;
        }

        public Builder setDenyMessage(String mDenyMessage) {
            Permission.this.mDenyMessage = mDenyMessage;
            return this;
        }

        public Builder setIsPresentSettingButton(boolean presentSettingButton) {
            Permission.this.isPresentSettingButton = presentSettingButton;
            return this;
        }

        public Builder setSettingText(String mSettingText) {
            Permission.this.mSettingText = mSettingText;
            return this;
        }

        public Builder setCloseText(String mCloseText) {
            Permission.this.mCloseText = mCloseText;
            return this;
        }

        public Builder setUserMessageText(String mUserMessageText) {
            Permission.this.mUserMessageConfirmText = mUserMessageText;
            return this;
        }

        public Permission build() {
            return Permission.this;
        }

    }

    @Override
    public void onEvent(PermissionEvent event) {
        PermissionObservableController.getInstance().removeObserver(this);
        if (event.isHasPermission()) {
            mPermissionListener.onPermissionAllow();
        } else {
            mPermissionListener.onPermissionDeny(event.getDeniedPermissions());
        }
    }
}
