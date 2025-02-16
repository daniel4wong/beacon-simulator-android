/****************************************************************************************
 * Copyright (c) 2016, 2017, 2019 Vincent Hiribarren                                    *
 *                                                                                      *
 * This program is free software; you can redistribute it and/or modify it under        *
 * the terms of the GNU General Public License as published by the Free Software        *
 * Foundation; either version 3 of the License, or (at your option) any later           *
 * version.                                                                             *
 *                                                                                      *
 * Linking Beacon Simulator statically or dynamically with other modules is making      *
 * a combined work based on Beacon Simulator. Thus, the terms and conditions of         *
 * the GNU General Public License cover the whole combination.                          *
 *                                                                                      *
 * As a special exception, the copyright holders of Beacon Simulator give you           *
 * permission to combine Beacon Simulator program with free software programs           *
 * or libraries that are released under the GNU LGPL and with independent               *
 * modules that communicate with Beacon Simulator solely through the                    *
 * net.alea.beaconsimulator.bluetooth.AdvertiseDataGenerator and the                    *
 * net.alea.beaconsimulator.bluetooth.AdvertiseDataParser interfaces. You may           *
 * copy and distribute such a system following the terms of the GNU GPL for             *
 * Beacon Simulator and the licenses of the other code concerned, provided that         *
 * you include the source code of that other code when and as the GNU GPL               *
 * requires distribution of source code and provided that you do not modify the         *
 * net.alea.beaconsimulator.bluetooth.AdvertiseDataGenerator and the                    *
 * net.alea.beaconsimulator.bluetooth.AdvertiseDataParser interfaces.                   *
 *                                                                                      *
 * The intent of this license exception and interface is to allow Bluetooth low energy  *
 * closed or proprietary advertise data packet structures and contents to be sensibly   *
 * kept closed, while ensuring the GPL is applied. This is done by using an interface   *
 * which only purpose is to generate android.bluetooth.le.AdvertiseData objects.        *
 *                                                                                      *
 * This exception is an additional permission under section 7 of the GNU General        *
 * Public License, version 3 (“GPLv3”).                                                 *
 *                                                                                      *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY      *
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A      *
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.             *
 *                                                                                      *
 * You should have received a copy of the GNU General Public License along with         *
 * this program.  If not, see <http://www.gnu.org/licenses/>.                           *
 ****************************************************************************************/

package net.alea.beaconsimulator.component;


import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.alea.beaconsimulator.R;

public class DialogAskScanPermission extends BottomSheetDialogFragment {

    private final static String NEED_BLUETOOTH = "NEED_BLUETOOTH";
    private final static String NEED_LOCATION = "NEED_LOCATION";

    // Problem if directly instantiate fragment with parameters in constructor, and if screen rotation
    // http://stackoverflow.com/questions/9245408/best-practice-for-instantiating-a-new-android-fragment
    public static DialogAskScanPermission newInstance(boolean needBluetooth, boolean needLocation) {
        DialogAskScanPermission myFragment = new DialogAskScanPermission();
        Bundle args = new Bundle();
        args.putBoolean(NEED_BLUETOOTH, needBluetooth);
        args.putBoolean(NEED_LOCATION, needLocation);
        myFragment.setArguments(args);
        return myFragment;
    }


    // Expansion problem in landscape mode
    // http://stackoverflow.com/questions/35937453/set-state-of-bottomsheetdialogfragment-to-expanded
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = (FrameLayout) d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final Bundle bundle = getArguments();
        final boolean needBluetooth = bundle.getBoolean(NEED_BLUETOOTH);
        final boolean needLocation = bundle.getBoolean(NEED_LOCATION);
        View contentView = inflater.inflate(R.layout.dialog_ask_scan_permission, container);
        TextView bluetoothText = (TextView)contentView.findViewById(R.id.askscan_textview_bluetooth);
        if (! needBluetooth) {
            bluetoothText.setVisibility(View.GONE);
        }
        TextView locationText = (TextView)contentView.findViewById(R.id.askscan_textview_location);
        if (! needLocation) {
            locationText.setVisibility(View.GONE);
        }
        Button askPermission = (Button)contentView.findViewById(R.id.askscan_button_permission);
        askPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (needLocation) {
                    Intent enableLocationIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(enableLocationIntent);
                }
                if (needBluetooth) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivity(enableBtIntent);
                }
                dismiss();
            }
        });
        return contentView;
    }
}
