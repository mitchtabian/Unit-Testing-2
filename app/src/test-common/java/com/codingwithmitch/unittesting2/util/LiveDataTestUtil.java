package com.codingwithmitch.unittesting2.util;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LiveDataTestUtil<T> {

    public T getValue(final LiveData<T> liveData) throws InterruptedException {

        final List<T> data = new ArrayList<>();

        // latch for blocking thread until data is set
        final CountDownLatch latch = new CountDownLatch(1);

        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(T t) {
                data.add(t);
                latch.countDown(); // release the latch
                liveData.removeObserver(this);
            }
        };
        liveData.observeForever(observer);
        try {
            latch.await(2, TimeUnit.SECONDS); // wait for onChanged to fire and set data
        } catch (InterruptedException e) {
            throw new InterruptedException("Latch failure");
        }
        if(data.size() > 0){
            return data.get(0);
        }
        return null;
    }

}

