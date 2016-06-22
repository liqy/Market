package tv.tipsee.vr;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.insthub.ecmobile.R;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import okhttp3.Call;
import okhttp3.Request;
import tv.tipsee.vr.models.VRVideo;
import tv.tipsee.vr.player.MD360PlayerActivity;
import tv.tipsee.vr.views.widgets.SquaredImageView;

public class VRVideoInfoActivity extends BaseActivity implements View.OnClickListener {

    private SquaredImageView vr_image;
    private TextView vr_video_title;
    private TextView vr_video_desc;

    private Button vr_video_download;
    private Button vr_video_play;

    private ImageView back_home;

    private VRVideo vrVideo;


    public static void openVRVideoInfoActivity(Activity activity, VRVideo vrVideo) {
        Intent intent = new Intent(activity, VRVideoInfoActivity.class);
        intent.putExtra("VRVideo", vrVideo);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vr_video_info);
        initView();
        initData();
    }

    private void initView() {

        vr_image = (SquaredImageView) findViewById(R.id.vr_image);
        vr_video_title = (TextView) findViewById(R.id.vr_video_title);
        vr_video_desc = (TextView) findViewById(R.id.vr_video_desc);

        vr_video_download = (Button) findViewById(R.id.vr_video_download);
        vr_video_play = (Button) findViewById(R.id.vr_video_play);

        back_home = (ImageView) findViewById(R.id.back_home);

        vr_video_download.setOnClickListener(this);
        vr_video_play.setOnClickListener(this);
        back_home.setOnClickListener(this);

    }

    private void initData() {
        vrVideo = getIntent().getParcelableExtra("VRVideo");
        vr_video_title.setText(vrVideo.title);
        vr_video_desc.setText(vrVideo.desc);

        Picasso.with(this).load(vrVideo.pic)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(vr_image);

    }

    public void downloadFile(VRVideo  vrVideo)
    {
        OkHttpUtils//
                .get()//
                .url(vrVideo.file)//
                .build()//
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), vrVideo.file.substring(vrVideo.file.indexOf("/")+1))//
                {

                    @Override
                    public void onBefore(Request request, int id)
                    {
                    }

                    @Override
                    public void inProgress(float progress, long total, int id)
                    {
                        Log.e(getLocalClassName(), "inProgress :" + (int) (100 * progress));
                    }

                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        Log.e(getLocalClassName(), "onError :" + e.getMessage());
                    }

                    @Override
                    public void onResponse(File file, int id)
                    {
                        Log.e(getLocalClassName(), "onResponse :" + file.getAbsolutePath());
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_home:
                finish();
                break;
            case R.id.vr_video_download:
                downloadFile(vrVideo);
                break;
            case R.id.vr_video_play:
                if (vrVideo != null) {
                    MD360PlayerActivity.startVideo(VRVideoInfoActivity.this, Uri.parse(vrVideo.file));
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
