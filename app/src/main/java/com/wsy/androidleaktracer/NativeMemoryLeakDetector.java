package com.wsy.androidleaktracer;

import android.util.Log;

import java.io.File;
import java.io.IOException;

public class NativeMemoryLeakDetector {

    static {
        System.loadLibrary("leaktracer");
    }

    /**
     * starts monitoring memory allocations in all threads
     */
    public static native void startMonitoringAllThreads();

    /**
     * starts monitoring memory allocations in current thread
     */
    public static native void startMonitoringThisThread();

    /**
     * stops monitoring memory allocations (in all threads or in
     * this thread only, depends on the function used to start
     * monitoring
     */
    public static native void stopMonitoringAllocations();

    /**
     * stops all monitoring - both of allocations and releases
     */
    public static native void stopAllMonitoring();


    /**
     * 手动触发内存泄漏
     **/
    public static native void performLeak();

    /**
     * writes report with all memory leaks
     */
    private static native void writeLeaksToFile(String filePath);

    /**
     * writes report with all memory leaks
     */
    public static void writeLeaksResultToFile(String filePath_) {
        String filePath = "/sdcard/files/memTracer/";
        String fileName = "native_heap.txt";
        Log.d("Leak", "writeLeaksResultToFile.");

        if (filePath == null) {
            throw new NullPointerException("filePath is null");
        }
        stopAllMonitoring();
        File tracerFilePath = new File(filePath);
        if (!tracerFilePath.exists()) {
            tracerFilePath.mkdirs();
        }
        File tracerFile = new File(tracerFilePath,fileName);
        if (!tracerFile.exists()){
            try {
                tracerFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d("Leak", "writeLeaksResultToFile, begin...tracerFile="+tracerFile);
        writeLeaksToFile(tracerFile.getAbsolutePath());
        Log.d("Leak", "writeLeaksResultToFile. finish---------------");
    }
}
