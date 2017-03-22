package com.j7arsen.event;

/**
 * Created by arsen on 21.03.17.
 */

public interface IPermissionSubject {

    void addObserver(IPermissionObserver iObserver);

    void removeObserver(IPermissionObserver iObserver);

    void removeAllObservers();

    void notifyEvent(PermissionEvent permissionEvent);

}
