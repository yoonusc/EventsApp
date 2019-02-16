package mycodlabs.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;
import java.util.List;

import mycodlabs.events.Comments;
import mycodlabs.events.Medais;
import mycodlabs.events.R;
import mycodlabs.events.UserSessionManager;


/**
 * Created by naman on 13-04-2017.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder>  {
    private Context context;
    private List<Comments> comments;
    private boolean organise;
    private Clickcb cb;
    UserSessionManager session;
    String user_id="";
    String usertype_id="";

    public CommentsAdapter(Context context, List<Comments> comments) {
        this.context = context;
        this.comments = comments;
        this.organise = organise;
        session = new UserSessionManager(context);
        HashMap<String, String> user = session.getUserDetails();
        user_id = user.get(UserSessionManager.KEY_USER_ID);
        usertype_id = user.get(UserSessionManager.KEY_USERTYPE_ID);

    }

    public CommentsAdapter(Context context, List<Comments> comments, Clickcb cickcb) {
        this(context,comments);
        this.organise = organise;
        this.cb=cickcb;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Comments data=comments.get(position);


         holder.userName.setText(data.getUserName());
         holder.commnets.setText(data.getComment());
         holder.time.setText(data.getTime());
         if(!(usertype_id.equals("2"))&&user_id.equals(String.valueOf(data.getUserId()))||usertype_id.equals("0"))
         {
             holder.iconDelete.setVisibility(View.VISIBLE);
         }
         else
         {
             holder.iconDelete.setVisibility(View.GONE);
         }
        holder.iconDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(cb!=null)
                {


                    cb.onclick(data);
                    comments.remove(position);
                    CommentsAdapter.this.notifyDataSetChanged();
                }


            }
        });


    }




    @Override
    public int getItemCount() {
        return comments.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder{


        public ImageView iconDelete;
        public TextView userName;
        public TextView commnets;
        public TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            iconDelete = (ImageView) itemView.findViewById(R.id.img_delet);
            userName=(TextView) itemView.findViewById(R.id.comment_user);
            commnets=(TextView)itemView.findViewById(R.id.text_comment);
            time=(TextView)itemView.findViewById(R.id.comment_date);

        }
    }
}
