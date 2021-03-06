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

package sysnetlab.android.sdc.ui.fragments;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.ui.CreateExperimentActivity;
import sysnetlab.android.sdc.ui.adapters.OperationAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class ExperimentSetupFragment extends Fragment {

    private View mView;
    private OnFragmentClickListener mCallback;
    private ExperimentSensorListFragment mExperimentSensorListFragment;

    public interface OnFragmentClickListener {
        public void onTagsClicked_ExperimentSetupFragment();

        public void onNotesClicked_ExperimentSetupFragment();

        public void onSensorsClicked_ExperimentSetupFragment();

        public void onDataStoreClicked_ExperimentSetupFragment();

        public void onBtnRunClicked_ExperimentSetupFragment(View v);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        EditText etName = (EditText) mView.findViewById(R.id.et_experiment_setup_name);
        etName.setText(((CreateExperimentActivity) getActivity()).getExperiment().getName());
        etName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        CreateExperimentActivity a = (CreateExperimentActivity) getActivity();
        ListView operationMenu = (ListView) mView.findViewById(R.id.lv_operations);
        OperationAdapter operationAdapter = new OperationAdapter(a, a.getExperiment(),
                OperationAdapter.CREATE_EXPERIMENT);
        operationMenu.setAdapter(operationAdapter);
        operationMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                if (position == OperationAdapter.OP_TAGS) {
                    mCallback.onTagsClicked_ExperimentSetupFragment();

                }
                else if (position == OperationAdapter.OP_NOTES) {
                    mCallback.onNotesClicked_ExperimentSetupFragment();
                }
                else if (position == OperationAdapter.OP_SENSORS) {
                    mCallback.onSensorsClicked_ExperimentSetupFragment();
                }
            }

        });

        // use nested fragment
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (mExperimentSensorListFragment == null) {
            mExperimentSensorListFragment = new ExperimentSensorListFragment();
            transaction
                    .add(R.id.layout_experiment_setup_sensor_list, mExperimentSensorListFragment);
        }
        transaction.commit();

        ((Button) mView.findViewById(R.id.button_experiment_run))
                .setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCallback.onBtnRunClicked_ExperimentSetupFragment(mView);
                    }
                });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_experiment_setup, container, false);

        return mView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnFragmentClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ExperimentSetupFragment.OnFragmentClickListener");
        }
    }

    public boolean isFragmentUIActive() {
        return isAdded() && !isDetached() && !isRemoving();
    }

    public View getView() {
        return mView;
    }

    public ExperimentSensorListFragment getExperimentSensorListFragment() {
        return mExperimentSensorListFragment;
    }

    public boolean hasChanges() {
        String experimentName = ((EditText) mView.findViewById(R.id.et_experiment_setup_name))
                .getText().toString();
        if (!experimentName.trim().equals(
                ((CreateExperimentActivity) getActivity()).getExperiment().getDefaultName()))
            return true;
        return false;
    }
}
