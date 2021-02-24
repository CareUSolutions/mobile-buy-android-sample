package com.shopify.careuandroid.domain.interactor;

import com.shopify.careuandroid.SampleApplication;
import com.shopify.careuandroid.domain.ShopSettingsQuery;
import com.shopify.careuandroid.domain.model.ShopSettings;
import com.shopify.careuandroid.domain.repository.ShopRepository;

import io.reactivex.Single;

public class RealShopSettingInteractor implements ShopSettingInteractor {
  private final ShopRepository repository;

  public RealShopSettingInteractor() {
    repository = new ShopRepository(SampleApplication.apolloClient());
  }

  @Override public Single<ShopSettings> execute() {
    ShopSettingsQuery query = new ShopSettingsQuery();
    return repository.shopSettings(query).map(Converters::convertToShopSettings);
  }
}
