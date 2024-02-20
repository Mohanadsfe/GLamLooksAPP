package com.example.glamlooksapp.fragments.user;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.glamlooksapp.Adapter.CustomersAdapter;
import com.example.glamlooksapp.R;
import com.example.glamlooksapp.callback.CustomerCallBack;
import com.example.glamlooksapp.utils.Database;
import com.example.glamlooksapp.utils.Datetime;
import com.example.glamlooksapp.utils.User;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class TimesCusFragment extends Fragment {

    AppCompatActivity activity;

    private CustomersAdapter customerAdapter;
    private ArrayList<Datetime> customerDatesList;
    private RecyclerView recycleViewDates;

    Database database;

    public TimesCusFragment(AppCompatActivity appCompatActivity) {
        activity = appCompatActivity;
    }

    public TimesCusFragment() {
        // Required empty public constructor
    }

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
                        Toast.makeText(activity, "There are no new dates!", Toast.LENGTH_SHORT).show();
                    }
                    customerAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(activity, "Dates list is null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAddICustomerComplete(Task<Void> task) {

            }

            @Override
            public void onFetchCustomerComplete(ArrayList<User> customers) {

            }
        });
    }

    private void initRecyclerView() {
        customerAdapter = new CustomersAdapter(getContext(), customerDatesList);
        recycleViewDates.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleViewDates.setAdapter(customerAdapter);
        // Fetch All DateTimes for each Customer by uid.
        database.fetchUserDatesByKey(database.getCurrentUser().getUid());
    }
}

