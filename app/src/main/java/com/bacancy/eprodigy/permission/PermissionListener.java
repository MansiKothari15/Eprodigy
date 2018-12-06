package com.bacancy.eprodigy.permission;

import java.util.ArrayList;

public interface PermissionListener {

  void onPermissionGranted();

  void onPermissionDenied(ArrayList<String> deniedPermissions);

}
