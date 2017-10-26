package com.chinasofti.rcs.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import com.chinasofti.common.utils.log.YLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class AssetsManager {

    private static final String TAG = "AssetsManager";

    //从assets复制出去的apk的目标目录
    public static final String PLUGIN_DIR = Environment.getExternalStorageDirectory() + File.separator + "plugin";

    //apk过滤
    private static final String FILE_FILTER = ".apk";


    /**
     * 将资源文件中的apk文件拷贝到私有目录中
     *
     * @param context
     */
    public static void copyAllAssetsApk(Context context) {

        AssetManager assetManager = context.getAssets();
        long startTime = System.currentTimeMillis();
        try {
            File plugin_dir = new File(PLUGIN_DIR);
            if (!plugin_dir.exists()) {
                plugin_dir.mkdir();
            }
            String[] fileNames = assetManager.list("");
            for (int i = 0; i < fileNames.length; i++) {
                String fileName = fileNames[i];
                if (fileName.endsWith(FILE_FILTER)) {
                    InputStream in = null;
                    OutputStream out = null;
                    in = assetManager.open(fileName);
                    File f = new File(plugin_dir, fileName);
                    if (f.exists() && f.length() == in.available()) {
                        YLog.w(TAG, fileName + " no change");
                        return;
                    }
                    YLog.i(TAG, fileName + " chaneged");
                    out = new FileOutputStream(f);
                    byte[] buffer = new byte[2048];
                    int read;
                    while ((read = in.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                    }
                    in.close();
                    in = null;
                    out.flush();
                    out.close();
                    out = null;
                    f.renameTo(new File(plugin_dir, fileName.replace('.', '-')));
                }

                if (i == fileNames.length - 1 && fileName.endsWith(FILE_FILTER)) {
                    YLog.e(TAG, "found no apks...");
                    return;
                }

            }
            YLog.i("###copyAssets time = " + (System.currentTimeMillis() - startTime) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
