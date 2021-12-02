package com.example.numberbaseball;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //랜덤으로 정해진 정수 3개 넣을 수 있는 배열.
    int[] comNumbers = new int[3];

    // 숫자 맞추기 시도 첫번째.
    int hitCount = 1;

    ImageButton backSpaceButton;
    ImageButton hitButton;

    TextView resultTextView;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        comNumbers = getComNumbers();

        backSpaceButton = findViewById(R.id.back_space_button);
        hitButton = findViewById(R.id.hit_button);
        resultTextView = findViewById(R.id.result_text_view);
        scrollView = findViewById(R.id.scroll_view);
    }

    public int[] getComNumbers() {
        int[] setComNumbers = new int[3];
        //for문을 돌려서 랜덤 숫자 3개를 setComNumbers에 넣기
        for (int i = 0; i < setComNumbers.length; i++) {
            //i->0, 1, 2일때 랜덤 숫자 생성
            // 0부터 9까지의 숫자가 setComNumbers에 들어감
            setComNumbers[i] = new Random().nextInt(10);
            //배열에 같은 숫자가 들어가면 안 되기 때문에
            for (int j = 0; i < i; i++) {
                if (setComNumbers[i] == setComNumbers[j]) {
                    //만약에 같으면 랜덤숫자를 다시 받아야 하기 때문에 for문을 다시 돌게끔 함
                    i--;
                    break;
                }
            }
        }
        Log.e("setComNumbers", "setComNumbers = " + setComNumbers[0] + ", " + setComNumbers[1] + ", " + setComNumbers[2]);
        return setComNumbers;
    }

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
                Log.e( tag: "hitButton", msg: "countCheck = S : " + countCheck[0] + " B : " + countCheck[1]);

                String resultCount;

                if(countCheck[0] == 3){
                    resultCount = "1 [" + userNumbers[0] + " " + userNumbers[1] + " " + userNumbers[2]
                            + "] 아웃입니다.";
                } else {
                    resultCount = "1 [" + userNumbers[0] + " " + userNumbers[1] + " " + userNumbers[2]
                            + "] s: " + countCheck[0] + " B : " + countCheck[1];
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
                inputTextCount = 0;

                for(TextView textView : inputTextView){
                    textView.setText("");
                }
                for(Button button : numButton){
                    button.setEnabled(true);
                }
            }
        }
    });

    private int[] getCountCheck(int[] comNumbers, int[] userNumbers){
        int [] setCount = new int[2];
        for (int i = 0; i < comNumbers.length; i++){
            for(int j =0; j < userNumbers.length; j++){
                if(comNumbers[i] == userNumbers[j] && i == j){
                    setCount[0]++;
                } else if(comNumbers[i] == userNumbers[j] && i != j){
                    setCount[1]++;
                }
            }
        }
        return setCount;
    }
}