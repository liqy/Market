package tv.tipsee.vr;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.insthub.ecmobile.EcmobileManager;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.wxapi.Util;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.squareup.picasso.Picasso;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

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
    private BottomSheet sheet;

    private static final int THUMB_SIZE = 150;
    private IWXAPI api;

    private int downloadId;

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

        donutProgress = (DonutProgress) findViewById(R.id.donut_progress);

        vr_video_download.setOnClickListener(this);
        vr_video_play.setOnClickListener(this);
        back_home.setOnClickListener(this);

        bar_share.setOnClickListener(this);
        bar_share.setVisibility(View.GONE);
        bar_delete.setOnClickListener(this);
    }

    private void initData() {

        api = WXAPIFactory.createWXAPI(this, EcmobileManager.getWeixinAppId(this));
        api.registerApp(EcmobileManager.getWeixinAppId(this));

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
                    downloadId = createDownloadTask(vrVideo.file, 0).start();
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
//                            Toast.makeText(VRVideoInfoActivity.this, "文件已下载", Toast.LENGTH_SHORT).show();
                        } else {//下载完毕自动播放
//                            downloadId = createDownloadTask(vrVideo.file, 1).start();
                            MD360PlayerActivity.startVideo(VRVideoInfoActivity.this, Uri.parse(vrVideo.file));
                        }
                    }
                }
                break;
            case R.id.bar_delete:
                deleteVRVideo();
                break;
            case R.id.bar_share:
                showDialog(0);
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
//                            Toast.makeText(VRVideoInfoActivity.this, "下载完毕,请点击观看", Toast.LENGTH_SHORT).show();
                            MD360PlayerActivity.startVideo(VRVideoInfoActivity.this, getVrUri(vrVideo.file));
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

    @Nullable
    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        sheet = new BottomSheet.Builder(this).sheet(R.menu.menu_share).listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == R.id.share_session) {
                    shareWebPage(false);
                } else if (which == R.id.share_timeline) {
                    shareWebPage(true);
                }

            }
        }).build();
        return sheet;
    }

    private void shareWebPage(boolean isTimeLine) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://www.baidu.com";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "WebPage Title WebPage Title WebPage Title WebPage Title WebPage Title WebPage Title WebPage Title WebPage Title WebPage Title Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long";
        msg.description = "WebPage Description WebPage Description WebPage Description WebPage Description WebPage Description WebPage Description WebPage Description WebPage Description WebPage Description Very Long Very Long Very Long Very Long Very Long Very Long Very Long";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        msg.thumbData = Util.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = isTimeLine ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileDownloader.getImpl().pause(downloadId);
    }
}
