package sg.edu.rp.id19037610.p10_gettingmylocationsenhanced;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class MyService extends Service {

    // For location
    LocationGetter locationGetter = new LocationGetter();
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    String msg = "";
    String folderLocation;
    File targetFile;

    class LocationGetter extends Binder {
        public void getLocation() {
            Log.d("MyService", "getLocation executed");

            // create folder
            folderLocation = getFilesDir().getAbsolutePath() + "/MyLocations";
            File folder = new File(folderLocation);
            if (!folder.exists()) {
                folder.mkdir();
            }

            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30000);
            locationRequest.setFastestInterval(5000);
            locationRequest.setSmallestDisplacement(500);

            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult != null) {
                        Location data = locationResult.getLastLocation();
                        double lat = data.getLatitude();
                        double lng = data.getLongitude();

                        msg = lat + ", " + lng + "\n";

                        Log.d("incallback", msg);
                        // create file
                        try {
                            targetFile = new File(folderLocation, "locations.txt");
                            FileWriter writer = new FileWriter(targetFile, true);
                            writer.write(msg);
                            writer.flush();
                            writer.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MyService.this, "Failed to write",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            };

            FusedLocationProviderClient client = new FusedLocationProviderClient(MyService.this);
            if (ActivityCompat.checkSelfPermission(MyService.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                client.requestLocationUpdates(locationRequest, locationCallback, null);
            }

        }
    }

    public boolean checkFilePermission() {
        int checkP = ContextCompat.checkSelfPermission(MyService.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (checkP == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return locationGetter;
    }

    @Override
    public void onCreate() {
        Log.d("MyService", "Service created");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d("MyService", "Service exited");
        super.onDestroy();
    }
}