package app.airtouchmedia.candidate.com.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.Gson;

import app.airtouchmedia.candidate.com.R;
import app.airtouchmedia.candidate.com.databinding.FragmentProductsBinding;
import app.airtouchmedia.candidate.com.factory.ProductsViewModelFactory;
import app.airtouchmedia.candidate.com.util.Constants;
import app.airtouchmedia.candidate.com.util.NetworkChangeEventListener;
import app.airtouchmedia.candidate.com.viewmodel.ProductsViewModel;

public class ProductsFragment extends Fragment implements NetworkChangeEventListener.NetworkStateListener {

    /**
     * TAG
     */
    private static final String TAG = "ScrollingActivity";

    /**
     * Binding
     */
    FragmentProductsBinding binding;

    /**
     * Network change event listener
     */
    NetworkChangeEventListener networkChangeEventListener;

    /**
     * Fragment Navigation controller
     */
    NavController navController;

    ProductsViewModel areaViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = FragmentProductsBinding.inflate(inflater, container, false);
            areaViewModel = ViewModelProviders.of(this, new ProductsViewModelFactory(getActivity().getApplication()))
                    .get(ProductsViewModel.class);
            setBindingAttributes(areaViewModel);
        }
        return binding.getRoot();
    }

    private void setBindingAttributes(ProductsViewModel areaViewModel) {
        binding.setMainData(areaViewModel);
        binding.included.setMainData(areaViewModel);
        binding.included.setProductsFragment(this);
        networkChangeEventListener = new NetworkChangeEventListener();
        navController = Navigation.findNavController(this.getActivity(), R.id.my_nav_host_fragment);
        handleCallBacks();
    }

    /**
     * Handling call backs which involves activity
     */
    private void handleCallBacks() {
        binding.getMainData().getBlogArticleLink().observe(this, post -> {
            Log.d(TAG, "handleCallBacks: Launch transactionfragment");
            launchTransactionFragment(post);
        });
    }

    private void launchTransactionFragment(String post) {
        try {
            final Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_TRANSACTION_RESULT, post);
            bundle.putString(Constants.KEY_RATES_RESULT1, new Gson().toJson(areaViewModel.currencyRates));

            navController.navigate(R.id.action_ScrollingFragment_to_detailsFragment, bundle);
        }catch (Exception e){
            Log.d(TAG, "launchTransactionFragment: IllegalArgumentException"+ e);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        networkChangeEventListener.unRegisterNetWorkChangeBroadCast(this.getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        networkChangeEventListener.setNetworkStateListener(this);
        networkChangeEventListener.registerNetWorkChangeBroadCast(this.getContext());
    }

    @Override
    public void onStateReceived(boolean state) {
        if (!state) {
            Toast.makeText(this.getContext(), R.string.network_ErrorMsg, Toast.LENGTH_LONG).show();
        }
    }
}
