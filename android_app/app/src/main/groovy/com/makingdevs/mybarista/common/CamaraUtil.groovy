package com.makingdevs.mybarista.common

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import groovy.transform.CompileStatic

@CompileStatic
class CamaraUtil{

    private static final String TAG = "CamaraUtil"

    File createImageFile(){
        String timeStamp = new Date().format("yyyyMMdd_HHmmss")
        String imageFileName = "Checkin_$timeStamp"
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        File image = File.createTempFile(imageFileName,".jpg", storageDir)
    }

    Bitmap resizeBitmapFromFilePath(String pathPhoto, Integer width, Integer height){
        Bitmap bitmap = lessResolution(pathPhoto, width, height)
        bitmap = Bitmap.createScaledBitmap(bitmap,width,height,false)
        bitmap
    }

    File saveBitmapToFile(Bitmap bitmap,String photoName){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + File.separator + photoName)
        FileOutputStream fileOutputStream
        try {
            file.createNewFile()
            fileOutputStream = new FileOutputStream(file)
            fileOutputStream.write(bytes.toByteArray())
            fileOutputStream.close()
        }catch (Exception e){
            Log.d(TAG,"Error... "+e.message)
        }
        file
    }

    // NOTE: http://stackoverflow.com/questions/17839388/creating-a-scaled-bitmap-with-createscaledbitmap-in-android
    private Bitmap lessResolution (String filePath, int width, int height) {
        int reqHeight = height;
        int reqWidth = width;
        BitmapFactory.Options options = new BitmapFactory.Options();

        // First decode with inJustDecodeBounds=true to check dimensions
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        BitmapFactory.decodeFile(filePath, options)
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight) as Integer
            final int widthRatio = Math.round((float) width / (float) reqWidth) as Integer

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        inSampleSize
    }

}