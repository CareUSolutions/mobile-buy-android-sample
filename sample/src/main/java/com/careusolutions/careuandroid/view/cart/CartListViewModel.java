package com.careusolutions.careuandroid.view.cart;

import androidx.annotation.NonNull;

import com.careusolutions.careuandroid.domain.interactor.CartAddItemInteractor;
import com.careusolutions.careuandroid.domain.interactor.CartFetchInteractor;
import com.careusolutions.careuandroid.domain.interactor.CartRemoveItemInteractor;
import com.careusolutions.careuandroid.domain.interactor.CartWatchInteractor;
import com.careusolutions.careuandroid.domain.interactor.RealCartAddItemInteractor;
import com.careusolutions.careuandroid.domain.interactor.RealCartFetchInteractor;
import com.careusolutions.careuandroid.domain.interactor.RealCartRemoveItemInteractor;
import com.careusolutions.careuandroid.domain.interactor.RealCartWatchInteractor;
import com.careusolutions.careuandroid.domain.model.Cart;
import com.careusolutions.careuandroid.domain.model.CartItem;
import com.careusolutions.careuandroid.view.BasePaginatedListViewModel;
import com.careusolutions.careuandroid.view.base.ListItemViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableTransformer;

@SuppressWarnings("WeakerAccess")
public class CartListViewModel extends BasePaginatedListViewModel<CartItem> implements
  CartListItemViewModel.OnChangeQuantityClickListener {
  private final CartWatchInteractor cartWatchInteractor = new RealCartWatchInteractor();
  private final CartAddItemInteractor cartAddItemInteractor = new RealCartAddItemInteractor();
  private final CartRemoveItemInteractor cartRemoveItemInteractor = new RealCartRemoveItemInteractor();
  private final CartFetchInteractor cartFetchInteractor = new RealCartFetchInteractor();

  @Override protected ObservableTransformer<String, List<CartItem>> nextPageRequestComposer() {
    return upstream -> upstream.flatMap(cursor -> cartWatchInteractor.execute().map(Cart::cartItems));
  }

  @NonNull @Override protected List<ListItemViewModel> convertAndMerge(@NonNull final List<CartItem> newItems,
    @NonNull final List<ListItemViewModel> existingItems) {
    List<ListItemViewModel> viewModels = new ArrayList<>();
    for (CartItem item : newItems) {
      viewModels.add(new CartListItemViewModel(item, this));
    }
    if (!newItems.isEmpty()) {
      viewModels.add(new CartSubtotalListItemViewModel(cartFetchInteractor.execute()));
    }
    return viewModels;
  }

  @Override public void onAddCartItemClick(final CartItem cartItem) {
    cartAddItemInteractor.execute(cartItem);
  }

  @Override public void onRemoveCartItemClick(final CartItem cartItem) {
    cartRemoveItemInteractor.execute(cartItem);
  }
}
