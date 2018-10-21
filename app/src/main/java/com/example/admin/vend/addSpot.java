package com.example.admin.vend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class addSpot extends AppCompatActivity {

    private Map<String, String> categories = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spot);

        final EditText name = (EditText)findViewById(R.id.name);
        final EditText longitude = (EditText)findViewById(R.id.longitude);
        final EditText latitude = (EditText)findViewById(R.id.latitude);
        final EditText open = (EditText)findViewById(R.id.open);
        final EditText close = (EditText)findViewById(R.id.close);
        final Spinner category = (Spinner)findViewById(R.id.category);

        Button currentLocation = (Button)findViewById(R.id.currentLocation);
        Button add = (Button)findViewById(R.id.add);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database data = new database();
                data.createVendor(name.getText().toString(), Double.parseDouble(longitude.getText().toString()), Double.parseDouble(latitude.getText().toString()), open.getText().toString(), close.getText().toString(), categories.get(((TextView) category.getSelectedView()).getText().toString()));

            }
        });

        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapsActivity ma = new MapsActivity();
                Log.d("HERE", Double.toString(ma.getLat()));
                Log.d("HERE", Double.toString(ma.getLng()));

            }
        });

        categories.put("Chinese food", "1");
        categories.put("Mexican food", "2");
        categories.put("Breakfast food", "3");
        categories.put("Ice cream", "4");
        categories.put("Jewelry", "5");
        categories.put("Art", "6");
        categories.put("Apparel", "7");
        categories.put("Hot dogs", "8");
    }
}
