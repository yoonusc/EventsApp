package mycodlabs.events;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mycodlabs.instapp.CommentsActivity;

/**
 * Created by naman on 13-04-2017.
 */

public class eventAdapter extends RecyclerView.Adapter<eventAdapter.ViewHolder>  {
    private Context context;
    private List<eventItem> event_data;
    private boolean organise;

    public eventAdapter(Context context, List<eventItem> event_data) {
        this.context = context;
        this.event_data = event_data;
        this.organise = organise;
    }

    public eventAdapter(Context context, List<eventItem> event_data, boolean organise) {
        this(context,event_data);
        this.organise = organise;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final eventItem  feedItem=event_data.get(position);
        holder.name.setText(event_data.get(position).getCreator()+" on "+event_data.get(position).getUpdated());
        String details=event_data.get(position).getDetails();
        holder.description.setText(details.length()>200?details.substring(0,150)+"...":details);
        if(feedItem.getMedais()!=null) {
            for (Medais medais : feedItem.getMedais()) {
                if (medais.getType().equals("image")) {
                    String path = context.getResources().getString(R.string.base_url) + medais.getPath();
                    Glide.with(context)
                            .load(path)
                            .apply(new RequestOptions()
                                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                    .placeholder(R.drawable.noimg)
                            )
                            .into(holder.feedimage);
                }
            }
        }
        else
        {

            Glide.with(context)
                    .load(R.drawable.noimg)
                    .apply(new RequestOptions()
                            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .placeholder(R.drawable.noimg)
                    )
                    .into(holder.feedimage);
        }
        Date date = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(event_data.get(position).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(organise)
        {
            holder.info.setImageResource(R.drawable.icon_edit);
            holder.comment.setVisibility(View.GONE);
            holder.like.setVisibility(View.GONE);
        }
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(context, CommentsActivity.class);
                int[] startingLocation = new int[2];
                view.getLocationOnScreen(startingLocation);
                intent.putExtra(CommentsActivity.ARG_DRAWING_START_LOCATION, startingLocation[1]);
                intent.putExtra("event_id", feedItem.getEvent_id());
                intent.putExtra("name",feedItem.getName());
                intent.putExtra("time", feedItem.getTime());
                intent.putExtra("venue", feedItem.getVenue());
                intent.putExtra("details", feedItem.getDetails());
                intent.putExtra("usertype_id", feedItem.getUsertype_id());
                intent.putExtra("usertype", feedItem.getUsertype());
                intent.putExtra("creator_id", feedItem.getCreator_id());
                intent.putExtra("creator", feedItem.getCreator());
                intent.putExtra("category_id", feedItem.getCategory_id());
                intent.putExtra("category", feedItem.getCategory());
                intent.putExtra("duration", feedItem.getDuration());
                context.startActivity(intent);
            }
        });

        holder.title.setText(feedItem.getName());
        holder.text_duration.setText(feedItem.getDuration());
        holder.time.setText(new SimpleDateFormat("dd MMM, yyyy hh:mm aa").format(date));
        String[] data=event_data.get(position).getVenue().split("\\r?\\n");
        String addres="";
        if(data.length>=4)
        {
          addres=data[2]+","+data[3];
        }
        holder.venue.setText(addres);

        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventDisplayUser.class);
                if (organise == true){
                    intent = new Intent(context, EditOrganisedEvent.class);
                }
                intent.putExtra("event_id", feedItem.getEvent_id());
                intent.putExtra("name",feedItem.getName());
                intent.putExtra("time", feedItem.getTime());
                intent.putExtra("venue", feedItem.getVenue());
                intent.putExtra("details", feedItem.getDetails());
                intent.putExtra("usertype_id", feedItem.getUsertype_id());
                intent.putExtra("usertype", feedItem.getUsertype());
                intent.putExtra("creator_id", feedItem.getCreator_id());
                intent.putExtra("creator", feedItem.getCreator());
                intent.putExtra("category_id", feedItem.getCategory_id());
                intent.putExtra("category", feedItem.getCategory());
                intent.putExtra("duration", feedItem.getDuration());
                intent.putParcelableArrayListExtra("images",(ArrayList<? extends Parcelable>) feedItem.getMedais());
                //intent.putExtra("videos", videos);
                context.startActivity(intent);
            }
        });

    }





    @Override
    public int getItemCount() {
        return event_data.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder{


        /*

          @BindView(R.id.ivFeedCenter)
        ImageView ivFeedCenter;
        @BindView(R.id.ivFeedBottom)
        TextView ivFeedBottom;
        @BindView(R.id.btnComments)
        ImageButton btnComments;
        @BindView(R.id.btnLike) // btn like
        ImageButton btnLike;
        @BindView(R.id.btnMore)
        ImageButton btnMore;
        @BindView(R.id.vBgLike)
        View vBgLike;
        @BindView(R.id.ivLike) // riple like
        ImageView ivLike;
        @BindView(R.id.tsLikesCounter)
        TextSwitcher tsLikesCounter;
        @BindView(R.id.ivUserProfile)
        ImageView ivUserProfile; // user top icon
        @BindView(R.id.vImageRoot)
        FrameLayout vImageRoot;
        @BindView(R.id.postuname)
        TextView postUname;
        @BindView(R.id.eventname)
        TextView eventname;
        @BindView(R.id.dateTime)
        TextView dateTime;
        @BindView(R.id.venu)
        TextView venue;
        @BindView(R.id.btninfo)

         */
        public TextView name;
        public TextView time;
        public TextView title;
        public TextView description;
        public ImageButton comment;
        public ImageButton like;
        public ImageButton more;
        public ImageButton info;
        public TextView venue;
        public TextView text_duration;
        public ImageView feedimage;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.postuname);
            title = (TextView) itemView.findViewById(R.id.eventname);
            time = (TextView) itemView.findViewById(R.id.dateTime);
            venue = (TextView) itemView.findViewById(R.id.venue);
            description = (TextView) itemView.findViewById(R.id.ivFeedBottom);
            comment = (ImageButton) itemView.findViewById(R.id.btnComments);
            like = (ImageButton) itemView.findViewById(R.id.btnLike);
//            more = (ImageButton) itemView.findViewById(R.id.btnMore);
            info=(ImageButton) itemView.findViewById(R.id.btninfo);
            feedimage=(ImageView)itemView.findViewById(R.id.ivFeedCenter);
            text_duration=(TextView)itemView.findViewById(R.id.text_duration);

        }
    }
}
