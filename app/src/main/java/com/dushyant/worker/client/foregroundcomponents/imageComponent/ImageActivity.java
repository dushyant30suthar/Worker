package com.dushyant.worker.client.foregroundcomponents.imageComponent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.dushyant.worker.R;
import com.dushyant.worker.domain.Worker;
import com.dushyant.worker.framework.network.NetworkModule;
import com.dushyant.worker.framework.utils.ThreadManager;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class ImageActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ImageActivity";
    /*
     * Network things should only be exposed in domain layer but the task I have been given expected network
     * call to be defined on ui side thus I tried to expose network module to ui.*/
    NetworkModule networkModule;

    private ImageView imageView1, imageView2;
    private Button button1, button2;
    Worker<Bitmap> worker1 = new Worker<>("Worker1");
    Worker<Bitmap> worker2 = new Worker<>("Worker2");
    private ThreadManager threadManager = ThreadManager.getInstance();
    private Task<Bitmap> task1 = new Task<Bitmap>() {

        @Override
        public Bitmap onExecuteTask() {
            try {

                Response<ResponseBody> response = networkModule.getImageNetworkDao().getImage("49648408941_0cca5e1f36_c.jpg").execute();

                if (response.body() != null) {

                    Log.d(TAG, "Request Success " + response.toString());

                    return BitmapFactory.decodeStream(response.body().byteStream());

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onTaskComplete(Bitmap result) {
            imageView1.setImageBitmap(result);
        }
    };
    private Task<Bitmap> task2 = new Task<Bitmap>() {

        @Override
        public Bitmap onExecuteTask() {
            try {

                Response<ResponseBody> response = networkModule.getImageNetworkDao().getImage("49654451496_c0c92aecab_h.jpg").execute();

                if (response.body() != null) {

                    Log.d(TAG, "Request Success " + response.toString());

                    return BitmapFactory.decodeStream(response.body().byteStream());

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onTaskComplete(Bitmap result) {
            imageView2.setImageBitmap(result);
        }

    };
    private int task1Count = 0;
    private int task2Count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpViews();

    }

    private void setUpViews() {
        networkModule = new NetworkModule(getApplicationContext());
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1: {
                task1Count++;
                worker1.addTask("task " + task1Count, task1);
            }
            break;
            case R.id.button2: {
                task2Count++;
                worker2.addTask("task " + task2Count, task2);
            }
            break;
        }
    }
}
