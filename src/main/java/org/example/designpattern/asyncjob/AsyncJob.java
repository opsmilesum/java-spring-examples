package org.example.designpattern.asyncjob;

import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@AllArgsConstructor
public class AsyncJob<T> implements Runnable {
    private boolean running = true;
    private final ArrayBlockingQueue<T> queue;

    private final Consumer<List<T>> consumer;
    private final List<T> buf;
    private static final int MAX_BUF_SIZE = 100;
    private Instant lastSendTime = Instant.now();
    private static final int MAX_STAGING_SECONDS = 60;

    @Override
    public void run() {
        T event = null;
        while (running) {
            try {
                event = queue.poll(100, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (event != null) {
                buf.add(event);
            }

            boolean isEventsEnough = buf.size() >= MAX_BUF_SIZE;
            boolean isTimeout =
                    (Instant.now().getEpochSecond() - lastSendTime.getEpochSecond()) >= MAX_STAGING_SECONDS;

            if (!isEventsEnough && !isTimeout) {
                return;
            }

            consumer.accept(buf);
            buf.clear();
            lastSendTime = Instant.now();
        }
    }

    public void setStop(boolean stop) {
        this.running = !stop;
    }

    public void addTask(T event) {
        queue.offer(event);
    }
}
