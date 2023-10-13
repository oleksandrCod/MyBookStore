package store.mybookstore.repository.impl;

import store.mybookstore.repository.SpecificationProvider;

public interface SpecificationProviderManager<T> {
    SpecificationProvider<T> getSpecificationProvider(String key);
}
