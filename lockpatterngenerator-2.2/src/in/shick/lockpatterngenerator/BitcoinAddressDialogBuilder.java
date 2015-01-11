/*
Copyright 2010-2013 Michael Shick

This file is part of 'Lock Pattern Generator'.

'Lock Pattern Generator' is free software: you can redistribute it and/or
modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or (at your option)
any later version.

'Lock Pattern Generator' is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details.

You should have received a copy of the GNU General Public License along with
'Lock Pattern Generator'.  If not, see <http://www.gnu.org/licenses/>.
*/
package in.shick.lockpatterngenerator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class BitcoinAddressDialogBuilder
{
    public static final String ADDR_INFO_URL_FMT =
        "https://blockchain.info/address/%s";
    public static final String MARKET_WALLET_URL =
        "market://details?id=de.schildbach.wallet";
    public static final String WEB_WALLET_URL =
        "https://code.google.com/p/bitcoin-wallet/downloads/list";

    protected final CharSequence address;
    protected final Context context;

    public BitcoinAddressDialogBuilder(Context context_, CharSequence address_)
    {
        context = context_;
        address = address_;
    }

    public AlertDialog build()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater)
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout dialogContent = (LinearLayout)
            inflater.inflate(R.layout.bitcoin_address_dialog, null, false);

        ((Button) dialogContent.findViewById(R.id.copy_address_button))
            .setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button button = (Button) view;
                    BitcoinAddressDialogBuilder.copyAddress(context, address);
                    button.setText(R.string.copied_address);
                    button.setEnabled(false);
                }
            });
        ((Button) dialogContent.findViewById(R.id.open_qr_button))
            .setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button button = (Button) view;
                    if(!BitcoinAddressDialogBuilder.openAddressInfo(context,
                            address)) {
                        button.setText(R.string.no_browser);
                        button.setEnabled(false);
                    }
                }
            });
        ((Button) dialogContent.findViewById(R.id.get_wallet_button))
            .setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button button = (Button) view;
                    if(!BitcoinAddressDialogBuilder.openWalletDownload(
                            context)) {
                        button.setText(R.string.no_browser);
                        button.setEnabled(false);
                    }
                }
            });

        builder.setView(dialogContent);
        builder.setNegativeButton(R.string.done,
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }

    public static boolean openWalletDownload(Context context)
    {
        try
        {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(MARKET_WALLET_URL)));
            return true;
        }
        catch(Throwable e)
        {
        }
        try
        {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(WEB_WALLET_URL)));
            return true;
        }
        catch(Throwable e)
        {
        }
        return false;
    }

    public static boolean openAddressInfo(Context context, CharSequence address)
    {
        try
        {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(String.format(ADDR_INFO_URL_FMT, address))));
        }
        catch(Throwable e)
        {
            return false;
        }
        return true;
    }

    public static void copyAddress(Context context, CharSequence address)
    {
        ClipboardManager clipboard = (ClipboardManager)
            context.getSystemService(Context.CLIPBOARD_SERVICE);

        clipboard.setText(address);
    }

    public CharSequence getAddress()
    {
        return address;
    }
}
