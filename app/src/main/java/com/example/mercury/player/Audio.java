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
import android.widget.Toast;

import org.w3c.dom.Text;

import static com.example.mercury.player.R.raw.mysound;


public class Audio extends Activity {

    private MediaPlayer mediaPlayer;
    private static final String TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);
        final Button btn = (Button) findViewById(R.id.bPlay);
        final TextView status = (TextView) findViewById(R.id.StatusView);

        btn.setOnClickListener(new View.OnClickListener() {

                                   @Override
                                   public void onClick(View v) {

                                        //Проверка, что - бы песня не запускалась несколько раз при нажатии Play(не уверен в ее работоспособности, надо будет потестить)
                                        //Создаем новый плеер, задаем параметры и
                                       if (mediaPlayer == null) {
                                           Uri path = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.mysound);
                                           mediaPlayer = MediaPlayer.create(Audio.this, path);

                                           //Создаем слушатель, который оповестит нас, когда песня закончится
                                           mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                                           //Обработка момента, когда песня заканчивается.
                                               @Override
                                               public void onCompletion(MediaPlayer mp) {
                                                   btn.setText("Play");
                                                   status.setText("Status:Idle");

                                               }
                                           });
                                       }

                                       //Ставим на паузу
                                       if (mediaPlayer.isPlaying()) {
                                           btn.setText("Play");
                                           status.setText("Status:Pause");
                                           mediaPlayer.pause();
                                       }
                                       //Возобновление
                                       else {
                                           btn.setText("Pause");
                                           status.setText("Status:Play");
                                           mediaPlayer.start();

                                       }
                                   }


                               }

        );

    }


    @Override
    //Пользователь выходит из приложения, зачищаем и освобождаем всё, что только можем.
    //При сворачивании музыка продолжает играть.
    protected void onDestroy() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;

        }


        super.onDestroy();
    }

}
