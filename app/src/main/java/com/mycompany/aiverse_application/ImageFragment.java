package com.mycompany.aiverse_application;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment {


    public ImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String filePath = ImageFragmentArgs.fromBundle(getArguments()).getImage();
        if (filePath.contains(".img")){
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            ImageView imageView =view.findViewById(R.id.image_zoomed);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(bitmap);
        }
        else if (filePath.contains(".video")){
            MediaController mediaController = new MediaController(view.getContext());
            VideoView videoView = view.findViewById(R.id.video_zoomed);
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoPath(filePath);
            videoView.setMediaController(mediaController);
            videoView.start();
        }

    }
}
