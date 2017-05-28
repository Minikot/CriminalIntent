package com.aleksandr.criminalintent;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

/**
 * Created by Aleksandr on 13.05.17.
 */

public class CommonUtils {

    public final static String FILE_SETTINGS = "file_setings";
    public final static String KEY_FORMAT = "KEY_FORMAT";
    public final static String KEY_CRIMES = "KEY_CRIMES";


    //IllegalStateException - не позволяет создавать экземпляр этого класса.
    // Этот класс может быть только для статических методов.
    public CommonUtils() {
    throw new IllegalStateException("This class is not for instantiation");
    }


    /**
     *
     * @param context
     * @param format
     */

    public static void saveDateFormat(Context context, String format){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FORMAT, format);
        editor.commit();
    }

    public static @Nullable String loadDateFormat(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_SETTINGS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_FORMAT, null);
    }

    public static void saveCrimes(Context context, String crimes){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_CRIMES, crimes);
        editor.commit();
    }

    public static @Nullable String loadCrimes(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_SETTINGS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_CRIMES, null);
    }

}
