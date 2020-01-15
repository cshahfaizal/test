package app.airtouchmedia.candidate.com.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import app.airtouchmedia.candidate.com.BR;
import app.airtouchmedia.candidate.com.listeners.ResultListener;
import app.airtouchmedia.candidate.com.model.Transactions;
import app.airtouchmedia.candidate.com.util.Constants;
import app.airtouchmedia.candidate.com.util.ViewModelCallback;


public class TransactionViewModel extends ViewModelCallback implements ResultListener {

    /**
     * TAG
     */
    private static final String TAG = "TransactionViewModel";

    /**
     * MutableLiveData
     */
    private MutableLiveData<ArrayList<Transactions>> productsLiveData;

    /**
     * Application
     */
    private Application application;

    /**
     * UI state on error
     */
    private int onErrorUIVisibility = View.GONE;


    public TransactionViewModel(Application mApplication, ArrayList<Transactions> mParam) {
        application = mApplication;
        productsLiveData = new MutableLiveData<>();
        setProductsLiveData(mParam);
    }

    public LiveData<ArrayList<Transactions>> getAllComments() {
        Log.d(TAG,"getAllComments: "+ getProductsLiveData().getValue());
        return getProductsLiveData();
    }

    private LiveData<ArrayList<Transactions>> getProductsLiveData() {
        return productsLiveData;
    }

    private void setProductsLiveData(ArrayList<Transactions> productsLiveData) {
        this.productsLiveData.setValue(productsLiveData); //the live data will help u push data
    }

    @Bindable
    public int getOnError() {
        return this.onErrorUIVisibility;
    }

    public void setOnError(int onErrror) {
        this.onErrorUIVisibility = onErrror;
        notifyPropertyChanged(BR.onError);
    }

    @Override
    public void onError(String err) {
        Log.d(TAG, "onError: onError ");
        setOnError(View.VISIBLE);
    }

    @Override
    public void onSuccess() {
        Log.d(TAG, "onSuccess: onSuccess ");
        setOnError(View.GONE);
    }





}
