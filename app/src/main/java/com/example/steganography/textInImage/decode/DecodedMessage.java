package com.example.steganography.textInImage.decode;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.steganography.R;

public class DecodedMessage extends AppCompatActivity {
    TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoded_message);
        message = findViewById(R.id.your_decode_message);
        String d = getIntent().getStringExtra("decode_message");
        message.setText(d);

    }
}
