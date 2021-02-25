package com.careusolutions.careuandroid.domain.usecases;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.careusolutions.careuandroid.core.UseCase;
import com.careusolutions.careuandroid.domain.model.Product;

import java.util.List;

public interface FetchProductsUseCase {

  UseCase.Cancelable execute(@NonNull String collectionId, @Nullable String cursor, int perPage, @NonNull UseCase.Callback1<List<Product>> callback);
}
