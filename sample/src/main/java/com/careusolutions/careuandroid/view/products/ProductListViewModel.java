package com.careusolutions.careuandroid.view.products;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.annotation.NonNull;

import com.careusolutions.careuandroid.core.UseCase;
import com.careusolutions.careuandroid.domain.model.Product;
import com.careusolutions.careuandroid.util.Util;
import com.careusolutions.careuandroid.view.Constant;
import com.careusolutions.careuandroid.view.base.BasePaginatedListViewModel;
import com.careusolutions.careuandroid.view.base.ListItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductListViewModel extends BasePaginatedListViewModel<Product> {

  private final String collectionId;

  private final LiveData<List<ListItemViewModel>> items = Transformations
    .map(data(), products -> Util.reduce(products, (viewModels, product) -> {
      viewModels.add(new ProductListItemViewModel(product));
      return viewModels;
    }, new ArrayList<ListItemViewModel>()));

  public ProductListViewModel(String collectionId) {
    super();
    this.collectionId = collectionId;
  }

  public LiveData<List<ListItemViewModel>> items() {
    return items;
  }

  @Override
  protected UseCase.Cancelable onFetchData(@NonNull final List<Product> data) {
    String cursor = Util.reduce(data, (acc, val) -> val.cursor, null);
    return useCases()
      .fetchProducts()
      .execute(collectionId, cursor, Constant.PAGE_SIZE, this);
  }
}
