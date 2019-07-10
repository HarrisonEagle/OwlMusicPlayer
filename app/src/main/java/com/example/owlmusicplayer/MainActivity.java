package com.example.owlmusicplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    public static List musiclst;
    final String MEDIA_PATH = Environment.getExternalStorageDirectory()+"";
    public static MediaPlayer mediaplayer=new MediaPlayer();
    public static String title;
    public static int currentplaying;
    public static ImageButton play;
    public static TextView musictitle;




    public List<String> getPlayList(){
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION
        };
        final String sortOrder = MediaStore.Audio.AudioColumns.TITLE + " COLLATE LOCALIZED ASC";
        List<String> mp3Files = new ArrayList<>();

        Cursor cursor = null;
        try {
            Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            cursor = getContentResolver().query(uri, projection, selection, null, sortOrder);
            if( cursor != null){
                cursor.moveToFirst();

                while( !cursor.isAfterLast() ){
                    String title = cursor.getString(0);
                    String artist = cursor.getString(1);
                    String path = cursor.getString(2);
                    String displayName  = cursor.getString(3);
                    String songDuration = cursor.getString(4);
                    cursor.moveToNext();
                    if(path != null && path.endsWith(".mp3")) {
                        mp3Files.add(path);
                    }
                }

            }

            // print to see list of mp3 files


        } catch (Exception e) {
            Log.e("TAG", e.toString());
        }finally{
            if( cursor != null){
                cursor.close();
            }
        }
        return mp3Files;


    }

    public void onRequestPermissionsResult(int paramInt, @NonNull String[] paramArrayOfString, @NonNull int[] paramArrayOfInt)
    {
        super.onRequestPermissionsResult(paramInt, paramArrayOfString, paramArrayOfInt);
        if (paramInt == 1) {
            if ((paramArrayOfInt.length > 0) && (paramArrayOfInt[0] == 0)) {
                Toast.makeText(getApplicationContext(), "You grant write external storage permission. ", 1).show();
            } else {
                Toast.makeText(getApplicationContext(), "You denied write external storage permission.", 1).show();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        musiclst=getPlayList();

        musictitle=findViewById(R.id.musictitle);
        for(int i=0;i<musiclst.size();i++){
            Log.d("filelist",(String) musiclst.get(i));
        }
        if(Build.VERSION.SDK_INT >= 23){
            //APIレベル23以降の機種の場合の処理
            ActivityCompat.requestPermissions(this, new String[] { "android.permission.WRITE_EXTERNAL_STORAGE" }, 1);

        }

        final ListView musiclist=findViewById(R.id.musiclist);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,musiclst);
        musiclist.setAdapter(arrayAdapter);

        play=findViewById(R.id.play);

        musiclist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filePath=musiclst.get(position).toString();
                Log.d("file",filePath);
                try
                {
                    mediaplayer.reset();
                    mediaplayer.setDataSource(filePath);
                    // 音量調整を端末のボタンに任せる
                    setVolumeControlStream(AudioManager.STREAM_MUSIC);
                    mediaplayer.prepare();
                    mediaplayer.start();
                    play.setImageResource(R.drawable.pause);
                    musictitle.setText(filePath);
                    title=filePath;
                    currentplaying = position;
                    Log.d("log","played");
                } catch (IOException e1) {
                    e1.printStackTrace();
                    Log.d("error", e1.toString());
                }
            }
        });

        LinearLayout musicbar=findViewById(R.id.playlayout);

        musicbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,musicplayer.class);
                startActivity(intent);
            }
        });



        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaplayer.isPlaying()){
                 mediaplayer.pause();
                 play.setImageResource(R.drawable.play);
                }else{
                    mediaplayer.start();
                    play.setImageResource(R.drawable.pause);
                }
            }
        });








    }
}
