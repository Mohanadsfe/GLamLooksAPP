package com.example.glamlooksapp.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.glamlooksapp.utils.Database;
import com.example.glamlooksapp.R;
import com.example.glamlooksapp.callback.AuthCallBack;
import com.example.glamlooksapp.utils.Manager;
import com.example.glamlooksapp.utils.Service;
import com.example.glamlooksapp.utils.Customer;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.Task;


public class SignUpActivity extends AppCompatActivity {
    private static final int ACCOUNT_TYPE_CUSTOMER = 0;
    private static final int ACCOUNT_TYPE_MANAGER = 1;

    EditText firstname, lastname, signupEmail, signupPassword, phoneNumber;
    TextView txtV_button_back;
    EditText serviceName, CostService, duration;
    Button signupButton;
    ImageButton backButton;
    private ProgressBar signup_PB_loading;
    private CheckBox showAdditionalInfoCheckbox;

    private Database database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        findViews();
        initVars();
    }

    private void findViews() {
        firstname = findViewById(R.id.firstName);
        lastname = findViewById(R.id.lastName);
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        phoneNumber = findViewById(R.id.editTextPhoneNumber);
        signup_PB_loading = findViewById(R.id.signup_PB_loading);
        txtV_button_back = findViewById(R.id.loginRedirectText_ls);
        serviceName = findViewById(R.id.serviceName);
        CostService = findViewById(R.id.CostService);
        duration = findViewById(R.id.Duration);
        signupButton = findViewById(R.id.signupButton);
        backButton = findViewById(R.id.customBackButton);
        showAdditionalInfoCheckbox = findViewById(R.id.showAdditionalInfoCheckbox);
    }

    private void initVars() {
        database = new Database();
        database.setAuthCallBack(new AuthCallBack() {
            @Override
            public void onLoginComplete(Task<AuthResult> task) {
                // Handle login completion
            }

            @Override
            public void onCreateAccountComplete(boolean status, String err) {
                signup_PB_loading.setVisibility(View.INVISIBLE);
                if (status) {
                    Toast.makeText(SignUpActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUpActivity.this, err, Toast.LENGTH_SHORT).show();
                }
            }
        });

        showAdditionalInfoCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (showAdditionalInfoCheckbox.isChecked()) {
                    serviceName.setVisibility(View.VISIBLE);
                    CostService.setVisibility(View.VISIBLE);
                    duration.setVisibility(View.VISIBLE);
                } else {
                    serviceName.setVisibility(View.GONE);
                    CostService.setVisibility(View.GONE);
                    duration.setVisibility(View.GONE);
                }
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkInput()) {
                    Toast.makeText(SignUpActivity.this, "Error CheckInput", Toast.LENGTH_LONG).show();
                    return;
                }
                signup_PB_loading.setVisibility(View.VISIBLE);
                Customer customer = null;
                Manager manager = null;
                String password = "";
                if (showAdditionalInfoCheckbox.isChecked()) {
                    manager = new Manager();
                    manager.setEmail(signupEmail.getText().toString());
                    manager.setFirstname(firstname.getText().toString());
                    manager.setLastname(lastname.getText().toString());
                    manager.setPhoneNumber(phoneNumber.getText().toString());
                    password = signupPassword.getText().toString().trim();
                    manager.setAccount_type(ACCOUNT_TYPE_MANAGER);
                    Service service = new Service(serviceName.getText().toString(),
                            Double.valueOf(CostService.getText().toString()),
                            duration.getText().toString());

                    manager.setService(service);


                }else{
                     customer = new Customer();
                    customer.setEmail(signupEmail.getText().toString());
                    customer.setFirstname(firstname.getText().toString());
                    customer.setLastname(lastname.getText().toString());
                    customer.setPhoneNumber(phoneNumber.getText().toString());
                     password = signupPassword.getText().toString().trim();
                    customer.setAccount_type(ACCOUNT_TYPE_CUSTOMER);

                }

                if(customer !=null) {
                    database.createAccount(customer.getEmail(), password, customer);
                }
                if(manager!=null){
                    database.createAccount(manager.getEmail(), password, manager);
                }



            }
        });

        txtV_button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                database.logout();
                finish();
            }
        });
    }





    private boolean checkInput() {
        Customer customer = new Customer();
        customer.setEmail(signupEmail.getText().toString());
        customer.setFirstname(firstname.getText().toString());
        customer.setLastname(lastname.getText().toString());
        customer.setPhoneNumber(phoneNumber.getText().toString());
        customer.setPassword(signupPassword.getText().toString());
        String password = customer.getPassword();
        String confirmPassword = customer.getPassword();

        if (!customer.isValid()) {
            Toast.makeText(SignUpActivity.this, "Please fill all user info!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 8) {
            Toast.makeText(SignUpActivity.this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(SignUpActivity.this, "Mismatch between password and confirm password", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }
}
