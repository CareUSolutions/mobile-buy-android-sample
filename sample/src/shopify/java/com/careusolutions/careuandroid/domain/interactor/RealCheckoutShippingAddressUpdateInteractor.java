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
import com.shopify.buy3.Storefront;
import com.careusolutions.careuandroid.SampleApplication;
import com.careusolutions.careuandroid.domain.model.Address;
import com.careusolutions.careuandroid.domain.model.Checkout;
import com.careusolutions.careuandroid.domain.model.UserMessageError;
import com.careusolutions.careuandroid.domain.repository.UserError;

import io.reactivex.Single;

import static com.careusolutions.careuandroid.util.Util.checkNotBlank;
import static com.careusolutions.careuandroid.util.Util.checkNotNull;

public final class RealCheckoutShippingAddressUpdateInteractor implements CheckoutShippingAddressUpdateInteractor {
  private final CheckoutRepository repository;

  public RealCheckoutShippingAddressUpdateInteractor() {
    repository = new CheckoutRepository(SampleApplication.graphClient());
  }

  @Override public Single<Checkout> execute(@NonNull final String checkoutId, @NonNull final Address address) {
    checkNotBlank(checkoutId, "checkoutId can't be empty");
    checkNotNull(address, "address == null");

    Storefront.MailingAddressInput input = new Storefront.MailingAddressInput()
      .setAddress1(address.address1)
      .setAddress2(address.address2)
      .setCity(address.city)
      .setCountry(address.country)
      .setFirstName(address.firstName)
      .setLastName(address.lastName)
      .setPhone(address.phone)
      .setProvince(address.province)
      .setZip(address.zip);

    return repository
      .updateShippingAddress(checkoutId, input, q -> q.checkout(new CheckoutFragment()))
      .map(Converters::convertToCheckout)
      .onErrorResumeNext(t -> Single.error((t instanceof UserError) ? new UserMessageError(t.getMessage()) : t));
  }
}
