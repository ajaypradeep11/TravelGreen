package com.example.user.finalf;



        import android.graphics.drawable.Drawable;
        import android.support.v4.view.ViewPager;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import static com.example.user.finalf.R.mipmap.ic_launcher;

public class MainActivity extends AppCompatActivity {
    ImageView imageview;
    TextView t;
    String newString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t=(TextView)findViewById(R.id.range);
        imageview=(ImageView)findViewById(R.id.image);
        if (savedInstanceState == null) {
            Bundle extras=getIntent().getExtras();
            if(extras==null)
            {
                newString=null;
            }
            else
            {

                newString=extras.getString("str");

            }

        }
        else{

            newString=(String)savedInstanceState.getSerializable("str");
        }
        t.setText(newString);
        if(newString.equals("Low to High"))
        {
            TextView t=(TextView)findViewById(R.id.textView3);
            t.setText("Health impact:\n     * Minor respiratory discomfort.\n     * Asthma.\n     * May cause breathing discomfort to the people on prolonged exposure.\nPrecautions:\n     * Wear Mask.");
        }
        else
        {
            TextView t=(TextView)findViewById(R.id.textView3);
            t.setText("Health impact:\n     Minimal impact.");
        }

    }

}

