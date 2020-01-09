package com.darko.themoviedbapp;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SuppressWarnings("FieldCanBeLocal")
public class AppExecutors {

    private static final Object LOCK = new Object();
    private static AppExecutors INSTANCE;
    private final Executor diskIo;
    private final Executor mainThread;
    private final Executor networkIo;

    private AppExecutors(Executor diskIo, Executor mainThread, Executor networkIo) {
        this.diskIo = diskIo;
        this.mainThread = mainThread;
        this.networkIo = networkIo;
    }

    public static AppExecutors getInstance() {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                INSTANCE = new AppExecutors(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        new MainThreadExecutor());
            }
        }
        return INSTANCE;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}