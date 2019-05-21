package com.weitong.flappycow;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.google.android.gms.ads.*;
import com.google.android.gms.games.Games;
import com.google.android.gms.common.api.GoogleApiClient;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class Game extends BaseGameActivity {
    /**
     * Name of the SharedPreference that saves the medals
     */
    public static final String coin_save = "coin_save";

    /**
     * Key that saves the medal
     */
    public static final String coin_key = "coin_key";

    private static final int GAMES_PER_AD = 3;
    /**
     * Counts number of played games
     */
    private static int gameOverCounter = 1;
    private InterstitialAd interstitial;


    /**
     * Whether the music should play or not
     */
    public boolean musicShouldPlay = false;

    /**
     * Time interval (ms) you have to press the backbutton twice in to exit
     */
    private static final long DOUBLE_BACK_TIME = 1000;

    /**
     * Saves the time of the last backbutton press
     */
    private long backPressed;

    /**
     * To do UI things from different threads
     */
    public MyHandler handler;

    /**
     * Hold all accomplishments
     */
    AccomplishmentBox accomplishmentBox;

    /**
     * The view that handles all kind of stuff
     */
    GameView view;

    /**
     * The amount of collected coins
     */
    int coins;

    /**
     * This will increase the revive price
     */
    public int numberOfRevive = 1;

    /**
     * The dialog displayed when the game is over
     */
    GameOverDialog gameOverDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accomplishmentBox = new AccomplishmentBox();
        view = new GameView(this);
        view.initTrack();
        gameOverDialog = new GameOverDialog(this);
        handler = new MyHandler(this);
        setContentView(view);
        loadCoins();
    }


    /**
     * Initializes the player with the nyan cat song
     * and sets the position to 0.
     */

    private void loadCoins() {
        SharedPreferences saves = this.getSharedPreferences(coin_save, 0);
        this.coins = saves.getInt(coin_key, 0);
    }

    /**
     * Pauses the view and the music
     */
    @Override
    protected void onPause() {
        view.pause();
        super.onPause();
    }

    /**
     * Resumes the view (but waits the view waits for a tap)
     * and starts the music if it should be running.
     * Also checks whether the Google Play Services are available.
     */
    @Override
    protected void onResume() {
        view.drawOnce();
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) != ConnectionResult.SUCCESS) {
            Toast.makeText(this, "Please check your Google Services", Toast.LENGTH_LONG).show();
        }
        super.onResume();
    }

    /**
     * Prevent accidental exits by requiring a double press.
     */
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - backPressed < DOUBLE_BACK_TIME) {
            super.onBackPressed();
        } else {
            backPressed = System.currentTimeMillis();
            Toast.makeText(this, getResources().getString(R.string.on_back_press), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Sends the handler the command to show the GameOverDialog.
     * Because it needs an UI thread.
     */
    public void gameOver() {
        if (gameOverCounter % GAMES_PER_AD == 0) {
            handler.sendMessage(Message.obtain(handler, MyHandler.SHOW_AD));
        } else {
            handler.sendMessage(Message.obtain(handler, MyHandler.GAME_OVER_DIALOG));
        }

    }

    public void increaseCoin() {
        this.coins++;
        if (coins >= 50 && !accomplishmentBox.achievement_50_coins) {
            accomplishmentBox.achievement_50_coins = true;
            if (getApiClient().isConnected()) {
                Games.Achievements.unlock(getApiClient(), getResources().getString(R.string.achievement_50_coins));
            } else {
                handler.sendMessage(Message.obtain(handler, 1, R.string.toast_achievement_50_coins, MyHandler.SHOW_TOAST));
            }
        }
    }

    /**
     * What should happen, when an obstacle is passed?
     */
    public void increasePoints() {
        accomplishmentBox.points++;

        this.view.getPlayer().upgradeBitmap(accomplishmentBox.points);

        if (accomplishmentBox.points >= AccomplishmentBox.BRONZE_POINTS) {
            if (!accomplishmentBox.achievement_bronze) {
                accomplishmentBox.achievement_bronze = true;
                if (getApiClient().isConnected()) {
                    Games.Achievements.unlock(getApiClient(), getResources().getString(R.string.achievement_bronze));
                } else {
                    handler.sendMessage(Message.obtain(handler, MyHandler.SHOW_TOAST, R.string.toast_achievement_bronze, MyHandler.SHOW_TOAST));
                }
            }

            if (accomplishmentBox.points >= AccomplishmentBox.SILVER_POINTS) {
                if (!accomplishmentBox.achievement_silver) {
                    accomplishmentBox.achievement_silver = true;
                    if (getApiClient().isConnected()) {
                        Games.Achievements.unlock(getApiClient(), getResources().getString(R.string.achievement_silver));
                    } else {
                        handler.sendMessage(Message.obtain(handler, MyHandler.SHOW_TOAST, R.string.toast_achievement_silver, MyHandler.SHOW_TOAST));
                    }
                }

                if (accomplishmentBox.points >= AccomplishmentBox.GOLD_POINTS) {
                    if (!accomplishmentBox.achievement_gold) {
                        accomplishmentBox.achievement_gold = true;
                        if (getApiClient().isConnected()) {
                            Games.Achievements.unlock(getApiClient(), getResources().getString(R.string.achievement_gold));
                        } else {
                            handler.sendMessage(Message.obtain(handler, MyHandler.SHOW_TOAST, R.string.toast_achievement_gold, MyHandler.SHOW_TOAST));
                        }
                    }
                }
            }
        }
    }

    public GoogleApiClient getApiClient() {
        return mHelper.getApiClient();
    }

    /**
     * Shows the GameOverDialog when a message with code 0 is received.
     */
    static class MyHandler extends Handler {
        public static final int GAME_OVER_DIALOG = 0;
        public static final int SHOW_TOAST = 1;
        public static final int SHOW_AD = 2;

        private Game game;

        public MyHandler(Game game) {
            this.game = game;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GAME_OVER_DIALOG:
                    showGameOverDialog();
                    break;
                case SHOW_TOAST:
                    Toast.makeText(game, msg.arg1, Toast.LENGTH_SHORT).show();
                    break;
                case SHOW_AD:
                    showAd();
                    break;
            }
        }

        private void showAd() {
            showGameOverDialog();
        }

        private void showGameOverDialog() {
            ++Game.gameOverCounter;
            game.gameOverDialog.init();
            game.gameOverDialog.show();
        }
    }


    @Override
    public void onSignInFailed() {
    }

    @Override
    public void onSignInSucceeded() {
    }


    private class MyAdListener extends AdListener {
        public void onAdClosed() {
            handler.sendMessage(Message.obtain(handler, MyHandler.GAME_OVER_DIALOG));
        }
    }
}
