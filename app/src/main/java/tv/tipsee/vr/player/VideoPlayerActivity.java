package tv.tipsee.vr.player;

import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.widget.Toast;

import com.asha.vrlib.MDVRLibrary;
import com.insthub.ecmobile.R;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by hzqiujiadi on 16/4/5.
 * hzqiujiadi ashqalcn@gmail.com
 */
public class VideoPlayerActivity extends MD360PlayerActivity {

    private MediaPlayerWrapper mMediaPlayerWrapper = new MediaPlayerWrapper();
    private int clickCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMediaPlayerWrapper.init();
        mMediaPlayerWrapper.setPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                cancelBusy();
            }
        });

        Uri uri = getUri();
        if (uri != null) {
            mMediaPlayerWrapper.openRemoteFile(uri.toString());
            mMediaPlayerWrapper.prepare();
        }
    }

    @Override
    protected MDVRLibrary createVRLibrary() {
        return MDVRLibrary.with(this)
                .displayMode(MDVRLibrary.DISPLAY_MODE_NORMAL)
                .interactiveMode(MDVRLibrary.INTERACTIVE_MODE_MOTION)
                .video(new MDVRLibrary.IOnSurfaceReadyCallback() {
                    @Override
                    public void onSurfaceReady(Surface surface) {
                        mMediaPlayerWrapper.getPlayer().setSurface(surface);
                    }
                })
                .ifNotSupport(new MDVRLibrary.INotSupportCallback() {
                    @Override
                    public void onNotSupport(int mode) {
                        String tip = mode == MDVRLibrary.INTERACTIVE_MODE_MOTION
                                ? "不支持:手势" : "不支持:" + String.valueOf(mode);
                        Toast.makeText(VideoPlayerActivity.this, tip, Toast.LENGTH_SHORT).show();
                    }
                })
                .pinchEnabled(true)
                .gesture(new MDVRLibrary.IGestureListener() {//点击
                    @Override
                    public void onClick(MotionEvent e) {
                        if (clickCount == 0) {
                            clickCount = 1;
                        } else if (clickCount == 1) {
                            clickCount = 0;
                            if (nav_layout!=null){
                                if (nav_layout.isShown()){
                                    nav_layout.setVisibility(View.GONE);
                                }else {
                                    nav_layout.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                })
                .build(R.id.surface_view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayerWrapper.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMediaPlayerWrapper.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMediaPlayerWrapper.onResume();
    }
}
