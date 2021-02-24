package com.careusolutions.careuandroid.domain.interactor;

import androidx.annotation.NonNull;

import com.careusolutions.careuandroid.domain.model.Cart;

public interface CartFetchInteractor {
  @NonNull Cart execute();
}
