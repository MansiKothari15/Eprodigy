package com.bacancy.eprodigy.permission;

import android.content.Context;

public class MyPermission extends MyPermissionBase {
  public static final String TAG=MyPermission.class.getSimpleName();

  public static Builder with(Context context) {
    return new Builder(context);
  }

    public static class Builder extends PermissionBuilder<Builder> {

    private Builder(Context context) {
      super(context);
    }

    public void check() {
      checkPermissions();
    }

  }
}
