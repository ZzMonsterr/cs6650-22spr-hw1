package SkierApp;

import java.io.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.opencsv.CSVWriter;

public class SkierClient {
    final static private int NUMTHREADS = 256;       // 32, 64, 128 and 256
    final static private String fileName = "EC2Output256.csv";

//    public static String basePath = "http://localhost:8080/SkiApp_war_exploded";
    public static String basePath = "http://54.186.183.123:8080/SkiApp_war";
    private static long startTime;
    private static long endTime;
    private static int totalRequests;
    private static int numSkiers = 20000;           // 20000
    private static int numLifts = 40;
    private static int numRuns = 10;
    // create a thread-safe int to count failure threads
    // ref https://stackoverflow.com/a/14983893/11760139
    public static AtomicInteger failureCounter = new AtomicInteger(0);
    // create a thread-safe List to record latency
    // ref https://stackoverflow.com/a/8203913/11760139
    public static ConcurrentLinkedQueue<String[]> latencyRecords =
            new ConcurrentLinkedQueue<>();
    // create a threadPool w/ maxThread number
    public static ExecutorService threadPool = Executors.newFixedThreadPool(NUMTHREADS);

    /**
     * Send POST requests to Skier Server in three phases.
     * @param args
     * @throws InterruptedException
     * @throws IOException
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        startTime = System.currentTimeMillis();

        // Test Approach: 1 thread & 10000 requests
//        totalRequests = 10000;
//        CountDownLatch completed = new CountDownLatch(1);
//        startPhase(1, numSkiers, numLifts,
//                totalRequests, completed,
//                1, 10);
//        completed.await();

        // Actual Approach
        int phase1NumThreads = helperCeil(NUMTHREADS, 4);
        int phase2NumThreads = NUMTHREADS;
        int phase3NumThreads = helperCeil(NUMTHREADS, 4);

        CountDownLatch completed1 =
                new CountDownLatch((int) Math.ceil(phase1NumThreads * 0.2));
        CountDownLatch completed2 =
                new CountDownLatch((int) Math.ceil(phase2NumThreads * 0.2));
        CountDownLatch completed3 = new CountDownLatch(phase3NumThreads);

        int phase1numRequests =
                (int) (0.2 * numRuns) * numSkiers;
        int phase2numRequests =
                (int) (0.6 * numRuns) * numSkiers;
        int phase3numRequests =
                (int) (0.1 * numRuns) * numSkiers;
        totalRequests =
                phase1numRequests + phase2numRequests + phase3numRequests;
        // Phase 1 starts
        System.out.println("!!!!!!!!!Phase 1 starts!!!!!!!!!");
        startPhase(phase1NumThreads, numSkiers, numLifts,
                phase1numRequests, completed1,
                1, 90);
        completed1.await();

        // Phase 2 starts
        System.out.println("!!!!!!!!!Phase 2 starts!!!!!!!!!");
        startPhase(phase2NumThreads, numSkiers, numLifts,
                phase2numRequests, completed2,
                91, 360);
        completed2.await();

        // Phase 3 starts
        System.out.println("!!!!!!!!!Phase 3 starts!!!!!!!!!");
        startPhase(phase3NumThreads, numSkiers, numLifts,
                phase3numRequests, completed3,
                361, 420);
        completed3.await();
        // END of actual approach

        // shutdown(): executes all previous tasks then finish, but no longer
        // accept new tasks
        threadPool.shutdown();
        endTime = System.currentTimeMillis();
        System.out.println("======== all threads finished ========");

        writeToFile();
        doCalculations();
    }

    /**
     * Start Phase 1, 2, or 3 based on their diff parameters.
     * @param numThreads max num of threads this phase could use
     * @param numSkiers num of skiers
     * @param numLifts avg of lifts skiers take
     * @param totalRequestsThisThread num of requests this thread is
     *                                responsible for
     * @param completed when CountDownLatch completed is zero, tell the main
     *                  thread to start next phase
     * @param intervalStart lift ride's time range, start point
     * @param intervalEnd lift ride's time range, end point
     */
    private static void startPhase(int numThreads, int numSkiers, int numLifts,
                                   int totalRequestsThisThread, CountDownLatch completed,
                                   int intervalStart, int intervalEnd) {
        int numRequestsPerThread = helperCeil(totalRequestsThisThread, numThreads);
        int skiIDPerGroup = helperCeil(numSkiers, numThreads);
        int i = 0;
        int skiIDHigher;

        while (i < numThreads) {

            if (i == numThreads - 1) {
                skiIDHigher = numSkiers;
            } else {
                skiIDHigher = (i + 1) * skiIDPerGroup;
            }

            int numRequestsThisThread;
            if (totalRequestsThisThread >= numRequestsPerThread) {
                numRequestsThisThread = numRequestsPerThread;
                totalRequestsThisThread -= numRequestsPerThread;
            } else {
                numRequestsThisThread = totalRequestsThisThread;
                totalRequestsThisThread = 0;
            }

            Runnable runnable = new SwaggerClientRunnable(completed,
                    numRequestsThisThread, numLifts,
                    i * skiIDPerGroup + 1,
                    skiIDHigher,
                    intervalStart, intervalEnd, failureCounter, latencyRecords);
            threadPool.execute(runnable);
            i++;
        }
    }

