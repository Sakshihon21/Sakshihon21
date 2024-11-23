package com.first.bookingform

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase
        database = FirebaseDatabase.getInstance().getReference("Bookings")

        // Get references to UI elements
        val emailEditText = findViewById<EditText>(R.id.etEmail)
        val mobileEditText = findViewById<EditText>(R.id.etMobile)
        val fromDateEditText = findViewById<EditText>(R.id.etFromDate)
        val toDateEditText = findViewById<EditText>(R.id.etToDate)
        val adultsCheckBox = findViewById<CheckBox>(R.id.cbAdults)
        val childrenCheckBox = findViewById<CheckBox>(R.id.cbChildren)
        val otherDetailsEditText = findViewById<EditText>(R.id.etOtherDetails)
        val submitButton = findViewById<Button>(R.id.btnSubmit)

        // DatePicker logic for 'From Date' EditText
        fromDateEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->
                val formattedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                fromDateEditText.setText(formattedDate)
            }, year, month, day)

            datePicker.show()
        }

        // DatePicker logic for 'To Date' EditText
        toDateEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->
                val formattedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                toDateEditText.setText(formattedDate)
            }, year, month, day)

            datePicker.show()
        }

        // Submit button logic
        submitButton.setOnClickListener {
            // Get values from form fields
            val email = emailEditText.text.toString()
            val mobile = mobileEditText.text.toString()
            val fromDate = fromDateEditText.text.toString()
            val toDate = toDateEditText.text.toString()
            val adults = adultsCheckBox.isChecked
            val children = childrenCheckBox.isChecked
            val otherDetails = otherDetailsEditText.text.toString()

            // Validate form inputs
            if (email.isEmpty() || mobile.isEmpty() || fromDate.isEmpty() || toDate.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create a unique ID for each booking
            val bookingId = database.push().key ?: ""

            // Create a data object
            val booking = Booking(email, mobile, fromDate, toDate, adults, children, otherDetails)

            // Store booking data in Firebase
            database.child(bookingId).setValue(booking).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Booking submitted successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to submit booking", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

// Booking data class
data class Booking(
    val email: String,
    val mobile: String,
    val fromDate: String,
    val toDate: String,
    val adults: Boolean,
    val children: Boolean,
    val otherDetails: String
)
