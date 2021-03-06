/*
 * Copyright (c) 2014, the SenSee authors.  Please see the AUTHORS file
 * for details. 
 * 
 * Licensed under the GNU Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *          http://www.gnu.org/copyleft/gpl.html
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package sysnetlab.android.sdc.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import sysnetlab.android.sdc.sensor.AbstractSensor;
import sysnetlab.android.sdc.ui.CreateExperimentActivity;
import sysnetlab.android.sdc.ui.ViewExperimentActivity;
import sysnetlab.android.sdc.ui.adapters.SensorListAdapter;
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

public class ExperimentViewSensorListFragment extends Fragment {
    private OnFragmentClickListener mCallback;
    private ListView mListView;

    public interface OnFragmentClickListener {
        public void onSensorClicked_ExperimentViewSensorListFragment(AbstractSensor sensor);

        public void onSensorClicked_ExperimentViewSensorListFragment(int sensorNo);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_experiment_sensor_list, container, false);

        ListView listView = (ListView) view.findViewById(R.id.listview_experiment_view_sensor_list);
        TextView textView = (TextView) view.findViewById(R.id.textview_experiment_view_no_sensors);
        Activity activity = getActivity();

        List<AbstractSensor> lstSensors = null;

        lstSensors = (ArrayList<AbstractSensor>) ExperimentManagerSingleton.getInstance()
                .getActiveExperiment()
                .getSensors();

        Log.d("SensorDataCollector",
                "ViewExperimentActivity::ExperimentViewSensorListFragment::onCreateView() sensors: "
                        + lstSensors.size());

        if (lstSensors.size() > 0) {
            textView.setVisibility(View.GONE);            
            SensorListAdapter sensorListAdaptor = new SensorListAdapter(activity, lstSensors,
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
                    Log.d("SensorDataCollector",
                            "onActivityCreated(): Sensor ListView clicked at postion " + position);
                    if (getActivity() instanceof ViewExperimentActivity) {
                        mCallback.onSensorClicked_ExperimentViewSensorListFragment(position);
                    } else if (getActivity() instanceof CreateExperimentActivity) {
                        mCallback
                                .onSensorClicked_ExperimentViewSensorListFragment((AbstractSensor) listView
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
                    + " must implement OnFragmentClickListener");
        }
    }

    public ListView getListView() {
        return mListView;
    }
}
