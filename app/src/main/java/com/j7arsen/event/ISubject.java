package com.j7arsen.event;

/**
 * Created by arsen on 21.03.17.
 */

public interface ISubject {

    void addObserver(IObserver iObserver);

    void removeObserver(IObserver iObserver);

    void removeAllObservers();

    void notifyEvent(PermissionEvent permissionEvent);

}
