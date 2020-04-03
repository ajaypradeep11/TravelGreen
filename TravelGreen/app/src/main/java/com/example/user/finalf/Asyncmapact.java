package com.example.user.finalf;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Asyncmapact extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button b;
    static String str;
    static LatLng s,d;

    public LatLng getD() {
        return d;
    }

    public void setD(LatLng d) {
        this.d = d;
    }

    public LatLng getS() {
        return s;
    }

    public void setS(LatLng s) {
        this.s = s;
    }
    public String newString=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asyncmapact);


         str = getIntent().getExtras().getString("str");



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        b=(Button)findViewById(R.id.divya);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                i.putExtra("str",str);
                startActivity(i);
            }
        });
        try {
            String url = getMapsApiDirectionsUrl(s, d);
            ReadTask downloadTask = new ReadTask();
            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
        }catch(Exception e){
            Toast.makeText(this, "Enter valid details", Toast.LENGTH_SHORT).show();
        }


    }
    private String  getMapsApiDirectionsUrl(LatLng origin,LatLng dest) {
        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&";
        //+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;


        return url;

    }
    private class ReadTask extends AsyncTask<String, Void , String> {

        @Override
        protected String doInBackground(String... url) {
            // TODO Auto-generated method stub
            String data = "";
            try {
                MapHttpConnection http = new MapHttpConnection();
                data = http.readUr(url[0]);


            } catch (Exception e) {
                // TODO: handle exception
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }

    }
    private class ParserTask extends AsyncTask<String,Integer, List<List<HashMap<String , String >>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {
            // TODO Auto-generated method stub
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser(getApplicationContext());
                routes = parser.parse(jObject);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;
            try {
                // traversing through routes
                for (int i = 0; i < routes.size(); i++) {
                    points = new ArrayList<LatLng>();
                    polyLineOptions = new PolylineOptions();
                    List<HashMap<String, String>> path = routes.get(i);

                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    polyLineOptions.addAll(points);
                    polyLineOptions.width(4);
                    polyLineOptions.color(Color.BLUE);
                }

                mMap.addPolyline(polyLineOptions);
                handleResult(points);
            }catch (Exception e)
            {
                MapsActivity.callfunc();
                Intent i=new Intent(getApplicationContext(),Maphome.class);
                startActivity(i);
            }

        }
    }
    public void handleResult(ArrayList<LatLng> directionPoints)
    {
        PolylineOptions rectLine = new PolylineOptions().width(3).color(Color.RED);
        for(int i = 0 ; i < directionPoints.size() ; i++)
        {
            rectLine.add(directionPoints.get(i));
        }

        //this polyline is stored so that it can be removed by calling drawnRoutePath.remove() if needed
        //drawnRoutePath = googleMap.addPolyline(rectLine);
        LatLngBounds.Builder builder;
        builder = new LatLngBounds.Builder();
        builder.include(s);
        builder.include(d);
        mMap.addMarker(new MarkerOptions().position(s));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(s));
        LatLng sydne = d;
        mMap.addMarker(new MarkerOptions().position(sydne));
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 200));

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
