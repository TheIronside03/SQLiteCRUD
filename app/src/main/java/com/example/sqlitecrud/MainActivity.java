package com.example.sqlitecrud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static final String DATABASE_NAME = "mydatabase";

    SQLiteDatabase mDatabase;

    TextView textViewViewEmployees;
    EditText editTextName, editTextSalary;
    Spinner spinnerDepartment;

    Button addEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creating a database
        mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

        textViewViewEmployees = (TextView) findViewById(R.id.textViewViewEmployees);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextSalary = (EditText) findViewById(R.id.editTextSalary);
        spinnerDepartment = (Spinner) findViewById(R.id.spinnerDepartment);



        addEmployee = findViewById(R.id.buttonAddEmployee);
        addEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEmployee();
            }
        });

        textViewViewEmployees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),EmployeeActivity.class));
                Toast.makeText(MainActivity.this, "clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        createTable();
    }

    private void createTable(){
        String sql =
                "CREATE TABLE IF NOT EXISTS employees (\n" +
                        "    id int NOT NULL CONSTRAINT employees_pk PRIMARY KEY,\n" +
                        "    name varchar(200) NOT NULL,\n" +
                        "    department varchar(200) NOT NULL,\n" +
                        "    joiningdate datetime NOT NULL,\n" +
                        "    salary double NOT NULL\n" +
                        ");"
                ;

        mDatabase.execSQL(sql);
    }

    private void addEmployee() {
        String name = editTextName.getText().toString().trim();
        String salary = editTextSalary.getText().toString().trim();
        String dept = spinnerDepartment.getSelectedItem().toString();

        if (name.isEmpty()) {
            editTextName.setError("name is required!");
            editTextName.requestFocus();
            return;
        }
        if (salary.isEmpty()) {
            editTextSalary.setError("salary is required!");
            editTextSalary.requestFocus();
            return;
        }

        //getting the current time for joining date
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String joiningDate = sdf.format(cal.getTime());

        String insertSQL = "INSERT INTO employees \n" +
                "(name, department, joiningdate, salary)\n" +
                "VALUES \n" +
                "(?, ?, ?, ?);";

        mDatabase.execSQL(insertSQL, new String[]{name, dept, joiningDate, salary});

        Toast.makeText(this, "employee added!", Toast.LENGTH_SHORT).show();

    }
}