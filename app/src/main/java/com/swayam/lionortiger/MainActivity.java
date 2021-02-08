package com.swayam.lionortiger;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity{

    private final String player1 = "Lion", player2 = "Tiger";
    private Player[] allPlaces = new Player[9];
    private Player currentPlayer = Player.ONE;
    private boolean isAnimationRunning = false;
    private static final int ANIMATION_DURATION = 300;
    private boolean gameOver = false;
    private final int[][] possibles = {{0,1,2},{3,4,5},{6,7,8},
                                    {0,3,6},{1,4,7},{2,5,8},
                                        {0,4,8},{2,4,6}};


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAll();

    }

    private void initAll(){
        gameOver = false;

        allPlaces = new Player[9];
        for(int i=0;i<allPlaces.length;i++){
            allPlaces[i] = Player.EMPTY;
        }
        currentPlayer = Player.ONE;

        GridLayout gridLayout =  findViewById(R.id.gridLayout);
        for(int i=0;i<gridLayout.getChildCount();i++){
            ImageView child = (ImageView)gridLayout.getChildAt(i);
            child.setImageDrawable(null);
        }
    }

    public void tappedImage(View imageView){

        if (isAnimationRunning){
            return;
        }

        ImageView image = (ImageView)imageView;
        int imgTag = Integer.parseInt(image.getTag().toString());
        if(allPlaces[imgTag] != Player.EMPTY || gameOver){
            return;
        }

        float x = image.getScaleX();
        float y = image.getScaleY();
        image.setScaleX(0);
        image.setScaleY(0);
        image.animate().scaleX(x).scaleY(y).setDuration(ANIMATION_DURATION);

        isAnimationRunning = true;

        if(currentPlayer == Player.ONE){
            image.setImageResource(R.mipmap.lion);
            allPlaces[imgTag] = Player.ONE;
        }
        else {
            image.setImageResource(R.mipmap.tiger);
            allPlaces[imgTag] = Player.TWO;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isAnimationRunning = false;
                onAnimationComplete();
            }
        },ANIMATION_DURATION);

    }

    public void onAnimationComplete(){
        for(int[] value : possibles){
            if(allPlaces[value[0]]==currentPlayer && allPlaces[value[1]]==currentPlayer && allPlaces[value[2]]==currentPlayer){
                gameOver = true;
                if(currentPlayer == Player.ONE){
                    showWonAlertDialogue(player1);
                }else {
                    showWonAlertDialogue(player2);
                }
                return;
            }
        }

        if(isAllPlacesEmpty()){
            gameOver = true;
            showTieAlertDialogue();
        }

        switchCurrentPlayer();
    }

    private void showTieAlertDialogue(){
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(" The Game tied")
                .setPositiveButton("PLAY AGAIN", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        initAll();
                    }
                })
                .setNegativeButton("EXIT", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        System.exit(0);
                    }
                })
                .create()
                .show();
    }

    private boolean isAllPlacesEmpty(){
        for(int i=0;i<allPlaces.length;i++){
            if(allPlaces[i] == Player.EMPTY){
                return false;
            }
        }return true;
    }

    private void showWonAlertDialogue(String player){
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(player+" won the game")
                .setPositiveButton("PLAY AGAIN", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        initAll();
                    }
                })
                .setNegativeButton("EXIT", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        System.exit(0);
                    }
                })
                .create()
                .show();
    }

    private void switchCurrentPlayer(){
        if(currentPlayer == Player.ONE){
            currentPlayer = Player.TWO;
        }else {
            currentPlayer = Player.ONE;
        }
    }

    enum Player{
        EMPTY, ONE, TWO;
    }
}