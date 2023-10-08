package edu.du.tubridyporject2copykotlin

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.time.LocalDateTime

private lateinit var Image: ImageView
private lateinit var Title: TextView
private lateinit var DateandTime: TextView
private lateinit var Lat: TextView
private lateinit var Long: TextView
private lateinit var Save: Button
private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var dateTime = LocalDateTime.now().toString()

        Image = findViewById(R.id.imageView)
        Title = findViewById(R.id.titleView)
        DateandTime = findViewById(R.id.dateTimeView)
        Lat = findViewById(R.id.latView)
        Long = findViewById(R.id.longView)
        Save = findViewById(R.id.saveButton)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        Image.isEnabled = false
        Lat.isEnabled = false
        Long.isEnabled = false

        if((ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            && (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 111)
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), 111)
        }
        else {
            Image.isEnabled = true
            Image.setOnClickListener {
                var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(i, 101)
                getLocations()
                DateandTime.text = dateTime


            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun getLocations() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            if (it == null) {
                Toast.makeText(this, "Sorry Can't get Location", Toast.LENGTH_SHORT).show()
            } else {
                it.apply {
                    val latitude = it.latitude
                    val longitude = it.longitude
                    Lat.text = latitude.toString()
                    Long.text = longitude.toString()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Image.isEnabled = true
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                getLocations()
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            var pic: Bitmap? = data?.getParcelableExtra("data")
            Image.setImageBitmap(pic)
        }
    }
}