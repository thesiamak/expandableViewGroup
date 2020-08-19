package ir.drax.expandableViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import ir.drax.expandableViewGroup.widget.Expandable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        injectFirstContainer();
    }

    private void injectFirstContainer() {
        TextView tv1=new TextView(this);
        tv1.setText("I am an injected TextView");


        Expandable lowerExpandable=new Expandable(this);
        lowerExpandable.update();

    }
}