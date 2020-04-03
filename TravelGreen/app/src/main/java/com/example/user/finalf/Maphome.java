package com.example.user.finalf;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Maphome extends AppCompatActivity {

    Button b;
    EditText f, t;
    String result,fro,tow;
    String city[]={"chennai","trichy","coimbatore","madurai","thoothukudi","semanthangal"};
    int value[]={70,12,13,19,26,20};




    double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maphome);

        b = (Button) findViewById(R.id.search);
        f = (EditText) findViewById(R.id.fe);
        t = (EditText) findViewById(R.id.te);


        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

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
        try{
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        latitude = location.getLatitude();
        longitude = location.getLongitude();


        String result = null;
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Internal problem",Toast.LENGTH_SHORT).show();
        }
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addressList = null;
            try {
                addressList = geocoder.getFromLocation(
                        latitude, longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)).append("\n");
                }
                //sb.append(address.getLocality()).append("\n");
                //sb.append(address.getPostalCode()).append("\n");
                sb.append(address.getCountryName());
                result = sb.toString();
                Toast.makeText(getApplicationContext(),"Your current adrress:"+result,Toast.LENGTH_LONG).show();
                f.setText("Yourlocation");
            }
        }catch (Exception e){e.printStackTrace();}

        t.requestFocus();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(f.getText().toString().equals("Yourlocation"))
                    {
                        fro="semanthangal";
                    String b = t.getText().toString().trim();
                    tow=t.getText().toString().trim().toLowerCase();

                    MapsActivity m = new MapsActivity();
                    Asyncmapact asy = new Asyncmapact();

                    LatLng d;

                        LatLng n = new LatLng(latitude,longitude);
                    d = m.getLatLng(b.toString().replaceAll("\\s+", ""));

                    asy.setD(d);
                    asy.setS(n);



                }else{
                        String b = t.getText().toString().trim();
                        tow=b.toLowerCase();
                        String a=f.getText().toString().trim();
                        fro=a.toLowerCase();


                        MapsActivity m = new MapsActivity();
                        Asyncmapact asy = new Asyncmapact();

                        LatLng d;

                        LatLng n =m.getLatLng(a.toString().replaceAll("\\s+", ""));
                        d = m.getLatLng(b.toString().replaceAll("\\s+", ""));

                        asy.setD(d);
                        asy.setS(n);





                    }
                    int ind1 = 0,ind2 = 0;
                    for(int i=0;i<city.length;i++)
                    {
                        if(fro.equals(city[i])){
                            ind1=i;
                        }
                        if(tow.equals(city[i])){
                            ind2=i;
                        }
                    }

                    String str;
                    if(value[ind1]<value[ind2]){
                        str="Low to High";
                    }
                    else
                    {
                        str="High to Low";
                    }
                    Intent i = new Intent(Maphome.this, Asyncmapact.class);
                    i.putExtra("str",str);
                    startActivity(i);

                }


                catch(Exception e){
                    Toast.makeText(getApplicationContext(),"Enter valid details",Toast.LENGTH_SHORT).show();
                }
            }

        });




    }



}