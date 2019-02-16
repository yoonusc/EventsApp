package mycodlabs.instapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.roger.catloadinglibrary.CatLoadingView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mycodlabs.adapters.Clickcb;
import mycodlabs.events.Comments;
import mycodlabs.events.DividerItemDecoration;
import mycodlabs.events.R;
import mycodlabs.events.UserSessionManager;

/**
 * Created by froger_mcs on 11.11.14.
 */
public class CommentsActivity extends AppCompatActivity implements SendCommentButton.OnSendClickListener {
    public static final String ARG_DRAWING_START_LOCATION = "arg_drawing_start_location";

    LinearLayout contentRoot;
    RecyclerView rvComments;
    LinearLayout llAddComment;
    EditText etComment;
    EditText guetUserName;
    SendCommentButton btnSendComment;
    CatLoadingView mView;
    private int drawingStartLocation;
    int eventId;
    String userName,usertype,userid;
    UserSessionManager session;
    HashMap<String, String> user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        contentRoot=(LinearLayout)findViewById(R.id.contentRoot);
        rvComments=(RecyclerView)findViewById(R.id.rvComments);
        llAddComment=(LinearLayout)findViewById(R.id.llAddComment);
        etComment=(EditText)findViewById(R.id.etComment);
        btnSendComment=(SendCommentButton)findViewById(R.id.btnSendComment);
        guetUserName=(EditText) findViewById(R.id.text_gust_user_Name);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mView=new CatLoadingView();
        refreshComments(new ArrayList<>());
        setupSendCommentButton();
        getComments();
        session = new UserSessionManager(getApplicationContext());
        user = session.getUserDetails();
        String user_type=user.get(UserSessionManager.KEY_USERTYPE_ID);
        eventId = getIntent().getIntExtra("event_id",0);
        userid =  user.get(UserSessionManager.KEY_USER_ID);
        userName =  user.get(UserSessionManager.KEY_NAME);
        if(user_type.equals("2")) {
            guetUserName.setVisibility(View.VISIBLE);
            guetUserName.setText(userName);
        }
        else {
            guetUserName.setVisibility(View.GONE);

        }

