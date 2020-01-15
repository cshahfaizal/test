package app.airtouchmedia.candidate.com.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.Bindable;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.google.gson.Gson;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import app.airtouchmedia.candidate.com.BR;
import app.airtouchmedia.candidate.com.adapters.ProductsAdapter;
import app.airtouchmedia.candidate.com.listeners.ResultListener;
import app.airtouchmedia.candidate.com.model.Rates;
import app.airtouchmedia.candidate.com.model.Transactions;
import app.airtouchmedia.candidate.com.repository.CommentsRepository;
import app.airtouchmedia.candidate.com.util.ViewModelCallback;


public class ProductsViewModel extends ViewModelCallback implements ResultListener {

    /**
     * TAG
     */
    private static final String TAG = "ProductsViewModel";

    /**
     * Post And Images Repository
     */
    private CommentsRepository postAndImagesRepository;

    /**
     * To handle multiple clicks
     */
    private long mLastClickTime;

    /**
     *  Mutable LiveData
     */
    private MutableLiveData<String> productsLivedata;

    /**
     * Post adapter
     */
    private ProductsAdapter productsAdapter;

    /**
     * Application
     */
    private Application application;

    /**
     * Progress bar visibility
     */
    private int progressBarVisible = View.VISIBLE;

    /**
     * UI state on error
     */
    private int onErrorUIVisibility = View.GONE;

    /**
     * Current rates
     */
    public HashMap<String, HashMap<String, String>> currencyRates;

    /**
     * Product Transactions
     */
    private HashMap<String, ArrayList<Transactions>> productTransactions;



    public ProductsViewModel(@NonNull Application application) {
        postAndImagesRepository = new CommentsRepository();
        productsLivedata = new MutableLiveData<String>();
        this.application = application;

    }

    public MutableLiveData<List<Rates>> getAllRates() {
        return postAndImagesRepository.getMutableLiveDataRates(this,application.getApplicationContext());
    }


    public MutableLiveData<List<Transactions>> getAllTransaction() {
        return postAndImagesRepository.getMutableLiveDataTransaction(this,application.getApplicationContext());
    }


    public void doOnListItemClick(String s) {

        Log.d("TAG", "doOnListItemClick: requestState clicked ");
        if (!handleMultipleClicks()) {
            productsLivedata.setValue(new Gson().toJson(productTransactions.get(s)));
        }
    }

    public void setProductsAdapter(ProductsAdapter productsAdapter) {
        this.productsAdapter = productsAdapter;
    }

    public ProductsAdapter getProductsAdapter() {
        return productsAdapter;
    }

    /**
     * To get article url
     */
    public LiveData<String> getBlogArticleLink() {
        return productsLivedata;
    }

    @Bindable
    public int getProgressBarVisible() {
        return this.progressBarVisible;
    }

    public void setProgressBarVisible(int progressBarVisible) {
        this.progressBarVisible = progressBarVisible;
        notifyPropertyChanged(BR.progressBarVisible);
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
        setProgressBarVisible(View.GONE);
        setOnError(View.VISIBLE);
    }

    @Override
    public void onSuccess() {
        Log.d(TAG, "onSuccess: onSuccess ");
        setProgressBarVisible(View.GONE);
        setOnError(View.GONE);
    }


    public Boolean handleMultipleClicks() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return true;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        return false;
    }


   public void valueOfTransactions(List<Transactions> value){

        ArrayList<String> products = new ArrayList<>();
        HashMap<String, ArrayList<Transactions>> productTransactions = new HashMap<>();
        for (int i = 0; i < value.size(); i++) {

             String productName = value.get(i).sku;
            ArrayList<Transactions> transactionsForProduct = productTransactions.get(productName);
            if (transactionsForProduct == null) {
                products.add( value.get(i).sku);
                transactionsForProduct = new ArrayList<>();
                transactionsForProduct.add(value.get(i));
                productTransactions.put(productName, transactionsForProduct);
            } else {
                transactionsForProduct.add(value.get(i));
            }


        }
        setTransactionsData(products, productTransactions);

    }

    public void setTransactionsData(final ArrayList<String> products,
                                           final HashMap<String, ArrayList<Transactions>> productTransactionsa) {
        //Handler used to set data and objects on the main thread
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                productsAdapter.setProductsList(products);
               // mAdapter.notifyDataSetChanged();
                productTransactions = productTransactionsa;
            }
        });
    }

    public void valueofRates(List<Rates> value){

        HashMap<String, HashMap<String, String>> rates = new HashMap<>();
        for (int i = 0; i < value.size(); i++) {

            HashMap<String, String> firstCurrencyRates = rates.get(value.get(i).from);

            if (firstCurrencyRates == null) {
                firstCurrencyRates = new HashMap<>();
            }
            firstCurrencyRates.put(value.get(i).to, value.get(i).rate);
            rates.put(value.get(i).from, firstCurrencyRates);
        }
        setRatesData(rates);
    }


    //To determine conversion rates between all currencies using a breadth search algorithm
    //For every currency finds new "links" to other currencies and stores the computed rate
    public void setRatesData(HashMap<String, HashMap<String, String>> rates) {
        Set<String> keys = rates.keySet();
        for(String key: keys){
            if (!rates.get(key).containsKey("EUR")) {
                Stack<Pair<String, String>> stack = new Stack<>();
                Set<String> newKeys = rates.get(key).keySet();
                for(String newKey: newKeys){
                    stack.push(new Pair(newKey, rates.get(key).get(newKey)));
                }

                while (!stack.empty()) {
                    Pair<String, String> popedPair = stack.pop();

                    newKeys = rates.get(popedPair.first).keySet();
                    for(String newKey: newKeys){
                        if (!rates.get(key).containsKey(newKey) && !newKey.equals(key)) {
                            BigDecimal firstValue = new BigDecimal(popedPair.second);
                            BigDecimal secondValue = new BigDecimal(rates.get(popedPair.first).get(newKey));
                            BigDecimal newValue = firstValue.multiply(secondValue).setScale(2, RoundingMode.HALF_EVEN);

                            stack.push(new Pair(newKey, newValue.toString()));
                            rates.get(key).put(newKey, newValue.toString());
                        }
                    }
                }
            }
        }

        final HashMap<String, HashMap<String, String>> finalRates = rates;

        //Handler used to pass data from background thread to main thread
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(() -> currencyRates = finalRates);
    }


}
