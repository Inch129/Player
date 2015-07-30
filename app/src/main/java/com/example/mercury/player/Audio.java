package com.example.mercury.player;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import static com.example.mercury.player.R.raw.mysound;


public class Audio extends Activity {

    private MediaPlayer mediaPlayer;
    private static final String TAG = "myLogs";
    private boolean orientation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);
        final Button btn = (Button) findViewById(R.id.bPlay);
        final TextView status = (TextView) findViewById(R.id.StatusView);
        if (savedInstanceState != null) {
            orientation = savedInstanceState.getBoolean("orientation", false);
        }
        if (orientation == false) {
            btn.setOnClickListener(new View.OnClickListener() {
                                       long max;


                                       @Override
                                       public void onClick(View v) {

                                           if (mediaPlayer == null) {
                                               Uri path = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.mysound);
                                               mediaPlayer = MediaPlayer.create(Audio.this, path);
                                               max = mediaPlayer.getDuration();
                                               mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                   @Override
                                                   public void onCompletion(MediaPlayer mp) {

                                                       btn.setText("Play");
                                                       status.setText("Status:Idle");

                                                   }
                                               });
                                           }


                                           if (mediaPlayer.isPlaying()) {
                                               btn.setText("Play");
                                               status.setText("Status:Pause");
                                               mediaPlayer.pause();
                                           } else {
                                               btn.setText("Pause");
                                               status.setText("Status:Play");
                                               mediaPlayer.start();

                                           }

                                           if (mediaPlayer.getCurrentPosition() == max) {
                                               mediaPlayer.reset();
                                               status.setText("Status:Idle");
                                               btn.setText("Play");
                                           }
                                       }


                                   }

            );

        }
    }
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("orientation", true);


    }
    @Override
    //реализация выключения музыки
    protected void onDestroy() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
             mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;

        }


        super.onDestroy();
    }

}
