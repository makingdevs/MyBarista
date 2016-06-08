import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Environment
import android.util.Base64
import android.webkit.MimeTypeMap
import groovyjarjarantlr.StringUtils

public class ImageUtils {

    private static final float STANDARD_ROTATION = 90;

    public static String encodeImage(Context context, Uri picturePickedUri) throws IOException {
        Bitmap bm = ImageUtils.resizePic(context, 200, 200, picturePickedUri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 70, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        bm.recycle();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static String encodeImage(Context context, String pictureTakedPath) {
        Bitmap bm = ImageUtils.resizePic(context, 200, 200, pictureTakedPath);
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
        int scaleFactor = Math.min(photoW / width, photoH / height);

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

    public
    static Bitmap resizePic(Context context, int width, int height, Uri uri) throws IOException {

        InputStream is = context.getContentResolver().openInputStream(uri);

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeStream(is, null, bmOptions);
        if (is != null)
            is.close();

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / width, photoH / height);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        is = context.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, bmOptions);
        if (is != null)
            is.close();


        int dimension;

        if (bitmap.getWidth() >= bitmap.getHeight())
            dimension = bitmap.getHeight();
        else
            dimension = bitmap.getWidth();

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
}