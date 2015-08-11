package com.example.misklahr.hopper;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;


public class DrawGame extends Activity implements View.OnTouchListener {

    private MyView surfaceView;
    private boolean input = false;
    private SharedPreferences spref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        spref = PreferenceManager.getDefaultSharedPreferences(this);

        surfaceView = new MyView(this);
        surfaceView.setOnTouchListener(this);
        setContentView(surfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        surfaceView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        surfaceView.resume();
    }


    @Override
    public boolean onTouch(View v, MotionEvent me) {
        if (!input)
            surfaceView.doAction(me);
        return true;
    }


    public class MyView extends SurfaceView implements Runnable {

        private Thread thread = null;
        private SurfaceHolder holder;
        private boolean isRunning = false;
        private Paint paint;
        private long compTime, currTime;

        private Bitmap playerPic;

        private float meX, meY;

        private String state = "beginning";

        private int width;
        private int height;

        private Game game;


        private int level;


        private int points;


        public MyView(Context context) {
            super(context);
            holder = getHolder();
            paint = new Paint();
            compTime = System.currentTimeMillis();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            playerPic = BitmapFactory.decodeResource(getResources(), R.drawable.player, options);


            paint.setColor(Color.WHITE);
            paint.setTextAlign(Paint.Align.CENTER);


        }

        public void run() {
            while (isRunning) {
                if (!holder.getSurface().isValid()) {
                    continue;
                }
                Canvas canvas = holder.lockCanvas();

                int color = 150;
                if (state.equals("initiateShop")){
                    color = 190;
                }

                canvas.drawRGB(02, 02, color);


                if (state.equals("beginning")) {
                    width = canvas.getWidth();
                    height = canvas.getHeight();
                    beginGameMenu(canvas);
                } else if (state.equals("gameAnimation") || state.equals("shopAnimation")) {
                    beginningAnim(canvas);
                } else if (state.equals("initiateGame")) {
                    initiateGame();
                } else if (state.equals("initiateShop")) {
                    initiateShop();
                }

                if (state.equals("startGame")) {
                    input = game.animateObjects(meX, meY, input, canvas);


                    if (game.isLevelComplete()) {

                        SharedPreferences.Editor editor = spref.edit();

                        if (level % 5 == 0) {
                            editor.putInt("points", spref.getInt("points", 0) + level);
                        }

                        editor.putInt("CurrentLevel", level + 1);
                        editor.commit();
                        points = spref.getInt("points", 0);

                        level++;

                        state = "beginning";
                    }

                    if (game.isLevelLost()) {
                        state = "beginning";
                        input = false;
                    }

                }

                holder.unlockCanvasAndPost(canvas);

            }
        }


        private void beginGameMenu(Canvas canvas) {

            input = false;

            Rect topRect = new Rect(0, 0, width, height / 2);
            Paint topPaint = new Paint();
            topPaint.setColor(Color.rgb(02, 02, 190));
            canvas.drawRect(topRect, topPaint);

            level = spref.getInt("CurrentLevel", 1);

            currTime = System.currentTimeMillis();

            canvas.drawText("Shop", width / 2, height / 4, paint);

            if (currTime - compTime < 700) {

                paint.setTextSize(width / 10);

                canvas.drawText("Level " + level, width / 2, 3 * height / 4, paint);
            }

            if (currTime - compTime > 1000) {
                compTime = currTime;
            }


        }

        private void beginningAnim(Canvas canvas) {
            currTime = System.currentTimeMillis();
            input = true;


                if (currTime - compTime > 1000) {
                    if (state.equals("gameAnimation")) {
                        state = "initiateGame";
                    } else if (state.equals("shopAnimation")) {
                        state = "initiateShop";
                        canvas.drawRGB(02, 02, 190);
                    }
                    input = false;

                } else {
                    if (state.equals("gameAnimation")) {
                        double topHeight = height / 2 - (height / 2) * (double) (currTime - compTime) / 1000;
                        Rect topRect = new Rect(0, 0, width, (int) topHeight);
                        Paint topPaint = new Paint();
                        topPaint.setColor(Color.rgb(02, 02, 190));
                        canvas.drawRect(topRect, topPaint);
                    } else if (state.equals("shopAnimation")) {
                        double topHeight = height / 2 + (height / 2) * (double) (currTime - compTime) / 1000;
                        Rect topRect = new Rect(0, 0, width, (int) topHeight);
                        Paint topPaint = new Paint();
                        topPaint.setColor(Color.rgb(02, 02, 190));
                        canvas.drawRect(topRect, topPaint);
                    }
                }



        }

        private void initiateGame() {
            meX = width / 2;
            meY = height / 2;

            game = new Game(level);

            playerPic = game.initiate(width, height, meX, meY, playerPic);

            state = "startGame";
        }

        private void initiateShop() {

        }

        private void doAction(MotionEvent me) {

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            switch (me.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (state.equals("startGame") && game.getNewInput()) {
                        meX = me.getX();
                        meY = me.getY();
                        input = true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (state.equals("beginning"))
                        state = "animation";
                    if (state.equals("animation")) {
                        compTime = System.currentTimeMillis();
                        if (me.getY() > height / 2) {
                            state = "gameAnimation";
                        } else {
                            state = "shopAnimation";
                        }

                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
            }

        }


        public void pause() {
            isRunning = false;
            while (true) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
            thread = null;
        }

        public void resume() {
            isRunning = true;
            thread = new Thread(this);
            thread.start();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_draw_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