    /**
     * Helper function, return a round up the output of a divided by b.
     * @param a
     * @param b
     * @return round up the output of a divided by b
     */
    public static int helperCeil(int a, int b) {
        return (int)Math.ceil((double) a / b);
    }

    /**
     * Helper function, return the sum of an int list in the form of long
     * because of the max limit of int might not be enough.
     * @param list
     * @return sum of an int list
     */
    public static long sum(List<Integer> list) {
        long total = 0;
        for (int i: list) {
            total += i;
        }
        return total;
    }

    /**
     * Hlper function, write all latency records into a CSV file in the
     * same working directory of this project.
     * @throws IOException
     */
    private static void writeToFile() throws IOException {
        // ref: https://www.tutorialspoint.com/how-to-write-data-to-csv-file-in-java
        FileWriter outputFile = new FileWriter(fileName);
        CSVWriter writer = new CSVWriter(outputFile);
        for (String[] record : latencyRecords) {
            writer.writeNext(record);
        }

        // Flush or Close: ref https://stackoverflow.com/a/9272606/11760139
        writer.close();
        System.out.println("Latency data is saved into a CSV file: " + fileName);
    }

    /**
     * Helper function.
     * Count successfulResponse, unsuccessfulResponse;
     * Calculate wall time, throughput, and Max / Mean / Median / p99 Response time.
     */
    private static void doCalculations() {
        int successResponseCounter = totalRequests - failureCounter.get();
        System.out.println("Num of Successful Requests: " +
                successResponseCounter);
        System.out.println("Num of Unsuccessful Requests: " +
                failureCounter.get());

        // Total run time (wall time) for all phases to complete (in millisecond)
        long wallTime = endTime - startTime;
        System.out.format("Wall Time (in second): %.2f\n", (double)wallTime / 1000);

        // int / long = long, ref https://stackoverflow.com/a/12314984/11760139
        double throughput = (double) totalRequests / (wallTime / 1000);
        // Throughput: total number of requests/wall time
        System.out.format("Throughput (in second): %.2f\n", throughput);

        // deal with latency calculations
        List<Integer> responseTimes = new ArrayList<>();
        for (String[] record : latencyRecords) {
            responseTimes.add(new Integer(record[2]));
        }
        Collections.sort(responseTimes);

        System.out.println("(If not mentioned, then it's in millisecond)");
        long totalResponseTime = sum(responseTimes);
        int maxResponseTime = Collections.max(responseTimes);
        System.out.println("Max Response Time: " + maxResponseTime);
        int minResponseTime = Collections.min(responseTimes);
        System.out.println("Min Response Time: " + minResponseTime);

        double meanResponseTime = totalResponseTime / successResponseCounter;
        System.out.println("Mean Response Time: " + (int) meanResponseTime);

        int medianResponseTime;
        int midIdx = successResponseCounter / 2;    // default floor
        if (successResponseCounter % 2 == 0) {
            medianResponseTime =
                    helperCeil(responseTimes.get(midIdx - 1) +
                            responseTimes.get(midIdx), 2);
        } else {
            medianResponseTime = responseTimes.get(midIdx);
        }
        System.out.println("Median Response Time: " + medianResponseTime);

        // 99% percentage calculation: "If you want to calculate the 95th
        // percentile, you need to sort all your values from least to
        // greatest, then find the value at myArray[count(myArray) * 0.95] "
        // ref: https://www.elastic.co/blog/averages-can-dangerous-use-percentile
        int p99 =
                responseTimes.get((int)(successResponseCounter * 0.99) - 1);
        System.out.println("p99 Response Time: " + p99);
    }

}