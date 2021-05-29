package com.mycompany.aiverse_application;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Audio;
import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.controls.Mode;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private String password = "";
    private EditText inputOne;
    private EditText inputTwo;
    private EditText inputThree;
    private EditText inputFour;
    private int counter = 0;
    private CameraView mCamera;
    private String defaultPassword = "03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4";

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        inputOne = rootView.findViewById(R.id.input_one);
        inputTwo = rootView.findViewById(R.id.input_two);
        inputThree = rootView.findViewById(R.id.input_three);
        inputFour = rootView.findViewById(R.id.input_four);
        // move the cursor forward if the input text is filled
        moveToNextInput(inputOne,inputTwo, 0);
        moveToNextInput(inputTwo,inputThree, 0);
        moveToNextInput(inputThree,inputFour, 0);
        moveToNextInput(inputFour, inputOne, 1);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // initialize the camera here because lifecycle awareness
        mCamera = view.findViewById(R.id.camera);
        mCamera.setLifecycleOwner(getViewLifecycleOwner());
        mCamera.setMode(Mode.PICTURE);
        mCamera.setFacing(Facing.FRONT);
        //set audio off so app doesnt crash
        mCamera.setAudio(Audio.OFF);
        mCamera.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(@NotNull PictureResult result) {
                File direct = new File(getContext().getFilesDir() + "/GalleryVault/front_capture/");
                //if the folder does not exist create a new folder
                if (!direct.exists()) {
                    File wallpaperDirectory = new File(getContext().getFilesDir() + "/GalleryVault/front_capture/");
                    wallpaperDirectory.mkdirs();
                    String random = generateString();
                    File file = new File(getContext().getFilesDir() + "/GalleryVault/front_capture/" + "perpetrator" + random + ".img");
                    result.toFile(file, file1 -> {
                        // do nothing
                    });
                }
                else{
                    File wallpaperDirectory = new File(getContext().getFilesDir() + "/GalleryVault/front_capture/");
                    String random = generateString();
                    File file = new File(getContext().getFilesDir() + "/GalleryVault/front_capture/" + "perpetrator" + random + ".img");
                    result.toFile(file, file1 -> {
                        // do nothing
                    });
                }
            }
        });
    }

    private void moveToNextInput(final EditText input1, final EditText input2, int last){
        if (last == 0) {
            input1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void afterTextChanged(Editable s) {
                    if (input1.getText().toString().length()==1){
                        password += input1.getText().toString();
                        input2.requestFocus();
                    }
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
            });
        }
        // if it is the last input
        else{
            input1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void afterTextChanged(Editable s) {
                    if (input1.getText().toString().length()==1){
                        password += input1.getText().toString();
                        // get a shared preference to store the password
                        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                        // if its the first time the user opens the app, the password is 1234 (hashed value)
                        String storedPasscode = sharedPref.getString("passcode", defaultPassword);
                        try {
                            // hash the input value and compare it with the stored hash value
                            MessageDigest digest = MessageDigest.getInstance("SHA-256");
                            byte[] hashedInputPasswordBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
                            String hashedInputPassword = hashByteToString(hashedInputPasswordBytes);
                            // if the two passwords are the same
                            if (hashedInputPassword.equals(storedPasscode)){
                                // close the camera
                                mCamera.close();
                                // navigate to albums
                                NavDirections action = LoginFragmentDirections.actionLoginFragmentToAlbumsFragment();
                                Navigation.findNavController(getView()).navigate(action);

                            }
                            else{
                                //increase the counter by 1
                                counter += 1;
                                //if there are 3 wrong attempts, take a picture from the front camera
                                if (counter == 3){
                                    //reset the counter
                                    counter = 0;
                                    // take a snapshot to be fast
                                    mCamera.takePictureSnapshot();
                                }
                                // reset the password and input
                                password = "";
                                inputOne.setText("");
                                inputTwo.setText("");
                                inputThree.setText("");
                                inputFour.setText("");
                                input2.requestFocus();
                                //close the keyboard
                                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                if (inputMethodManager != null){
                                    inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                }
                                // tell user that the password is wrong
                                Toast.makeText(getContext(), "Wrong Password", Toast.LENGTH_SHORT).show();

                            }
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }

                    }
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
            });
        }

    }

    private String hashByteToString(byte[] hash){
        StringBuilder sb = new StringBuilder();

        for (byte b : hash) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    private String generateString(){
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }

}
