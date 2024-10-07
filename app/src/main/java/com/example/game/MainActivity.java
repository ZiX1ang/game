package com.example.game;

import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements AMap.OnMapListener {
    private AMap aMap;
    private LocationManager locationManager;
    private Location location;
    private double energy = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化地图
        MapView mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        aMap.setOnMapListener(this);
        // 初始化定位
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        // 更新地图位置
        if (location != null) {
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16));
        }
    }
    @Override
    public void onMapClick(LatLng latLng) {
        // 在玩家位置周围10m随机生成能量球
        if (location != null) {
            double distance = distanceBetween(location.getLatitude(), location.getLongitude(), latLng.latitude, latLng.longitude);
            if (distance <= 10) {
                energy += 10; // 增加能量
                updateEnergyDisplay();
            }
        }
    }
    private double distanceBetween(double lat1, double lon1, double lat2, double lon2) {
        // 使用Haversine公式计算两点之间的距离
        final int R = 6371; // 地球半径（千米）
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // 返回千米
    }
    private void updateEnergyDisplay() {
        TextView energyText = findViewById(R.id.energyText);
        energyText.setText("Energy: " + energy);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (aMap != null) {
            aMap.onResume();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (aMap != null) {
            aMap.onPause();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (aMap != null) {
            aMap.onDestroy();
        }
    }
}