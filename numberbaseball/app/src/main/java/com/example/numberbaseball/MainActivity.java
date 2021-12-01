package com.example.numberbaseball;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // 숫자 맞추기 시도 첫번째.
    int hitCount = 1;

    ImageButton backSpaceButton;
    ImageButton hitButton;

    TextView resultTextView;
    ScrollView scrollView;

    SoundPool soundPool;
    int[] buttonSound = new int[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backSpaceButton = findViewById(R.id.back_space_button);
        hitButton = findViewById(R.id.hit_button);
        resultTextView = findViewById(R.id.result_text_view);
        scrollView = findViewById(R.id.scroll_view);
    }

    BackSpaceButton back = new BackSpaceButton();

    // backSpaceButton 클릭 시의 동작을 정의한다.
    backSpaceButton.setOnClickListener(new View.OnClickListener(){
        @Override
        Public void onClick(View view){
            // backSpaceButton 이 실행될 조건을 설정한다. // 입력된 숫자가 있을 경우
            if(inputTextCount > 0){
                // Text를 int로 형변환한다.
                int buttonEnableCount = Integer.parseInt(inputTextView[inputTextCount-1].getText().toString());
                // 숫자버튼이 선택되기 이전 상태로 돌아간다.
                numButton[buttonEnableCount].setEnabled(true);
                inputTextView[inputTextCount-1].setText("");
                inputTextCount--;
                soundPool.play(buttonSound[3], leftVolume:1, rightVoluem:1, priority:1, loop:0, rate:1);
            // 입력된 숫자가 없을 경우, 다음과 같은 text를 출력한다
            } else {
                Toast.makeText(getApplicationContext(), text: "숫자를 입력해 주세요", Toast.LENGTH_SHORT).show();
            }
        }
    });

    // hitButton 클릭 시의 동작을 정의한다.
    hitButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view){
            // inputTextCount 이 실행될 조건을 설정한다. // 선택된 숫자가 3개 미만일 경우
            if(inputTextCount < 3){
                // "숫자를 입력해 주세요." 를 출력한다.
                Toast.makeText(getApplicationContext(), text: "숫자를 입력해 주세요", Toast.LENGTH_SHORT).show();
            // 선택된 숫자가 3개 이상일 경우
            } else{
                int[] userNumbers = new int[3];
                // i = 0, 1, 2 일 때까지 반복문을 실행한다.
                for (int i =0; i < userNumbers.length; i++){
                    userNumbers[i] = Integer.parseInt(inputTextView[i].getText().toString());
                }
                int[] countCheck = new int[2];
                countCheck = getCountCheck(comNumbers.userNumbers);
                Log.e(tag: "hitButton", msg: "countCheck = S : " + countCheck[0] + " B : " + countCheck[1]);

                String resultCount;

                if(countCheck[0] == 3){
                    resultCount = "1 [" + userNumbers[0] + " " + userNumbers[1] + " " + userNumbers[2]
                            + "] 아웃입니다.";
                    soundPool.play(buttonSound[0], leftVolume:1, rightVoluem:!, priority:1, loop:0, rate:1);
                } else {
                    resultCount = "1 [" + userNumbers[0] + " " + userNumbers[1] + " " + userNumbers[2]
                            + "] s: " + countCheck[0] + " B : " + countCheck[1];
                    soundPool.play(buttonSound[4], leftVolume:1, rightVoluem:!, priority:1, loop:0, rate:1);
                }
                if(hitCount == 1){
                    resultTextView.setText(resultCount + "\n");
                } else {
                    resultTextView.append(resultCount + "\n");
                }
                if(countCheck[0] == 3){
                    hitCount = 1;
                    comNumbers = getComNumbers();
                } else{
                    hitCount++;
                }

                ScrollView.fullScroll(View.FOCUS_DOWN);

                backSpaceClick();
                backspaceClick();
                backspaceClick();

            }
        }
    });

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
            soundPool = new SoundPool(maxStreams:6, AudioManager.STREAM_MUSIC, srcQuality:0);
        }

        for (int i = 0; i < buttonSound.length ; i++){
            buttonSound[i] = soundPool.load(getApplicationContext(), R.raw.button1 + i, priority: 1);
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        soundPool.release();
    }
}