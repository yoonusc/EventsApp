package mycodlabs.instapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.roger.catloadinglibrary.CatLoadingView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import mycodlabs.adapters.Clickcb;
import mycodlabs.events.NavBar;
import mycodlabs.events.R;
import mycodlabs.events.UserSessionManager;
import mycodlabs.utils.Events;
import mycodlabs.utils.GlobalBus;
import mycodlabs.utils.PickerDialogFragment;


/**
 * Created by Miroslaw Stanek on 21.02.15.
 */
public class PublishActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String ARG_TAKEN_PHOTO_URI = "arg_taken_photo_uri";

  //  @BindView(R.id.tbFollowers)
    ToggleButton tbFollowers;
  //  @BindView(R.id.tbDirect)
    ToggleButton tbDirect;
    // @BindView(R.id.ivPhoto)
    ImageView ivPhoto;

    Button btnSignin;
    ImageView imageCalender;
    ImageView imageMap;
    ImageView imageTime;
    ImageView imageGallery;
    ImageView imageDuration;
    TextView textEventLocation;
    TextView textEventDate;
    TextView textEventTime;
    EditText eventName;
    EditText eventDescription;
    EditText eventVedioUrl;
    Spinner eventCategory;
    CatLoadingView mView;


    UserSessionManager session;
    RequestQueue queue;

    private boolean propagatingToggleState = false;
    private Uri photoUri;
    private int photoSize;
    String duration,eventDuration;
    PrefManager preferenceManager;
    TextView editEventDuration;

    public static void openWithPhotoUri(Activity openingActivity) {
        Intent intent = new Intent(openingActivity, PublishActivity.class);
        openingActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_publish);
        imageTime=(ImageView)findViewById(R.id.imageTime) ;
        imageCalender=(ImageView)findViewById(R.id.imagecalender);
        imageMap=(ImageView)findViewById(R.id.imageMap);
        imageDuration=(ImageView)findViewById(R.id.imageDuration);
        imageGallery=(ImageView)findViewById(R.id.imageGallery);
        eventCategory=(Spinner)findViewById(R.id.eventCategory) ;
        imageTime.setOnClickListener(this);
        imageCalender.setOnClickListener(this);
        imageMap.setOnClickListener(this);
        imageGallery.setOnClickListener(this);
        imageTime.setOnClickListener(this);
        imageCalender.setOnClickListener(this);
        imageDuration.setOnClickListener(this);
        textEventLocation=(TextView)findViewById(R.id.textEventLocation);
        textEventDate=(TextView)findViewById(R.id.textViewEventDate);
        textEventTime=(TextView)findViewById(R.id.texEventTime);
        eventName=(EditText)findViewById(R.id.evntName);
        eventDescription=(EditText)findViewById(R.id.evtDescription);
        editEventDuration=(TextView) findViewById(R.id.texEventDuration);
        imageDuration=(ImageView)findViewById(R.id.imageDuration) ;
        imageDuration.setOnClickListener(this);
        eventVedioUrl=(EditText)findViewById(R.id.evntVediourl);
        btnSignin=(Button)findViewById(R.id.singin);
        btnSignin.setOnClickListener(this);
        queue = Volley.newRequestQueue(getApplicationContext());
        session=new UserSessionManager(getApplicationContext());
        mView = new CatLoadingView();
        preferenceManager =new PrefManager(this);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(),connectionResult.getErrorMessage(),Toast.LENGTH_LONG).show();
                    }
                })
                .build();
        mSelectedImagesContainer = (ViewGroup) findViewById(R.id.selected_photos_container);
        getcategory();
        photoSize = getResources().getDimensionPixelSize(R.dimen.publish_photo_thumbnail_size);
//
//        if (savedInstanceState == null) {
//            photoUri = getIntent().getParcelableExtra(ARG_TAKEN_PHOTO_URI);
//        } else {
//            photoUri = savedInstanceState.getParcelable(ARG_TAKEN_PHOTO_URI);
//        }
//        updateStatusBarColor();
//
//        ivPhoto.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                ivPhoto.getViewTreeObserver().removeOnPreDrawListener(this);
//                loadThumbnailPhoto();
//                return true;
//            }
//        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        GlobalBus.getBus().register(this);
    }


    @Subscribe
    public void getMessage(Events.FragmentActivityMessage fragmentActivityMessage) {

        editEventDuration.setText(fragmentActivityMessage.getMessage());

    }
        @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
            GlobalBus.getBus().unregister(this);
        super.onStop();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void updateStatusBarColor() {
        if (Utils.isAndroid5()) {
            getWindow().setStatusBarColor(0xff888888);
        }
    }

