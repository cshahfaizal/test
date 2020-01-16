package app.airtouchmedia.candidate.com.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import app.airtouchmedia.candidate.com.R;
import app.airtouchmedia.candidate.com.databinding.TransactionItemBinding;
import app.airtouchmedia.candidate.com.model.Transactions;
import app.airtouchmedia.candidate.com.viewmodel.TransactionViewModel;


/**
 * Adapter to show the list of transactions
 */
public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    /**
     * TAG
     */
    private final String TAG = "TransactionAdapter";

    /**
     * Transaction view model
     */
    private TransactionViewModel transactionViewModel;

    /**
     * List of transactions
     */
    private ArrayList<Transactions> transactions;

    /**
     *
     * @param transactionViewModel
     */
    public TransactionAdapter(TransactionViewModel transactionViewModel) {
        this.transactionViewModel = transactionViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.transaction_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TransactionViewModel viewModel = transactionViewModel;
        viewHolder.binding.name.setText(transactions.get(i).amount);
        viewHolder.binding.currency.setText(transactions.get(i).currency);
        viewHolder.binding.setMainData(viewModel);
        viewHolder.binding.setSimpleListAdapter(this);
        viewHolder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return (transactions == null) ? 0 : transactions.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setTransactionList(ArrayList<Transactions> transactionsArrayList) {
        Log.d(TAG, "setTransactionList:");
        this.transactions = transactionsArrayList;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        //Transaction item binding
        TransactionItemBinding binding;

        ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}