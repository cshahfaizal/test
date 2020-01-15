package app.airtouchmedia.candidate.com.factory;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import app.airtouchmedia.candidate.com.viewmodel.ProductsViewModel;

/**
 * Detailing viewModel factory class
 */
public class ProductsViewModelFactory implements ViewModelProvider.Factory {


    @NonNull
    private final Application mApplication;


    public ProductsViewModelFactory(@NonNull Application application) {
        mApplication= application;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new ProductsViewModel(mApplication);
    }
}