package com.example.meita.fetchjson;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


public class ShowAlertDialog {
    public static void showAlert(String mess, Context context) {
        try {
            if (context != null) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog);

                TextView text = (TextView) dialog.findViewById(R.id.tv);
                text.setText(mess);
                Button dialogButton = (Button) dialog.findViewById(R.id.btnok);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        } catch (Exception e) {

        }

    }
}
