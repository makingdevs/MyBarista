package com.makingdevs.mybarista.common

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.os.Environment
import android.util.Log
import groovy.transform.CompileStatic

@CompileStatic
class CamaraUtil {

    private static final String TAG = "CamaraUtil"

    File createImageFile() {
        String timeStamp = new Date().format("yyyyMMdd_HHmmss")
        String imageFileName = "Checkin_$timeStamp"
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        File image = File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    File saveBitmapToFile(Bitmap bitmap, String photoName) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + File.separator + photoName)
        FileOutputStream fileOutputStream
        try {
            file.createNewFile()
            fileOutputStream = new FileOutputStream(file)
            fileOutputStream.write(bytes.toByteArray())
            fileOutputStream.close()
        } catch (Exception e) {
            Log.d(TAG, "Error... " + e.message)
        }
        file
    }

    static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        int srcWidth = options.outWidth
        int srcHeight = options.outHeight
        int inSampleSize = 6

        if (srcHeight > destHeight || srcWidth > destWidth) {
            int halfHeight = (srcHeight / 2) as int
            int halfWidth = (srcWidth / 2) as int
            while ((halfHeight / inSampleSize) >= srcHeight && (halfWidth / inSampleSize) >= srcWidth) {
                inSampleSize *= 2
            }
        }
        options = new BitmapFactory.Options()
        options.inSampleSize = inSampleSize
        options.inJustDecodeBounds = false
        BitmapFactory.decodeFile(path, options)

    }

    static Bitmap getScaledBitmap(String path, Activity activity) {
        Point size = new Point()
        activity.getWindowManager()
                .getDefaultDisplay()
                .getSize(size)
        getScaledBitmap(path, size.x, size.y)
    }
}