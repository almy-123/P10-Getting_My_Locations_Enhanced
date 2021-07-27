package sg.edu.rp.id19037610.p10_gettingmylocationsenhanced;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.Buffer;
import java.util.ArrayList;

public class ViewLocationsActivity extends AppCompatActivity {

    Button btnRefresh, btnFavs;
    TextView tvNum;
    ListView lvLocations;
    ArrayList<String> alLocations;
    ArrayList<String> alFavs;
    String folderLocation;
    ActionBar ab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_locations);

        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        btnRefresh = findViewById(R.id.btnRefresh);
        btnFavs = findViewById(R.id.btnFavs);
        tvNum = findViewById(R.id.tvNum);
        lvLocations = findViewById(R.id.lvLocations);
        alLocations = new ArrayList<String>();
        alFavs = new ArrayList<String>();
        folderLocation = getFilesDir().getAbsolutePath() + "/MyLocations";

        getLocations();

        lvLocations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(ViewLocationsActivity.this);
                builder.setMessage("Add this location in your favourite list?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int di) {
                        try {
                            File targetFile = new File(folderLocation, "favourites.txt");
                            FileWriter writer = new FileWriter(targetFile, true);
                            writer.write(alLocations.get(i) + "\n");
                            writer.flush();
                            writer.close();
                            Toast.makeText(ViewLocationsActivity.this, "Added to Favourites",
                                    Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ViewLocationsActivity.this, "Failed to add to Favourites",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("No", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alLocations.clear();
                getLocations();
            }
        });

        btnFavs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alFavs.clear();
                getFavs();
            }
        });
    }

    public void getFavs() {
        File targetFile = new File(folderLocation, "favourites.txt");

        if (targetFile.exists()) {
            try {
                FileReader reader = new FileReader(targetFile);
                BufferedReader br = new BufferedReader(reader);

                String line = br.readLine();
                while (line != null) {
                    alFavs.add(line);
                    line = br.readLine();
                }
                br.close();
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ArrayAdapter aa = new ArrayAdapter(ViewLocationsActivity.this,
                    android.R.layout.simple_list_item_1, alFavs);
            lvLocations.setAdapter(aa);

            tvNum.setText(String.valueOf(alFavs.size()));
        }
    }

    public void getLocations() {
        File targetFile = new File(folderLocation, "locations.txt");

        if (targetFile.exists()) {
            try {
                FileReader reader = new FileReader(targetFile);
                BufferedReader br = new BufferedReader(reader);

                String line = br.readLine();
                while (line != null) {
                    alLocations.add(line);
                    line = br.readLine();
                }
                br.close();
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ArrayAdapter aa = new ArrayAdapter(ViewLocationsActivity.this,
                    android.R.layout.simple_list_item_1, alLocations);
            lvLocations.setAdapter(aa);

            tvNum.setText(String.valueOf(alLocations.size()));
        }
    }
}