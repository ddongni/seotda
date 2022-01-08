package com.jd.sutda;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private boolean[] isSelected = new boolean[21];
    private int[][] results = new int[21][21];
    private List<Integer> selectedCards = new LinkedList<>();

    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* 선택된 두 카드 값에 따른 결과 값 저장 */
        results[3][8] = results[8][3] = 1;
        results[4][9] = results[9][4] = 2;
        results[4][7] = results[7][4] = 3;
        results[1][3] = results[3][1] = results[1][8] = results[8][1] = 4;
        results[9][14] = results[14][9] = results[19][4] = results[4][19] = results[19][14] = results[14][19] = 5;
        results[3][7] = results[7][3] = results[3][17] = results[17][3] = results[13][7] = results[7][13] = results[13][17] = results[17][13] = 6;
        for(int i = 1; i<=10; i++){
            results[i][i+10] = results[i+10][i] = 7;
        }
        results[1][2] = results[2][1] = results[1][12] = results[12][1] = results[11][2] = results[2][11] = results[11][12] = results[12][11] = 8;
        results[1][4] = results[4][1] = results[1][14] = results[14][1] = results[11][4] = results[4][11] = results[11][14] = results[14][11] = 9;
        results[1][9] = results[9][1] = results[1][19] = results[19][1] = results[11][9] = results[9][11] = results[11][19] = results[19][11] = 10;
        results[1][10] = results[10][1] = results[1][20] = results[20][1] = results[11][10] = results[10][11] = results[11][20] = results[20][11] = 11;
        results[4][10] = results[10][4] = results[4][20] = results[20][4] = results[14][10] = results[10][14] = results[14][20] = results[20][14] = 12;
        results[6][4] = results[4][6] = results[6][14] = results[14][6] = results[16][4] = results[4][16] = results[16][14] = results[14][16] = 13;


        /* 배너 */
        MobileAds.initialize(this, new OnInitializationCompleteListener() {

            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }

        });
        adView = findViewById(R.id.adView); //배너광고 레이아웃 가져오기
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER); //광고 사이즈는 배너 사이즈로 설정
        adView.setAdUnitId("ca-app-pub-6854372688341522/2728854828");

        ImageButton resetBtn = (ImageButton) findViewById(R.id.imageButton);
        resetBtn.setOnClickListener(new ImageButton.OnClickListener(){
           @Override
           public void onClick(View view){
               //투명도 초기화, isSelected에 선택 해제 (false 값으로) 표시
               for(int i =0;i<selectedCards.size();i++) {
                   ImageView card = (ImageView) findViewById(getResources().getIdentifier("imageView" + selectedCards.get(i), "id", getPackageName()));
                   card.setColorFilter(null);
                   isSelected[selectedCards.get(i)] = false;
               }

               resetResults();

               selectedCards = new LinkedList<>();
           }
        });

    }

    public void showSelectedImage(View view){

        // 클릭된 뷰를 버튼으로 받아옴
        String imageId = view.getResources().getResourceEntryName(view.getId());
        int id = Integer.parseInt(imageId.replace("imageView",""));

        //이미 선택된 버튼일 때
        if(isSelected[id]){

            //투명도 초기화, isSelected에 선택 해제 (false 값으로) 표시
            ImageView card = (ImageView)findViewById(view.getId());
            card.setColorFilter(null);
            isSelected[id] = false;

            resetResults();

            //선택된 카드 찾아서 list에서 제거
            for(int i=0;i<selectedCards.size();i++){
                if(selectedCards.get(i)==id){
                    selectedCards.remove(i);
                    break;
                }
            }

        //선택되지 않은 버튼일 때(새로 선택됐을 때)
        }else{

            if(selectedCards.size()==2){
                return;
            }else if(selectedCards.size()==1){
                selectedCards.add(id);

                //count가 2가 되어서 결과값 보여주기
                int result = results[selectedCards.get(0)][selectedCards.get(1)];
                if(result == 0){
                    int sum = (selectedCards.get(0) + selectedCards.get(1)) % 10;
                    if(sum==9)
                        result = 14;
                    else if(sum==0)
                        result = 16;
                    else
                        result = 15;
                }

                /* 우측 textView 배열에서 결과값 보여주기 */
                TextView resultTextView = (TextView) findViewById(getResources().getIdentifier("textView" +result, "id", getPackageName()));
                resultTextView.setTextColor(Color.WHITE);
                resultTextView.setBackgroundColor(Color.GRAY);

                /* 상단 textView로 결과값 보여주기 */
                TextView resultText = (TextView) findViewById(R.id.resultText);
                //끗일 때 무슨 끗인지 표시
                if(result==15){
                    int sum = (selectedCards.get(0) + selectedCards.get(1)) % 10;
                    resultText.setText(sum+"끗");
                //땡일 때 무슨 땡인지 표시
                }else if(result==7){
                    if(selectedCards.get(0)%10==0){
                        resultText.setText("장땡");
                    }else{
                        resultText.setText(selectedCards.get(0)%10+"땡");
                    }
                //나머지는 결과 textView의 text값 표시
                }else if(result==2) {
                    resultText.setText("멍텅구리 구사(재경기)");
                }else if(result==3) {
                    resultText.setText("암행어사(상대방 족보 중 광땡이 없다면 1끗)");
                }else if(result==6) {
                    resultText.setText("땡잡이(상대방 족보 중 땡이 없다면 망통)");
                }else{
                    resultText.setText(resultTextView.getText());
                }
            }else if(selectedCards.size()==0){
                selectedCards.add(id);
            }

            //선택된 카드 투명도 주기, isSelected에 선택되었다고 (true 값으로) 표시
            ImageView card = (ImageView)findViewById(view.getId());
            card.setColorFilter(Color.parseColor("#80000000"));
            isSelected[id] = true;

        }

    }

    public void resetResults(){
        if(selectedCards.size()==2) {
            int result = results[selectedCards.get(0)][selectedCards.get(1)];
            if (result == 0) {
                int sum = (selectedCards.get(0) + selectedCards.get(1)) % 10;
                if (sum == 9)
                    result = 14;
                else if (sum == 0)
                    result = 16;
                else
                    result = 15;
            }

            /* 우측 textView 배열에서 결과값 보여준 것 되돌리기 */
            TextView resultTextView = (TextView) findViewById(getResources().getIdentifier("textView" + result, "id", getPackageName()));
            resultTextView.setTextColor(Color.BLACK);
            resultTextView.setBackgroundResource(R.drawable.borderline);

            /* 상단 textView로 보여준 결과값 지우기 */
            TextView resultText = (TextView) findViewById(R.id.resultText);
            resultText.setText("족보 순위");
        }
    }
}