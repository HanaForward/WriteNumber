package cn.hana.writenumber;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends Activity {

    private Button Music_btn;
    private static boolean isPlay;
    private static MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        Music_btn = (Button)findViewById(R.id.music_btn);
        PlayMusci();
    }
    private void PlayMusci()
    {
        mediaPlayer = MediaPlayer.create(this,R.raw.main_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    @Override
    protected void onRestart() {
        if(isPlay)
            PlayMusci();
        super.onRestart();

    }

    @Override
    protected void onStop() {
        if(mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        super.onStop();


    }

    public void OnPlay(View v)
    {
        startActivity( new Intent(this,SelectActivity.class));
    }
    public void OnAbout(View v)
    {
        startActivity( new Intent(this,AboutActivity.class));
    }
    public void OnMusic(View v)
    {

        if(isPlay)
        {
            if(mediaPlayer != null)
            {
                mediaPlayer.stop();
                Music_btn.setBackgroundResource(R.drawable.btn_music2);
                isPlay = false;
            }

        }
        else
        {
            PlayMusci();
            Music_btn.setBackgroundResource(R.drawable.btn_music1);
            isPlay = true;
        }

    }
}
