package app.airtouchmedia.candidate.com.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

import app.airtouchmedia.candidate.com.R;
import app.airtouchmedia.candidate.com.databinding.FragmentTransactionBinding;
import app.airtouchmedia.candidate.com.factory.TransactionViewModelFactory;
import app.airtouchmedia.candidate.com.model.Transactions;
import app.airtouchmedia.candidate.com.util.Constants;
import app.airtouchmedia.candidate.com.util.NetworkChangeEventListener;
import app.airtouchmedia.candidate.com.viewmodel.TransactionViewModel;

public class TransactionFragment extends Fragment implements NetworkChangeEventListener.NetworkStateListener {


    /**
     * Binding
     */
    FragmentTransactionBinding binding;

    /**
     * Network change listener
     */
    NetworkChangeEventListener networkChangeEventListener;

    /**
     * Navigation controller
     */
    NavController navController;

    /**
     * Current Rates
     */
    ArrayList<Transactions> currencyRates;

    /**
     * Product transaction
     */
    HashMap<String, HashMap<String, String>> productTransactions;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            currencyRates = getCurrencyRates();
            productTransactions = getTransaction();
            binding = FragmentTransactionBinding.inflate(inflater, container, false);
            TransactionViewModel areaViewModel = ViewModelProviders.of(this,
                    new TransactionViewModelFactory(this.getActivity().getApplication(), currencyRates)).get(TransactionViewModel.class);
            setBindingAttributes(areaViewModel);
        }
        return binding.getRoot();
    }


    private void setBindingAttributes( TransactionViewModel areaViewModel) {
        binding.setMainData(areaViewModel);
        binding.included.setMainData(areaViewModel);
        binding.included.setTransactionFragment(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        networkChangeEventListener = new NetworkChangeEventListener();
        navController = Navigation.findNavController(this.getActivity(), R.id.my_nav_host_fragment);
        sumOfTransaction();
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

    private ArrayList<Transactions> getCurrencyRates() {
        String jsonValue = getArguments().getString(Constants.KEY_TRANSACTION_RESULT);
        Type listType = new TypeToken<ArrayList<Transactions>>(){}.getType();
        return new Gson().fromJson(jsonValue, listType);

    }

    private  HashMap<String, HashMap<String, String>> getTransaction() {
        String jsonValue2 = getArguments().getString(Constants.KEY_RATES_RESULT1);
        Type listType2 = new TypeToken<HashMap<String, HashMap<String, String>> >(){}.getType();
        return new Gson().fromJson(jsonValue2, listType2);
    }


    void sumOfTransaction(){
        Thread workingThread = new Thread(){
            public void run(){
                BigDecimal total = new BigDecimal(0);
                for (Transactions transaction: currencyRates) {
                    BigDecimal newSum;
                    if (transaction.currency.equals("EUR")) {
                        newSum = new BigDecimal(transaction.amount);
                    } else {
                        BigDecimal amount = new BigDecimal(transaction.amount);
                        BigDecimal rate = new BigDecimal(productTransactions.get(transaction.currency).get("EUR"));
                        newSum = amount.multiply(rate).setScale(2, RoundingMode.HALF_EVEN);
                    }
                    total = total.add(newSum).setScale(2, RoundingMode.HALF_EVEN);
                }
                final BigDecimal finalTotal = total;

                binding.included.transactionValue.setText(finalTotal.doubleValue() + " EUR");
            }
        };
        workingThread.start();
    }


}
