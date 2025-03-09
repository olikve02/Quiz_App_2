package com.example.quizapp2;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends ListAdapter<Image, ImageAdapter.ImageViewHolder> {
    private final onDeleteIconClickListener listener;

    public interface onDeleteIconClickListener {
        void onImageDeleteClick(Image image);
    }

    public ImageAdapter(onDeleteIconClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_item_template, parent, false);
        return new ImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Image currentImage = getItem(position);
        holder.bind(currentImage);
    }

    public static final DiffUtil.ItemCallback<Image> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Image>() {
                @Override
                public boolean areItemsTheSame(@NonNull Image oldImage, @NonNull Image newImage) {
                    return oldImage.getId() == newImage.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Image oldImage, @NonNull Image newImage) {
                    return oldImage.getName().equals(newImage.getName()) &&
                            oldImage.getUriString().equals(newImage.getUriString());
                }
            };

    class ImageViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final ImageView imageView;
        private final ImageView deleteIcon;

        public ImageViewHolder(View itemView) {
            super(itemView);
            deleteIcon = itemView.findViewById(R.id.deleteIcon);
            nameTextView = itemView.findViewById(R.id.textViewTemplate);
            imageView = itemView.findViewById(R.id.imageViewTemplate);

            deleteIcon.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null);{
                    Image image = getItem(pos);
                    listener.onImageDeleteClick(image);
                }
            });
        }

        public void bind(Image image) {
            nameTextView.setText(image.getName());
            imageView.setImageURI(Uri.parse(image.getUriString()));
            deleteIcon.setImageResource(R.drawable.baseline_delete_24);
        }
    }
}