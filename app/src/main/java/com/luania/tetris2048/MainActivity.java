package com.luania.tetris2048;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    AdView adView;

    LinearLayout llContainer;
    FrameLayout flGameOver;
    RelativeLayout rlMain;

    TextView tvBest;
    TextView tvScore;
    TextView tvScoreResult;
    TextView tvGameOver;

    TextView[] tvAnims = new TextView[4];

    int animatingIndex = 0;

    Button btnToLeft;
    Button btnDrop;
    Button btnToRight;

    ImageView ivNewGame;
    Button btnRestart;

    TextView[][] cards;
    int[][] datas;

    Tetromino tetromino;

    Map<Integer, Integer> mapColor = new HashMap<>();

    int score = 0;

    SharedPreferences sp;

    int record = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-7058546042833523~2137689665");
        initViews();
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        sp = getSharedPreferences("data", MODE_PRIVATE);

        String strDatas = sp.getString("datas", "");
        if (!"".equals(strDatas)) {
            datas = new Gson().fromJson(strDatas, int[][].class);
        }
        score = sp.getInt("score", 0);
        record = sp.getInt("record", 0);
        tvBest.setText("Best: " + record);

        mapColor.put(0, R.color.color0);
        mapColor.put(2, R.color.color2);
        mapColor.put(4, R.color.color4);
        mapColor.put(8, R.color.color8);
        mapColor.put(16, R.color.color16);
        mapColor.put(32, R.color.color32);
        mapColor.put(64, R.color.color64);
        mapColor.put(128, R.color.color128);
        mapColor.put(256, R.color.color256);
        mapColor.put(512, R.color.color512);
        mapColor.put(1024, R.color.color1024);
        mapColor.put(2048, R.color.color2048);

        btnToRight.setOnClickListener(view -> {
            tetromino.right(7);
            drawAll();
        });
        btnToLeft.setOnClickListener(view -> {
            tetromino.left();
            drawAll();
        });

        btnDrop.setOnClickListener(view -> {
            drop();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> {
                        animatingIndex = 0;
                        saveSpDatas();
                        updateRecord();
                        drawScore();
                        if (isGameOver()) {
                            clearSpDatas();
                            tetromino.setCells(new ArrayList<>());
                            drawCards();
                            flGameOver.setVisibility(View.VISIBLE);
                        } else {
                            tetromino = Tetromino.random();
                            drawAll();
                        }
                    });
                }
            }, 100);
        });

        btnRestart.setOnClickListener(view -> {
            tvGameOver.setText("Game Over");
            flGameOver.setVisibility(View.GONE);
            newGame(12, 8);
        });

        ivNewGame.setOnClickListener(view -> new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.confirm)
                .setMessage(R.string.confirm_new_game)
                .setNegativeButton(R.string.confirm_no, null)
                .setPositiveButton(R.string.confirm_yes, (dialogInterface, i) -> newGame(12, 8))
                .create().show());
        tetromino = Tetromino.random();
        datas = new int[12][8];
        generateCards(12, 8);
        drawAll();
    }

    private void newGame(int height, int width) {
        datas = new int[height][width];
        generateCards(height, width);
        score = 0;
        tetromino = Tetromino.random();
        drawAll();
    }

    private void initViews() {
        tvBest = findViewById(R.id.tvBest);
        tvScore = findViewById(R.id.tvScore);
        tvScoreResult = findViewById(R.id.tvScoreResult);
        tvGameOver = findViewById(R.id.tvGameOver);

        btnToLeft = findViewById(R.id.btnToLeft);
        btnDrop = findViewById(R.id.btnDrop);
        btnToRight = findViewById(R.id.btnToRight);

        ivNewGame = findViewById(R.id.ivNewGame);
        btnRestart = findViewById(R.id.btnRestart);

        flGameOver = findViewById(R.id.flGameOver);
        llContainer = findViewById(R.id.llContainer);
        rlMain = findViewById(R.id.rlMain);

        adView = findViewById(R.id.adView);
        tvAnims[0] = findViewById(R.id.tvAnim1);
        tvAnims[1] = findViewById(R.id.tvAnim2);
        tvAnims[2] = findViewById(R.id.tvAnim3);
        tvAnims[3] = findViewById(R.id.tvAnim4);
        generateCards(12, 8);
    }

    private void generateCards(int height, int width) {
        cards = new TextView[height][width];
        llContainer.removeAllViews();
        for (int i = 0; i < height; i++) {
            LinearLayout llRow = (LinearLayout) getLayoutInflater().inflate(R.layout.row, null);
            for (int j = 0; j < width; j++) {
                View vCell = getLayoutInflater().inflate(R.layout.cell, null);
                llRow.addView(vCell);
                cards[i][j] = vCell.findViewById(R.id.tvCard);
            }
            llContainer.addView(llRow);
        }
    }

    private boolean isGameOver() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 8; j++) {
                if (datas[i][j] != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private void saveSpDatas() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("datas", new Gson().toJson(datas));
        editor.putInt("score", score);
        editor.apply();
    }

    private void clearSpDatas() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("datas", "");
        editor.putInt("score", 0);
        editor.apply();
    }

    private void updateRecord() {
        if (score > record) {
            record = score;
            sp.edit().putInt("record", record).apply();
            tvGameOver.setText(R.string.new_high);
            tvBest.setText("Best: " + record);
        }
    }

    private void drop() {
        int scOrigin = score;
        for (Cell cell : tetromino.getCells()) {
            dropCell(cell);
        }
        animScore(score - scOrigin);
    }

    private void dropCell(Cell cell) {
        int x = cell.getX();
        final int num = cell.getNum();
        boolean isEmptyColumn = true;
        if (datas[1][x] != 0) {
            datas[0][x] = num;
        }
        int toY = cell.getY();
        for (int i = 2; i < 12; i++) {
            int nextNum = datas[i][x];
            int cellNum = cell.getNum();
            if (nextNum == 0) {
            } else if (nextNum == cellNum) {
                datas[i][x] = 0;
                score += nextNum + cellNum;
                cell.setNum(nextNum + cellNum);
            } else {
                datas[i - 1][x] = cellNum;
                toY = i - 1;
                isEmptyColumn = false;
                break;
            }
        }
        if (isEmptyColumn) {
            toY = 11;
            datas[11][x] = cell.getNum();
        }
        anim(cell.getX(), cell.getY(), toY, num);
    }

    private void anim(final int x, final int y, final int toY, final int num) {
        final TextView tvAnim = tvAnims[animatingIndex];
        animatingIndex++;
        final View animView = (View) tvAnim.getParent();

        final View fromView = (View) cards[y][x].getParent().getParent();
        final View toView = (View) cards[toY][x].getParent().getParent();

        final int[] fromPos = new int[2];
        final int[] toPos = new int[2];
        fromView.getLocationInWindow(fromPos);
        toView.getLocationInWindow(toPos);

        final ValueAnimator animator = ValueAnimator.ofInt(fromPos[1], toPos[1]);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                drawCellView(tvAnim, num);
                animView.setTranslationX(fromPos[0]);
                animView.setTranslationY(fromPos[1]);
                animView.setVisibility(View.VISIBLE);
                drawCell(y, x, 0);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.addUpdateListener(valueAnimator -> {
            Integer value = (Integer) valueAnimator.getAnimatedValue();
            animView.setTranslationY(value);
        });
        animator.setDuration(100);
        animator.start();
    }

    private void drawAll() {
        drawCards();
        drawTetromino();
        drawScore();
    }

    private void drawScore() {
        tvScore.setText("" + score);
        tvScoreResult.setText("Score： " + score);
    }


    private void drawCards() {
        for (int i = 0; i < datas.length; i++) {
            for (int j = 0; j < datas[i].length; j++) {
                drawCell(i, j, datas[i][j]);
            }
        }
    }

    private void drawTetromino() {
        for (Cell cell : tetromino.getCells()) {
            drawCell(cell.getY(), cell.getX(), cell.getNum());
        }
    }

    private void drawCell(int y, int x, int num) {
        TextView tv = cards[y][x];
        drawCellView(tv, num);
        if (num == 0 && y < 2) {
            tv.setBackgroundColor(getResources().getColor(R.color.colorCardDisabled));
        }
    }

    private void drawCellView(TextView tv, int num) {
        tv.setText(num == 0 ? "" : ("" + num));
        tv.setBackgroundColor(getResources().getColor(mapColor.get(num)));
        int textSize;
        if (num < 128) {
            textSize = 20;
        } else if (num < 1024) {
            textSize = 16;
        } else {
            textSize = 12;
        }
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }

    private void animScore(int sc) {
        final int[] fromPos = new int[2];
        tvScore.getLocationInWindow(fromPos);

        final TextView tvAnim = (TextView) getLayoutInflater().inflate(R.layout.score_anim_view, null);
        tvAnim.setText("+" + sc);
        tvAnim.setTranslationX(fromPos[0]);
        rlMain.addView(tvAnim);

        ValueAnimator animator = ValueAnimator.ofFloat(fromPos[1], fromPos[1] - 50);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                tvAnim.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                tvAnim.setVisibility(View.GONE);
                rlMain.removeView(tvAnim);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.setDuration(500);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(valueAnimator -> {
            Float value = (Float) valueAnimator.getAnimatedValue();
            tvAnim.setTranslationY(value);
            tvAnim.setAlpha(1 - (fromPos[1] - value) / 50);
        });
        animator.start();
    }
}

