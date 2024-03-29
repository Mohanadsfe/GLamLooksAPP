package com.example.glamlooksapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.glamlooksapp.R;
import com.example.glamlooksapp.utils.Customer;
import com.example.glamlooksapp.utils.Database;
import com.example.glamlooksapp.utils.Datetime;

import java.util.ArrayList;

public class managerAppointmentListAdapter extends RecyclerView.Adapter<managerAppointmentListAdapter.CustomerViewHolder> {
    private Context context;
    private ArrayList<Customer> customerList;
    Database database = new Database();

    public managerAppointmentListAdapter(Context context, ArrayList<Customer> customerList) {
        this.context = context;
        this.customerList = customerList;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.manager_appointment_item, parent, false);
        return new CustomerViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        String fullName = customer.getFirstname() + " " + customer.getLastname();
        holder.customerName.setText(fullName);
        holder.textViewPN.setText(String.valueOf(customer.getPhoneNumber()));
        Datetime datetime = customer.getDateTime();
        holder.textViewTime.setText(datetime.getFormattedTime());
        holder.textViewDate.setText(datetime.getFormattedDate());

        holder.callBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            String phoneNumber = holder.textViewPN.getText().toString();
            intent.setData(Uri.parse("tel:" + customer.getPhoneNumber()));
            context.startActivity(intent);
        });
        holder.removeButton.setOnClickListener(v -> {
            String datetimeUid = datetime.getUUid();
            database.deleteUserTime(datetimeUid);
            customerList.remove(position);
            notifyDataSetChanged();
        });
        Glide.with(context)
                .load(customer.getImageUrl())
                .placeholder(R.drawable.hair_logo)
                .into(holder.appointmentIcon);
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    static class CustomerViewHolder extends RecyclerView.ViewHolder {
        ImageView appointmentIcon;
        TextView customerName;
        TextView textViewPN, textViewTime,textViewDate;
        Button callBtn;
        ImageButton removeButton;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            appointmentIcon = itemView.findViewById(R.id.imageViewCustomer);
            customerName = itemView.findViewById(R.id.textViewCustomerName);
            textViewPN = itemView.findViewById(R.id.textViewPN);
            callBtn = itemView.findViewById(R.id.callButton);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}
