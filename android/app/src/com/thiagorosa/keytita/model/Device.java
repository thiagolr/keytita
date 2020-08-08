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

package com.thiagorosa.keytita.model;

import android.text.TextUtils;

public class Device {

    public enum Status {
        NEW, BONDED, CONNECTING, CONNECTED
    }

    private String mName;
    private String mMAC;
    private Status mStatus;

    public Device(String name, String mac, Status status) {
        mName = name;
        mMAC = mac;
        mStatus = status;
    }

    public String getName() {
        return TextUtils.isEmpty(mName) ? "Unknown" : mName;
    }

    public String getMAC() {
        return mMAC;
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status status) {
        mStatus = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (!(o instanceof Device)) {
            return false;
        }
        return ((Device) o).getMAC().equals(mMAC);
    }

    @Override
    public int hashCode() {
        return mMAC.hashCode();
    }

    @Override
    public String toString() {
        return mName + " (" + mMAC + ")";
    }

}
