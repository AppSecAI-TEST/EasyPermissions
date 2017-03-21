package com.j7arsen.event;

import java.util.ArrayList;

/**
 * Created by arsen on 21.03.17.
 */

public class PermissionEvent {

    private boolean mHasPermission;
    private ArrayList<String> mDeniedPermissions;

    public PermissionEvent(boolean mHasPermission, ArrayList<String> mDeniedPermissions) {
        this.mHasPermission = mHasPermission;
        this.mDeniedPermissions = mDeniedPermissions;
    }

    public boolean isHasPermission() {
        return mHasPermission;
    }

    public ArrayList<String> getDeniedPermissions() {
        return mDeniedPermissions;
    }
}
