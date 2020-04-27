package com.example.reno.ui.share;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.util.Util;
import com.example.reno.AddNumber;
import com.example.reno.BuildConfig;
import com.example.reno.DiologsendActivity;
import com.example.reno.HomeActivity;
import com.example.reno.MessageCredit;
import com.example.reno.MessageHistory;
import com.example.reno.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ShareFragment extends Fragment {

    private ShareViewModel shareViewModel;
    public CardView cardView;
    public GridLayout gridLayout;
    private TextView textViews;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shareViewModel =
                ViewModelProviders.of(this).get(ShareViewModel.class);
        View root = inflater.inflate(R.layout.fragment_share, container, false);


        gridLayout = root.findViewById(R.id.mainGridshare);
        setEvent(gridLayout);

        textViews = (TextView) root.findViewById(R.id.sharetext);
        return root;
    }
    public void setEvent(GridLayout gridLayout) {
        for (int a = 0; a < gridLayout.getChildCount();a++){
            cardView = (CardView)gridLayout.getChildAt(a);
            final int finali = a;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = null;
                    if(finali ==0){
                        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                        whatsappIntent.setType("text/plain");
                        whatsappIntent.setPackage("com.whatsapp");
                        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "http://www.reno.com/app/download_app");
                        try {
                            startActivity(whatsappIntent);
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(getActivity(), "Whatsapp have not been installed", Toast.LENGTH_SHORT).show();                        }
                    }else if(finali ==1){
                        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto","Ecourserep@gmail.com", null));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Sms pin Request");
                        intent.putExtra(Intent.EXTRA_TEXT, "I would like to purchase a pin.");
                        startActivity(Intent.createChooser(intent, "Reno:"));

                    }else if(finali == 2){
                        String shareBody = "https://play.google.com/store/apps/details";
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "APP NAME (E Course Rep)");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    }else if(finali == 3){
                        final String appPackageName = getContext().getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }

                    }

                }
            });
        }
    }



}