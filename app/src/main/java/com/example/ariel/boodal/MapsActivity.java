package com.example.ariel.boodal;

import android.app.Activity;
        import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.os.Build;
        import android.os.Bundle;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.ContextCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.text.Editable;
        import android.text.TextWatcher;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.AutoCompleteTextView;
        import android.widget.ImageView;
        import android.widget.Toast;
import com.example.ariel.boodal.helper.AppController;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.android.volley.AuthFailureError;
import com.directions.route.AbstractRouting;
        import com.directions.route.Route;
        import com.directions.route.RouteException;
        import com.directions.route.Routing;
        import com.directions.route.RoutingListener;
import com.example.ariel.boodal.Object.UserObject;
import com.example.ariel.boodal.helper.GPSTracker;
        import com.example.ariel.boodal.helper.PlaceAutoCompleteAdapter;
import com.example.ariel.boodal.helper.SQLiteHandler;
import com.example.ariel.boodal.helper.SessionManager;
import com.example.ariel.boodal.helper.Util;
        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.common.api.PendingResult;
        import com.google.android.gms.common.api.ResultCallback;
        import com.google.android.gms.location.places.Place;
        import com.google.android.gms.location.places.PlaceBuffer;
        import com.google.android.gms.location.places.Places;
        import com.google.android.gms.maps.CameraUpdate;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.MapsInitializer;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.LatLngBounds;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.google.android.gms.maps.model.Polyline;
        import com.google.android.gms.maps.model.PolylineOptions;

        import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.UnsupportedEncodingException;


import butterknife.ButterKnife;
        import butterknife.InjectView;
        import butterknife.OnClick;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, RoutingListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    protected GoogleMap map;
    GPSTracker gps;
    LatLng sydney;
    protected LatLng start;
    protected LatLng end;
    String LOG_TAG = "MyActivity";
    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutoCompleteAdapter mAdapter;
    private ProgressDialog progressDialog;
    private List<Polyline> polylines;
    private ProgressDialog pDialog;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final int[] COLORS = new int[]{R.color.primary_dark, R.color.primary, R.color.primary_light, R.color.accent, R.color.primary_dark_material_light};


    private ArrayList<UserObject> allItems = new ArrayList<UserObject>();
    private String urlJsonObj = "http://mlogg.com/boodal/public/api/service/1/agent";
    private String jsonResponse;
    private SQLiteHandler db;
    private SessionManager session;
    private String api_token;


    private static final LatLngBounds BOUNDS_JAMAICA = new LatLngBounds(new LatLng(-57.965341647205726, 144.9987719580531),
            new LatLng(72.77492067739843, -9.998857788741589));

    /**
     * This activity loads a map and then displays the route and pushpins on it.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.inject(this);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();

        api_token= user.get("api_token");

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(MapsActivity.this,
                    User_Drawer.class);
            startActivity(intent);
            finish();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(MapsActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
    }


    @Override
    public void onRoutingFailure(RouteException e) {
        // The Routing request failed
        progressDialog.dismiss();
        if(e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {
        // The Routing Request starts
    }


    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex)
    {

    }

    @Override
    public void onRoutingCancelled() {
        Log.i(LOG_TAG, "Routing was cancelled.");
    }

    @Override
    public void onConnected(Bundle bundle) {

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 5000, 0,new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

                        map.moveCamera(center);
                        map.animateCamera(zoom);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude()));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
                map.moveCamera(center);
                map.animateCamera(zoom);
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
        Log.i(LOG_TAG, "Google Places API connected.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        gps = new GPSTracker(this);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                map.setMyLocationEnabled(true);
            }
        }else {
            buildGoogleApiClient();
            map.setMyLocationEnabled(true);
        }
        if(gps.canGetLocation()){
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            sydney = new LatLng(latitude, longitude);
            // \n is for new line
            //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            sydney = new LatLng(-7.865076, 111.469632);
            gps.showSettingsAlert();
        }

        ConnectivityManager connMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            AppController.getInstance().getRequestQueue().getCache().invalidate(urlJsonObj, true);
            makeJsonObjectRequest();
        } else {
            Cache cache = AppController.getInstance().getRequestQueue().getCache();
            Cache.Entry entry = cache.get(urlJsonObj);
            if (entry != null) {
                //Cache data available.
                try {
                    String data = new String(entry.data, "UTF-8");
                    Log.d("CACHE DATA", data);
                    JSONObject json = new JSONObject(data);
                    setData(json, true);
                    Toast.makeText(getApplicationContext(), "Offline Service.", Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                // Cache data not exist.
                makeJsonObjectRequest();
            }
        }

        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        sydney = new LatLng(-7.865076, 111.469632);
        map.addMarker(new MarkerOptions().position(sydney).title("You"));
        sydney = new LatLng(-6.865076, 111.469632);
        map.addMarker(new MarkerOptions().position(sydney).title("You"));
    }

    private void setData(JSONObject response, boolean isCache) {
        JSONArray setdata = new JSONArray();
        //Log.d(TAG, response.toString());
        try {
            String status = response.getString("status_code");
            if (status.equals("1")){
                setdata = response.getJSONArray("data");
                for (int i = 0; i < setdata.length(); i++) {
                    JSONObject data = setdata.getJSONObject(i);
                    LatLng latLng = new LatLng(data.getDouble("Lat"),data.getDouble("Lng"));

                    map.addMarker(new MarkerOptions().position(latLng).title("You"));
                }
            }
        } catch (JSONException e) {
            setData(response, true);
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),"Tidak Bisa Menyambung Ke Server",Toast.LENGTH_LONG).show();
        }
        if(!isCache){
            //Toast.makeText(getApplicationContext(), "Cache not available..Loading from service", Toast.LENGTH_SHORT).show();
            hidepDialog();
        }
    }

    private void makeJsonObjectRequest() {
        showpDialog();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,urlJsonObj,null, new Response.Listener<JSONObject>() {
            private JSONArray setdata = new JSONArray();
            @Override
            public void onResponse(JSONObject response) {
                setData(response,false);
                hidepDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d(TAG, "Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"Tidak Bisa Menyambung Ke Server",Toast.LENGTH_LONG).show();
                // hide the progress dialog
                hidepDialog();
            }
        })
        {
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String,String> params = new HashMap<String, String>();
            params.put("Content-Type","application/json");
            params.put("Accept","application/json");
            params.put("Authorization","Bearer "+api_token);
            return params;
        }
    };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        MapsInitializer.initialize(this);
        mGoogleApiClient.connect();
    }
}
