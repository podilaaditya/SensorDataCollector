package sysnetlab.android.sdc.services;

import java.util.ArrayList;
import java.util.Iterator;

import sysnetlab.android.sdc.datacollector.AndroidSensorEventListener;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datastore.AbstractStore.Channel;
import sysnetlab.android.sdc.sensor.AbstractSensor;
import sysnetlab.android.sdc.sensor.AndroidSensor;
import sysnetlab.android.sdc.sensor.SensorDiscoverer;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;

public class RunExperimentService extends IntentService{

	private SensorManager mSensorManager;
	
	public RunExperimentService() {
		super("runexperimentservice");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		Experiment experiment=intent.getParcelableExtra("experiment");
		
		experiment.getStore().addExperiment();

        Iterator<AbstractSensor> iter = SensorDiscoverer.discoverSensorList(this).iterator();
        ArrayList<AbstractSensor> lstSensors = new ArrayList<AbstractSensor>();

        int nChecked = 0;
        while (iter.hasNext()) {
            AndroidSensor sensor = (AndroidSensor) iter.next();
            if (sensor.isSelected()) {
                nChecked++;

                Channel channel = experiment.getStore().getChannel(sensor.getName());
                AndroidSensorEventListener listener =
                        new AndroidSensorEventListener(channel);
                sensor.setListener(listener);
                mSensorManager.registerListener(listener, (Sensor) sensor.getSensor(),
                        sensor.getSamplingInterval());

                lstSensors.add(sensor);
            }
        }

        experiment.setSensors(lstSensors);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Iterator<AbstractSensor> iter = SensorDiscoverer.discoverSensorList(this).iterator();
        int nChecked = 0;
        while (iter.hasNext()) {
            AndroidSensor sensor = (AndroidSensor) iter.next();
            if (sensor.isSelected()) {
                nChecked++;
                AndroidSensorEventListener listener = sensor.getListener();
                mSensorManager.unregisterListener(listener);
            }
        }
	}
}