package com.example.ichat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener{
    private Button feedbackSendButton, feedbackClearButton ;
    private Button backtohomeButton;
    private EditText feedbackNameEditText, feedbackMessageEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        feedbackSendButton = findViewById(R.id.feedbackSendButtonId);
        feedbackClearButton = findViewById(R.id.feedBackClearButtonId);
        feedbackNameEditText = findViewById(R.id.feedbackNameEditTextId);
        feedbackMessageEditText = findViewById(R.id.feedbackMessageEditTextId);
        backtohomeButton = findViewById(R.id.backtohomeid);

        feedbackSendButton.setOnClickListener(this);
        feedbackClearButton.setOnClickListener(this);
        backtohomeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            String name = feedbackNameEditText.getText().toString();
            String message = feedbackMessageEditText.getText().toString();
            if(v.getId() == R.id.backtohomeid)
            {
                startActivity(new Intent(FeedbackActivity.this , MainActivity.class));
            }

            if (v.getId() == R.id.feedbackSendButtonId) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/email");

                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"codebreakers8094@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback From iChat");
                intent.putExtra(Intent.EXTRA_TEXT, "Name:" + name + "\n Message:" + message);
                startActivity(Intent.createChooser(intent, "Feedback with"));
            } else if (v.getId() == R.id.feedBackClearButtonId) {
                feedbackNameEditText.setText("");
                feedbackMessageEditText.setText("");
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Exception:"+e,Toast.LENGTH_SHORT).show();
        }
    }
}
