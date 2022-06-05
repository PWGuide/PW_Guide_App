package com.example.pwguide;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MenuActivity extends AppCompatActivity {

    Button btn_table;
    Button btn_camera;
    Button btn_navig;
    Button btn_info;

    //MultiLayerNetwork model;
    ComputationGraph model;

    InputStream inputStream;

    AlertActivity alert = new AlertActivity();

    String[] titles;
    String[] short_texts;
    String[] long_texts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().setTitle("Nawigator po PW");

        btn_table = findViewById(R.id.btn_table);
        btn_table.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, TimetableActivity.class);
                startActivity(intent);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);

        //inputStream = getResources().openRawResource(R.raw.trained_model_vgg16_6);
        inputStream = getResources().openRawResource(R.raw.trained_model_mn2);

        titles = getResources().getStringArray(R.array.titles_array);
        short_texts = getResources().getStringArray(R.array.short_texts_array);
        long_texts = getResources().getStringArray(R.array.long_texts_array);

        try {
                //model = ModelSerializer.restoreMultiLayerNetwork(inputStream);
                model = ModelSerializer.restoreComputationGraph(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
        }

        btn_camera = findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(new View.OnClickListener() {
            ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                setResult(Activity.RESULT_FIRST_USER);
                                Intent intent = result.getData();
                                Bitmap bitmap = (Bitmap) intent.getExtras().get("data");

                                //NativeImageLoader loader = new NativeImageLoader(150, 150, 3);
                                NativeImageLoader loader = new NativeImageLoader(128, 128, 3);

                                INDArray image = null;

                                try {
                                    image = loader.asMatrix(bitmap);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                DataNormalization scalar = new ImagePreProcessingScaler(0, 1);
                                scalar.transform(image);

                                INDArray[] output = model.output(image);

                                int index;

                                index = getIndex(output[0]);

                                String title = titles[index];
                                String short_txt = short_texts[index];
                                String long_txt = long_texts[index];

                                alert.createAlert(builder, title, short_txt, long_txt);
                            }
                        }
                    });

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                someActivityResultLauncher.launch(intent);
            }
        });

        btn_navig = findViewById(R.id.btn_navig);
        btn_navig.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, NavigationMenuActivity.class);
                startActivity(intent);
            }
        });

        btn_info = findViewById(R.id.btn_info);
        btn_info.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });

    }

    int getIndex(INDArray output) {
        int index = 0;
        double max = output.getDouble(0);

        for (int i=1; i < output.length(); i++) {
            if (output.getDouble(i) > max) {
                max = output.getDouble(i);
                index = i;
            }
        }

        if (max < 0.8){
            index = 22;
        }

        return index;
    }
}