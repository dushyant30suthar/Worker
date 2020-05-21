package com.dushyant.worker.client.foregroundcomponents.imageComponent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dushyant.worker.R;
import com.dushyant.worker.client.foregroundcomponents.base.WayFindingConfiguration;
import com.dushyant.worker.domain.DomainRequestObserver;
import com.dushyant.worker.domain.Worker;
import com.dushyant.worker.framework.network.NetworkModule;
import com.dushyant.worker.framework.utils.ThreadManager;

import java.io.IOException;

import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class ImageActivity extends AppCompatActivity implements WayFindingConfiguration, View.OnClickListener {

    private static final String TAG = "ImageActivity";
    /*
     * Network things should only be exposed in domain layer but the task I have been given expected network
     * call to be defined on ui side thus I tried to expose network module to ui.*/
    NetworkModule networkModule;

    private ImageView imageView1, imageView2;
    private Button button1, button2;
    Worker<Bitmap> worker1 = new Worker<>();
    Worker<Bitmap> worker2 = new Worker<>();
    private ThreadManager threadManager = ThreadManager.getInstance();
    private Task<Bitmap> task1 = new Task<Bitmap>() {
        private String taskName;
        private Bitmap result;

        @Override
        public Bitmap getResult() {
            return result;
        }

        @Override
        public Bitmap onExecuteTask() {
            try {

                Response<ResponseBody> response = networkModule.getImageNetworkDao().getImage("49648408941_0cca5e1f36_c.jpg").execute();

                if (response.body() != null) {

                    Log.d(TAG, "Request Success " + response.toString());

                    result = BitmapFactory.decodeStream(response.body().byteStream());

                    return result;

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

        @Override
        public String getTaskName() {
            return taskName;
        }

        @Override
        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }
    };
    private Task<Bitmap> task2 = new Task<Bitmap>() {
        private String taskName;
        private Bitmap result;

        @Override
        public Bitmap getResult() {
            return result;
        }

        @Override
        public Bitmap onExecuteTask() {
            try {

                Response<ResponseBody> response = networkModule.getImageNetworkDao().getImage("49654451496_c0c92aecab_h.jpg").execute();

                if (response.body() != null) {

                    Log.d(TAG, "Request Success " + response.toString());

                    result = BitmapFactory.decodeStream(response.body().byteStream());

                    return result;

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

        @Override
        public String getTaskName() {
            return taskName;
        }

        @Override
        public void setTaskName(String taskName) {
            this.taskName = taskName;
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
        worker1.subscribeOn(Schedulers.from(threadManager.getNetworkOperationThread()))
                .observeOn(Schedulers.from(threadManager.getMainThread()))
                .subscribe(new DomainRequestObserver<>());
        worker2.subscribeOn(Schedulers.from(threadManager.getNetworkOperationThread()))
                .observeOn(Schedulers.from(threadManager.getMainThread()))
                .subscribe(new DomainRequestObserver<>());
    }

    @Override
    public Toolbar getToolbar() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1: {
                task1Count++;
                task1.setTaskName("task " + task1Count);
                worker1.addTask(task1);
            }
            break;
            case R.id.button2: {
                task2Count++;
                task2.setTaskName("task " + task2Count);
                worker2.addTask(task2);
            }
            break;
        }
    }
}
