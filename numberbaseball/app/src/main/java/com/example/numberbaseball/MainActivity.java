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
    int[] randomNum = new int[3];
    // 내가 숫자를 몇 번 눌렀는지 카운트 세는 int형 변수
    int inputNumCount = 0;
    // hit 버튼을 몇 번 눌렀는지 카운트 세는 int형 변수
    int hitCount = 1;

    // 내가 누른 숫자 3개를 보여주는 텍스트뷰이다.
    TextView[] clickNum = new TextView[3];
    // 내가 누를 수 있는 숫자 버튼(0~9)
    Button[] btnNum = new Button[10];

    ImageButton btnBackSpace;
    ImageButton btnHit;

    TextView resultTextView;
    ScrollView scrollView;

    SoundPool soundPool;
    int[] btnsound = new int[5];

    /**
    * 버튼들이 클릭되었을 때 실행할 메소드들을 지정한 메소드이다.
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        randomNum = getrandomNum();

        for (int i = 0; i < clickNum.length; i++) {
            clickNum[i] = findViewById(R.id.input_text_view_0 + i);
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
        // 선택된 숫자가 3개 미만일 경우에 다음과 같은 Text가 뜨도록 한다.
        if (inputNumCount < 3) {
            Toast.makeText(getApplicationContext(), "숫자를 입력해 주세요", Toast.LENGTH_SHORT).show();
        // 선택된 숫자가 3개일 경우에 실행할 조건문이다.
        } else {
            int[] userNum = new int[3];

            for (int i = 0; i < userNum.length; i++) {
                userNum[i] = Integer.parseInt(clickNum[i].getText().toString());
            }
            /*
            * getCountCheck 메소드에서 strike와 ball에 대해 카운트 되어있던 것을
            * countCheck에 다시 넣는다
            * */
            int[] countCheck = new int[2];
            countCheck = getCountCheck(randomNum, userNum);
            Log.e("btnHit", "countCheck = S : " + countCheck[0] + " B : " + countCheck[1]);

            String resultCount = getCountString(userNum, countCheck);

            /*
            * setText는 기존에 있는 내용을 지우고 새롭게 셋팅해주는 메소드
            * append는 기존 내용을 유지한채 뒤에 내용을 붙이는 메소드
            * */
            if (hitCount == 1) {
                resultTextView.setText(resultCount + "\n");
            } else {
                resultTextView.append(resultCount + "\n");
            }

            /*
            * Strike가 3개 나오면 hit count를 1로 초기화 하고 새로운 랜덤 숫자 3개를 만든다.*/
            if (countCheck[0] == 3) {
                hitCount = 1;
                randomNum = getrandomNum();
            } else {
                hitCount++;
            }

            scrollView.fullScroll(View.FOCUS_DOWN);

            for (int i =0; i< clickNum.length; i++){
                btnBackSpaceClick();
            }

            inputNumCount = 0;
        }
    }

    /**
    * 입력된 값과 정답 값을 비교한 결과를 텍스트로 출력하는 메소드이다.
    * */
    private String getCountString(int[] userNum, int[] countCheck){
        String resultCount;

        // Strike의 카운트가 3일 때
        if (countCheck[0] == 3) {
            resultCount = hitCount + "  [" + userNum[0] + " " + userNum[1] + " " + userNum[2]
                    + "] 아웃입니다.";
            soundPool.play(btnsound[0], 1, 1, 1, 0, 1);
        } else {
            resultCount = hitCount + "  [" + userNum[0] + " " + userNum[1] + " " + userNum[2]
                    + "] s: " + countCheck[0] + " B : " + countCheck[1];
            soundPool.play(btnsound[4], 1, 1, 1, 0, 1);
        }
        return resultCount;
    }

    /**
    * backSpace 클릭 시 실행할 메소드이다.
    * */
    private void btnBackSpaceClick(){
        // 입력된 숫자가 있을 경우에 실행할 조건문이다.
        if(inputNumCount > 0){
            // 눌렀던 버튼의 숫자를 string형으로 받고 Int형으로 변환한다
            int btnEnableNum = Integer.parseInt(clickNum[inputNumCount-1].getText().toString());
            // 숫자를 지우면 눌렸던 버튼을 다시 누를 수 있도록 재활성화 한다.
            btnNum[btnEnableNum].setEnabled(true);
            clickNum[inputNumCount-1].setText("");
            inputNumCount--;
            soundPool.play(btnsound[3], 1, 1, 1, 0, 1);
            // 입력된 숫자가 없을 경우, 다음과 같은 text를 출력한다.
        } else {
            Toast.makeText(getApplicationContext(), "숫자를 입력해 주세요", Toast.LENGTH_SHORT).show();
        }
    }

    /**
    * btnNum 클릭 시 실행할 메소드이다.
    * */
    private void btnNumClick(View view) {
        if(inputNumCount < 3) {
            Button button = findViewById(view.getId());
            // 버튼에 있는 숫자값을 받아온다.
            clickNum[inputNumCount].setText(button.getText().toString());
            // 중복된 숫자를 입력받을 수 없기 때문에 한 번 눌린 버튼은 못 누르게 한다.
            button.setEnabled(false);
            inputNumCount++;
            soundPool.play(btnsound[2], 1,1, 1, 0, 1);
        }
        // 숫자 3개 누르고 더 누르려고 할 때 알림 뜬다.
        else {
            Toast.makeText(getApplicationContext(), "히트 버튼을 눌러주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
    * 입력된 값과 정답 값(랜덤 숫자)을 비교하여 strike와 ball을 카운트하는 메소드이다.
     * @param randomNum > 정답 값(랜덤 숫자)
     * @param userNum > 유저가 입력한 값
    * */
    private int[] getCountCheck(int[] randomNum, int[] userNum){
        // Strike와 Ball에 대한 카운트가 각 인덱스에 카운트 되어있는 int형 배열 setCount
        int [] setCount = new int[2];
        for (int i = 0; i < randomNum.length; i++){
            for(int j = 0; j < userNum.length; j++){
                // randomNum[i] 와 userNum[j] 가 다르면 조건문을 실행하지 않고 빠져나간다.
                if(randomNum[i] == userNum[j]){
                    /*
                    * 랜덤 숫자와 유저 입력 값이 같고, 위치마저 똑같을 때 Strike
                    * 위치만 다르면 Ball로 카운트를 누적시킨다.
                    * */
                    if(i == j){
                        setCount[0]++; //Strike
                    }else {
                        setCount[1]++; //Ball
                    }
                }
            }
        }
        return setCount;
    }

    /**
    * 정답 값을 생성하는 메소드이다.
    * */
    public int[] getrandomNum() {
        int[] setrandomNum = new int[3];
        // for문을 돌려서 랜덤 숫자 3개를 setrandomNum에 넣는다.
        for (int i = 0; i < setrandomNum.length; i++) {
            /**
             *  i ->0, 1, 2일때 랜덤 숫자 생성한다.
             *  0부터 9까지의 숫자가 setrandomNum에 들어간다.
             */
            setrandomNum[i] = new Random().nextInt(10);
            /*
            * 각 배열에 같은 숫자가 들어가면 안 된다.
            * 만약에 같으면 랜덤숫자를 다시 받아야 하기 때문에 i를 감소시켜 for문을 다시 돌아 랜덤 숫자를 생성한다.
            * */
            for (int j = 0; j < i; j++) {
                if (setrandomNum[i] == setrandomNum[j]) {
                    i--;
                    break;
                }
            }
            Log.e("setrandomNum", "setrandomNum [" + i + "]" + "= " + setrandomNum[i]);
        }
        Log.e("setrandomNum", "setrandomNum = " + setrandomNum[0] + ", " + setrandomNum[1] + ", " + setrandomNum[2]);
        return setrandomNum;
    }

    /**
    * 사운드 파일을 실행시키기 위해 필요한 메소드이다.
    * */
    protected void onStart(){
        super.onStart();
        //SDK의 버전이 롤리팝 보다 클 때
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

        for (int i = 0; i < btnsound.length ; i++){
            btnsound[i] = soundPool.load(getApplicationContext(), R.raw.button1 + i, 1);
        }
    }

    /**
     * 사운드 리소스를 해제하는 메소드이다.
     * */
    @Override
    protected void onStop(){
        super.onStop();
        soundPool.release();
    }
}
