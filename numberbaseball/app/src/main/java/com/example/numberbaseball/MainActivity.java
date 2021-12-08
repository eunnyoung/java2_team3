/**
* 프로젝트 참여자: 서은영, 이규연
* 프로젝트 주제: 안드로이트 스튜디오 프로그램을 이용한 숫자야구게임 제작
* */

package com.example.numberbaseball;

import androidx.appcompat.app.AppCompatActivity;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // 랜덤으로 정해진 정수 3개 넣을 수 있는 배열이다.
    int[] comNumbers = new int[3];
    int inputTextCount = 0;
    int hitCount = 1;

    // 랜덤 숫자 3개를 보여주는 텍스트뷰이다.
    TextView[] inputTextView = new TextView[3];
    Button[] btnNum = new Button[10];

    ImageButton btnBackSpace;
    ImageButton btnHit;

    TextView resultTextView;
    ScrollView scrollView;

    SoundPool soundPool;
    int[] buttonSound = new int[5];

    /**
    * 버튼들이 클릭되었을 때 실행할 메소드들을 지정한 메소드이다.
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        comNumbers = getComNumbers();

        for (int i = 0; i < inputTextView.length; i++) {
            inputTextView[i] = findViewById(R.id.input_text_view_0 + i);
        }

        for (int i = 0; i < btnNum.length; i++) {
            btnNum[i] = findViewById(R.id.num_button_0 + i);
        }

        btnBackSpace = findViewById(R.id.back_space_button);
        btnHit = findViewById(R.id.hit_button);
        resultTextView = findViewById(R.id.result_text_view);
        scrollView = findViewById(R.id.scroll_view);

        // for문이 반복 되면서 btnNum을 getbtnNum으로 던진다.
        for(Button getbtnNum : btnNum) {
            getbtnNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnNumClick(view);
                }
            });
        }

        // btnBackSpace 클릭 시의 동작을 정의한다.
        btnBackSpace.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                btnBackSpaceClick();
            }
        });

        // btnHit 클릭 시의 동작을 정의한다.
        btnHit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                btnHitClick();
            }
        });
    }

    /**
    * btnHit 클릭 시 실행할 메소드이다.
    * */
    private void btnHitClick(){
        // 선택된 숫자가 3개 미만일 경우에 실행할 조건문이다.
        if (inputTextCount < 3) {
            Toast.makeText(getApplicationContext(), "숫자를 입력해 주세요", Toast.LENGTH_SHORT).show();
        // 선택된 숫자가 3개일 경우에 실행할 조건문이다.
        } else {
            int[] userNumbers = new int[3];

            for (int i = 0; i < userNumbers.length; i++) {
                userNumbers[i] = Integer.parseInt(inputTextView[i].getText().toString());
            }

            int[] countCheck = new int[2];
            countCheck = getCountCheck(comNumbers, userNumbers);
            Log.e("btnHit", "countCheck = S : " + countCheck[0] + " B : " + countCheck[1]);

            String resultCount = getCountString(userNumbers, countCheck);

            if (hitCount == 1) {
                resultTextView.setText(resultCount + "\n");
            } else {
                resultTextView.append(resultCount + "\n");
            }

            if (countCheck[0] == 3) {
                hitCount = 1;
                comNumbers = getComNumbers();
            } else {
                hitCount++;
            }

            scrollView.fullScroll(View.FOCUS_DOWN);

            for (int i =0; i< inputTextView.length; i++){
                btnBackSpaceClick();
            }

            inputTextCount = 0;
        }
    }

    /**
    * 입력된 값과 정답 값을 비교한 결과를 텍스트로 출력하는 메소드이다.
    * */
    private String getCountString(int[] userNumbers, int[] countCheck){
        String resultCount;

        if (countCheck[0] == 3) {
            resultCount = hitCount + "  [" + userNumbers[0] + " " + userNumbers[1] + " " + userNumbers[2]
                    + "] 아웃입니다.";
            soundPool.play(buttonSound[0], 1, 1, 1, 0, 1);
        } else {
            resultCount = hitCount + "  [" + userNumbers[0] + " " + userNumbers[1] + " " + userNumbers[2]
                    + "] s: " + countCheck[0] + " B : " + countCheck[1];
            soundPool.play(buttonSound[4], 1, 1, 1, 0, 1);
        }
        return resultCount;
    }

    /**
    * backSpace 클릭 시 실행할 메소드이다.
    * */
    private void btnBackSpaceClick(){
        // 입력된 숫자가 있을 경우에 실행할 조건문이다.
        if(inputTextCount > 0){
            // Text를 int로 형변환한다.
            int buttonEnableCount = Integer.parseInt(inputTextView[inputTextCount-1].getText().toString());
            // 숫자버튼이 선택되기 이전 상태로 돌아간다.
            btnNum[buttonEnableCount].setEnabled(true);
            inputTextView[inputTextCount-1].setText("");
            inputTextCount--;
            soundPool.play(buttonSound[3], 1, 1, 1, 0, 1);
            // 입력된 숫자가 없을 경우, 다음과 같은 text를 출력한다.
        } else {
            Toast.makeText(getApplicationContext(), "숫자를 입력해 주세요", Toast.LENGTH_SHORT).show();
        }
    }

    /**
    * btnNum 클릭 시 실행할 메소드이다.
    * */
    private void btnNumClick(View view) {
        if(inputTextCount < 3) {
            Button button = findViewById(view.getId());
            // 버튼에 있는 숫자값을 받아온다.
            inputTextView[inputTextCount].setText(button.getText().toString());
            // 중복된 숫자를 입력받을 수 없기 때문에 한 번 눌린 버튼은 못 누르게 한다.
            button.setEnabled(false);
            inputTextCount++;
            soundPool.play(buttonSound[2], 1,1, 1, 0, 1);
        }
        // 숫자 3개 누르고 더 누르려고 할 때 알림 뜬다.
        else {
            Toast.makeText(getApplicationContext(), "히트 버튼을 눌러주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
    * 입력된 값과 정답 값을 비교하는 메소드이다.
    * */
    private int[] getCountCheck(int[] comNumbers, int[] userNumbers){
        int [] setCount = new int[2];
        for (int i = 0; i < comNumbers.length; i++){
            for(int j = 0; j < userNumbers.length; j++){
                // comNumbers[i] 와 userNumbers[j] 가 다르면 조건문을 실행하지 않고 빠져나간다.
                if(comNumbers[i] == userNumbers[j]){
                    if(i == j){
                        setCount[0]++;
                    }else {
                        setCount[1]++;
                    }
                }
            }
        }
        return setCount;
    }

    /**
    * 정답 값을 생성하는 메소드이다.
    * */
    public int[] getComNumbers() {
        int[] setComNumbers = new int[3];
        // for문을 돌려서 랜덤 숫자 3개를 setComNumbers에 넣는다.
        for (int i = 0; i < setComNumbers.length; i++) {
            /**
             *  i ->0, 1, 2일때 랜덤 숫자 생성한다.
             *  0부터 9까지의 숫자가 setComNumbers에 들어간다.
             */
            setComNumbers[i] = new Random().nextInt(10);
            // 배열에 같은 숫자가 들어가면 안 되기 때문에
            for (int j = 0; i < i; i++) {
                if (setComNumbers[i] == setComNumbers[j]) {
                    // 만약에 같으면 랜덤숫자를 다시 받아야 하기 때문에 for문을 다시 돌게끔 한다.
                    i--;
                    break;
                }
            }
        }
        Log.e("setComNumbers", "setComNumbers = " + setComNumbers[0] + ", " + setComNumbers[1] + ", " + setComNumbers[2]);
        return setComNumbers;
    }

    /**
    * 사운드 파일을 실행시키기 위해 필요한 메소드이다.
    * */
    protected void onStart(){
        super.onStart();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(6)
                    .build();
        } else {
            soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        }

        for (int i = 0; i < buttonSound.length ; i++){
            buttonSound[i] = soundPool.load(getApplicationContext(), R.raw.button1 + i, 1);
        }
    }

    /**
     * 리소스를 해제하는 메소드이다.
     * */
    @Override
    // 리소스를 해제하는 메소드이다.
    protected void onStop(){
        super.onStop();
        soundPool.release();
    }
}
