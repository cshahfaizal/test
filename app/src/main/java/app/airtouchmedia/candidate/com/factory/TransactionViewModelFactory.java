package app.airtouchmedia.candidate.com.factory;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import java.util.ArrayList;
import app.airtouchmedia.candidate.com.model.Transactions;
import app.airtouchmedia.candidate.com.viewmodel.TransactionViewModel;

/**
 * Scrolling viewModel factory class
 */
public class TransactionViewModelFactory implements ViewModelProvider.Factory {


    @NonNull
    private final Application mApplication;

    private ArrayList<Transactions> transactionsArrayList;

    public TransactionViewModelFactory(@NonNull Application application, ArrayList<Transactions> transactions) {
        mApplication= application;
        transactionsArrayList = transactions;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new TransactionViewModel(mApplication, transactionsArrayList);
    }
}