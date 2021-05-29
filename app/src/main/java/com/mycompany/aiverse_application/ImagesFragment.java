package com.mycompany.aiverse_application;


import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImagesFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ImagesAdapter mAdapter;
    private GridLayoutManager mGridLayoutManager;
    private ActivityResultLauncher<Intent> mActivityResultLauncher;
    private ArrayList<File> mDataset;
    private TextView mTextView;
    private String mFolderName;
    private MaterialToolbar mAppBar;


    public ImagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFolderName = ImagesFragmentArgs.fromBundle(getArguments()).getFolderName();
        // Initialize dataset
        initDataset();
        // Register for Activity result
        mActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // hide the no images textview
                            mTextView.setVisibility(View.INVISIBLE);
                            Intent data = result.getData();
                            // if data is not null (i.e. there are images selected), add the images to the VaultGallery folder
                            if (data != null) {
                                ClipData clipData = data.getClipData();
                                // for multiple data
                                if (clipData != null){
                                    for (int i = 0; i < clipData.getItemCount(); i++) {
                                        Uri uri = clipData.getItemAt(i).getUri();
                                        saveFileToDataset(uri);
                                    }
                                }
                                // if clip data is null (somehow samsung gallery only allows user to choose 1 image)
                                // and so clip data is null, then go to this part
                                // if not the app will crash because there is only 1 data (no ClipData)
                                else {
                                    Uri uri = data.getData();
                                    saveFileToDataset(uri);
                                }

                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_images, container, false);
        mRecyclerView = rootView.findViewById(R.id.recycler_view_images);
        mTextView = rootView.findViewById(R.id.no_image);
        mGridLayoutManager = new GridLayoutManager(getActivity(), 4);
        mAdapter = new ImagesAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mAppBar = rootView.findViewById(R.id.topAppBar);
        mAppBar.setTitle(mFolderName);

        // if the dataset is empty, show the No images textview
        if (mDataset.isEmpty()){
            mTextView.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // add a floating action button
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // launch image picker activity
                Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
                String[] mimeTypes = {"image/*", "video/*"};
                imagePickerIntent.setType("*/*");
                // allow multiple images to be picked
                imagePickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                imagePickerIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                mActivityResultLauncher.launch(imagePickerIntent);
            }
        });
    }

    private void initDataset() {
        mDataset = new ArrayList<>();
        File folder = new File(getContext().getFilesDir() + "/GalleryVault/" + mFolderName);
        File[] allFiles = folder.listFiles();
        if (allFiles != null){
            mDataset.addAll(Arrays.asList(allFiles));
        }
    }

    private String getFileName(Uri uri) {
        String name = uri.getPath();
        int cut = name.lastIndexOf("/");
        if (cut != -1) {
            name = name.substring(cut + 1);
        }
        return name;
    }

    private void saveFileToDataset(Uri uri){
        InputStream inputStream = null;
        try {
            inputStream = getActivity().getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String filename = getFileName(uri);
        // if file is image, add .img extension behind
        // if file is video, add .video extension behind
        if (uri.toString().contains("image")){
            filename += ".img";
        }
        else if (uri.toString().contains("video")){
            filename += ".video";
        }
        // save the file to the folder
        saveFileToDirectory(inputStream, filename);
        mDataset.add(new File(getContext().getFilesDir() + "/GalleryVault/" + mFolderName + "/" + filename));

    }

    private void saveFileToDirectory(InputStream stream, String filename) {
        File file = new File(getContext().getFilesDir() + "/GalleryVault/" + mFolderName, filename);

        byte[] buffer = new byte[4 * 1024];
        int read;
        try {
            FileOutputStream out = new FileOutputStream(file);
            while ((read = stream.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            out.flush();
            out.close();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
