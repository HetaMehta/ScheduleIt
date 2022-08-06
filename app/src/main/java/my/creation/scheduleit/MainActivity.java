package my.creation.scheduleit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import my.creation.scheduleit.Activity.SecondActivity;

public class MainActivity extends AppCompatActivity {

    Button b1;
    EditText e1;
    ListView l1;
    ArrayList<String> itemLst = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    private ImageView iv_mic;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_mic = findViewById(R.id.iv_mic);
        e1 = findViewById(R.id.ed1);
        b1 = findViewById(R.id.btn);
        l1 = findViewById(R.id.lstv);

        itemLst = SecondActivity.readIt(this);
        arrayAdapter = new ArrayAdapter<>(this,R.layout.activity_second,itemLst);
        l1.setAdapter(arrayAdapter);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemName = e1.getText().toString();
                itemLst.add(itemName);
                e1.setText("");
                arrayAdapter.notifyDataSetChanged();

            }
        });

        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Remove");
                alert.setMessage("Do you want to remove this item?");
                alert.setCancelable(false);
                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        itemLst.remove(position);
                        arrayAdapter.notifyDataSetChanged();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });

        iv_mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent
                        = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                }
                catch (Exception e) {
                    Toast.makeText(MainActivity.this, " " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                e1.setText(
                        Objects.requireNonNull(result).get(0));
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SecondActivity.writeIt(itemLst,getApplicationContext());
    }
}