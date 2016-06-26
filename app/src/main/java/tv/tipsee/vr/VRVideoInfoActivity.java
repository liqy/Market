package tv.tipsee.vr;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.insthub.ecmobile.R;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.squareup.picasso.Picasso;

import java.io.File;

import me.drakeet.materialdialog.MaterialDialog;
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

    private ImageView bar_share;
    private ImageView bar_delete;

    private VRVideo vrVideo;
    private MaterialDialog mMaterialDialog;

    private DonutProgress donutProgress;

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

        bar_delete = (ImageView) findViewById(R.id.bar_delete);
        bar_share = (ImageView) findViewById(R.id.bar_share);

        donutProgress=(DonutProgress)findViewById(R.id.donut_progress);

        vr_video_download.setOnClickListener(this);
        vr_video_play.setOnClickListener(this);
        back_home.setOnClickListener(this);

        bar_share.setOnClickListener(this);
        bar_delete.setOnClickListener(this);
    }

    private void initData() {
        vrVideo = getIntent().getParcelableExtra("VRVideo");
        vr_video_title.setText(vrVideo.title);
        vr_video_desc.setText(vrVideo.desc);

        Picasso.with(this).load(vrVideo.pic)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(vr_image);

        File vrFile = new File(getVideoFilePath(vrVideo.file));

        if (vrFile.exists()) {
            vr_video_download.setText("已下载");
        } else {
            vr_video_download.setText("下载");
        }

    }

    @Override
    public void onClick(View v) {
        File vrFile = new File(getVideoFilePath(vrVideo.file));
        switch (v.getId()) {
            case R.id.back_home:
                finish();
                break;
            case R.id.vr_video_download:
                if (!vrFile.exists()) {
                    createDownloadTask(vrVideo.file, 0).start();
                } else {
                    Toast.makeText(VRVideoInfoActivity.this, "下载完毕,请点击观看", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.vr_video_play:
                if (vrVideo != null) {
                    if (TextUtils.isEmpty(vrVideo.file)) {
                        Toast.makeText(VRVideoInfoActivity.this, "播放文件不存在", Toast.LENGTH_SHORT).show();
                    } else {//TODO 下载
                        if (vrFile.exists()) {
                            MD360PlayerActivity.startVideo(VRVideoInfoActivity.this, getVrUri(vrVideo.file));
                            Toast.makeText(VRVideoInfoActivity.this, "文件已下载", Toast.LENGTH_SHORT).show();
                        } else {//下载完毕自动播放
                            createDownloadTask(vrVideo.file, 1).start();
                        }
                    }
                }
                break;
            case R.id.bar_delete:
                deleteVRVideo();
                break;
            case R.id.bar_share:

                break;
            default:
                break;
        }
    }

    private void deleteVRVideo() {
        final File vrFile = new File(getVideoFilePath(vrVideo.file));
        if (vrFile.exists()) {//弹框二次确认
            if (mMaterialDialog == null) {
                mMaterialDialog = new MaterialDialog(this)
                        .setTitle("提示")
                        .setMessage("你将删除已下载的视频文件,请确认?")
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();
                                vrFile.delete();
                                vr_video_download.setText("下载");
                            }
                        })
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();
                            }
                        });
            }
            mMaterialDialog.show();
        } else {
            Toast.makeText(VRVideoInfoActivity.this, "文件不存在", Toast.LENGTH_SHORT).show();
        }
    }

    private Uri getVrUri(String url) {
        return Uri.parse("file://" + getVideoFilePath(url));
    }

    private String getVideoFilePath(String url) {
        return FileDownloadUtils.getDefaultSaveRootPath() + url.substring(url.lastIndexOf("/"));
    }

    private BaseDownloadTask createDownloadTask(String url, final int from) {
        donutProgress.setVisibility(View.VISIBLE);
        return FileDownloader.getImpl().create(url)
                .setPath(getVideoFilePath(url))
                .setCallbackProgressTimes(300)
                .setMinIntervalUpdateSpeed(400)
                .setListener(new FileDownloadSampleListener() {
                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.progress(task, soFarBytes, totalBytes);
                        int p = (int) (soFarBytes / (totalBytes * 1.0) * 100);
                        vr_video_download.setText("下载中");
                        donutProgress.setProgress(p);
                        donutProgress.setMax(100);
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        super.error(task, e);
                        donutProgress.setVisibility(View.GONE);
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.paused(task, soFarBytes, totalBytes);

                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        super.completed(task);
                        vr_video_download.setText("已下载");
                        if (from == 0) {
                            Toast.makeText(VRVideoInfoActivity.this, "下载完毕,请点击观看", Toast.LENGTH_SHORT).show();
                        } else {
                            MD360PlayerActivity.startVideo(VRVideoInfoActivity.this, getVrUri(vrVideo.file));
                        }
                        donutProgress.setVisibility(View.GONE);
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        super.warn(task);
                    }
                });
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