        drawingStartLocation = getIntent().getIntExtra(ARG_DRAWING_START_LOCATION, 0);
        if (savedInstanceState == null) {
            contentRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    contentRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                    startIntroAnimation();
                    return true;
                }
            });
        }
    }


    List<Comments> comments=new ArrayList<>();
    private void getComments() {

        showDialog();

        StringRequest createComment= new StringRequest(Request.Method.POST, getResources().getString(R.string.getComment),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        hideDialog();
                        try{
                            Log.d("response",response);
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                comments.clear();
                                JSONArray array = jsonResponse.getJSONArray("comments");
                                for (int i=0; i<array.length(); i++){

                                    JSONObject e = array.getJSONObject(i);
                                    Comments data = new Comments(e.getInt("id"),e.getInt("user_id"),e.getInt("event_id"),e.getString("user_name"),e.getString("comment"),e.getString("time"));//)//,e.getString("name"),e.getString("time"),e.getString("venue"),e.getString("details"),e.getInt("usertype_id"),e.getString("usertype"),e.getInt("creator_id"),e.getString("creator"),e.getInt("category_id"),e.getString("category"));
                                    comments.add(data);


                                }
                                refreshComments(comments);


                            } else {
                                refreshComments(comments);
                                Log.d("empty","empty");
                                // Toast.makeText(getApplicationContext(),"Empty Comments",Toast.LENGTH_SHORT).show();

                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                hideDialog();
                Toast.makeText(getApplicationContext(),"Error from server ",Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("event_id", eventId+"");
                params.put("user_id", userid+"");
                params.put("comment",etComment.getText().toString());
                params.put("user_name", userName);
                return params;
            }

        };
        RequestQueue queue3 = Volley.newRequestQueue(getApplicationContext());
        queue3.add(createComment);



    }

    boolean isShowing=false;
    public void showDialog() {


        if(!isShowing) {
            try {

                mView.show(getSupportFragmentManager(), "");
                isShowing=true;
            } catch (Exception e) {
                Log.d("exception", e.getMessage());
            }
        }
    }
    public void hideDialog() {

        try {
            if(isShowing) {
                mView.dismiss();
                isShowing=false;
            }
        }
        catch (Exception e)
        {
            Log.d("exception",e.getMessage());
        }
    }

    private void submitComment(HashMap<String, String> user) {

        showDialog();
        if(user.get(UserSessionManager.KEY_USERTYPE_ID).equals("2"))
        {
            userName=guetUserName.getText().toString();
        }
        StringRequest createComment= new StringRequest(Request.Method.POST, getResources().getString(R.string.addComment),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                        try{
                            hideDialog();
                            Log.d("response",response);
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                etComment.setText("");
                                getComments();

                            } else {
                                Toast.makeText(getApplicationContext(),"Error from server ",Toast.LENGTH_SHORT).show();

                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                hideDialog();
                Toast.makeText(getApplicationContext(),"Error from server ",Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("event_id", eventId+"");
                params.put("user_id", userid+"");
                params.put("comment",etComment.getText().toString());
                params.put("user_name", userName);
                return params;
            }

        };
        RequestQueue queue3 = Volley.newRequestQueue(getApplicationContext());
        queue3.add(createComment);



    }

    private void deletComments(final Comments comments) {

        showDialog();

        StringRequest createComment= new StringRequest(Request.Method.POST, getResources().getString(R.string.deleteComment),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                        try{
                            hideDialog();
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                hideDialog();
                                Toast.makeText(getApplicationContext(),"Deleted",Toast.LENGTH_SHORT).show();
                                getComments();
                            } else {

                                hideDialog();
                                Toast.makeText(getApplicationContext(),"Error from server ",Toast.LENGTH_SHORT).show();

                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                hideDialog();
                Toast.makeText(getApplicationContext(),"Error from server ",Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("event_id", eventId+"");
                params.put("user_id", userid+"");
                params.put("id", comments.getId()+"");
                return params;
            }

        };
        RequestQueue queue3 = Volley.newRequestQueue(getApplicationContext());
        queue3.add(createComment);



    }

     CommentsAdapter commentsAdapter;
    private void refreshComments(List<Comments> comments) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvComments.setLayoutManager(linearLayoutManager);
        rvComments.setHasFixedSize(true);

        commentsAdapter = new CommentsAdapter(getApplicationContext(),comments, new Clickcb() {
            @Override
            public void onclick(Object object) {


                if(object instanceof Comments)
                {
                    deletComments((Comments)object);
                }


            }
        });
        rvComments.setAdapter(commentsAdapter);
        rvComments.setOverScrollMode(View.OVER_SCROLL_NEVER);
        rvComments.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    commentsAdapter.setAnimationsLocked(true);
                }
            }
        });


    }


    private void setupSendCommentButton() {
        btnSendComment.setOnSendClickListener(this);
    }

    private void startIntroAnimation() {
        contentRoot.setScaleY(0.1f);
        contentRoot.setPivotY(drawingStartLocation);
        llAddComment.setTranslationY(200);

        contentRoot.animate()
                .scaleY(1)
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animateContent();
                    }
                })
                .start();
    }

    private void animateContent() {
        llAddComment.animate().translationY(0)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(200)
                .start();
    }

    @Override
    public void onBackPressed() {
        contentRoot.animate()
                .translationY(Utils.getScreenHeight(this))
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        CommentsActivity.super.onBackPressed();
                        overridePendingTransition(0, 0);
                    }
                })
                .start();
    }

    @Override
    public void onSendClickListener(View v) {
        if (validateComment()) {

            submitComment(user);
        }
    }



    private boolean validateComment() {
        if (TextUtils.isEmpty(etComment.getText())) {
            btnSendComment.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake_error));
            return false;
        }

        return true;
    }



}
