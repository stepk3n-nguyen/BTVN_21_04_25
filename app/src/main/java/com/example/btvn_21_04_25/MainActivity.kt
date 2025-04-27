package com.example.btvn_21_04_25

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var etFrom : EditText
    private lateinit var etTo : EditText
    private lateinit var spinnerFrom : Spinner
    private lateinit var spinnerTo : Spinner

    private var isFromFocused = true

    private val currencyRates = mapOf(
        "United States - Dollar" to 1.0,
        "Vietnam - Dong" to 23185.0,
        "Europe - EUR" to 1.08,
        "Japan - JPY" to 151.5,
        "GBP" to 0.86,
        "Australia - AUD" to 1.53,
        "CAD" to 1.36,
        "CHF" to 0.91,
        "China - CNY" to 7.24,
        "Korea - KRW" to 1376.5
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etFrom = findViewById(R.id.etFrom)
        etTo = findViewById(R.id.etTo)
        spinnerFrom = findViewById(R.id.spinnerFrom)
        spinnerTo = findViewById(R.id.spinnerTo)

        val currencies = currencyRates.keys.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFrom.adapter = adapter
        spinnerTo.adapter = adapter

        etFrom.addTextChangedListener(textWatcher)
        etTo.addTextChangedListener(textWatcher)

        spinnerFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateConversion()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        spinnerTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateConversion()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        etFrom.setOnFocusChangeListener { _, hasFocus ->
            isFromFocused = hasFocus
            etTo.isFocusable = !hasFocus
            etTo.isFocusableInTouchMode = !hasFocus
            etFrom.isFocusable = hasFocus
            etFrom.isFocusableInTouchMode = hasFocus
        }

        etTo.setOnFocusChangeListener { _, hasFocus ->
            isFromFocused = !hasFocus
            etFrom.isFocusable = !hasFocus
            etFrom.isFocusableInTouchMode = !hasFocus
            etTo.isFocusable = hasFocus
            etTo.isFocusableInTouchMode = hasFocus
        }
        etFrom.requestFocus()
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            updateConversion()
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private fun updateConversion() {
        if (isFromFocused) {
            val amountText = etFrom.text.toString()
            //------bug treo app
//            if (amountText.isEmpty()) {
//                etTo.setText("")
//                return
//            }
            val amount = amountText.toDoubleOrNull() ?: return
            val fromRate = currencyRates[spinnerFrom.selectedItem.toString()] ?: 1.0
            val toRate = currencyRates[spinnerTo.selectedItem.toString()] ?: 1.0
            val result = amount * toRate / fromRate
            etTo.setText("%.2f".format(result))
        } else {
            val amountText = etTo.text.toString()
            //------bug treo app
//            if (amountText.isEmpty()) {
//                etFrom.setText("")
//                return
//            }
            val amount = amountText.toDoubleOrNull() ?: return
            val fromRate = currencyRates[spinnerTo.selectedItem.toString()] ?: 1.0
            val toRate = currencyRates[spinnerFrom.selectedItem.toString()] ?: 1.0
            val result = amount * toRate / fromRate
            etFrom.setText("%.2f".format(result))
        }
    }
}
