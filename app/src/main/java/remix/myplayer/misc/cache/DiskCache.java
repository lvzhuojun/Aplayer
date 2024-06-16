package remix.myplayer.misc.cache;

import static remix.myplayer.util.Constants.MB;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;
import java.io.File;
import java.io.IOException;

/**
 * Created by Remix on 2016/6/14.
 */
public class DiskCache {

  private static DiskLruCache mLrcCache;

  public static void init(Context context, String name) {
    try {
      File lrcCacheDir = getDiskCacheDir(context, name);
      if (!lrcCacheDir.exists()) {
        lrcCacheDir.mkdir();
      }
      mLrcCache = DiskLruCache.open(lrcCacheDir, getAppVersion(context), 1, 200 * MB);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static DiskLruCache getLrcDiskCache() {
    return mLrcCache;
  }

  public static File getDiskCacheDir(Context context, String uniqueName) {
    String cachePath = "";
    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
        || !Environment.isExternalStorageRemovable()) {
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
        File[] files = context.getExternalCacheDirs();
        for (File file : files) {
          if (file != null && file.exists() && file.isDirectory()) {
            cachePath = file.getAbsolutePath();
            break;
          }
        }
      } else {
        File file = context.getExternalCacheDir();
        if (file != null && file.exists() && file.isDirectory()) {
          cachePath = file.getAbsolutePath();
        }
      }
    }
    if (TextUtils.isEmpty(cachePath)) {
      cachePath = context.getCacheDir().getPath();
    }
    return new File(cachePath + File.separator + uniqueName);
  }

  public static int getAppVersion(Context context) {
    try {
      PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
      return info.versionCode;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return 1;
  }

}
