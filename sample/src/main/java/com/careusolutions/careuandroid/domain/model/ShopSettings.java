package com.careusolutions.careuandroid.domain.model;

import androidx.annotation.NonNull;

import com.careusolutions.careuandroid.util.Util;

public final class ShopSettings {
  @NonNull
  public final String name;
  @NonNull
  public final String countryCode;

  public ShopSettings(@NonNull final String name, @NonNull final String countryCode) {
    this.name = Util.checkNotNull(name, "name can't be null");
    this.countryCode = Util.checkNotNull(countryCode, "countryCode can't be null");
  }

  @Override
  public String toString() {
    return "ShopSettings{" +
        "name='" + name + '\'' +
        ", countryCode='" + countryCode + '\'' +
        '}';
  }
}
