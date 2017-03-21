package com.j7arsen.event;

import java.util.ArrayList;

/**
 * Created by arsen on 21.03.17.
 */

public class ObservableController implements ISubject{

    private static ObservableController mInstance;

    private ArrayList<IObserver> mObservers;

    private ObservableController() {
        mObservers = new ArrayList<>();
    }

    public static ObservableController getInstance(){
        if(mInstance == null){
            mInstance = new ObservableController();
        }
        return mInstance;
    }

    @Override
    public void addObserver(IObserver iObserver) {
        if (!mObservers.contains(iObserver)) {
            mObservers.add(iObserver);
        }
    }

    @Override
    public void removeObserver(IObserver iObserver) {
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
