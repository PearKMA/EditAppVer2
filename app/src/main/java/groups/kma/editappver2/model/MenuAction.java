package groups.kma.editappver2.model;

import android.graphics.Bitmap;

public class MenuAction {
    private int bitmap;
    private String title;

    public MenuAction() {
    }

    public MenuAction(int bitmap, String title) {
        this.bitmap = bitmap;
        this.title = title;
    }

    public int getBitmap() {
        return bitmap;
    }

    public void setBitmap(int bitmap) {
        this.bitmap = bitmap;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
