package com.idletimeout.andrewapp;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
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
		mCoordinates = (TextView) findViewById(R.id.coordinates);
		mAddress = (TextView) findViewById(R.id.address);
		mActivityIndicator = (ProgressBar) findViewById(R.id.location_progress);
		mConnectionState = (TextView) findViewById(R.id.text_connection_state);
		mConnectionStatus = (TextView) findViewById(R.id.text_connection_status);
		
		// create Location Object
		mLocationRequest  = LocationRequest.create();
		
		// set location update interval
		mLocationRequest.setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);
		
		// set accuracy
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		
		// set interval ceiling
		mLocationRequest.setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);
		
		// open shared pref
		mPrefs = getSharedPreferences(LocationUtils.SHARED_PREFERENCES, Context.MODE_PRIVATE);
		
		// get an editor
		mEditor = mPrefs.edit();
		
		// create location client
		mLocationClient = new LocationClient(this, this, this);
	}
	
	private boolean servicesConnected() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		
		if(ConnectionResult.SUCCESS == resultCode) {
			return true;
		} else {
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
			if(dialog != null) {
				ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
				dialogFragment.setDialog(dialog);
				dialogFragment.show(getSupportFragmentManager(), LocationUtils.APPTAG);
			}
			return false;
		}
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
		super.onStart();
		mLocationClient.connect();
	}



	@Override
	protected void onStop() {
		if(mLocationClient.isConnected()) {
			mLocationClient.removeLocationUpdates(this);
			mConnectionState.setText(R.string.label_unknown);
		}
		mLocationClient.disconnect();
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
		mConnectionStatus.setText(R.string.label_connected);
	}

	@Override
	public void onDisconnected() {
		mConnectionStatus.setText(R.string.label_disconnected);
		
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * get_location_button
	 * @param v
	 */
	public void getLocation(View v) {
		if (servicesConnected()) {
			Location currentLocation = mLocationClient.getLastLocation();
			mCoordinates
					.setText(LocationUtils.getLatLng(this, currentLocation));
		}
	}
	
	/**
	 * get_address_button
	 * @param v
	 */
	public void getAddress(View v) {
		if (servicesConnected()) {
			Location currentLocation = mLocationClient.getLastLocation();
			mActivityIndicator.setVisibility(View.VISIBLE);
			new GetAddressTask(this).execute(currentLocation);
		} 
	}
	
	/**
	 * Nested class to display error dialog
	 * @author abhi
	 *
	 */
	@SuppressLint("NewApi")
	protected static class ErrorDialogFragment extends DialogFragment {
		private Dialog mDialog;

		public ErrorDialogFragment() {
			super();
			this.mDialog = null;
		}

		public void setDialog(Dialog dialog) {
			this.mDialog = dialog;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	
	}
	
	protected class GetAddressTask extends AsyncTask<Location, Void, String> {
		
		Context context;
		
		public GetAddressTask(Context context) {
			super();
			this.context = context;
		}

		@Override
		protected String doInBackground(Location... params) {
			
			Geocoder geocoder = new Geocoder(context, Locale.getDefault());
			
			Location location = params[0];
			
			List<Address> addresses = null;
			
			try {
				addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
			} catch (Exception e) {
				return getString(R.string.label_unknown);
			}
			
			if(addresses != null && addresses.size() > 0) {
				Address address = addresses.get(0);
				String line1 = address.getMaxAddressLineIndex()>0 ? address.getAddressLine(0) : address.getThoroughfare();
				String line2 = address.getMaxAddressLineIndex()>1 ? address.getAddressLine(1) : address.getLocality();
				String line3 = address.getMaxAddressLineIndex()>2 ? address.getAddressLine(2) : address.getAdminArea();
				String addressString = getString(R.string.address_output_string,line1, line2, line3);
				return addressString;
			} else {
				return getString(R.string.label_unknown);
			}
		}

		@Override
		protected void onPostExecute(String result) {
			mActivityIndicator.setVisibility(View.GONE);
			mAddress.setText(result);
		}
		
		
		
	}

}
