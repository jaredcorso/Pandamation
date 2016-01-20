package codes.corso.jared.pandamation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class AnimationTestActivity extends AppCompatActivity {

    ImageView testImage;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_test);
        testImage = (ImageView) findViewById(R.id.imageView);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pandamate.animateManuallyWithStopFrame(R.drawable.testanim, testImage, null, null,10);
            }
        });

    }
}
