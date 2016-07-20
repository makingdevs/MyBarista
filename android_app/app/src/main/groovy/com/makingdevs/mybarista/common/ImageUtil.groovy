package com.makingdevs.mybarista.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.webkit.MimeTypeMap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import groovy.transform.CompileStatic

@CompileStatic
public class ImageUtil {

    private static final float STANDARD_ROTATION = 90;

    public static String encodeImage(Context context, Uri picturePickedUri) throws IOException {
        Bitmap bm = this.resizePic(context, 200, 200, picturePickedUri.toString())
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 70, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        bm.recycle();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static String encodeImage(Context context, String pictureTakedPath) {
        Bitmap bm = ImageUtil.resizePic(context, 200, 200, pictureTakedPath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 70, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        bm.recycle();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap resizePic(Context context, int width, int height, String picturePath) {

        int orientation = getOrientation(context, picturePath);
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, bmOptions);

        int photoW;
        int photoH;

        if (orientation == ExifInterface.ORIENTATION_ROTATE_90 || orientation == ExifInterface.ORIENTATION_ROTATE_270) {
            photoW = bmOptions.outHeight;
            photoH = bmOptions.outWidth;
        } else {
            photoW = bmOptions.outWidth;
            photoH = bmOptions.outHeight;
        }

        // Determine how much to scale down the image
        int scaleFactor = Math.min(new Integer(photoW.intValue() / width.intValue() as int), new Integer(photoH.intValue() / height.intValue() as int))

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(picturePath, bmOptions);
        int dimension;

        if (bitmap.getWidth() >= bitmap.getHeight())
            dimension = bitmap.getHeight();
        else
            dimension = bitmap.getWidth();

        if (orientation == ExifInterface.ORIENTATION_ROTATE_90 || orientation == ExifInterface.ORIENTATION_ROTATE_270) {
            Matrix matrix = new Matrix();
            matrix.postRotate(STANDARD_ROTATION);

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
        }

        return ThumbnailUtils.extractThumbnail(bitmap, dimension, dimension);
    }


    public static void addPictureToGallery(Context context, String picturePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(picturePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public static File createPictureFile() throws IOException {
        String imageFileName = getSimpleImageFile();
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        return File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir);
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    private static int getOrientation(Context context, String filename) {
        int orientation = -1;
        try {
            ExifInterface exif = new ExifInterface(filename);
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return orientation;
    }

    private static String getSimpleImageFile(){
        String timeStamp = new Date().format("yyyyMMdd_HHmmss")
        "Checkin_$timeStamp"
    }

    void setPhotoImageView(Context context, String photoUrl, ImageView imageViewPhoto){
        Glide.with(context).load(photoUrl)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(400, 350)
                .into(imageViewPhoto)
    }

    static ArrayList<String> getGalleryPhotos(Activity activity) {
        Uri uri
        Cursor cursor
        Integer indexData
        ArrayList<String> photosGallery = new ArrayList<String>()
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        String[] projection = [MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME] as String[]
        cursor = activity.getContentResolver().query(uri, projection, null, null, null)
        indexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        while (cursor.moveToNext()) {
            photosGallery << cursor.getString(indexData)
        }
        photosGallery
    }
}