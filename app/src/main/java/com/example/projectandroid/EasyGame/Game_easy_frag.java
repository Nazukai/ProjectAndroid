package com.example.projectandroid.EasyGame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projectandroid.EndGame.GameOver;
import com.example.projectandroid.R;
import com.example.projectandroid.Databases.SQLiteDBHandler;
import com.example.projectandroid.EndGame.Score;

import java.util.Random;


public class Game_easy_frag extends Fragment {

    private SQLiteDBHandler db;

    private double numberItem1;
    private double numberItem2;

    private Button btnItem1;
    private Button btnItem2;

    private TextView nbrItem1;
    private TextView nbrItem2;
    private TextView scoreString;
    private TextView scoring;
    private ImageView cup;

    private int finalScore;
    private String sc;

    private onMsgListennerEz msgListennerEasy;

    public Game_easy_frag() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_easy_frag, container, false);

        db = new SQLiteDBHandler(getActivity());

        //All FindViewByID
        btnItem1 = view.findViewById(R.id.item1);
        btnItem2 = view.findViewById(R.id.item2);
        nbrItem1 = view.findViewById(R.id.resultTxt1);
        nbrItem2 = view.findViewById(R.id.resultTxt2);
        scoreString = view.findViewById(R.id.scoreStr);
        scoring = view.findViewById(R.id.score);
        cup = view.findViewById(R.id.cupScore);

        //ALL Setting
        scoreString.setText("Score :");
        btnItem1.setText(getRandomString());
        btnItem2.setText(getRandomString());
        while (btnItem2.getText().toString().equals(btnItem1.getText().toString())) {       //For duplicate
            btnItem2.setText(getRandomString());
        }

        nbrItem1.setText(db.getItem(btnItem1.getText().toString()).getNumberTV());
        nbrItem2.setText(db.getItem(btnItem2.getText().toString()).getNumberTV());

        //Get new score of result fragment
        final Bundle bundleScore = getArguments();

        if(bundleScore != null){
            sc = bundleScore.getString("updateScore");
            finalScore = Integer.valueOf(sc);
            scoring.setText(sc);
        }else {
            sc = "0";
            finalScore = Integer.valueOf(sc);
            scoring.setText(sc);
        }

        //String to Double
        numberItem1 = Double.valueOf(nbrItem1.getText().toString());
        numberItem2 = Double.valueOf(nbrItem2.getText().toString());

        //View result
        nbrItem1.setVisibility(View.INVISIBLE);
        nbrItem2.setVisibility(View.INVISIBLE);

        //Events onClickButtons
        btnItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberItem1 > numberItem2) {
                    finalScore++;

                    //Send data for framgent result
                    msgListennerEasy.ResultEasy(btnItem1.getText().toString(),
                                            btnItem2.getText().toString(),
                                            nbrItem1.getText().toString(),
                                            nbrItem2.getText().toString(),
                                            String.valueOf(finalScore));

                } else {
                    Intent over = new Intent(getActivity().getApplicationContext(), GameOver.class);
                    over.putExtra("score", String.valueOf(finalScore));
                    startActivity(over);
                }
            }
        });

        btnItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberItem1 < numberItem2) {
                    finalScore++;

                    //Send data for framgent result
                    msgListennerEasy.ResultEasy(btnItem1.getText().toString(),
                                            btnItem2.getText().toString(),
                                            nbrItem1.getText().toString(),
                                            nbrItem2.getText().toString(),
                                            String.valueOf(finalScore));
                } else {
                    Intent over = new Intent(getActivity().getApplicationContext(), GameOver.class);
                    over.putExtra("score", String.valueOf(finalScore));
                    startActivity(over);
                }
            }
        });

        //Show the tab of high score
        cup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cupList = new Intent(getActivity().getApplicationContext(), Score.class);
                startActivity(cupList);
            }
        });


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;

        try{
            msgListennerEasy = (onMsgListennerEz) activity;
        }
        catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + "must implement onMessageSend...");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        msgListennerEasy = null;
    }

    public interface onMsgListennerEz{

        void ResultEasy(String btnItem1, String btnItem2, String nbrItem1, String nbrItem2, String score);
    }

    //Method to give random string from array-list in xml
    public String getRandomString(){
        String[] array = getResources().getStringArray(R.array.words);
        String randomStr = array[new Random().nextInt(array.length)];

        return randomStr;
    }
}
