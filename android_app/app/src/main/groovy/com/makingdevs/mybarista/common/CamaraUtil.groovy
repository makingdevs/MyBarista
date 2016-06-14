package com.makingdevs.mybarista.common

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log

class CamaraUtil{

    private static final String TAG = "CamaraUtil"

    File createImageFile(){
        String timeStamp = new Date().format("yyyyMMdd_HHmmss")
        String imageFileName = "Checkin_$timeStamp"
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        File image = File.createTempFile(imageFileName,".jpg", storageDir)
    }

    Bitmap resizeBitmapFromFilePath(String pathPhoto, Integer width, Integer height){
        BitmapFactory.Options bmOptions = new BitmapFactory.Options()
        Bitmap bitmap = BitmapFactory.decodeFile(pathPhoto,bmOptions)
        bitmap = Bitmap.createScaledBitmap(bitmap,width,height,true)
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

}