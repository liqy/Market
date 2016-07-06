package tv.tipsee.vr.utils;

import java.io.File;

import android.os.Environment;

import com.liulishuo.filedownloader.util.FileDownloadUtils;

/**
 * SD卡工具类
 */
public class SDCardUtils {

    public static final String TIP_SEE_PATH = "TipSee";

    /**
     * SDCard是否可用
     **/
    public static boolean IS_MOUNTED = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());

    /**
     * SDCard的根路径
     **/
    private static String SDCARD_PATH;

    /**
     * 取得SD卡路径，以/结尾
     *
     * @return SD卡路径
     */
    public static String getSDCardPath() {
        if (!IS_MOUNTED) return FileDownloadUtils.getDefaultSaveRootPath();
        if (null != SDCARD_PATH) return SDCARD_PATH;
        File path = Environment.getExternalStorageDirectory();
        String SDCardPath = path.getAbsolutePath();
        SDCardPath += SDCardPath.endsWith(File.separator) ? "" : File.separator;
        SDCARD_PATH = SDCardPath;
        return SDCardPath;
    }

    public static String getTipSeePath() {
        return SDCardUtils.getSDCardPath() + TIP_SEE_PATH + File.separator;
    }

}
