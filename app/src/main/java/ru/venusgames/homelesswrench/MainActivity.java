package ru.venusgames.homelesswrench;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GoogleMapOptions options = new GoogleMapOptions().liteMode(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                                startActivity(intent);
                            }
                        }).show();
            }
        });

        final ListView listView = (ListView) findViewById(R.id.mapListView);

        final ArrayList<MyLocation> locations = new ArrayList<>();
        locations.add(new MyLocation("Home", new LatLng(54.693546, 63.854459)));
        locations.add(new MyLocation("Apartment", new LatLng(55.205374, 61.289694)));
        locations.add(new MyLocation("Moscow apartment", new LatLng(55.648848, 37.388092)));

        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return locations.size();
            }

            @Override
            public Object getItem(int position) {
                return locations.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = convertView;

                MapListViewHolder holder;
                if (view == null) {
                    LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                    view = layoutInflater.inflate(R.layout.map_list_item, parent, false);

                    holder = new MapListViewHolder();
                    holder.mapView = (MapView) view.findViewById(R.id.map_list_item_map);
                    holder.textView = (TextView) view.findViewById(R.id.map_list_item_name);

                    holder.mapView.setTag(getItem(position));

                    holder.init();

                    view.setTag(holder);

                } else {
                    holder = (MapListViewHolder) view.getTag();
                }

                holder.setLocation((MyLocation) getItem(position));
                holder.textView.setText(locations.get(position).getName());

                return view;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class MapListViewHolder implements OnMapReadyCallback {
        MapView mapView;
        TextView textView;
        GoogleMap googleMap;

        public void init() {
            if (mapView != null) {
                mapView.onCreate(null);
                mapView.getMapAsync(this);
            }
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsInitializer.initialize(getApplicationContext());
            this.googleMap = googleMap;
            setLocation((MyLocation) mapView.getTag());
        }

        public void setLocation(MyLocation location) {
            if (location == null || googleMap == null) return;
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location.getLatLng()));
            googleMap.addMarker(new MarkerOptions().position(location.getLatLng()).title(location.getName()));
        }
    }
}
