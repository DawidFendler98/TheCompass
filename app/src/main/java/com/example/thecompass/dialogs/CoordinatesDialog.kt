package com.example.thecompass.dialogs


import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import com.example.thecompass.AddDialogListener
import com.example.thecompass.R
import com.example.thecompass.model.Coordinates


class CoordinatesDialog(context: Context, var addDialogListener: AddDialogListener): AppCompatDialog(context) {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: Bundle())
        setContentView(R.layout.destination_pick_alert_dialog)
        setCanceledOnTouchOutside(true)
        setCancelable(true)

        findViewById<Button>(R.id.btn_submit)?.setOnClickListener {

            val longitude = findViewById<EditText>(R.id.et_Longitude)?.text.toString()
            val latitude = findViewById<EditText>(R.id.et_Latitude)?.text.toString()

            //Checking if ETs are empty
            if(!checkEmptyEt(longitude,latitude)) return@setOnClickListener


            //Checking if ETs have numerical format
            if(!checkFormatET(longitude,latitude)) return@setOnClickListener


            //Check that the latitude is between -90 and 90 degrees and longitude is between -180 and 180 degrees
            if(!checkRange(longitude.toDouble(),latitude.toDouble())) return@setOnClickListener


            val coordinates = Coordinates(longitude.toDouble(), latitude.toDouble())
            addDialogListener.onAddButtonClicked(coordinates)

            dismiss()
        }
    }
    //Checking if ETs have numerical format Function
    private fun checkFormatET(longitude:String, latitude: String): Boolean{
        try {
            longitude.toDouble()
            latitude.toDouble()
        } catch (e: NumberFormatException) {
            Toast.makeText(context, "Please enter correct data format", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    //Checking if ETs are empty Function
    private fun checkEmptyEt(longitude:String, latitude: String): Boolean{
        if(longitude.isEmpty() || latitude.isEmpty()){
            Toast.makeText(context, "Please enter all the information", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    //Check that the latitude is between -90 and 90 degrees and longitude is between -180 and 180 degrees Function
    private fun checkRange(longitude: Double, latitude: Double): Boolean{
        if((longitude<-180 || longitude>180) || (latitude<-90 || latitude>90)){
            Toast.makeText(context, "Longitude must be in the range [-180,180] and latitude [-90.90]", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}

