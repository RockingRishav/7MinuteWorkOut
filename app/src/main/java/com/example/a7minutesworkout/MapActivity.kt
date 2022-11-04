package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity() {
    lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it
            it.isMyLocationEnabled = true
            val IIITLKO = LatLng(26.801251683501096, 81.0256178264966)
            val gym1 = LatLng(26.7935576315459, 81.00512744938628)
            val gym2 = LatLng(26.80281109813198, 80.99873051636575)
            val gym3 = LatLng(26.801728131112668, 80.99339916219743)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(IIITLKO, 15f))


            googleMap.addMarker(MarkerOptions().position(gym1).title("Gym 1"))
            googleMap.addMarker(MarkerOptions().position(gym2).title("Gym 2"))
            googleMap.addMarker(MarkerOptions().position(gym3).title("Gym 3"))




        })
    }
}