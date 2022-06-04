package com.example.pwguide;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;

import java.io.IOException;
import java.io.InputStream;

public class CameraActivity extends AppCompatActivity {

    ImageView imageView;
    Button button;
    TextView txtView;

    ComputationGraph model;

    InputStream inputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        getSupportActionBar().setTitle("Kamera");

        imageView = findViewById(R.id.image_view);
        button = findViewById(R.id.button);
        txtView = (TextView)findViewById(R.id.txt_view);

        inputStream = getResources().openRawResource(R.raw.trained_model_mnv2);

        {
            try {
                model = ModelSerializer.restoreComputationGraph(inputStream);
                System.out.println(model.summary());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (ContextCompat.checkSelfPermission(CameraActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CameraActivity.this,
                    new String[]{Manifest.permission.CAMERA}, 101);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 101);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);

            NativeImageLoader loader = new NativeImageLoader(150, 150, 3);

            INDArray image = null;

            try {
                image = loader.asMatrix(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            image = image.permute(0,2,3,1);
            DataNormalization scalar = new ImagePreProcessingScaler(0, 1);
            scalar.transform(image);

            INDArray[] output = model.output(image);

            double EE = output[0].getDouble(0);
            double GG = output[0].getDouble(1);

            if (EE > GG) {
                txtView.setText("To jest Wydział Elektryczny!");
            } else {
                txtView.setText("To jest Gmach Główny!");
            }
        }
    }
}