//    private void loadThumbnailPhoto() {
//        ivPhoto.setScaleX(0);
//        ivPhoto.setScaleY(0);
//        Picasso.with(this)
//                .load(photoUri)
//                .centerCrop()
//                .resize(photoSize, photoSize)
//                .into(ivPhoto, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        ivPhoto.animate()
//                                .scaleX(1.f).scaleY(1.f)
//                                .setInterpolator(new OvershootInterpolator())
//                                .setDuration(400)
//                                .setStartDelay(200)
//                                .start();
//                    }
//
//                    @Override
//                    public void onError() {
//                    }
//                });
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


    return true;
    }

    private void bringMainActivityToTop() {
        Intent intent = new Intent(this, NavBar.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setAction(NavBar.ACTION_SHOW_LOADING_ITEM);
        startActivity(intent);
    }





    private void getcategory(){
        //Creating a string request
        showDialog();
        StringRequest categoryRequest = new StringRequest(Request.Method.GET, getResources().getString(R.string.categoryRegister_url),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonResponse = null;
                        hideDialog();
                        try {
                            //Parsing the fetched Json String to JSON Object
                            jsonResponse = new JSONObject(response);
                            JSONArray result = jsonResponse.getJSONArray("category");
                            ArrayList<String> categorys = new ArrayList<String>();
                            for(int i=0;i<result.length();i++){

                                //Getting json object
                                JSONObject obj = result.getJSONObject(i);

                                //Adding the name of the student to array list
                                categorys.add(obj.getString("name"));

                            }
                            eventCategory.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner, categorys));
                            eventCategory.setSelection(0);
                            //Calling method getStudents to get the students from the JSON Array
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            hideDialog();
                        }
                        catch (Exception e)
                        {
                           Log.d("tag" ,e.getMessage());
                        }
                    }
                });

        //Creating a request queue


        //Adding request to the queue
        queue.add(categoryRequest);

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_TAKEN_PHOTO_URI, photoUri);
    }
    ArrayList<String> image_uris = new ArrayList<String>();
    private ViewGroup mSelectedImagesContainer;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    final String[] selectedDate = new String[1];
    final Calendar c = Calendar.getInstance();
    final String[] selectedTime = new String[1];
    private GoogleApiClient mGoogleApiClient;
    int PLACE_PICKER_REQUEST = 1;

    public void showDatePicker()
    {


        DatePickerDialog dd = new DatePickerDialog(PublishActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        try {
                            Calendar cal = Calendar.getInstance();
                            cal.set(Calendar.YEAR, year);
                            cal.set(Calendar.MONTH, monthOfYear);
                            cal.set(Calendar.DATE, dayOfMonth);
                            Date selected = cal.getTime();




                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            String dateInString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            Date date = formatter.parse(dateInString);



                            formatter = new SimpleDateFormat("yyyy-MM-dd");

                            selectedDate[0] = formatter.format(date).toString();
                            textEventDate.setText(selectedDate[0]);


                        } catch (Exception ex) {

                        }


                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dd.show();


    }

    public void showTimePicker()
    {
        TimePickerDialog td = new TimePickerDialog(PublishActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        try {
                            Calendar cal = Calendar.getInstance();
                            cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            cal.set(Calendar.MINUTE, minute);
                            cal.set(Calendar.SECOND, 0);
                            Date selected = cal.getTime();

                            String dtStart = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
//                            textEventTime.setText(new SimpleDateFormat("hh:mm aa").format(selected));



                            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                            java.sql.Time timeValue = new java.sql.Time(format.parse(dtStart).getTime());
                            selectedTime[0] = String.valueOf(timeValue);
                            textEventTime.setText(selectedTime[0]);

                        } catch (Exception ex) {
                        }
                    }
                },
                c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                android.text.format.DateFormat.is24HourFormat(getApplicationContext())
        );
        td.show();
    }

    public void showLocationPicker()
    {
        LatLngBounds latLngBounds = new LatLngBounds(new LatLng(47.64299816, -122.14351988),
                new LatLng(47.64299816, -122.14351988));
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        builder.setLatLngBounds(latLngBounds);
        try {
            startActivityForResult(builder.build(PublishActivity.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            Log.d("bugggggggggggggg",e.getMessage());
            e.printStackTrace();
        }

    }


    public static String extractVideoId(String ytUrl) {
        String vId = null;
        Pattern pattern = Pattern.compile(".*(?:youtu.be\\/|v\\/|u\\/\\w\\/|embed\\/|watch\\?v=)([^#\\&\\?]*).*");
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()){
            vId = matcher.group(1);
        }
        return vId;
    }
    public void signIn()
    {

        final String event_name = eventName.getText().toString().trim();
        final String venue = textEventLocation.getText().toString().trim();
        final int category = eventCategory.getSelectedItemPosition() ;

        final String details = eventDescription.getText().toString().trim();
        final String time = textEventDate.getText()+ " " + textEventTime.getText();
        final String videourl = eventVedioUrl.getText().toString().trim();
                     duration=editEventDuration.getText().toString().trim();
        if(extractVideoId(videourl)==null)
        {
            Log.d("url",videourl);
            eventVedioUrl.setError("Invalid Link");
        }

               Log.e("d",time);
        HashMap<String, String> user = session.getUserDetails();
        final String usertype = user.get(UserSessionManager.KEY_USERTYPE_ID);

        // get name
        final String user_id = user.get(UserSessionManager.KEY_USER_ID);
        final String user_name = user.get(UserSessionManager.KEY_NAME);
        if (event_name.equals("")){
            Toast.makeText(getApplicationContext(), "Please specify Name", Toast.LENGTH_SHORT).show();
        }
        else if (venue.equals("")){
            Toast.makeText(getApplicationContext(), "Please specify Venue", Toast.LENGTH_SHORT).show();
        }
        else if (details.equals("")){
            Toast.makeText(getApplicationContext(), "Please specify Details", Toast.LENGTH_SHORT).show();
        }
        else{

            showDialog();
            StringRequest registerRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.addEvent_url),
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {

                            try{
                                Log.d("response", Utils.fromHtml(response.toString()));
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {

                                     hideDialog();
                                    Toast.makeText(getApplicationContext(), "Event " + event_name + " was added Successfully ", Toast.LENGTH_SHORT).show();
                                    bringMainActivityToTop();
                                    //startActivity(new Intent(getActivity(), NavBar.class));

                                } else {
                                    hideDialog();
                                    Toast.makeText(getApplicationContext(), "Event adding Failed", Toast.LENGTH_SHORT).show();

                                }
                            }catch (JSONException e) {
                                hideDialog();
                                Toast.makeText(getApplicationContext(),"Invalid Response",Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();
                    Toast.makeText(getApplicationContext(),error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            }
            ) {
                @Override
                protected Map<String, String> getParams()
                {


                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("name", event_name);
                    params.put("user_id",user_id);
                    params.put("user",user_name);
                    params.put("category_id", category+"");
                    params.put("usertype_id", usertype);
                    params.put("venue",venue);
                    params.put("time",time);
                    params.put("details",details);
                    params.put("video",videourl);
                    params.put("duration",duration);
                    if(jsonArray!=null) {
                        params.put("images", jsonArray.toString());
                        Log.d(getLocalClassName(), "getParams: "+jsonArray.toString());
                    }


                    return params;
                }

            };
            queue.add(registerRequest);
        }


    }
    private static final int REQUEST_CODE = 123;
    public void chooseImages()
    {

      image_uris.clear();

//// start multiple photos selector
//        Intent intent = new Intent(PublishActivity.this, ImagesSelectorActivity.class);
//// max number of images to be selected
//        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 5);
//        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 5);
//
//// min size of image which will be shown; to filter tiny images (mainly icons)
//        intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
//// show camera or not
//        intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
//// pass current selected images as the initial value
//        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, image_uris);
//// start the selector
//        startActivityForResult(intent, REQUEST_CODE);
        Intent intent = new Intent(this, AlbumSelectActivity.class);
//set limit on number of images that can be selected, default is 10
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 5);
        startActivityForResult(intent, Constants.REQUEST_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(intent, this);
                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());
                String latitude = String.valueOf(place.getLatLng().latitude);
                String longitude = String.valueOf(place.getLatLng().longitude);
                String address = String.format("%s", place.getAddress());
                stBuilder.append("Name: ");
                stBuilder.append(placename);
                stBuilder.append("\n");
                stBuilder.append("Latitude: ");
                stBuilder.append(latitude);
                stBuilder.append("\n");
                stBuilder.append("Logitude: ");
                stBuilder.append(longitude);
                stBuilder.append("\n");
                stBuilder.append("Address: ");
                stBuilder.append(address);
                textEventLocation.setText(latitude+"\n"+longitude+"\n"+placename + "\n" + address);
            }
        }


