package com.weitong.flappycow.sprites;

import com.weitong.flappycow.Game;
import com.weitong.flappycow.GameView;
import com.weitong.flappycow.R;
import com.weitong.flappycow.Util;

import android.graphics.Bitmap;

public class Rainbow extends Sprite {

    /**
     * Static bitmap to reduce memory usage.
     */
    public static Bitmap globalBitmap;

    public Rainbow(GameView view, Game game) {
        super(view, game);
        if (globalBitmap == null) {
            globalBitmap = Util.getScaledBitmapAlpha8(game, R.drawable.rainbow);
        }
        this.bitmap = globalBitmap;
        this.width = this.bitmap.getWidth() / (colNr = 4);
        this.height = this.bitmap.getHeight() / 3;
    }

    @Override
    public void move() {
        changeToNextFrame();
        super.move();
    }


}
