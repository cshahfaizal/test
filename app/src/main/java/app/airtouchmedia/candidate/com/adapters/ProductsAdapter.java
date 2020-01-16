package app.airtouchmedia.candidate.com.adapters;


import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import app.airtouchmedia.candidate.com.R;
import app.airtouchmedia.candidate.com.databinding.ProductItemBinding;
import app.airtouchmedia.candidate.com.listeners.ItemEventListener;
import app.airtouchmedia.candidate.com.viewmodel.ProductsViewModel;
import io.reactivex.annotations.NonNull;


/**
 * Adapter class for product recyclerview
 */
public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder>
        implements ItemEventListener {

    /**
     * TAG
     */
    private final String TAG = "ProductsAdapter";

    /**
     * Products activity view model
     */
    private ProductsViewModel viewModel;

    /**
     * Products
     */
    private List<String> productsList;

    public ProductsAdapter(ProductsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ProductsViewModel viewModel = this.viewModel;
        viewHolder.binding.title.setText(productsList.get(i));
        viewHolder.binding.setItemPosition(i);
        viewHolder.binding.setMainData(viewModel);
        viewHolder.binding.setItemClickListener(this);
        viewHolder.binding.setSimpleListAdapter(this);
        viewHolder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return (productsList == null) ? 0 : productsList.size();
    }

    /**
     * function to set products object
     *
     * @param productsList productsList object
     */
    public void setProductsList(List<String> productsList) {
        this.productsList = productsList;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onClickListItem(int position) {
        Log.d(TAG, "onClickListItem: viewModel position " + position);
        viewModel.doOnListItemClick(productsList.get(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        // Product item binding
        ProductItemBinding binding;

        ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}