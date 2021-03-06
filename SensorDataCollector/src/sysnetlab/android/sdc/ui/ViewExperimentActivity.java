/*
 * Copyright (c) 2014, the SenSee authors.  Please see the AUTHORS file
 * for details. 
 * 
 * Licensed under the GNU Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 			http://www.gnu.org/copyleft/gpl.html
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package sysnetlab.android.sdc.ui;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import sysnetlab.android.sdc.sensor.AbstractSensor;
import sysnetlab.android.sdc.datacollector.DropboxHelper;
import sysnetlab.android.sdc.ui.fragments.ExperimentViewFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentViewSensorListFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentViewTagsFragment; //maybe class name is different
import sysnetlab.android.sdc.ui.fragments.ExperimentViewNotesFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentViewSensorDataFragment;
import sysnetlab.android.sdc.ui.fragments.FragmentUtils;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ViewExperimentActivity extends FragmentActivityBase implements
        ExperimentViewFragment.OnFragmentClickListener,
        ExperimentViewSensorListFragment.OnFragmentClickListener {

    private ExperimentViewFragment mExperimentViewFragment;
    private ExperimentViewTagsFragment mExperimentViewTagsFragment;
    private ExperimentViewNotesFragment mExperimentViewNotesFragment;
    private ExperimentViewSensorListFragment mExperimentViewSensorListFragment;
    private ExperimentViewSensorDataFragment mExperimentViewSensorDataFragment;

    private Experiment mExperiment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO handle configuration change
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        mLoadingTask = new TaskLoadingSpinner();
        mLoadingTask.execute();
    }

    @Override
    protected void loadTask() {
        mExperiment = ExperimentManagerSingleton.getInstance().getActiveExperiment();

        if (mExperiment == null) {
            Log.d("SensorDataColelctor",
                    "ViewExperimentActivity failed to get experiment from intent");
            Toast.makeText(this, "Failed to  load experiment.", Toast.LENGTH_LONG).show();
            finish();
        }

        Log.d("SensorDataCollector",
                "ViewExperimentActivity: experiment is " + mExperiment.toString());

        if (findViewById(R.id.fragment_container) != null) {
            mExperimentViewFragment = new ExperimentViewFragment();
            FragmentUtils.addFragment(this, mExperimentViewFragment);
        }

        Log.d("SensorDataCollector", "ViewExperimentActivity.loadTask() called.");
    }

    public void onResume() {
        super.onResume();

        // Complete the Dropbox Authorization
        DropboxHelper.getInstance().completeAuthentication();
    }

    @Override
    public void onBtnCloneClicked_ExperimentViewFragment() {
        Intent intent = new Intent(this, CreateExperimentActivity.class);
        intent.putExtra(SensorDataCollectorActivity.APP_OPERATION_KEY,
                SensorDataCollectorActivity.APP_OPERATION_CLONE_EXPERIMENT);
        startActivity(intent);
    }

    @Override
    public void onBtnDropboxClicked_ExperimentViewFragment() {
        DropboxHelper dbHelper = DropboxHelper.getInstance();
        if (!dbHelper.isLinked()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(R.string.text_dropbox_not_yet_linked_explanation);
            builder.setTitle(R.string.text_link_to_dropbox);
            builder.setPositiveButton(R.string.text_link_to_dropbox,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            DropboxHelper.getInstance().link();
                        }
                    });
            builder.setNegativeButton(R.string.text_do_not_link_to_dropbox,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else {
            dbHelper.writeAllFilesInDirToDropbox(mExperiment.getPath(), ViewExperimentActivity.this);
        }
    }

    @Override
    public void onTagsClicked_ExperimentViewFragment() {
        if (mExperimentViewTagsFragment == null) {
            mExperimentViewTagsFragment = new ExperimentViewTagsFragment();
        }
        FragmentUtils.switchFragment(this,
                mExperimentViewTagsFragment,
                "experimentviewmoretags",
                FragmentUtils.FRAGMENT_SWITCH_ADD_TO_BACKSTACK);
        changeActionBarTitle(R.string.text_viewing_tags, R.drawable.icon_tags_inverse);
    }

    @Override
    public void onNotesClicked_ExperimentViewFragment() {
        if (mExperimentViewNotesFragment == null) {
            mExperimentViewNotesFragment = new ExperimentViewNotesFragment();
        }
        FragmentUtils.switchFragment(this,
                mExperimentViewNotesFragment,
                "experimentviewmorenotes",
                FragmentUtils.FRAGMENT_SWITCH_ADD_TO_BACKSTACK);
        changeActionBarTitle(R.string.text_viewing_notes, R.drawable.icon_notes_inverse);
    }
    
    @Override
    public void onSensorsClicked_ExperimentViewFragment() {
        if (mExperimentViewSensorListFragment == null) {
            mExperimentViewSensorListFragment = new ExperimentViewSensorListFragment();
        }
        FragmentUtils.switchFragment(this,
                mExperimentViewSensorListFragment,
                "experimentviewsensorlist",
                FragmentUtils.FRAGMENT_SWITCH_ADD_TO_BACKSTACK);
        changeActionBarTitle(R.string.text_viewing_sensors, R.drawable.icon_sensors_inverse);
    }
    
    /*
    @Override
    public void onSensorsClicked_ExperimentViewFragment() {
        if (mExperimentViewSensorDataFragment == null) {
            mExperimentViewSensorDataFragment = new ExperimentViewSensorDataFragment();
        }
        FragmentUtils.switchFragment(this,
                mExperimentViewSensorDataFragment,
                "experimentviewsensordata",
                FragmentUtils.FRAGMENT_SWITCH_ADD_TO_BACKSTACK);
        changeActionBarTitle(R.string.text_viewing_sensors, R.drawable.icon_sensors_inverse);
    }
    */

    @Override
    public void onSensorClicked_ExperimentViewSensorListFragment(int sensorNo) {
        Log.d("SensorDataCollector",
                "ViewExperimentActivity::onSensorClicked_ExperimentSensorListFragment() called with sensorNo = "
                        + sensorNo);

        if (mExperimentViewSensorDataFragment == null) {
            mExperimentViewSensorDataFragment = new ExperimentViewSensorDataFragment();
        }

        mExperimentViewSensorDataFragment.setSensorNo(sensorNo);

        FragmentUtils.switchFragment(this,
                mExperimentViewSensorDataFragment,
                "experimentviewsensordata",
                FragmentUtils.FRAGMENT_SWITCH_ADD_TO_BACKSTACK);
    }

    @Override
    public void onSensorClicked_ExperimentViewSensorListFragment(AbstractSensor sensor) {
        Log.d("SensorDataCollector",
                "ViewExperimentActivity::onSensorClicked_ExperimentSensorListFragment() called");
        // do nothing
    }
    


    public Experiment getExperiment() {
        return mExperiment;
    }

    public ExperimentViewTagsFragment getExperimentViewTagsFragment() {
        return mExperimentViewTagsFragment;
    }

    public ExperimentViewFragment getExperimentViewFragment() {
        return mExperimentViewFragment;
    }

    public ExperimentViewNotesFragment getExperimentViewNotesFragment() {
        return mExperimentViewNotesFragment;
    }

    @Override
    public void onBackPressed() {
        if (!mExperimentViewFragment.isFragmentUIActive()) {
            changeActionBarTitle(R.string.text_viewing_experiment, R.drawable.ic_launcher);
        }
        super.onBackPressed();
    }

    public void changeActionBarTitle(int titleResId, int iconResId) {
        getSupportActionBar().setTitle(titleResId);
        getSupportActionBar().setIcon(iconResId);
    }
}
