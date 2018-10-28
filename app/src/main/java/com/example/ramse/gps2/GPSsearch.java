package com.example.ramse.gps2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GPSsearch extends AppCompatActivity
{
    private Button btnGPS;
    private TextView mTxtLocation;
    private boolean isRun = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpssearch);

        mTxtLocation = findViewById(R.id.txtLocation);
        btnGPS = findViewById(R.id.button2);

        btnGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //stop trip
                if (isRun) {
                    btnGPS.setText("Comenzar Recorrido");
                    isRun = false;
                    return;
                }

                // Set text for button if is running
                btnGPS.setText("Detener Recorrido");
                isRun = true;
                Toast.makeText(GPSsearch.this, "El recorrido ha comenzado", Toast.LENGTH_LONG);

                LocationManager locationManager = (LocationManager) GPSsearch.this.getSystemService(Context.LOCATION_SERVICE);

                LocationListener locationListener = new LocationListener() {
                    private Location mLastLocation;

                    public void onLocationChanged(Location location) {
                        if (isRun) {
                            //TODO: Send to web service
                            double speed = 0;
                            if (this.mLastLocation != null)
                                speed = Math.sqrt(
                                        Math.pow(location.getLongitude() - mLastLocation.getLongitude(), 2)
                                                + Math.pow(location.getLatitude() - mLastLocation.getLatitude(), 2)
                                ) / (location.getTime() - this.mLastLocation.getTime());
                            //if there is speed from location
                            if (location.hasSpeed())
                                //get location speed
                                speed = location.getSpeed();
                            this.mLastLocation = location;

                            mTxtLocation.setText(String.valueOf(speed));
                            //mTxtLocation.setText("" + location.getLatitude() + ", " + location.getLongitude());
                            return;
                        }

                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    public void onProviderEnabled(String provider) {
                    }

                    public void onProviderDisabled(String provider) {
                    }
                };


                int permissionCheck = ContextCompat.checkSelfPermission(GPSsearch.this, Manifest.permission.ACCESS_FINE_LOCATION);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            }
        });



        int permissionCheck = ContextCompat.checkSelfPermission(GPSsearch.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if(permissionCheck== PackageManager.PERMISSION_DENIED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }
}







