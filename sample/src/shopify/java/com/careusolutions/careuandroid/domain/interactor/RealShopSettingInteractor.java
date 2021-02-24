package com.careusolutions.careuandroid.domain.interactor;

import androidx.annotation.NonNull;

import com.careusolutions.careuandroid.domain.repository.ShopRepository;
import com.shopify.buy3.Storefront;
import com.careusolutions.careuandroid.SampleApplication;
import com.careusolutions.careuandroid.domain.model.ShopSettings;

import io.reactivex.Single;

public final class RealShopSettingInteractor implements ShopSettingInteractor {
  private final ShopRepository repository;

  public RealShopSettingInteractor() {
    repository = new ShopRepository(SampleApplication.graphClient());
  }

  @NonNull @Override public Single<ShopSettings> execute() {
    Storefront.ShopQueryDefinition query = q -> q
      .name()
      .paymentSettings(settings -> settings
        .countryCode()
        .acceptedCardBrands()
      );
    return repository
      .shopSettings(query)
      .map(Converters::convertToShopSettings);
  }
}
