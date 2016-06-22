package com.insthub.ecmobile;
//
//                       __
//                      /\ \   _
//    ____    ____   ___\ \ \_/ \           _____    ___     ___
//   / _  \  / __ \ / __ \ \    <     __   /\__  \  / __ \  / __ \
//  /\ \_\ \/\  __//\  __/\ \ \\ \   /\_\  \/_/  / /\ \_\ \/\ \_\ \
//  \ \____ \ \____\ \____\\ \_\\_\  \/_/   /\____\\ \____/\ \____/
//   \/____\ \/____/\/____/ \/_//_/         \/____/ \/___/  \/___/
//     /\____/
//     \/___/
//
//  Powered by BeeFramework
//

import android.graphics.Bitmap;
import com.insthub.BeeFramework.BeeFrameworkApp;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class EcmobileApp extends BeeFrameworkApp
{
    public static DisplayImageOptions options;		// DisplayImageOptions是用于设置图片显示的类
    public static DisplayImageOptions options_head;		// DisplayImageOptions是用于设置图片显示的类
    @Override
    public void onCreate() {
        super.onCreate();


        FileDownloader.init(getApplicationContext(),
                new FileDownloadHelper.OkHttpClientCustomMaker() { // is not has to provide.
                    @Override
                    public OkHttpClient customMake() {
                        // just for OkHttpClient customize.
                        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
                        // you can set the connection timeout.
                        builder.connectTimeout(15_000, TimeUnit.MILLISECONDS);
                        // you can set the HTTP proxy.
                        builder.proxy(Proxy.NO_PROXY);
                        // etc.
                        return builder.build();
                    }
                });

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_image)			// 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_image)	// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.default_image)		// 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)						// 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)							// 设置下载的图片是否缓存在SD卡中
                        //.displayer(new RoundedBitmapDisplayer(20))	// 设置成圆角图片
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        options_head = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.profile_no_avarta_icon)			// 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.profile_no_avarta_icon)	// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.profile_no_avarta_icon)		// 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)						// 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)							// 设置下载的图片是否缓存在SD卡中
                        //.displayer(new RoundedBitmapDisplayer(30))	// 设置成圆角图片
                .build();
    }
}