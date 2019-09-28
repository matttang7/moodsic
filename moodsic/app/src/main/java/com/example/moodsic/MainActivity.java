package com.example.moodsic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;
import com.example.moodsic.R;
import android.os.Bundle;

import java.net.HttpURLConnection;
import java.net.URI;
import org.json.JSONArray;
import org.json.JSONObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import com.microsoft.projectoxford.face.*;
import com.microsoft.projectoxford.face.contract.*;


public class MainActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.happysong);
        mediaPlayer.start();
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_main);



        final Button happy = findViewById(R.id.btn_happy);
        final Button sad = findViewById(R.id.btn_sad);
        final Button angry = findViewById(R.id.btn_angry);
        final Button worried = findViewById(R.id.btn_worried);
        final Button pause = findViewById(R.id.pause);
        final Button picture = findViewById(R.id.picture);

        final int[] length = {0};
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    pause.setText("play");
                }
                else if(!mediaPlayer.isPlaying()) {
                    length[0] = mediaPlayer.getCurrentPosition();
                    mediaPlayer.seekTo(length[0]);
                    mediaPlayer.start();
                    pause.setText("pause");
                }
            }
        });

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Your directory with files to be deleted
                String sdcard = Environment.getExternalStorageDirectory() + "/Android/data/com.example.moodsic/files/Pictures/";
                // go to your directory
                File fileList = new File(sdcard);
                //check if dir is not null
                if (fileList != null) {
                    // so we can list all files
                    File[] filenames = fileList.listFiles();
                    // loop through each file and delete
                    for (File tmpf : filenames) {
                        System.out.println("REACHED");
                        System.out.println(tmpf);
                        tmpf.delete();
                    }
                }
                dispatchTakePictureIntent();
            }
        });

        happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, happy);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        buttonClicked(item);
                        happy.setText("Happy\n" + item.getTitle());
                        return true;
                    }
                });

                popup.show();//showing popup menu
//                String currentEmotion = getEmotion();

            }
        });

        sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, sad);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        buttonClicked(item);
                        sad.setText("Sad\n" + item.getTitle());
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });

        angry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, angry);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        buttonClicked(item);
                        angry.setText("Angry\n" + item.getTitle());
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });

        worried.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, worried);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        buttonClicked(item);
                        worried.setText("Worried\n" + item.getTitle());
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });
    }

    // Show toast
    public void buttonClicked(MenuItem item) {
        Toast.makeText(MainActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private String getEmotion() {
        //Replace <Subscription Key> with your valid subscription key.
        final String subscriptionKey = "048296827ecd4c0a8c98ad2cb189363b";

        final String uriBase =
                "https://jihoonk2.cognitiveservices.azure.com/face/v1.0/detect";

        final String imageWithFaces =
                "{\"url\":\"https://upload.wikimedia.org/wikipedia/commons/c/c3/RH_Louise_Lillian_Gish.jpg\"}";

        final String faceAttributes =
                "emotion";

        try {

            URL url = new URL(uriBase);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");

            // Request parameters. All of them are optional.
            Map<String, String> parameters = new HashMap<>();
            parameters.put("returnFaceId", "false");
            parameters.put("returnFaceLandmarks","false");
            parameters.put("returnFaceAttributes", faceAttributes);

            Log.d("JIHOON", "con = " + con);

            con.setDoOutput(true);

            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(getParamsString(parameters));

            // Request headers.
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey);

            // Request body.
            byte[] outputInBytes = imageWithFaces.getBytes("UTF-8");
            System.out.println(con);
            OutputStream os = con.getOutputStream();
            os.write(outputInBytes);
            os.close();

            // Execute the REST API call and get the response entity.
//            HttpResponse response = httpclient.execute(request);
//            HttpEntity entity = response.getEntity();

            System.out.println("Line 290 reached");
            if (os != null) {
                // Format and display the JSON response.
                System.out.println("JIHOON REST Response:\n");

                ByteArrayOutputStream baos = (ByteArrayOutputStream)os;
                String jsonString = new String(baos.toByteArray()).trim();
                JSONObject reader = new JSONObject(jsonString);
                double anger = reader.getJSONObject("faceAttribute").getJSONObject("emotion").getDouble("anger");
                double fear = reader.getJSONObject("faceAttribute").getJSONObject("emotion").getDouble("fear");
                double happiness = reader.getJSONObject("faceAttribute").getJSONObject("emotion").getDouble("happiness");
                double sadness = reader.getJSONObject("faceAttribute").getJSONObject("emotion").getDouble("sadness");

                double[] emotions = {anger, fear, happiness, sadness};
                double temp = anger;
                int val = 0;
                for (int i = 0; i < 4; i++){
                    if (emotions[i] > temp) {
                        temp = emotions[i];
                        val = i;
                    }
                }

                if (val == 0) return "angry";
                else if (val == 1) return "worried";
                else if (val == 2) return "happy";
                else return "sad";

//                System.out.print(anger);
//                if (jsonString.charAt(0) == '[') {
//                    JSONArray jsonArray = new JSONArray(jsonString);
//                    System.out.println(jsonArray.toString(2));
//                } else if (jsonString.charAt(0) == '{') {
//                    JSONObject jsonObject = new JSONObject(jsonString);
//                    System.out.println(jsonObject.toString(2));
//                } else {
//                    System.out.println(jsonString);
//                }

            }


        } catch (Exception e) {
            // Display error message.
            System.out.println(e.getMessage());
        }

        return "";
    }

    public static String getParamsString(Map<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }

}

