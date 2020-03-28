package com.ocheresh.ft_hangouts;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.ocheresh.ft_hangouts.Model.Abonent;
import java.io.File;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Abonent> list;
    Adapter.OnItemListener onItemListener;
    private static Uri image_uri;



    public Adapter(Context context, List<Abonent> list, Adapter.OnItemListener onItemListener){
        this.context = context;
        this.list = list;
        this.onItemListener = onItemListener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View row = layoutInflater.inflate(R.layout.adapter_main_activity, parent,false);
        Item item = new Item(row, onItemListener);
        return item;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((Item)holder).text_name.setText(list.get(position).getName());
        ((Item)holder).text_telephone.setText(list.get(position).getTelephonenumber());
        if (list.get(position).getPhoto_path() != null) {
            File img = new File(list.get(position).getPhoto_path());
            image_uri = null;
            try {
                image_uri = FileProvider.getUriForFile(context, "com.ocheresh.ft_hangouts", img);
            } catch (Exception e) {
                Log.i("Error: ", e.getMessage());
            }
            if (image_uri != null)
                ((Item) holder).imageButton.setImageURI(image_uri);
        }
    }

    @Override
    public int getItemCount() {
        return Integer.valueOf(list.size());
    }



    public class Item extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text_name;
        TextView text_telephone;
        ImageButton imageButton;
        Adapter.OnItemListener onItemListener;

        public Item(@NonNull View itemView, Adapter.OnItemListener onItemListener) {
            super(itemView);
            this.onItemListener = onItemListener;
            text_name = itemView.findViewById(R.id.textname);
            text_telephone = itemView.findViewById(R.id.textphonenumber);
            imageButton = itemView.findViewById(R.id.image_main);

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImage(getAdapterPosition());
                }
            });
            itemView.setOnClickListener(this);
        }




        @Override
        public void onClick(View v) {
            onItemListener.OnItemClick(getAdapterPosition());
        }
    }

    public interface OnItemListener
    {
        void OnItemClick(int position);
    }

    private void showImage(int position) {
        Dialog builder = new Dialog(context);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) { }
        });

        image_uri = null;
        if (list.get(position).getPhoto_path() != null) {
            File img = new File(list.get(position).getPhoto_path());
            try {
                image_uri = FileProvider.getUriForFile(context, "com.ocheresh.ft_hangouts", img);
            } catch (Exception e) {
                Log.i("Error: ", e.getMessage());
            }
        }

        if (image_uri != null) {
            ImageView imageView = new ImageView(context);
            imageView.setImageURI(image_uri);
            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            builder.show();
        }

    }
}
