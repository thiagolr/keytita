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

package com.thiagorosa.keytita.common;

import android.text.TextUtils;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.thiagorosa.keytita.ActivityMain;
import com.thiagorosa.keytita.R;
import com.thiagorosa.keytita.manager.BluetoothManager;

public abstract class CustomFragment extends Fragment {

    public void setScreenTitle(String title, String subtitle) {
        if (getActivity() != null) {
            ((ActivityMain) getActivity()).setScreenTitle(title, subtitle);
        }
    }

    public ActionBar getSupportActionBar() {
        if (getActivity() != null) {
            return ((ActivityMain) getActivity()).getSupportActionBar();
        }
        return null;
    }

    public Toolbar getToolbar() {
        if (getActivity() != null) {
            return ((ActivityMain) getActivity()).getToolbar();
        }
        return null;
    }

    public void update(int title, boolean show) {
        String connection = getText(R.string.device_not_connected).toString();

        String devices = "";
        if (BluetoothManager.getInstance().isConnected()) {
            devices = getText(R.string.device_connected).toString();
        }

        setScreenTitle(getText(title).toString(), show ? TextUtils.isEmpty(devices) ? connection : devices : "");

        if (getActivity() != null) {
            getActivity().invalidateOptionsMenu();
        }
    }

}
