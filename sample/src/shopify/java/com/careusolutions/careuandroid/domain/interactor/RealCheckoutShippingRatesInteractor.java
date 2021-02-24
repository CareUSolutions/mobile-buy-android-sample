/*
 *   The MIT License (MIT)
 *
 *   Copyright (c) 2015 Shopify Inc.
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */

package com.careusolutions.careuandroid.domain.interactor;

import androidx.annotation.NonNull;

import com.careusolutions.careuandroid.domain.repository.CheckoutRepository;
import com.shopify.buy3.GraphError;
import com.careusolutions.careuandroid.SampleApplication;
import com.careusolutions.careuandroid.domain.model.Checkout;
import com.careusolutions.careuandroid.util.NotReadyException;
import com.careusolutions.careuandroid.util.RxRetryHandler;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;

import static com.careusolutions.careuandroid.util.Util.checkNotBlank;

public final class RealCheckoutShippingRatesInteractor implements CheckoutShippingRatesInteractor {
  private final CheckoutRepository repository;

  public RealCheckoutShippingRatesInteractor() {
    repository = new CheckoutRepository(SampleApplication.graphClient());
  }

  @Override public Single<Checkout.ShippingRates> execute(@NonNull final String checkoutId) {
    checkNotBlank(checkoutId, "checkoutId can't be empty");
    return repository
      .shippingRates(checkoutId, q -> q.availableShippingRates(new CheckoutShippingRatesFragment()))
      .map(Converters::convertToShippingRates)
      .flatMap(shippingRates ->
        shippingRates.ready ? Single.just(shippingRates) : Single.error(new NotReadyException("Shipping rates not available yet")))
      .retryWhen(RxRetryHandler
        .exponentialBackoff(500, TimeUnit.MILLISECONDS, 1.2f)
        .maxRetries(10)
        .when(t -> t instanceof NotReadyException || t instanceof GraphError.HttpError || t instanceof GraphError.NetworkError)
        .build());
  }
}