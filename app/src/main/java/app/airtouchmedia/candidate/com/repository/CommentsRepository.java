package app.airtouchmedia.candidate.com.repository;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;
import java.util.List;
import app.airtouchmedia.candidate.com.listeners.ResultListener;
import app.airtouchmedia.candidate.com.model.Rates;
import app.airtouchmedia.candidate.com.model.Transactions;
import app.airtouchmedia.candidate.com.network.GetDataService;
import app.airtouchmedia.candidate.com.network.RetrofitClientInstance;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CommentsRepository {

    /**
     * TAG
     */
    private static final String TAG = "CommentsRepository";

    /**
     * funtion to provide list of comments
     */
    public MutableLiveData<List<Rates>> getMutableLiveDataRates(final ResultListener resultListener, Context context) {

        //Comments list
        MutableLiveData<List<Rates>> mutableLiveData = new MutableLiveData<>();

        //retrofit service
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);

        Observable<List<Rates>> responseObservable =  service.getAllRRates();
        responseObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Rates>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Rates> comments) {
                        Log.d(TAG, "initRecycler: initRequest onNext"+comments.size());
                        mutableLiveData.setValue(comments);
                        resultListener.onSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "initRecycler: initRequest onFailures "+e.getMessage());
                        resultListener.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "initRecycler: initRequest onComplete");
                    }
                });

        return  mutableLiveData;
    }



    /**
     * funtion to provide list of comments
     */
    public MutableLiveData<List<Transactions>> getMutableLiveDataTransaction(final ResultListener resultListener, Context context) {

        //Comments list
        MutableLiveData<List<Transactions>> mutableLiveData = new MutableLiveData<>();

        //retrofit service
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);

        Observable<List<Transactions>> responseObservable =  service.getAllTransactions();
        responseObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Transactions>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Transactions> comments) {
                        Log.d(TAG, "initRecycler: initRequest onNext"+comments.size());
                        mutableLiveData.setValue(comments);
                        resultListener.onSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "initRecycler: initRequest onFailured "+e.getMessage());
                        resultListener.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "initRecycler: initRequest onComplete");
                    }
                });

        return  mutableLiveData;
    }

}
