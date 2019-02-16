package mycodlabs.events;

import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.text.LocaleDisplayNames;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;
import android.util.Log;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.glide.slider.library.Animations.DescriptionAnimation;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.SliderTypes.BaseSliderView;
import com.glide.slider.library.SliderTypes.TextSliderView;
import com.glide.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerInitListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;
import com.roger.catloadinglibrary.CatLoadingView;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mycodlabs.adapters.Clickcb;
import mycodlabs.adapters.CommentsAdapter;
import mycodlabs.instapp.PublishActivity;
import mycodlabs.instapp.Utils;
import mycodlabs.utils.Events;
import mycodlabs.utils.FullScreenManager;


public class EventDisplayUser extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener ,View.OnClickListener , OnMapReadyCallback {

    private int event_id;
    private String name;
    private String time;
    private String venue;
    private String details;
    private int usertype_id;
    private String usertype;
    private int creator_id;
    private String creator;
    private int category_id;
    private String category;
    private String hours;

    TextView nametv;
    TextView datetv;
    TextView timetv;
    TextView venuetv;
    TextView organisertv;
    TextView detailstv;
    TextView categorytv;
    TextView textVideos;
    TextView textDuration;
    Button bookmark;
    Button addcal;
    List<Medais> medais=new ArrayList<>();
    UserSessionManager session;
    private static String user_id,userName,user_type;
    RequestQueue queue;
    CatLoadingView mView;
    private SliderLayout mDemoSlider;
    EditText gustUserName;
    EditText textComment;
    Button submitComment;
    RecyclerView recyclercomments;
    private FullScreenManager fullScreenManager = new FullScreenManager(this);
    String vediour="";
    private YouTubePlayer initializedPlayer;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_event_display_user);
        mDemoSlider = findViewById(R.id.slider);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
          mView=new CatLoadingView();
        queue = Volley.newRequestQueue(getApplicationContext());
        final LinearLayout layout = (LinearLayout)findViewById(R.id.buttonPanel);
//        ViewTreeObserver vto = layout.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//
//            @Override
//            public void onGlobalLayout() {
//                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) findViewById(R.id.datapanel).getLayoutParams();
//                mlp.setMargins(0,0,0,layout.getHeight());
//                layout.getViewTreeObserver().removeGlobalOnLayoutListener(
//                        this);
//            }
//        });


        session = new UserSessionManager(getApplicationContext());
        if(session.checkLogin())
            finish();
        HashMap<String, String> user = session.getUserDetails();
        user_id = user.get(UserSessionManager.KEY_USER_ID);
        userName= user.get(UserSessionManager.KEY_NAME);
        user_type=user.get(UserSessionManager.KEY_USERTYPE_ID);
      //  textComment=(EditText) findViewById(R.id.text_comment);
//        submitComment=(Button)findViewById(R.id.btn_submit_comment);
//        recyclercomments=(RecyclerView)findViewById(R.id.recyckerComment);
        nametv = (TextView) findViewById(R.id.tvname);
        datetv = (TextView) findViewById(R.id.tvDate);
        timetv = (TextView) findViewById(R.id.tvTime);
        venuetv = (TextView) findViewById(R.id.tvVenue);
        organisertv = (TextView) findViewById(R.id.tvOrganiser);
        detailstv = (TextView) findViewById(R.id.tvdetails);
        categorytv = (TextView) findViewById(R.id.tvCategory);
        bookmark = (Button) findViewById(R.id.tvBookmark);
        addcal = (Button) findViewById(R.id.tvAddCalender);
        textDuration=(TextView)findViewById(R.id.tvHours);
        //textVideos=(TextView)findViewById(R.id.textVideo);
