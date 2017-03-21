package com.j7arsen;

import java.util.ArrayList;

/**
 * Created by arsen on 21.03.17.
 */

public interface PermissionListener {

    void onPermissionAllow();

    void onPermissionDeny(ArrayList<String> permissionDeniedList);

}
