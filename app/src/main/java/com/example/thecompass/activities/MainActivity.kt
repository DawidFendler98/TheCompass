package com.example.thecompass.activities

import android.Manifest
import android.annotation.SuppressLint

import android.content.pm.PackageManager

import android.location.Location

import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.thecompass.AddDialogListener
import com.example.thecompass.R
import com.example.thecompass.dialogs.CoordinatesDialog
import com.example.thecompass.model.Coordinates
import com.example.thecompass.utils.Constants
import com.google.android.gms.location.*
import java.math.BigDecimal
import java.math.RoundingMode


class MainActivity : AppCompatActivity() {

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var mLatitude : Double = 0.0
    private var mLongitude : Double = 0.0
    private lateinit var tvDestination: TextView

    private var degree:Float  = 0f
    private lateinit var image: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val button =findViewById<Button>(R.id.button)
        image =findViewById(R.id.imageView)
        tvDestination = findViewById(R.id.tv_Destination)

        //Check Permission
        button.setOnClickListener {

            dialogCoordinates()

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {
                requestNewLocationData()
            } else {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    Constants.LOCATION_PERMISSION_CODE
                )
            }

        }}

    //Get User Location
    @SuppressLint("MissingPermission")
    private  fun requestNewLocationData(){
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 1000
        mLocationRequest.numUpdates = 1
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallBack, Looper.myLooper())
    }

    private val mLocationCallBack = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult?) {
            val mLastLocation: Location = locationResult!!.lastLocation
            mLatitude = mLastLocation.latitude
            Log.i("Current Latitude", "$mLatitude")
            mLongitude = mLastLocation.longitude
            Log.i("Current Longitude", "$mLongitude")



        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.LOCATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestNewLocationData()
            } else {
                Toast.makeText(
                    this,
                    "Oops, you just denied the permission for location. You can also allow it from settings.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun dialogCoordinates(){
        CoordinatesDialog(this, object: AddDialogListener{
            override fun onAddButtonClicked(coordinates: Coordinates) {
                mLongitude = coordinates.longitude
                mLatitude = coordinates.latitude
                calculateDistance()
            }
        }).show()

    }

    //Calculate distance between user location and location enter from user
    @SuppressLint("SetTextI18n")
    private fun calculateDistance(){
        val loc1 = Location("")
        loc1.latitude = mLatitude
        loc1.longitude = mLongitude

        val loc2 = Location("")
        loc2.latitude = 37.3738
        loc2.longitude = -122.1324
        degree = loc1.bearingTo(loc2)
        val distanceInMeters = loc1.distanceTo(loc2)
        Log.i("Distance in m", "$distanceInMeters")
        val decimal = BigDecimal(distanceInMeters.toDouble()).setScale(2, RoundingMode.HALF_EVEN)
        println(decimal)
        tvDestination.text= "Distance from the destination: $decimal m"
        tvDestination.visibility = TextView.VISIBLE
        rotateImage()

    }

    private fun rotateImage(){
        val cDegree = 0f
        val rotateAnimation = RotateAnimation(
                cDegree,
                (-degree),
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
        )
        rotateAnimation.duration = 210
        rotateAnimation.fillAfter = true
        image.startAnimation(rotateAnimation)
    }
}