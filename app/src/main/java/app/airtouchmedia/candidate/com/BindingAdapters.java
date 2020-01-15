package app.airtouchmedia.candidate.com;

import android.databinding.BindingAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import app.airtouchmedia.candidate.com.adapters.TransactionAdapter;
import app.airtouchmedia.candidate.com.adapters.ProductsAdapter;
import app.airtouchmedia.candidate.com.view.TransactionFragment;
import app.airtouchmedia.candidate.com.view.ProductsFragment;
import app.airtouchmedia.candidate.com.viewmodel.ProductsViewModel;
import app.airtouchmedia.candidate.com.viewmodel.TransactionViewModel;

/**
 * Class to manage all binding adapters
 */
public class BindingAdapters {


    /**
     * To show the list of products in recyclerview
     * @param ratesViewmodel : View model of the home class
     * @param recyclerView       : jsonplaceholder Products recycler view
     * @param activity:          Scrolling activity
     */
    @BindingAdapter({"app:postListAdpater", "app:postListActivity"})
    public static void adapter(final RecyclerView recyclerView, final ProductsViewModel ratesViewmodel, final ProductsFragment activity) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        ProductsAdapter listAdapter = new ProductsAdapter(ratesViewmodel);
        recyclerView.setAdapter(listAdapter);
    ratesViewmodel.setProductsAdapter(listAdapter);

        //set products object values to  list
        ratesViewmodel.getAllRates().observe(activity, value -> ratesViewmodel.valueofRates(value));

        ratesViewmodel.getAllTransaction().observe(activity, value -> ratesViewmodel.valueOfTransactions(value));
    }


    /**
     * To show the list of comments in recyclerview
     * @param transactionViewModel : View model of comments lists
     * @param recyclerView       : jsonplaceholder comments recycler view
     * @param activity:          TransactionFragment
     */

    @BindingAdapter({"app:postInfoListAdpater", "app:postInfoListActivity"})
    public static void adapter(final RecyclerView recyclerView, final TransactionViewModel transactionViewModel, final TransactionFragment activity) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        final TransactionAdapter listAdapter = new TransactionAdapter(transactionViewModel);
        recyclerView.setAdapter(listAdapter);

        //set comments object values to  list
        transactionViewModel.getAllComments().observe(activity, listAdapter::setCommentsList);
    }

}
