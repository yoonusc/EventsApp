package mycodlabs.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import mycodlabs.events.EventDisplayUser;
import mycodlabs.events.Medais;
import mycodlabs.events.R;
import mycodlabs.events.eventItem;


/**
 * Created by naman on 13-04-2017.
 */

public class OldImagesAdapter extends RecyclerView.Adapter<OldImagesAdapter.ViewHolder>  {
    private Context context;
    private List<Medais> medais;
    private boolean organise;
    private Clickcb cb;

    public OldImagesAdapter(Context context, List<Medais> medais) {
        this.context = context;
        this.medais = medais;
        this.organise = organise;
    }

    public OldImagesAdapter(Context context, List<Medais> medais, boolean organise,Clickcb cickcb) {
        this(context,medais);
        this.organise = organise;
        this.cb=cickcb;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.old_imgs_feed,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Medais data=medais.get(position);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(cb!=null)
                {
                    cb.onclick(data);
                }


            }
        });

        if (data.getType().equals("image")) {
            String path = context.getResources().getString(R.string.base_url) + data.getPath();
            Glide.with(context)
                    .load(path)
                    .apply(new RequestOptions()
                            .centerCrop()
                            .override(250,250)
                            .placeholder(R.drawable.noimg)
                    )
                    .into(holder.oldImage);
        }
        else
        {
            Glide.with(context)
                    .load("")
                    .apply(new RequestOptions()
                            .centerCrop()
                            .override(250,250)
                            .placeholder(R.drawable.ic_media_play_light)
                    )
                    .into(holder.oldImage);
        }
    }




    @Override
    public int getItemCount() {
        return medais.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder{


        public ImageView oldImage;
        public Button delete;

        public ViewHolder(View itemView) {
            super(itemView);
            oldImage = (ImageView) itemView.findViewById(R.id.old_img);
            delete=(Button) itemView.findViewById(R.id.btn_delet_img);

        }
    }
}
