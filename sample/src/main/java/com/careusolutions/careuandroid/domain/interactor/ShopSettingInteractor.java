package com.careusolutions.careuandroid.domain.interactor;

import androidx.annotation.NonNull;

import com.careusolutions.careuandroid.domain.model.ShopSettings;

import io.reactivex.Single;

public interface ShopSettingInteractor {

  @NonNull Single<ShopSettings> execute();
}
