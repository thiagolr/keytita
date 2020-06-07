/*
  Copyright (c) 2020 Thiago Lopes Rosa

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

package com.thiagorosa.keytita;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.thiagorosa.keytita.common.CustomFragment;

public class FragmentMain extends CustomFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main, null);

        CardView mSelectDevice = (CardView) view.findViewById(R.id.select_device);
        mSelectDevice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FragmentDeviceList();
                FragmentTransaction transactionControl = getFragmentManager().beginTransaction();
                transactionControl.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right);
                transactionControl.replace(R.id.fragment, fragment, "fragment");
                transactionControl.addToBackStack(null);
                transactionControl.commitAllowingStateLoss();

                update(R.string.app_title, true);
            }
        });

        CardView mSequenceCreate = (CardView) view.findViewById(R.id.setup);
        mSequenceCreate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FragmentSequenceCreate();
                FragmentTransaction transactionControl = getFragmentManager().beginTransaction();
                transactionControl.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right);
                transactionControl.replace(R.id.fragment, fragment, "fragment");
                transactionControl.addToBackStack(null);
                transactionControl.commitAllowingStateLoss();
            }
        });

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.toolbar_icon);

        update(R.string.app_title, true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getActivity() != null) {
                getActivity().finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}