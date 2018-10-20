package com.example.admin.vend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

public class addSpot extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spot);

        final EditText name = (EditText)findViewById(R.id.name);
        final EditText longitude = (EditText)findViewById(R.id.longitude);
        final EditText latitude = (EditText)findViewById(R.id.latitude);
        final EditText open = (EditText)findViewById(R.id.open);
        final EditText close = (EditText)findViewById(R.id.close);
        final EditText category = (EditText)findViewById(R.id.category);

        Button currentLocation = (Button)findViewById(R.id.currentLocation);
        Button add = (Button)findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database data = new database();
                data.createVendor(name.getText().toString(), Double.parseDouble(longitude.getText().toString()), Double.parseDouble(latitude.getText().toString()), open.getText().toString(), close.getText().toString(), category.getText().toString());

            }
        });
    }

}
