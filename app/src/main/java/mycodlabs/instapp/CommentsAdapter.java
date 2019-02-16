package mycodlabs.instapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.darsh.multipleimageselect.models.Image;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mycodlabs.adapters.Clickcb;
import mycodlabs.events.Comments;
import mycodlabs.events.R;
import mycodlabs.events.UserSessionManager;


/**
 * Created by froger_mcs on 11.11.14.
 */
public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private int itemsCount = 0;
    private int lastAnimatedPosition = -1;
    private int avatarSize;

    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;
    List<Comments> comments;
    Clickcb clickcb;
    UserSessionManager user;
    HashMap<String ,String> loged;

    public CommentsAdapter(Context context, List<Comments> comeComments, Clickcb clickcb) {
        this.context = context;
        comments=comeComments;
        user=new UserSessionManager(context);
        loged=user.getUserDetails();
        this.clickcb=clickcb;
        avatarSize = context.getResources().getDimensionPixelSize(R.dimen.comment_avatar_size);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        runEnterAnimation(viewHolder.itemView, position);
        CommentViewHolder holder = (CommentViewHolder) viewHolder;
        Comments cm = comments.get(position);
        if (comments.size() > 0) {

            holder.tvComment.setText(cm.getComment());
            holder.userinfo.setText(cm.getUserName());
            holder.date.setText( cm.getTime());
        }

        if(!(loged.get(UserSessionManager.KEY_USERTYPE_ID).equals("2"))&&
                loged.get(UserSessionManager.KEY_USER_ID).equals(String.valueOf(cm.getUserId()))||
               loged.get(UserSessionManager.KEY_USERTYPE_ID).equals("0"))
        {
            holder.imgDelete.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.imgDelete.setVisibility(View.GONE);
        }
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(clickcb!=null)
                {


                    clickcb.onclick(cm);
                    comments.remove(position);
                    CommentsAdapter.this.notifyDataSetChanged();
                }


            }
        });




        Picasso.with(context)
                .load(R.drawable.privateinfo)
                .centerCrop()
                .resize(avatarSize, avatarSize)
                .transform(new RoundedTransformation())
                .into(holder.ivUserAvatar);
    }


    private void runEnterAnimation(View view, int position) {
        if (animationsLocked) return;

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(100);
            view.setAlpha(0.f);
            view.animate()
                    .translationY(0).alpha(1.f)
                    .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)
                    .setInterpolator(new DecelerateInterpolator(2.f))
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animationsLocked = true;
                        }
                    })
                    .start();
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }



    public void setAnimationsLocked(boolean animationsLocked) {
        this.animationsLocked = animationsLocked;
    }

    public void setDelayEnterAnimation(boolean delayEnterAnimation) {
        this.delayEnterAnimation = delayEnterAnimation;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivUserAvatar)
        ImageView ivUserAvatar;
        @BindView(R.id.tvComment)
        TextView tvComment;
        @BindView(R.id.datetime)
        TextView date;
        @BindView(R.id.userinfo)
        TextView userinfo;
        @BindView(R.id.delete)
        ImageView imgDelete;

        public CommentViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
