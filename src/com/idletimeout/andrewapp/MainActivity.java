package com.idletimeout.andrewapp;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class MainActivity extends FragmentActivity implements 
		LocationListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {
	
	//	UI widgets
	private TextView mCoordinates;
	private TextView mAddress;
	private ProgressBar mActivityIndicator;
	private TextView mConnectionState;
	private TextView mConnectionStatus;
	
	// Shared Preferences ???
	SharedPreferences mPrefs;
	
	// Shared Preferences handler
	SharedPreferences.Editor mEditor;
	
	// request to connect to location services
	private LocationRequest mLocationRequest;
	
	// location client object
	private LocationClient mLocationClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// UI view objects
		mCoordinates = (TextView) findViewById(R.id.label_coordinates);
		
		// create Location Object
		mLocationRequest  = LocationRequest.create();
		
		// set location update interval
		mLocationRequest.setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);
		
		// set accuracy
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		
		// set interval ceiling
		mLocationRequest.setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);
		
		// create location client
		mLocationClient = new LocationClient(this, this, this);
	}
	
	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}



	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}



	@Override
	protected void onStop() {
		if(mLocationClient.isConnected()) {
			mLocationClient.removeLocationUpdates(this);
			
		}
		super.onStop();
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

}
