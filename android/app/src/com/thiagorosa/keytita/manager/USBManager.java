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

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.view.View;

import com.thiagorosa.keytita.ActivityMain;
import com.thiagorosa.keytita.common.Constants;
import com.thiagorosa.keytita.common.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;

public class USBManager {

    private static final USBManager INSTANCE = new USBManager();

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    private ActivityMain mActivity;
    private PendingIntent mPermissionIntent;

    private UsbManager mManager = null;
    private UsbDevice mDevice = null;
    private UsbInterface mInterface = null;
    private UsbEndpoint mEndpointIn = null;
    private UsbEndpoint mEndpointOut = null;
    private UsbDeviceConnection mConnection;

    private OutputStream mOutputStream = null;
    private InputStream mInputStream = null;
    private boolean isConnected = false;

    /*******************************************************************************************
     *******************************************************************************************/

    private USBManager() {
    }

    public static USBManager getInstance() {
        return INSTANCE;
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        Logger.USB("permission granted");
                        UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        if (device != null) {
                            connect();
                        }
                    } else {
                        Logger.USB("permission denied");
                    }
                }
            }
        }
    };

    private final BroadcastReceiver mUsbDeviceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                Logger.USB("device attached");

                mDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                connect();
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                Logger.USB("device detached");

                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (device != null && device.getProductId() == mDevice.getProductId()) {
                    disconnect();
                }
            }
        }
    };

    /*******************************************************************************************
     *******************************************************************************************/

    public void setup(ActivityMain activity) {
        if (!isConnected) {
            mActivity = activity;
            mManager = (UsbManager) mActivity.getSystemService(Context.USB_SERVICE);

            mPermissionIntent = PendingIntent.getBroadcast(mActivity, 0, new Intent(ACTION_USB_PERMISSION), 0);

            IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
            mActivity.registerReceiver(mUsbReceiver, filter);

            //context.registerReceiver(mUsbDeviceReceiver, new IntentFilter(UsbManager.ACTION_USB_DEVICE_ATTACHED));
            mActivity.registerReceiver(mUsbDeviceReceiver, new IntentFilter(UsbManager.ACTION_USB_DEVICE_DETACHED));

            connect();
        }
    }

    public void close() {
        disconnect();

        mActivity.unregisterReceiver(mUsbReceiver);
        mActivity.unregisterReceiver(mUsbDeviceReceiver);
        mActivity = null;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void connect() {
        Logger.USB("connecting");

        searchEndPoint();

        if (mInterface != null) {
            setupCommunication();
        } else {
            Log.d("thiago", "ERROR: interface not found");
        }
    }

    public void disconnect() {
        isConnected = false;
        mActivity.refreshConnection();

        if (mConnection != null) {
            if (mInterface != null) {
                mConnection.releaseInterface(mInterface);
                mInterface = null;
            }
            mConnection.close();
            mConnection = null;
        }

        mDevice = null;
        mInterface = null;
        mEndpointIn = null;
        mEndpointOut = null;
    }

    private void searchEndPoint() {
        Logger.USB("searching end points");

        mInterface = null;
        mEndpointOut = null;
        mEndpointIn = null;

        if (mDevice == null) {
            HashMap<String, UsbDevice> deviceList = mManager.getDeviceList();
            Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();

            while (deviceIterator.hasNext()) {
                UsbDevice device = deviceIterator.next();
                if (device.getVendorId() == Constants.USB_VENDOR_ID && device.getProductId() == Constants.USB_PRODUCT_ID) {
                    mDevice = device;
                }
            }
        }

        if (mDevice != null) {
            Logger.USB("device found");

            //String s = mDevice.toString() + "\n" + "DeviceID: " + mDevice.getDeviceId() + "\n" + "DeviceName: " + mDevice.getDeviceName() + "\n" + "DeviceClass: " + mDevice.getDeviceClass() + "\n" + "DeviceSubClass: " + mDevice.getDeviceSubclass() + "\n" + "VendorID: " + mDevice.getVendorId() + "\n" + "ProductID: " + mDevice.getProductId() + "\n" + "InterfaceCount: " + mDevice.getInterfaceCount();
            //Logger.USB(s);

            for (int i = 0; i < mDevice.getInterfaceCount(); i++) {
                UsbInterface usbif = mDevice.getInterface(i);
                UsbEndpoint tOut = null;
                UsbEndpoint tIn = null;

                int tEndpointCnt = usbif.getEndpointCount();
                if (tEndpointCnt >= 2) {
                    for (int j = 0; j < tEndpointCnt; j++) {
                        if (usbif.getEndpoint(j).getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                            if (usbif.getEndpoint(j).getDirection() == UsbConstants.USB_DIR_OUT) {
                                tOut = usbif.getEndpoint(j);
                            } else if (usbif.getEndpoint(j).getDirection() == UsbConstants.USB_DIR_IN) {
                                tIn = usbif.getEndpoint(j);
                            }
                        }
                    }

                    if (tOut != null && tIn != null && usbif.getInterfaceClass() == 0xA) {
                        mInterface = usbif;
                        mEndpointOut = tOut;
                        mEndpointIn = tIn;
                        break;
                    }
                }
            }

            if (mInterface == null) {
                Logger.USB("no suitable interface found");
            } else {
                //Logger.USB("interface: " + mInterface);
                //Logger.USB("endpointIn: " + mEndpointIn);
                //Logger.USB("endpointOut: " + mEndpointOut);
            }
        } else {
            Logger.USB("device not found");
        }
    }

    private void setupCommunication() {
        Logger.USB("configuring communication");

        final int RQSID_SET_LINE_CODING = 0x20;
        final int RQSID_SET_CONTROL_LINE_STATE = 0x22;

        boolean hasPermission = mManager.hasPermission(mDevice);
        Logger.USB("permission status: " + hasPermission);

        if (hasPermission) {
            mConnection = mManager.openDevice(mDevice);
            if (mConnection != null) {
                mConnection.claimInterface(mInterface, true);

                mConnection.controlTransfer(0x21, RQSID_SET_CONTROL_LINE_STATE, 0x1, 0, null, 0, 0);

                // baud rate = 9600, 8 data bit, 1 stop bit
                byte[] settings = new byte[]{(byte) 0x80, 0x25, 0x00, 0x00, 0x00, 0x00, 0x08};
                //byte[] settings = getLineEncoding(115200);
                int result = mConnection.controlTransfer(0x21, RQSID_SET_LINE_CODING, 0, 0, settings, settings.length, 0);
                Logger.USB("controlTransfer(RQSID_SET_LINE_CODING): " + result);

                isConnected = true;
                mActivity.refreshConnection();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (isConnected) {
                            int packetSize = mEndpointIn.getMaxPacketSize();
                            byte[] bytes = new byte[packetSize];

                            int r = mConnection.bulkTransfer(mEndpointIn, bytes, packetSize, 5);
                            if (r >= 0) {
                                byte[] truncated = new byte[r];
                                for (int b = 0; b < r; b++) {
                                    truncated[b] = bytes[b];
                                }
                                Logger.SERIAL(new String(truncated));
                            }
                        }
                    }
                }).start();
            }
        } else {
            Logger.USB("requesting permission");
            mManager.requestPermission(mDevice, mPermissionIntent);
        }
    }

    private byte[] getLineEncoding(int baudRate) {
        final byte[] lineEncodingRequest = {(byte) 0x80, 0x25, 0x00, 0x00, 0x00, 0x00, 0x08};
        //Get the least significant byte of baudRate,
        //and put it in first byte of the array being sent
        lineEncodingRequest[0] = (byte) (baudRate & 0xFF);

        //Get the 2nd byte of baudRate,
        //and put it in second byte of the array being sent
        lineEncodingRequest[1] = (byte) ((baudRate >> 8) & 0xFF);

        //ibid, for 3rd byte (my guess, because you need at least 3 bytes
        //to encode your 115200+ settings)
        lineEncodingRequest[2] = (byte) ((baudRate >> 16) & 0xFF);

        return lineEncodingRequest;

    }

    /*******************************************************************************************
     *******************************************************************************************/

    View.OnClickListener buttonSendOnClickListener =
            new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (mDevice != null) {

                       /* String tOut = textOut.getText().toString();
                        byte[] bytesOut = tOut.getBytes(); //convert String to byte[]
                        int usbResult = usbDeviceConnection.bulkTransfer(
                                endpointOut, bytesOut, bytesOut.length, 0);*/

                    } else {
                       /* Toast.makeText(MainActivity.this,
                                "deviceFound == null",
                                Toast.LENGTH_LONG).show();*/
                    }


                                    /*
                byte[] bytesHello = new byte[]{(byte) 'H', 'e', 'l', 'l',
                        'o', ' ', 'f', 'r', 'o', 'm', ' ', 'A', 'n', 'd', 'r',
                        'o', 'i', 'd'};
                usbResult = mConnection.bulkTransfer(mEndpointOut,
                        bytesHello, bytesHello.length, 0);

                Log.d("thiago", "bulkTransfer: " + usbResult);*/


                }
            };

    public void write(int type) {
        write(type + "#");
    }

    public void write(int type, int value) {
        write(type + "," + value + "#");
    }

    public void write(int type, int red, int green, int blue) {
        write(type + "," + red + "," + green + "," + blue + "#");
    }

    private void write(String data) {
        try {
            if (isConnected) {
                Logger.BT("WRITE: " + data);

                byte[] dataBytes = data.getBytes();
                /*for (int b = 0; b < dataBytes.length; b++) {
                    mOutputStream.write(dataBytes[b]);
                    Thread.sleep(100);
                }*/

                int result = mConnection.bulkTransfer(mEndpointOut, dataBytes, dataBytes.length, 5);
                Logger.BT("WRITE result: " + result);

            }
        } catch (Exception e) {
            Logger.BT("ERROR: " + e.getMessage());
        }
    }

}
