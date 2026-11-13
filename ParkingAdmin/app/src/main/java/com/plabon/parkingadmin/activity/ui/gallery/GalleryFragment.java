package com.plabon.parkingadmin.activity.ui.gallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.plabon.parkingadmin.R;
import com.plabon.parkingadmin.activity.ReportActivity;
import com.plabon.parkingadmin.activity.UpdatePriceActivity;
import com.plabon.parkingadmin.api.clients.RestClient;


public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    TextView btnReport;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        btnReport = root.findViewById(R.id.btnReport);

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(getContext(), ReportActivity.class);
                startActivity(intent);*/
                String url = RestClient.BASE_URL + "api/v1/report.php";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        return root;
    }
}