package com.shopify.careuandroid.data.apollo;

import com.shopify.careuandroid.domain.CollectionPageWithProductsQuery;
import com.shopify.careuandroid.domain.CollectionProductPageQuery;
import com.shopify.careuandroid.domain.model.Collection;
import com.shopify.careuandroid.domain.model.Product;
import com.shopify.careuandroid.util.Util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class Converter {

  public static List<Collection> convertCollections(final CollectionPageWithProductsQuery.CollectionConnection collectionConnection) {
    List<Collection> collections = new ArrayList<>();
    for (CollectionPageWithProductsQuery.Edge collectionEdge : collectionConnection.edges()) {
      final CollectionPageWithProductsQuery.Collection collection = collectionEdge.collection();
      collections.add(new Collection(
        collection.id(),
        collection.title(),
        collection.descriptionHtml(),
        collection.image().orNull() != null ? collection.image().get().src() : null,
        collectionEdge.cursor(),
        convertProducts(collection.productConnection())
      ));
    }
    return collections;
  }

  public static List<Product> convertProducts(final CollectionPageWithProductsQuery.ProductConnection productConnection) {
    List<Product> products = new ArrayList<>();
    for (CollectionPageWithProductsQuery.Edge1 productEdge : productConnection.edges()) {
      final CollectionPageWithProductsQuery.Product product = productEdge.product();
      products.add(new Product(
        product.id(),
        product.title(),
        Util.reduce(product.imageConnection().edges(), (acc, val) -> val.image().src(), null),
        Util.reduce(product.variantConnection().variantEdge(), (acc, val) -> val.variant().price(), BigDecimal.ZERO),
        productEdge.cursor()
      ));
    }
    return products;
  }

  public static List<Product> convertProducts(final CollectionProductPageQuery.ProductConnection productConnection) {
    List<Product> products = new ArrayList<>();
    for (CollectionProductPageQuery.ProductEdge productEdge : productConnection.productEdges()) {
      final CollectionProductPageQuery.Product product = productEdge.product();
      products.add(new Product(
        product.id(),
        product.title(),
        Util.reduce(product.imageConnection().imageEdges(), (acc, val) -> val.image().src(), null),
        Util.reduce(product.variantConnection().variantEdges(), (acc, val) -> val.variant().price(), BigDecimal.ZERO),
        productEdge.cursor()
      ));
    }
    return products;
  }

  private Converter() {
  }
}
