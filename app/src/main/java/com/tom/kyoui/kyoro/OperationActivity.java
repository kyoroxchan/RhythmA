package com.tom.kyoui.kyoro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OperationActivity extends AppCompatActivity {

    private Button esc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);

        esc = (Button)findViewById(R.id.esc);


        esc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplication(),MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
