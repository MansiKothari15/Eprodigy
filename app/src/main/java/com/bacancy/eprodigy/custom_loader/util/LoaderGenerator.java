package com.bacancy.eprodigy.custom_loader.util;


import com.bacancy.eprodigy.custom_loader.type.LoaderView;
import com.bacancy.eprodigy.custom_loader.type.Whirlpool;

public class LoaderGenerator {

  public static LoaderView generateLoaderView(int type) {
    switch (type) {

      case 0:
        return new Whirlpool();

      default:
        return new Whirlpool();
    }
  }

  public static LoaderView generateLoaderView(String type) {
    switch (type) {

      case "Whirlpool":
        return new Whirlpool();

      default:
        return new Whirlpool();
    }
  }
}
