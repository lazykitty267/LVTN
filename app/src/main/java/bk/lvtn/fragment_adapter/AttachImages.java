package bk.lvtn.fragment_adapter;

import android.graphics.Bitmap;

/**
 * Created by Phupc on 11/12/17.
 */

public class AttachImages {
    String path;
    Bitmap bitmap;

    public AttachImages() {
    }
    public AttachImages(String path,Bitmap bitmap) {
        this.path= path;
        this.bitmap=bitmap;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
