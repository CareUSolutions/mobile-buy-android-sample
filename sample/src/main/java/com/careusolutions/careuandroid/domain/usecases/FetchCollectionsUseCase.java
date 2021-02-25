package com.careusolutions.careuandroid.domain.usecases;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.careusolutions.careuandroid.core.UseCase;
import com.careusolutions.careuandroid.domain.model.Collection;

import java.util.List;

public interface FetchCollectionsUseCase extends UseCase {

  Cancelable execute(@Nullable String cursor, int perPage, @NonNull Callback1<List<Collection>> callback);
}
