package com.example.mercury.player;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


public class Audio extends Activity implements SeekBar.OnSeekBarChangeListener {

    private MediaPlayer mediaPlayer;
    private static final String TAG = "pff";
    private SeekBar bar;
    private Thread updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);
        final Button btn = (Button) findViewById(R.id.bPlay);
        final TextView status = (TextView) findViewById(R.id.StatusView);
        bar = (SeekBar) findViewById(R.id.seekBar);

        //Проверка, что - бы песня не запускалась несколько раз при нажатии Play
        //Создаем новый плеер, задаем параметры
        if (mediaPlayer == null) {
            Log.i(TAG, "Player == null");
            Uri path = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.mysound);
            mediaPlayer = MediaPlayer.create(Audio.this, path);
            //Создаем слушатель, который оповестит нас, когда песня закончится
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                //Обработка момента, когда песня заканчивается.
                @Override
                public void onCompletion(MediaPlayer mp) {
                    btn.setText("Play");
                    status.setText("Status:Idle");
                    bar.setProgress(0);
                }
            });
        }

        bar.setOnSeekBarChangeListener(this);

        //обновляем слайдер в отдельном потоке
        updateSeekBar = new Thread() {
            @Override
            public void run() {
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = 0;
                while (currentPosition < totalDuration) {
                    try {
                        sleep(500);
                        currentPosition = mediaPlayer.getCurrentPosition();
                        bar.setProgress(currentPosition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //Когда песня заканчивается, закрываем поток.Позволяем обновить бар.
                if (totalDuration == currentPosition) {
                    Thread.interrupted();

                }
            }
        };
        updateSeekBar.start();
        bar.setMax(mediaPlayer.getDuration());


        //слушаем постукивания на кнопку, делаем выводы:
        btn.setOnClickListener(new View.OnClickListener() {

                                   @Override
                                   public void onClick(View v) {
                                       Log.i(TAG, "OnClick");

                                       //Ставим на паузу
                                       if (mediaPlayer.isPlaying()) {
                                           btn.setText("Play");
                                           status.setText("Status:Pause");
                                           mediaPlayer.pause();
                                           Log.i(TAG, "Button:Pause");

                                       }
                                       //Возобновляем
                                       else {
                                           Log.i(TAG, "Button:Play");
                                           btn.setText("Pause");
                                           status.setText("Status:Play");
                                           mediaPlayer.start();
                                       }
                                   }
                               }

        );

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.i(TAG, "onProgressChanged");

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.i(TAG, "OnStartTrackingTouch");

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.i(TAG, "OnStopTrackingTouch");
        mediaPlayer.seekTo(seekBar.getProgress());
    }

    @Override
    //Пользователь выходит из приложения, зачищаем и освобождаем всё, что только можем.
    //При сворачивании музыка продолжает играть.
    protected void onDestroy() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        //Придумай, как закрыть поток!
        }


        super.onDestroy();
    }


}