package com.mycompany.aiverse_application;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.ViewHolder> {
    private ArrayList<File> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;
        private String folderName;


        public ViewHolder(final View view) {
            super(view);
            imageView = view.findViewById(R.id.album_button);
            textView = view.findViewById(R.id.album_name);
            // set an onClick listener so each item can be clicked
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // navigate to the specified folder
                    NavDirections action = AlbumsFragmentDirections.actionAlbumsFragmentToImagesFragment(folderName);
                    Navigation.findNavController(view).navigate(action);
                }
            });
        }

        public TextView getTextView(){
            return textView;
        }

        public void setFolderName(String name){
            folderName = name;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public AlbumsAdapter(ArrayList<File> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AlbumsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.album_row_item, viewGroup, false);

        return new AlbumsAdapter.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(AlbumsAdapter.ViewHolder viewHolder, final int position) {
        File file = localDataSet.get(position);
        // Get element from your dataset at this position and replace the names of the albums
        String folderName = getFileName(file.getPath());
        viewHolder.setFolderName(folderName);
        viewHolder.getTextView().setText(folderName);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    private String getFileName(String uri) {
        int cut = uri.lastIndexOf("/");
        if (cut != -1) {
            uri = uri.substring(cut + 1);
        }
        return uri;
    }
}
