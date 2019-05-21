package com.weitong.flappycow.sprites;

import com.weitong.flappycow.Game;
import com.weitong.flappycow.GameView;
import com.weitong.flappycow.R;
import com.weitong.flappycow.Util;

public class PauseButton extends Sprite {
    public PauseButton(GameView view, Game game) {
        super(view, game);
        this.bitmap = Util.getScaledBitmapAlpha8(game, R.drawable.pause_button);
        this.width = this.bitmap.getWidth();
        this.height = this.bitmap.getHeight();
    }

    /**
     * Sets the button in the right upper corner.
     */
    @Override
    public void move() {
        this.x = this.view.getWidth() - this.width;
        this.y = 0;
    }
}