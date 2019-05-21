package com.weitong.flappycow.sprites;

import com.weitong.flappycow.Game;
import com.weitong.flappycow.GameView;
import com.weitong.flappycow.R;
import com.weitong.flappycow.Util;

import android.graphics.Bitmap;

public class Frontground extends Background {
    /**
     * Height of the ground relative to the height of the bitmap
     */
    public static final float GROUND_HEIGHT = (1f * /*45*/ 35) / 720;

    /**
     * Static bitmap to reduce memory usage.
     */
    public static Bitmap globalBitmap;

    public Frontground(GameView view, Game game) {
        super(view, game);
        if (globalBitmap == null) {
            globalBitmap = Util.getDownScaledBitmapAlpha8(game, R.drawable.fg);
        }
        this.bitmap = globalBitmap;
    }

}
