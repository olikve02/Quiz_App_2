package com.example.quizapp2;

import android.content.Context;
import android.net.Uri;

public class MyUtils {
    public static Uri getUriToDrawable(Context context, int drawableId) {
        return Uri.parse("android.resource://" + context.getPackageName() + "/" + drawableId);
    }
}
