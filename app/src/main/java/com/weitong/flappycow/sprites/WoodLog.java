package com.weitong.flappycow.sprites;

import com.weitong.flappycow.Game;
import com.weitong.flappycow.GameView;
import com.weitong.flappycow.R;
import com.weitong.flappycow.Util;

import android.graphics.Bitmap;

public class WoodLog extends Sprite {

    /**
     * Static bitmap to reduce memory usage.
     */
    public static Bitmap globalBitmap;

    public WoodLog(GameView view, Game game) {
        super(view, game);
        if (globalBitmap == null) {
            globalBitmap = Util.getScaledBitmapAlpha8(game, R.drawable.log_full);
        }
        this.bitmap = globalBitmap;
        this.width = this.bitmap.getWidth();
        this.height = this.bitmap.getHeight();
    }

    /**
     * Sets the position
     *
     * @param x
     * @param y
     */
    public void init(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
