package bk.lvtn.fragment_adapter;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bk.lvtn.R;

/**
 * Created by Phupc on 11/12/17.
 */

public class AttachImgAdapter extends RecyclerView.Adapter<AttachImgAdapter.RecyclerViewHolder> {
    private List<AttachImages> data = new ArrayList<>();
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView img_item;
        FloatingActionButton delete_img;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            img_item = (ImageView) itemView.findViewById(R.id.img);
            delete_img = (FloatingActionButton) itemView.findViewById(R.id.deleteimg_button);
        }
    }


    public AttachImgAdapter(List<AttachImages> data) {
        this.data = data;
    }
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        holder.img_item.setImageBitmap(data.get(position).getBitmap());
        holder.delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(),data.size());
            }
        });
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.attachfile_item, parent, false);
        return new RecyclerViewHolder(view);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
//    public void update(ArrayList<AttachImages> newdata) {
//        data.clear();
//        data.addAll(newdata);
//        notifyDataSetChanged();
//    }
}
