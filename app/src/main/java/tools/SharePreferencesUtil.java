package tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.baidu.platform.comapi.map.E;

/**
 * Created by yanqijs on 2016/5/13.
 */
public class SharePreferencesUtil {
    public static void putData(int data, Context context, String name, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, data);
        editor.commit();
    }

    public static void putData(Boolean data, Context context, String name, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, data);
        editor.commit();
    }

    public static void putData(String data, Context context, String name, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, data);
        editor.commit();
    }

    public static String getData(Context context, String name, String key, String defaultData) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultData);
    }

    public static int getData(Context context, String name, String key, int defaultData) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultData);
    }

    public static boolean getData(Context context, String name, String key, boolean defaultData) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultData);
    }
}
