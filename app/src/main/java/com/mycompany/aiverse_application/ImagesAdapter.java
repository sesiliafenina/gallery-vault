package com.mycompany.aiverse_application;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;


public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {

    private ArrayList<File> localDataSet;
    final int THUMBSIZE = 290;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private File imageFile;

        public ViewHolder(final View view) {
            super(view);
            imageView = view.findViewById(R.id.image_button);
            // set a onClick listener so each item can be clicked
             imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavDirections action = ImagesFragmentDirections.actionImagesFragmentToImageFragment(imageFile.getPath());
                    Navigation.findNavController(view).navigate(action);
                }
            });
        }



        public ImageView getImageView() {
            return imageView;
        }

        public void bind(File image){
            imageFile = image;
        }

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public ImagesAdapter(ArrayList<File> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.image_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        File file = localDataSet.get(position);
        // Get element from your dataset at this position and replace the
        // contents of the view with the thumbnail of the image/video
        ImageView imageView = viewHolder.getImageView();
        RequestOptions reqOpt = RequestOptions
                .centerCropTransform()
                .diskCacheStrategy(DiskCacheStrategy.ALL) // cache image after first load
                .override(THUMBSIZE); // Convert image size to smaller size
        Glide.with(imageView.getContext()).load(file).apply(reqOpt).into(imageView);
        viewHolder.bind(file);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}

