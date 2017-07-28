package com.seemran.koble.Activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.seemran.koble.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class OnCallActivity extends AppCompatActivity {
private CircleImageView oncall,speaker,microphone,message;
    boolean isPressed = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_call);
        oncall=(CircleImageView)findViewById(R.id.callphone);
        speaker = (CircleImageView)findViewById(R.id.loud);
        microphone=(CircleImageView)findViewById(R.id.voice);
        message=(CircleImageView)findViewById(R.id.messagecall);
    }


    @Override
    protected void onStart() {
        super.onStart();
//         AudioManager mAudioMgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(mAudioMgr.isWiredHeadsetOn()){
//                    mAudioMgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
//                    mAudioMgr.setWiredHeadsetOn(false);
//                    mAudioMgr.setSpeakerphoneOn(true);
//                    mAudioMgr.setMode(AudioManager.MODE_IN_COMMUNICATION);
//
//                    Toast.makeText(getApplicationContext(), "SpeakerPhone On", Toast.LENGTH_LONG).show();
//                }else{
//                    mAudioMgr.setMode(AudioManager.MODE_IN_COMMUNICATION);
//                    mAudioMgr.setSpeakerphoneOn(false);
//                    mAudioMgr.setWiredHeadsetOn(true);
//                    Toast.makeText(getApplicationContext(), "Wired Headset On", Toast.LENGTH_LONG).show();
//                }
                if(isPressed)
                    speaker.setBackgroundResource(R.drawable.ic_volume_up);
                else
                    speaker.setBackgroundResource(R.drawable.ic_volume_down);
                isPressed=!isPressed;
            }
        });

        microphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isPressed)
                    microphone.setBackgroundResource(R.drawable.ic_mic_off);
                else
                    microphone.setBackgroundResource(R.drawable.ic_voice);

                isPressed = !isPressed;
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OnCallActivity.this,ChatActivity.class);
                startActivity(i);
            }
        });
    }
}
