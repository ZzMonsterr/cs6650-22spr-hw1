package SkierApp;

import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.*;
import io.swagger.client.ApiException;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static SkierApp.SkierClient.basePath;

public class SwaggerClientRunnable implements Runnable {

    private CountDownLatch completed;
    private int numRequestsPerThread;
    private int numLifts;
    private int skiIDLower;
    private int skiIDHigher;
    private int intervalStart;
    private int intervalEnd;
    private AtomicInteger failureCounter;

    private SkiersApi skiersApi;

    public SwaggerClientRunnable(CountDownLatch completed,
                                 int numRequestsPerThread, int numLifts,
                                 int skiIDLower, int skiIDHigher,
                                 int intervalStart, int intervalEnd,
                                 AtomicInteger failureCounter) {
        this.completed = completed;
        this.numRequestsPerThread = numRequestsPerThread;
        this.numLifts = numLifts;
        this.skiIDLower = skiIDLower;
        this.skiIDHigher = skiIDHigher;
        this.intervalStart = intervalStart;
        this.intervalEnd = intervalEnd;
        this.failureCounter = failureCounter;

        // base on Swagger Client Class, SkiersApi
        this.skiersApi = new SkiersApi();
        this.skiersApi.getApiClient().setBasePath(basePath);
    }

    /**
     * send numRequestsPerThread times POST request in this thread.
     */
    @Override
    public void run() {
        // start to run
        for (int i = 0; i < this.numRequestsPerThread; i++) {
            try {

                // generate random data: who, which lift, and when
                int liftID = (int) (Math.random() * (numLifts + 1 - 5)) + 5;
                int skierID = (int) (Math.random() * (skiIDHigher + 1 - skiIDLower)) + skiIDLower;
                int time = (int) (Math.random() * (intervalEnd + 1 - intervalStart)) + intervalStart;
                int waitTime = (int) (Math.random() * 10);

                // generate liftRide
                LiftRide liftRide = new LiftRide();
                liftRide.setTime(time);
                liftRide.setLiftID(liftID);
                liftRide.setWaitTime(waitTime);

                // use SkiersApi existing post request function
                skiersApi.writeNewLiftRide(liftRide, 1, "2022", "11", skierID);
				
            } catch (ApiException e) {
                failureCounter.incrementAndGet();
                System.err.println("Exception when calling " +
                        "SkiersApi#writeNewLiftRide" + e.getResponseHeaders());
                e.printStackTrace();
            }
        }
        if (this.completed != null) this.completed.countDown();
    }
}
