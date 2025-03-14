package com.example.rideshareapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var etDestination: EditText
    private lateinit var btnFindRide: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etDestination = findViewById(R.id.etDestination)
        btnFindRide = findViewById(R.id.btnFindRide)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        btnFindRide.setOnClickListener {
            val destination = etDestination.text.toString().trim()
            if (destination.isNotEmpty()) {
                Toast.makeText(this, "Finding ride to $destination...", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Enter a destination!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getCurrentLocation()
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.addMarker(MarkerOptions().position(currentLatLng).title("You are here"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            }
        }
    }
}
