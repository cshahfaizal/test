package app.airtouchmedia.candidate.com.network;

import java.util.List;

import app.airtouchmedia.candidate.com.model.Rates;
import app.airtouchmedia.candidate.com.model.Transactions;
import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Class to Get data service
 */
public interface GetDataService {

    @GET("/rates.json")
    Observable<List<Rates>> getAllRRates();

    @GET("/transactions.json")
    Observable<List<Transactions>> getAllTransactions();

   
}