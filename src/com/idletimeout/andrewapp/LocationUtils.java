package com.idletimeout.andrewapp;

import android.content.Context;
import android.location.Location;

public class LocationUtils {
	
	// Debugging tag for the application
    public static final String APPTAG = "AndrewSample";
    
	// Location update interval 
	protected static final long UPDATE_INTERVAL_IN_MILLISECONDS = 5000L;
	
	// Fast ceiling in milliseconds
	protected static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = 60000L;
	
	// Name of shared preferences repository that stores persistent state
    public static final String SHARED_PREFERENCES =
            "com.idleTimeout.andrewapp.SHARED_PREFERENCES";
    
    // Create an empty string for initializing strings -- This is a good idea, saves some memory
    public static final String EMPTY_STRING = new String();
    
    /**
     * Get the latitude and longitude from the Location object returned by
     * Location Services.
     *
     * @param currentLocation A Location object containing the current location
     * @return The latitude and longitude of the current location, or null if no
     * location is available.
     */
    protected static String getLatLng(Context context, Location currentLocation) {
        // If the location is valid
        if (currentLocation != null) {
            // Return the latitude and longitude as strings
            return context.getString(
                    R.string.latitude_longitude,
                    currentLocation.getLatitude(),
                    currentLocation.getLongitude());
        } else {
            // Otherwise, return the empty string
            return EMPTY_STRING;
        }
    }
}
