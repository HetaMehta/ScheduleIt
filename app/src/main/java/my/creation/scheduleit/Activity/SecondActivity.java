package my.creation.scheduleit.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    public static final String FILENAME = "task1.dat";

    public static void writeIt(ArrayList<String> item, Context context){
        try {
            FileOutputStream fs = context.openFileOutput(FILENAME,Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(item);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> readIt(Context context){
        ArrayList<String> list = null;

        FileInputStream fi = null;
        try {
            fi = context.openFileInput(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fi);
            list = (ArrayList<String>) ois.readObject();
        } catch (FileNotFoundException e) {
            list = new ArrayList<>();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }
}