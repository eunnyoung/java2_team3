package com.example.numberbaseball;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // backSpaceButton 생성
    ImageButton backSpaceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backSpaceButton = findViewById(R.id.back_space_button);
    }

    backSpaceButton.setOnClickListener(new View.OnClickListener(){
        @Override
        Public void onClick(View view){
            // backSpaceButton 이 실행될 조건을 설정한다.
            if(inputTextCount > 0){ // 입력된 숫자가 있을 경우, 하나씩 지운다.
                // Text를 int로 형변환한다.
                int buttonEnableCount = Integer.parseInt(inputTextView[inputTextCount-1].getText().toString());
                // 숫자버튼이 선택되기 이전 상태로 돌아간다.
                numButton[buttonEnableCount].setEnabled(true);
                inputTextView[inputTextCount-1].setText("");
                inputTextCount--;
            } else { // 입력된 숫자가 없을 경우, 다음과 같은 text를 출력한다.
                Toast.makeText(getApplicationContext(), text: "숫자를 입력해 주세요", Toast.LENGTH_SHORT).show();
            }
        }
    });
}