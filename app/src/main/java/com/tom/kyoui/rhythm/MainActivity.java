package com.tom.kyoui.kyoroxchan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends YouTubeBaseActivity {

    private long mCount, mDelay, mPeriod, mCurrentTime;
    private ArrayList<Integer> mTapTimeList1, mTapTimeList2;
    private Timer mTimer;
    private Handler mHandler;

    private TextView timerTextView, tapTextView1, tapTextView2, loadTextView;
    private Button startButton, stopButton, tapButton1, tapButton2, playButton,menu,search;

    private ConstraintLayout startLayout;

    private SimpleDateFormat dataFormat =
            new SimpleDateFormat("mm:ss.SS", Locale.US);

    String YoutubeAPI = YoutubeAccessToken.YOUTUBE_ACCESS_TOKEN;
    YouTubePlayerView player;
    YouTubePlayer mYouTubePlayer;

    EditText editText;
    boolean isSearchClicked = false;
    boolean isVideoAvailable = false;
    boolean isMenuClicked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerTextView = (TextView) findViewById(R.id.timer_text_view);
        tapTextView1 = (TextView) findViewById(R.id.tap_text_view1);
        tapTextView2 = (TextView) findViewById(R.id.tap_text_view2);
        loadTextView = (TextView) findViewById(R.id.loadtext);
        editText = (EditText) findViewById(R.id.editText);

        search = (Button) findViewById(R.id.search);
        startButton = (Button) findViewById(R.id.start_button);
        stopButton = (Button) findViewById(R.id.stop_button);
        tapButton1 = (Button) findViewById(R.id.tap_button1);
        tapButton2 = (Button) findViewById(R.id.tap_button2);
        playButton = (Button) findViewById(R.id.playButton);
        player = (YouTubePlayerView) findViewById(R.id.player);
        menu = (Button) findViewById(R.id.menu);


        startLayout = (ConstraintLayout) findViewById(R.id.layout);

        player.initialize(YoutubeAPI, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(final YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {

                mYouTubePlayer = youTubePlayer;
                mYouTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                    @Override
                    public void onLoading() {
                        if (isSearchClicked)
                            loadTextView.setText("動画取得中...");
                            loadTextView.setTextColor(Color.rgb(0,0,255));

                    }

                    @Override
                    public void onLoaded(String s) {
                        if (isSearchClicked) {
                            loadTextView.setText("動画取得成功!!");
                            loadTextView.setTextColor(Color.rgb(0,0,255));
                            isVideoAvailable = true;
                            search.setVisibility(View.INVISIBLE);
                            playButton.setVisibility(View.VISIBLE);


                        }
                        // 読み込み成功したとき

                    }

                    @Override
                    public void onAdStarted() {

                    }

                    @Override
                    public void onVideoStarted() {

                    }

                    @Override
                    public void onVideoEnded() {

                    }

                    @Override
                    public void onError(YouTubePlayer.ErrorReason errorReason) {
                        //loadTextView.setText("動画取得失敗もう一度URLを入力してください");
                        //loadTextView.setTextColor(Color.rgb(255,0,0));
                        // 読み込み失敗したとき

                    }
                });
//                mYouTubePlayer.loadVideo("6j_e-7VXOHc");

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

        mHandler = new Handler();

        mCount = 0;
        mDelay = 0;
        mPeriod = 10;
        mCurrentTime = 0;
        mTapTimeList1 = new ArrayList<>();
        mTapTimeList2 = new ArrayList<>();
        mTapTimeList1.add(0);
        mTapTimeList2.add(0);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimer == null) {
                    mYouTubePlayer.play();
                    mCount = 0;
                    mTapTimeList1 = new ArrayList<>();
                    mTapTimeList2 = new ArrayList<>();
                    mTimer = new Timer(false);
                    mTimer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mCount++;
                                    mCurrentTime = mCount * mPeriod;
                                    timerTextView.setText(dataFormat.format(mCurrentTime));

                                    startButton.setVisibility(View.INVISIBLE);
                                    stopButton.setVisibility(View.VISIBLE);
                                }
                            });

                        }
                    }, mDelay, mPeriod);
                }
            }
        });
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVideoAvailable) {
                    search.setVisibility(View.VISIBLE);
                    startLayout.setVisibility(View.INVISIBLE);
                    tapButton1.setVisibility(View.VISIBLE);
                    tapButton2.setVisibility(View.VISIBLE);
                    startButton.setVisibility(View.VISIBLE);
                    stopButton.setVisibility(View.INVISIBLE);
                    timerTextView.setVisibility(View.VISIBLE);
                    tapTextView1.setVisibility(View.VISIBLE);
                    tapTextView2.setVisibility(View.VISIBLE);
                    player.setVisibility(View.VISIBLE);
                    mYouTubePlayer.pause();
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimer != null) {
                    mTimer.cancel();
                    mTimer = null;
                    timerTextView.setText(dataFormat.format(0));
                    tapTextView1.setText(dataFormat.format(0));
                    tapTextView2.setText(dataFormat.format(0));
                    mYouTubePlayer.pause();
                    Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                    intent.putExtra("1", mTapTimeList1);
                    intent.putExtra("2", mTapTimeList2);
                    startActivityForResult(intent, 123);
                }

            }
        });

        tapButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentTime > 0) {
                    mTapTimeList1.add((int) mCurrentTime);
                    tapTextView1.setText(dataFormat.format(mCurrentTime));
                    Log.d("TIME1：", mTapTimeList1.get(mTapTimeList1.size() - 1) + "");
                }
            }
        });

        tapButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentTime > 0) {
                    mTapTimeList2.add((int) mCurrentTime);
                    tapTextView2.setText(dataFormat.format(mCurrentTime));
                    Log.d("TIME2：", mTapTimeList2.get(mTapTimeList2.size() - 1) + "");
                }
            }
        });
        menu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplication(),OperationActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 123:
                if (data.getIntExtra("isnewtrak", 0) == 1) {
                    search.setVisibility(View.VISIBLE);
                    playButton.setVisibility(View.INVISIBLE);
                    startLayout.setVisibility(View.VISIBLE);
                    tapButton1.setVisibility(View.INVISIBLE);
                    tapButton2.setVisibility(View.INVISIBLE);
                    startButton.setVisibility(View.INVISIBLE);
                    stopButton.setVisibility(View.INVISIBLE);
                    timerTextView.setVisibility(View.INVISIBLE);
                    tapTextView1.setVisibility(View.INVISIBLE);
                    tapTextView2.setVisibility(View.INVISIBLE);
                    player.setVisibility(View.INVISIBLE);

                    editText.setText("");
                    loadTextView.setText("URLを入力してください");
                    isSearchClicked = false;
                    isVideoAvailable = false;

                } else {
                    search.setVisibility(View.VISIBLE);
                    startLayout.setVisibility(View.INVISIBLE);
                    tapButton1.setVisibility(View.VISIBLE);
                    tapButton2.setVisibility(View.VISIBLE);
                    startButton.setVisibility(View.VISIBLE);
                    stopButton.setVisibility(View.INVISIBLE);
                    timerTextView.setVisibility(View.VISIBLE);
                    tapTextView1.setVisibility(View.VISIBLE);
                    tapTextView2.setVisibility(View.VISIBLE);
                    player.setVisibility(View.VISIBLE);

                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        super.onDestroy();
    }

    public void onSearchClick(View v) {
        String url = editText.getText().toString();
        String videoId = getYoutubeID(url);
        isSearchClicked = true;

        mYouTubePlayer.loadVideo(videoId);
    }

    private String getYoutubeID(String youtubeUrl) {
        if (TextUtils.isEmpty(youtubeUrl)) {
            return "";
        }
        String videoId = "";

        String expression =
                "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(youtubeUrl);
        if (matcher.matches()) {
            String groupIndex1 = matcher.group(1);
            if (groupIndex1 != null && groupIndex1.length() == 11)
                videoId = groupIndex1;
        }

        return videoId;
    }
}

