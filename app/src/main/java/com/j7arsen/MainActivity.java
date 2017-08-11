package com.j7arsen;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionAllow() {
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDeny(ArrayList<String> permissionDeniedList) {
                Toast.makeText(MainActivity.this, "Permission Denied\n" + permissionDeniedList.toString(), Toast.LENGTH_SHORT).show();
            }

        };
        Permission.newBuilder().setPermissionListener(permissionlistener).setUserMessage("Lalala").setDenyMessage("Denied").setIsPresentSettingButton(true).setSettingText("Sett").setCloseText("Closer").setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA).build().checkPermissions(this);
    }
}