//        gustUserName=(EditText)findViewById(R.id.text_gust_user_Name);
//        if(user_type.equals("2")) {
//            gustUserName.setVisibility(View.VISIBLE);
//            gustUserName.setText(userName);
//        }
//        else {
//            gustUserName.setVisibility(View.GONE);
//
//        }
//        submitComment.setOnClickListener(this);
        Intent intent = getIntent();
        event_id = intent.getIntExtra("event_id",0);
        name =  intent.getStringExtra("name");
        time =  intent.getStringExtra("time");
        venue =  intent.getStringExtra("venue");
        details =  intent.getStringExtra("details");
        usertype_id =  intent.getIntExtra("usertype_id",0);
        usertype =  intent.getStringExtra("usertype");
        creator_id =  intent.getIntExtra("creator_id",0);
        creator =  intent.getStringExtra("creator");
        category_id =  intent.getIntExtra("category_id",0);
        category =  intent.getStringExtra("category");
        hours =  intent.getStringExtra("duration");
        Date date = new Date();
        getImages(event_id,null);
        startPlaying();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        nametv.setText(name);
        detailstv.setText(details);
        datetv.setText(new SimpleDateFormat("dd MMM, yyyy").format(date));
        timetv.setText(new SimpleDateFormat("hh:mm aa").format(date));
        organisertv.setText("Organised By: " + creator);
        String[] data=venue.split("\\r?\\n");
        String addres="";
        if(data.length>=4)
        {
            addres=data[2]+","+data[3];
        }
        venuetv.setText(addres);
        categorytv.setText(category);
        textDuration.setText(hours);
        final Date finalDate = date;
        addcal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(user.get(UserSessionManager.KEY_USERTYPE_ID).equals("2"))
                {

                    Toast.makeText(getApplicationContext(),"Please Register to access ",Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_INSERT);
                    intent.setType("vnd.android.cursor.item/event");

                    // intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endTime);
                    // intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
                    // intent.putExtra(Events.RRULE, "FREQ=YEARLY");
                    intent.putExtra(CalendarContract.Events.TITLE, name);
                    intent.putExtra(CalendarContract.Events.DESCRIPTION, details);
                    intent.putExtra(CalendarContract.Events.EVENT_LOCATION, venue);

                    Calendar beginTime = Calendar.getInstance();
                    beginTime.setTime(finalDate);
                    long startMillis = beginTime.getTimeInMillis();
                    intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis);

                    startActivity(intent);
                }
            }
        });


        checkbookmark();
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.get(UserSessionManager.KEY_USERTYPE_ID).equals("2"))
                {

                    Toast.makeText(getApplicationContext(),"Please Register to access ",Toast.LENGTH_LONG).show();
                }
                else {
                    toggleBookmark();
                }

            }
        });



    }
    YouTubePlayerView youTubePlayerView;
    private void startPlaying() {

        youTubePlayerView= findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.initialize(new YouTubePlayerInitListener() {
            @Override
            public void onInitSuccess(final YouTubePlayer initializedYouTubePlayer) {
                initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady() {
                        String videoId = PublishActivity.extractVideoId(vediour.length()>0?vediour:"");
                        initializedYouTubePlayer.cueVideo(videoId, 0);
                    }
                });
                initializedPlayer=initializedYouTubePlayer;
            }
        }, true);


    }



    @Override
    public void onBackPressed() {
        if (youTubePlayerView.isFullScreen())
            youTubePlayerView.exitFullScreen();
        else
            super.onBackPressed();
        if(initializedPlayer!=null) {
            initializedPlayer.pause();
        }
    }



    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(initializedPlayer!=null) {
            initializedPlayer.pause();
        }
    }

    void showSlider()
    {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions
                .centerCrop()
         .diskCacheStrategy(DiskCacheStrategy.NONE)
          .placeholder(R.drawable.noimg)
           .error(R.drawable.noimg);

        for (Medais med:medais) {
            TextSliderView sliderView = new TextSliderView(this);
            // if you want show image only / without description text use DefaultSliderView instead

            // initialize SliderLayout
            if ("image".equals(med.getType())) {
                sliderView
                        .image(getResources().getString(R.string.base_url)+med.getPath())
                         //name +" By "+creator
                        .setRequestOption(requestOptions)
                        .setBackgroundColor(Color.WHITE)
                        .setProgressBarVisible(true)
                        .setOnSliderClickListener(this);

                //add your extra information
                sliderView.bundle(new Bundle());
                sliderView.getBundle().putString("extra","test");
                mDemoSlider.addSlider(sliderView);
            }
            else
            {
//                textVideos.setText();
                vediour=med.getPath();
            }
        }

        // set Slider Transition Animation
        // mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);

    }

    private void getImages(final int eventId , final DisplayEventList.OncompleteCallaback oncompleteCallaback) {

         showDialog();
        StringRequest eventRequest= new StringRequest(Request.Method.GET,getResources().getString(R.string.get_media)+String.valueOf(eventId),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try{
                            Log.d("response",response.toString());
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                             hideDialog();
                            if (success) {
                                medais.clear();
                                JSONArray array = jsonResponse.getJSONArray("media");
                                for (int i=0; i<array.length(); i++){

                                    JSONObject e = array.getJSONObject(i);
                                    Medais data = new Medais(e.getInt("id"),e.getInt("event_id"),e.getString("type"),e.getString("path"));//)//,e.getString("name"),e.getString("time"),e.getString("venue"),e.getString("details"),e.getInt("usertype_id"),e.getString("usertype"),e.getInt("creator_id"),e.getString("creator"),e.getInt("category_id"),e.getString("category"));
                                    medais.add(data);

                                }
                                showSlider();
                                if(oncompleteCallaback!=null) {
                                    oncompleteCallaback.oncomplete();

                                }


                            } else {

                                Toast.makeText(getApplicationContext(),"invalid response",Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(),"Error fetching image",Toast.LENGTH_LONG).show();
                hideDialog();


            }
        }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                return params;
            }

        };

        queue.add(eventRequest);


    }


    private void checkbookmark() {
        bookmark.setEnabled(false);
        StringRequest bookmarkcheckRequest= new StringRequest(Request.Method.POST, getResources().getString(R.string.getBookmark_url),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                        try{
                            Log.d("response",response);
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                bookmark.setEnabled(true);
                                bookmark.setText("Un-Bookmark");

                            } else {
                                bookmark.setEnabled(true);
                                bookmark.setText("Bookmark");
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
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("event_id", event_id+"");
                params.put("user_id", user_id);
                return params;
            }

        };
        RequestQueue queue2 = Volley.newRequestQueue(getApplicationContext());
        queue2.add(bookmarkcheckRequest);
    }

    private void toggleBookmark() {
        bookmark.setEnabled(false);
        StringRequest togglebookmarkRequest= new StringRequest(Request.Method.POST, getResources().getString(R.string.toggleBookmark_url),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                        try{
                            Log.d("response",response);
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                bookmark.setEnabled(true);
                                checkbookmark();

                            } else {
                                bookmark.setEnabled(true);
                                checkbookmark();
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
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("event_id", event_id+"");
                params.put("user_id", user_id);
                return params;
            }

        };
        RequestQueue queue3 = Volley.newRequestQueue(getApplicationContext());
        queue3.add(togglebookmarkRequest);
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

    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onClick(View view) {


    }

    /*private void submitComment(HashMap<String, String> user) {

         showDialog();
         if(user_type.equals("2"))
         {
           userName=gustUserName.getText().toString();
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
                             textComment.setText("");
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
                params.put("event_id", event_id+"");
                params.put("user_id", user_id);
                params.put("comment",textComment.getText().toString());
                params.put("user_name", userName);
                return params;
            }

        };
        RequestQueue queue3 = Volley.newRequestQueue(getApplicationContext());
        queue3.add(createComment);



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
                params.put("event_id", event_id+"");
                params.put("user_id", user_id);
                params.put("comment",textComment.getText().toString());
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
                params.put("event_id", event_id+"");
                params.put("user_id", user_id);
                params.put("id", comments.getId()+"");
                return params;
            }

        };
        RequestQueue queue3 = Volley.newRequestQueue(getApplicationContext());
        queue3.add(createComment);



    }

    CommentsAdapter commentsAdapter;
    private void refreshComments(List<Comments> comments) {

     recyclercomments.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclercomments.addItemDecoration(
                new DividerItemDecoration(getApplicationContext(), null));
     commentsAdapter=new CommentsAdapter(getApplicationContext(),comments, new Clickcb() {
         @Override
         public void onclick(Object object) {


             if(object instanceof Comments)
             {
                 deletComments((Comments)object);
             }


         }
     });

     recyclercomments.setAdapter(commentsAdapter);

    }*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        double lat=33.852;
       double logit=151.211;
        String lines[] = venue.split("\\r?\\n");
         String address="";
        try
        {
            lat=Double.parseDouble(lines[0]);
            logit=Double.parseDouble(lines[1]);
            address=lines[2]+"\n"+lines[3];
        }
        catch (Exception e)
        {
           Log.d("Exception","converting failed");
        }
        LatLng sydney = new LatLng(lat, logit);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title(address));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
