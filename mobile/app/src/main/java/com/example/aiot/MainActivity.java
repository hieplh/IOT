package com.example.aiot;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.aiot.model.Feed;
import com.example.aiot.service.AppDatabase;
import com.example.aiot.service.FeedService;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;
    private FeedService feedService;
    private ToggleButton btnOnOffLightScreen;
    private ToggleButton btnReadSensorData;
    private Thread threadSensor;
    private Runnable runnableSensor;
    private boolean isUpdateSensorUI;
    private TextView tvTemperature;
    private TextView tvHumidity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = Room.databaseBuilder(this, AppDatabase.class, "AIOT")
                .allowMainThreadQueries()
                .build();
        feedService = new FeedService(this, db);
        feedService.startFetchDataSensor();

        tvTemperature = findViewById(R.id.temperature_value);
        tvHumidity = findViewById(R.id.humidity_value);
        updateSensorUI();

        btnOnOffLightScreen = findViewById(R.id.btn_turn_on_light_screen);
        btnOnOffLightScreen.setOnCheckedChangeListener((compoundButton, b) -> feedService.create("screen-light-callback", b));

        btnReadSensorData = findViewById(R.id.btn_read_data_sensor);
        btnReadSensorData.setOnCheckedChangeListener((compoundButton, b) -> feedService.create("receive-sensor-data-callback", b));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void updateSensorUI() {
        if (threadSensor == null || threadSensor.isInterrupted()) {
            isUpdateSensorUI = true;
            runnableSensor = () -> {
                try {
                    while (isUpdateSensorUI) {
                        Feed feedTemperature = feedService.getLast("sensor.sensor-1");
                        Feed feedHumidity = feedService.getLast("sensor.sensor-2");
                        runOnUiThread(() -> {
                            if (feedTemperature != null) {
                                tvTemperature.setText(feedTemperature.getValue() + "°C");
                            }

                            if (feedHumidity != null) {
                                tvHumidity.setText(feedHumidity.getValue() + "%");
                            }
                        });

                        Thread.sleep(1000);
                    }
                } catch (Exception e) {}
            };
            threadSensor = new Thread(runnableSensor, "threadSensor");
            threadSensor.start();
        }
    }
}