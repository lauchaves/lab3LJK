package cr.ac.itcr.currentlocation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Context;
import android.location.Location;

import android.view.View.OnClickListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/// Buscar best quality presicion in android
// github fmgarcia27 localizacion gps


public class MainActivity extends AppCompatActivity {


    private TextView editTextShowLocation;
    private TextView longitudtext;
    private TextView latitudtext;
    private Button buttonGetLocation;

    private LocationManager locManager;
    private LocationListener locListener;
    private Location mobileLocation;

    private double latitude2;
    private double longitude;

    private boolean gps_enabled = false;
    private boolean network_enabled = false;

    double LATITUDEEE;
    double LONGITUDEEE;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextShowLocation = (TextView) findViewById(R.id.editTextShowLocation);
        longitudtext = (TextView) findViewById(R.id.longitud);
        latitudtext = (TextView) findViewById(R.id.latitud);


        buttonGetLocation = (Button) findViewById(R.id.button);
        buttonGetLocation.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                try {
                    gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                } catch (Exception ex) {
                }
                try {
                    network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                } catch (Exception ex) {
                }

                buttonGetLocationClick();
            }
        });





    }

    /** Gets the current location and update the mobileLocation variable*/
    private void getCurrentLocation() {
        locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locListener = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onLocationChanged(Location location) {
                // TODO Auto-generated method stub
                mobileLocation = location;
                latitude2 = location.getLatitude();
                longitude = location.getLongitude();
                LATITUDEEE = latitude2;
                LONGITUDEEE = longitude;


            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (gps_enabled) {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
        }
        if (network_enabled) {
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
        }
    }



    private void buttonGetLocationClick() {
        getCurrentLocation(); // gets the current location and update mobileLocation variable

        if (mobileLocation != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locManager.removeUpdates(locListener); // This needs to stop getting the location data and save the battery power.

            String londitude = "Londitude: " + longitude;
            String latitude = "Latitude: " + latitude2;
            //String altitiude = "Altitiude: " + mobileLocation.getAltitude();
            //String accuracy = "Accuracy: " + mobileLocation.getAccuracy();
            //String time = "Time: " + mobileLocation.getTime();


            longitudtext.setText("Longitud: "+longitude);
            latitudtext.setText("Latitud: "+latitude2);

            Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);

            try {
                List<Address> addresses = geocoder.getFromLocation(LATITUDEEE, LONGITUDEEE, 1);

                if(addresses != null) {
                    Address returnedAddress = addresses.get(0);
                    StringBuilder strReturnedAddress = new StringBuilder("Esta en:\n");
                    for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                    }
                    editTextShowLocation.setText(strReturnedAddress.toString());
                }
                else{
                    editTextShowLocation.setText("No hay nombre para su ubicación.");
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                editTextShowLocation.setText("");
            }

        } else {
            editTextShowLocation.setText("Ups! No se pudo determinar la ubicación. Intentlo de nuevo");
        }
    }

}