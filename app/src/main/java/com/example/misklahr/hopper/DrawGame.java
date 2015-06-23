package com.example.misklahr.hopper;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;


public class DrawGame extends Activity implements View.OnTouchListener {

    private MyView surfaceView;
    private boolean input = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        private int level;

        private float meX, meY;
        private boolean beginning = true;
        private boolean initiateGame = false;
        private boolean startGame = false;

        private int width;
        private int height;

        private Game game;




        public MyView(Context context) {
            super(context);
            holder = getHolder();
            paint = new Paint();
            compTime = System.currentTimeMillis();

            level = 1;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            playerPic = BitmapFactory.decodeResource(getResources(),R.drawable.player,options);



            paint.setColor(Color.WHITE);
            paint.setTextAlign(Paint.Align.CENTER);

        }

        public void run() {
            while (isRunning) {
                if (!holder.getSurface().isValid()) {
                    continue;
                }
                Canvas canvas = holder.lockCanvas();
                canvas.drawRGB(02, 02, 150);


                if (beginning && !startGame) {
                    width = canvas.getWidth();
                    height = canvas.getHeight();
                    beginGameMenu(canvas);
                } else if (initiateGame && !startGame) {
                    initiateGame();
                } else {
//                    canvas.drawText("In air: " + player.isInAir(), 300, 100, paint);
//                    canvas.drawText("Jump: " + player.jumpReady(), 300, 300, paint);
                    input = game.animateObjects(meX, meY, input, canvas);

                    if (game.isLevelComplete()){
                        level++;
                        beginning = true;
                        initiateGame = true;
                        startGame = false;
                    }

                    if (game.isLevelLost()){
                        beginning = true;
                        initiateGame = true;
                        startGame = false;
                        input = false;
                    }

                }

                holder.unlockCanvasAndPost(canvas);

            }
        }


        private void beginGameMenu(Canvas canvas) {
            currTime = System.currentTimeMillis();
            if (currTime - compTime < 700) {

                paint.setTextSize(width / 10);

                canvas.drawText("Level " + level, width / 2, height / 2, paint);
            }

            if (currTime - compTime > 1000) {
                compTime = currTime;
            }
        }

        private void initiateGame() {
            meX = width / 2;
            meY = height / 2;

            game = new Game(level);

            playerPic = game.initiate(width, height, meX, meY, playerPic);

            initiateGame = false;
            startGame = true;
        }

        private void doAction(MotionEvent me) {

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            switch (me.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (startGame && game.getNewInput()) {
                        meX = me.getX();
                        meY = me.getY();
                        input = true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (beginning)
                        beginning = false;
                    if (!startGame)
                        initiateGame = true;
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
