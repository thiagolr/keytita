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

package com.thiagorosa.keytita.manager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.thiagorosa.keytita.common.Logger;
import com.thiagorosa.keytita.model.Device;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothManager {

    private static final BluetoothManager INSTANCE = new BluetoothManager();

    // UUID
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static final int REQUEST_ENABLE_BLUETOOTH = 1;

    // DEVICE A
    private BluetoothDevice mDevice = null;
    private BluetoothSocket mDeviceSocket = null;
    private OutputStream mOutputStream = null;
    private InputStream mInputStream = null;
    private boolean isConnected = false;

    /*******************************************************************************************
     *******************************************************************************************/

    private BluetoothManager() {
    }

    public static BluetoothManager getInstance() {
        return INSTANCE;
    }

    /*******************************************************************************************
     *******************************************************************************************/

    public BluetoothAdapter getAdapter() {
        return BluetoothAdapter.getDefaultAdapter();
    }

    public boolean isSupported() {
        return getAdapter() != null;
    }

    public boolean isEnabled() {
        return getAdapter().isEnabled();
    }

    public void promptToEnable(Fragment fragment) {
        if (!isEnabled() && (fragment != null)) {
            fragment.startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BLUETOOTH);
        }
    }

    /*******************************************************************************************
     *******************************************************************************************/

    public boolean isDiscovering() {
        return getAdapter().isDiscovering();
    }

    public void startDiscovery() {
        if (isDiscovering()) {
            cancelDiscovery();
        }
        getAdapter().startDiscovery();
    }

    public void cancelDiscovery() {
        getAdapter().cancelDiscovery();
    }

    /*******************************************************************************************
     *******************************************************************************************/

    public String getName() {
        if (isConnected) {
            return mDevice.getName();
        }
        return "";
    }

    public String getMAC() {
        if (isConnected) {
            return mDevice.getAddress();
        }
        return "";
    }

    /*******************************************************************************************
     *******************************************************************************************/

    public boolean isConnected() {
        return isConnected;
    }

    public boolean connect(Device device) {
        cancelDiscovery();

        mDevice = getAdapter().getRemoteDevice(device.getMAC());

        try {
            mDeviceSocket = mDevice.createRfcommSocketToServiceRecord(SPP_UUID);
        } catch (IOException e) {
            Logger.BT("ERROR: socket create failed (" + e.getMessage() + ")");
            return false;
        }

        try {
            mDeviceSocket.connect();
        } catch (IOException e) {
            Logger.BT("ERROR: open socket failed (" + e.getMessage() + ")");
            try {
                if (mDeviceSocket != null) {
                    mDeviceSocket.close();
                    mDeviceSocket = null;
                }
            } catch (IOException ignored) {
                Logger.BT("ERROR: close socket failed (" + e.getMessage() + ")");
            }
            return false;
        }

        try {
            mOutputStream = mDeviceSocket.getOutputStream();
        } catch (IOException e) {
            Logger.BT("ERROR: get output stream failed (" + e.getMessage() + ")");
            return false;
        }

        try {
            mInputStream = mDeviceSocket.getInputStream();
        } catch (IOException e) {
            Logger.BT("ERROR: get input stream failed (" + e.getMessage() + ")");
            return false;
        }

        isConnected = true;

        return true;
    }

    public void disconnect() {
        if (isConnected) {
            if (mDeviceSocket != null) {
                try {
                    mDeviceSocket.close();
                    mDeviceSocket = null;
                } catch (IOException e) {
                    Logger.BT("ERROR: close socket failed (" + e.getMessage() + ")");
                }
            }

            if (mOutputStream != null) {
                try {
                    mOutputStream.close();
                    mOutputStream = null;
                } catch (IOException e) {
                    Logger.BT("ERROR: close output stream failed (" + e.getMessage() + ")");
                }
            }

            if (mInputStream != null) {
                try {
                    mInputStream.close();
                    mInputStream = null;
                } catch (IOException e) {
                    Logger.BT("ERROR: close input stream failed (" + e.getMessage() + ")");
                }
            }

            isConnected = false;
        }
    }

    /*******************************************************************************************
     *******************************************************************************************/

    public boolean write(int type, int param1, int param2, int param3, int param4, int param5) {
        try {
            Logger.BT("WRITE: " + type + "," + param1 + "," + param2 + "," + param3 + "," + param4 + "," + param5);

            if (isConnected) {
                mOutputStream.write(type);
                mOutputStream.write(param1);
                mOutputStream.write(param2);
                mOutputStream.write(param3);
                mOutputStream.write(param4);
                mOutputStream.write(param5);
            }
        } catch (IOException e) {
            Logger.BT("ERROR: failed to write (" + e.getMessage() + ")");
            return false;
        } catch (Exception e) {
            Logger.BT("ERROR: " + e.getMessage());
            return false;
        }
        return true;
    }

}
