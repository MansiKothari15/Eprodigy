package com.bacancy.eprodigy.permission;


import java.util.ArrayList;

public class MyPermissionResult {

  private boolean granted;
  private ArrayList<String> deniedPermissions;

  public MyPermissionResult(ArrayList<String> deniedPermissions) {
    this.granted = ObjectUtils.isEmpty(deniedPermissions);
    this.deniedPermissions = deniedPermissions;
  }

  public boolean isGranted() {
    return granted;
  }

  public ArrayList<String> getDeniedPermissions() {
    return deniedPermissions;
  }
}