//        if (requestCode == REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                image_uris = intent.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
//                showMedia();
//
//            }

            if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && intent != null) {
                ArrayList<Image> images = intent.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0, l = images.size(); i < l; i++) {
                    image_uris.add(images.get(i).path);
                }
                showMedia();

            }
//
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == INTENT_REQUEST_GET_IMAGES) {
//
//                image_uris = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
//
//                if (image_uris != null) {
//                    showMedia();
//                }
//
//
//            }
//        }
        }


    JSONArray jsonArray;

    private void showMedia() {
        jsonArray=new JSONArray();
        // Remove all views before
        // adding the new ones.
        mSelectedImagesContainer.removeAllViews();
        if (image_uris.size() >= 1) {
            mSelectedImagesContainer.setVisibility(View.VISIBLE);
        }

        int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        int htpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());


        for (String uri : image_uris) {

//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
//                jsonObject.put("image",Utils.getStringImage(bitmap));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            Glide.with(this)
                    .asBitmap().load(uri)
                    .into(new SimpleTarget<Bitmap>(250,250) {


                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            try {
                                jsonArray.put(new JSONObject().put("image",Utils.getStringImage(resource)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

            View imageHolder = LayoutInflater.from(this).inflate(R.layout.image_item, null);
            ImageView thumbnail = (ImageView) imageHolder.findViewById(R.id.media_image);

            Glide.with(this)
                    .load(uri.toString())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.noimg)
                            .fitCenter())
                            .into(thumbnail);


            mSelectedImagesContainer.addView(imageHolder);

            thumbnail.setLayoutParams(new FrameLayout.LayoutParams(wdpx, htpx));


        }
    }

    public void showDialog() {
        mView.show(getSupportFragmentManager(), "");
    }
    public void hideDialog() {
        mView.dismiss();
    }



    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.imagecalender:

                showDatePicker();

                break;
            case R.id.imageGallery:
                   chooseImages();
                break;
            case R.id.imageTime:

                 showTimePicker();
                break;
            case R.id.imageMap:
                     showLocationPicker();
                   break;

            case R.id.singin:
                  signIn();
                   break;
            case R.id.imageDuration:


                showDurationPicekr();

                break;



        }

    }

    private void showDurationPicekr() {

        new PickerDialogFragment().geInstance(new Clickcb() {
            @Override
            public void onclick(Object object) {

                if(object instanceof String)
                {
                 editEventDuration.setText(object.toString());
                }
            }
        }).show(getFragmentManager(), "dialog");
    }
/*
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(filePath);

                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
                entity.addPart("website",
                        new StringBody("www.androidhive.info"));
                entity.addPart("email", new StringBody("abc@gmail.com"));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);

            // showing the server response in an alert dialog
            showAlert(result);

            super.onPostExecute(result);
        }

    }
*/

//    @OnCheckedChanged(R.id.tbFollowers)
//    public void onFollowersCheckedChange(boolean checked) {
//        if (!propagatingToggleState) {
//            propagatingToggleState = true;
//            tbDirect.setChecked(!checked);
//            propagatingToggleState = false;
//        }
//    }
//
//    @OnCheckedChanged(R.id.tbDirect)
//    public void onDirectCheckedChange(boolean checked) {
//        if (!propagatingToggleState) {
//            propagatingToggleState = true;
//            tbFollowers.setChecked(!checked);
//            propagatingToggleState = false;
//        }
//    }
}
