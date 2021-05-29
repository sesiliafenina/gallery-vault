package com.mycompany.aiverse_application;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumsFragment extends Fragment {
    private ArrayList<File> mDataset;
    private RecyclerView mRecyclerView;
    private AlbumsAdapter mAdapter;
    private GridLayoutManager mGridLayoutManager;
    private String defaultPassword = "03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4";

    public AlbumsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize dataset
        initDataset();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_albums, container, false);
        mRecyclerView = rootView.findViewById(R.id.recycler_view_albums);
        mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mAdapter = new AlbumsAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        MaterialToolbar topAppBar = rootView.findViewById(R.id.topAppBar);
        topAppBar.setOnMenuItemClickListener(menuItem -> {
            // use switch here for further developments (if need to add more than 1 menu)
            switch(menuItem.getItemId()){
                case (R.id.change_password):
                    //get the display size
                    Display display = getActivity().getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int width = size.x;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;

                    //get the layout inflater
                    LayoutInflater layoutInflater = getLayoutInflater();
                    View popUpView = layoutInflater.inflate(R.layout.change_password_popup, null);

                    //construct the popup window
                    PopupWindow popupWindow = new PopupWindow(popUpView, width-60, height, true);
                    popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
                    dimBehind(popupWindow);
                    final EditText popupOldPassword = popUpView.findViewById(R.id.popup_old_password);
                    final EditText popupNewPassword = popUpView.findViewById(R.id.popup_new_password);
                    final EditText popupReenterNewPassword = popUpView.findViewById(R.id.popup_reenter_new_password);
                    Button confirm = popUpView.findViewById(R.id.confirm_password);
                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String oldPassword = popupOldPassword.getText().toString();
                            String newPassword = popupNewPassword.getText().toString();
                            String newReenterPassword = popupReenterNewPassword.getText().toString();

                            if (!newPassword.equals(newReenterPassword)){
                                Toast.makeText(getContext(), "New password does not match", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                // get the old password from shared preference
                                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                                // if its the first time the user opens the app, the password is 1234 (hashed value)
                                String storedPasscode = sharedPref.getString("passcode", defaultPassword);
                                try{
                                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                                    byte[] hashedInputPasswordBytes = digest.digest(oldPassword.getBytes(StandardCharsets.UTF_8));
                                    String hashedInputPassword = hashByteToString(hashedInputPasswordBytes);
                                    // if the two passwords are the same
                                    if (hashedInputPassword.equals(storedPasscode)){
                                        byte[] newPasswordBytes = digest.digest(newPassword.getBytes(StandardCharsets.UTF_8));
                                        String newPasswordHashed = hashByteToString(newPasswordBytes);
                                        // change the password
                                        sharedPref.edit().putString("passcode", newPasswordHashed).apply();
                                        Toast.makeText(getContext(), "Password changed successfully", Toast.LENGTH_SHORT).show();
                                        popupWindow.dismiss();
                                    }
                                    else{
                                        Toast.makeText(getContext(), "Old password does not match", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    return true;
            }
            return false;
        });
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // add a floating action button
        FloatingActionButton fab = view.findViewById(R.id.albums_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the display size
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;

                //get the layout inflater
                LayoutInflater layoutInflater = getLayoutInflater();
                View popUpView = layoutInflater.inflate(R.layout.create_album_popup, null);

                //construct the popup window
                PopupWindow popupWindow = new PopupWindow(popUpView, width-60, height, true);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                dimBehind(popupWindow);
                final EditText input = popUpView.findViewById(R.id.popup_input);

                Button createAlbum = popUpView.findViewById(R.id.create_album);
                createAlbum.setOnClickListener(v -> {
                    String folderName = input.getText().toString();
                    if (folderName.contains("/")){
                        Toast.makeText(getContext(), "Folder name cannot contain symbols", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        File direct = new File(getContext().getFilesDir() + "/GalleryVault/" + folderName);
                        //if the folder does not exist create a new folder
                        if (!direct.exists()) {
                            File wallpaperDirectory = new File(getContext().getFilesDir() + "/GalleryVault/" + folderName);
                            wallpaperDirectory.mkdirs();
                            mDataset.add(wallpaperDirectory);
                            mAdapter.notifyDataSetChanged();
                        }
                        // else if the album already exist, show a toast that says name already exist
                        else{
                            Toast.makeText(getContext(), "Folder already exist. Please choose another name!", Toast.LENGTH_SHORT).show();
                        }
                        popupWindow.dismiss();
                    }
                });
            }
        });

    }

    private void initDataset() {
        mDataset = new ArrayList<>();
        // list all the albums inside the app
        File folder = new File(getContext().getFilesDir() + "/GalleryVault");
        File[] allFiles = folder.listFiles();
        if (allFiles != null){
            mDataset.addAll(Arrays.asList(allFiles));
        }
    }

    private static void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                container = (View) popupWindow.getContentView().getParent();
            } else {
                container = popupWindow.getContentView();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent().getParent();
            } else {
                container = (View) popupWindow.getContentView().getParent();
            }
        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.3f;
        wm.updateViewLayout(container, p);
    }

    private String hashByteToString(byte[] hash){
        StringBuilder sb = new StringBuilder();

        for (byte b : hash) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

}
