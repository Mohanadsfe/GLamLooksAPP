package com.example.glamlooksapp.fragments.user;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.glamlooksapp.Adapter.customersAppointmentListAdapter;
import com.example.glamlooksapp.R;
import com.example.glamlooksapp.callback.CustomerCallBack;
import com.example.glamlooksapp.utils.Customer;
import com.example.glamlooksapp.utils.Database;
import com.example.glamlooksapp.utils.Datetime;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class AppointmentsListFragment extends Fragment {
    AppCompatActivity activity;
    private customersAppointmentListAdapter customerAdapter;
    private ArrayList<Datetime> customerDatesList;
    private RecyclerView recycleViewDates;
    Database database;

    public AppointmentsListFragment(AppCompatActivity appCompatActivity) {
        activity = appCompatActivity;
    }

    public AppointmentsListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_times_cus, container, false);
        recycleViewDates = view.findViewById(R.id.recycleViewDates);
        database = new Database();
        customerDatesList = new ArrayList<>();
        initVars();
        initRecyclerView();
        return view;
    }

    private void initVars() {
        database.setCustomerCallBack(new CustomerCallBack() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onCompleteFetchUserDates(ArrayList<Datetime> datetimes) {
                if (!datetimes.isEmpty()) {
                    customerDatesList.clear();
                    customerDatesList.addAll(datetimes);
                    if (customerDatesList.isEmpty()) {
                        Log.d("AppointmentsListFragment", "No new dates");
                        Toast.makeText(activity, "No new dates", Toast.LENGTH_SHORT).show();
                    }
                    customerAdapter.notifyDataSetChanged();
                } else {
                    Log.d("AppointmentsListFragment", "Dates list is null");
                    Toast.makeText(activity, "Dates list is null", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onAddICustomerComplete(Task<Void> task) {}
            @Override
            public void onFetchCustomerComplete(ArrayList<Customer> customers) {}
        });
    }

    private void initRecyclerView() {
        customerAdapter = new customersAppointmentListAdapter(getContext(), customerDatesList);
        recycleViewDates.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleViewDates.setAdapter(customerAdapter);
        database.fetchUserDatesByKey(database.getCurrentUser().getUid());
    }
}