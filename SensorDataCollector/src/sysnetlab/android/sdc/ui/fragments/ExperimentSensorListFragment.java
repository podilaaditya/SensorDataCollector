
package sysnetlab.android.sdc.ui.fragments;

import java.util.ArrayList;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import sysnetlab.android.sdc.sensor.AbstractSensor;
import sysnetlab.android.sdc.sensor.SensorDiscoverer;
import sysnetlab.android.sdc.ui.CreateExperimentActivity;
import sysnetlab.android.sdc.ui.ViewExperimentActivity;
import sysnetlab.android.sdc.ui.adaptors.SensorListAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class ExperimentSensorListFragment extends Fragment {
    private OnFragmentClickListener mCallback;
    private ListView mListView;

    public interface OnFragmentClickListener {
        public void onSensorClicked_ExperimentSensorListFragment(AbstractSensor sensor);

        public void onSensorClicked_ExperimentSensorListFragment(int sensorNo);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_experiment_sensor_list, container, false);

        ListView listView = (ListView) view.findViewById(R.id.listview_experiment_view_sensor_list);
        TextView textView = (TextView) view.findViewById(R.id.textview_experiment_view_no_sensors);

        ArrayList<AbstractSensor> lstSensors = null;
        if (getActivity() instanceof ViewExperimentActivity) {
            lstSensors = (ArrayList<AbstractSensor>) ExperimentManagerSingleton.getInstance()
                    .getActiveExperiment()
                    .getSensors();
            Log.d("SensorDataCollector",
                    "ViewExperimentActivity::ExperimentSensorListFragment::onCreateView() sensors: "
                            + lstSensors.size());
        } else if (getActivity() instanceof CreateExperimentActivity) {
            lstSensors = new ArrayList<AbstractSensor>();
            if (!SensorDiscoverer.isInitialized())
                SensorDiscoverer.initialize(getActivity().getApplicationContext());
            for (AbstractSensor sensor : SensorDiscoverer.discoverSensorList()) {
                if (sensor.isSelected()) {
                    lstSensors.add(sensor);
                }
            }
            Log.d("SensorDataCollector",
                    "CreateExperimentActivity::ExperimentSensorListFragment::onCreateView() sensors: "
                            + lstSensors.size());
        }

        if (lstSensors == null || lstSensors.size() == 0) {
            Log.d("SensorDataCollector",
                    "CreateExperimentActivity::ExperimentSensorListFragment::onCreateView() nothing to show");
            textView.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
        } else {
            Log.d("SensorDataCollector",
                    "CreateExperimentActivity::ExperimentSensorListFragment::onCreateView() show sensors");
            textView.setVisibility(View.GONE);

            SensorListAdapter sensorListAdaptor = new SensorListAdapter(getActivity(), lstSensors,
                    View.GONE);
            listView.setAdapter(sensorListAdaptor);

            listView.setVisibility(View.VISIBLE);
        }
        mListView = listView;

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mListView != null) {
            mListView.setOnItemClickListener(new ListView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                    Log.d("SensorDataCollector", "onActivityCreated(): Sensor ListView clicked at postion " + position);
                    if (getActivity() instanceof ViewExperimentActivity) {
                        mCallback.onSensorClicked_ExperimentSensorListFragment(position);
                    } else if (getActivity() instanceof CreateExperimentActivity) {
                        mCallback
                                .onSensorClicked_ExperimentSensorListFragment((AbstractSensor) listView
                                        .getItemAtPosition(position));
                    }
                }
            });
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnFragmentClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSwipeListener");
        }
    }

    public ListView getListView() {
        return mListView;
    }
}
