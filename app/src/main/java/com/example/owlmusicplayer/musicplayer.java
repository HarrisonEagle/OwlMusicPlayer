package com.example.owlmusicplayer;

import android.media.AudioManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.os.Handler;
import android.os.Message;
import java.io.IOException;

public class musicplayer extends AppCompatActivity {
    public static TextView title;
    public static SeekBar seekbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);

        Button close=findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title=findViewById(R.id.title);
        title.setText(MainActivity.title);

        ImageButton left=findViewById(R.id.left);
        final ImageButton play=findViewById(R.id.play);
        if(MainActivity.mediaplayer.isPlaying()){
            play.setImageResource(R.drawable.pause);
        }
        ImageButton right=findViewById(R.id.right);

        int max=MainActivity.mediaplayer.getDuration();
        int current=MainActivity.mediaplayer.getCurrentPosition();

        seekbar=findViewById(R.id.seekbar);
        seekbar.setMax(max);
        seekbar.setProgress(current);



        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    MainActivity.mediaplayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.currentplaying!=0){
                    MainActivity.currentplaying-=1;
                }
                String filePath=MainActivity.musiclst.get(MainActivity.currentplaying).toString();
                Log.d("file",filePath);
                try
                {
                    MainActivity.mediaplayer.reset();
                    MainActivity.mediaplayer.setDataSource(filePath);
                    // 音量調整を端末のボタンに任せる
                    setVolumeControlStream(AudioManager.STREAM_MUSIC);
                    MainActivity.mediaplayer.prepare();
                    MainActivity.mediaplayer.start();
                    MainActivity.play.setImageResource(R.drawable.pause);
                    play.setImageResource(R.drawable.pause);
                    MainActivity.musictitle.setText(filePath);
                    MainActivity.title=filePath;
                    title.setText(filePath);
                    Log.d("log","played");
                } catch (IOException e1) {
                    e1.printStackTrace();
                    Log.d("error", e1.toString());
                }
            }
        });


        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(MainActivity.currentplaying+1!=MainActivity.musiclst.size()){
                   MainActivity.currentplaying+=1;
               }
                String filePath=MainActivity.musiclst.get(MainActivity.currentplaying).toString();
                Log.d("file",filePath);
                try
                {
                    MainActivity.mediaplayer.reset();
                    MainActivity.mediaplayer.setDataSource(filePath);
                    // 音量調整を端末のボタンに任せる
                    setVolumeControlStream(AudioManager.STREAM_MUSIC);
                    MainActivity.mediaplayer.prepare();
                    MainActivity.mediaplayer.start();
                    MainActivity.play.setImageResource(R.drawable.pause);
                    play.setImageResource(R.drawable.pause);
                    MainActivity.musictitle.setText(filePath);
                    title.setText(filePath);
                    MainActivity.title=filePath;
                    MainActivity.currentplaying = MainActivity.currentplaying;
                    Log.d("log","played");
                } catch (IOException e1) {
                    e1.printStackTrace();
                    Log.d("error", e1.toString());
                }

            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.mediaplayer.isPlaying()){
                    MainActivity.mediaplayer.pause();
                    MainActivity.play.setImageResource(R.drawable.play);
                    play.setImageResource(R.drawable.play);
                }else{
                    MainActivity.mediaplayer.start();
                    MainActivity.play.setImageResource(R.drawable.pause);
                    play.setImageResource(R.drawable.pause);

                }
            }
        });









        new Thread(new Runnable() {
            @Override
            public void run() {
                while (MainActivity.mediaplayer != null) {
                    try {
                        Message msg = new Message();
                        msg.what = MainActivity.mediaplayer.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}
                }
            }
        }).start();







    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;

            // 再生位置を更新
            seekbar.setProgress(currentPosition);

        }
    };

    private Handler mSeekbarUpdateHandler = new Handler();
    private Runnable mUpdateSeekbar = new Runnable() {
        @Override
        public void run() {
            seekbar.setProgress(MainActivity.mediaplayer.getCurrentPosition());
            mSeekbarUpdateHandler.postDelayed(this, 50);
        }
    };



}
