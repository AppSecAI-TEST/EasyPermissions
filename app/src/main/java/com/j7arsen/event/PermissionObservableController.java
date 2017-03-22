package com.j7arsen.event;

import java.util.ArrayList;

/**
 * Created by arsen on 21.03.17.
 */

public class PermissionObservableController implements IPermissionSubject {

    private static PermissionObservableController mInstance;

    private ArrayList<IPermissionObserver> mObservers;

    private PermissionObservableController() {
        mObservers = new ArrayList<>();
    }

    public static PermissionObservableController getInstance(){
        if(mInstance == null){
            mInstance = new PermissionObservableController();
        }
        return mInstance;
    }

    @Override
    public void addObserver(IPermissionObserver iObserver) {
        if (!mObservers.contains(iObserver)) {
            mObservers.add(iObserver);
        }
    }

    @Override
    public void removeObserver(IPermissionObserver iObserver) {
        if (iObserver != null) {
            final int i = mObservers.indexOf(iObserver);
            if (i >= 0) {
                mObservers.remove(iObserver);
            }
        }
    }

    @Override
    public void removeAllObservers() {
        if (mObservers != null) {
            mObservers.clear();
        }
    }

    @Override
    public void notifyEvent(PermissionEvent permissionEvent) {
        for (int i = 0; i < mObservers.size(); i++) {
            mObservers.get(i).onEvent(permissionEvent);
        }
    }
}
