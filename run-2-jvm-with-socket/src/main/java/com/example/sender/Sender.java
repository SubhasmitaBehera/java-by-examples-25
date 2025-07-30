package com.example.sender;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Sender {
    private static final int CHUNK_SIZE = 1024;
    private static final int THREAD_COUNT = 4;
    private static final String HOST = "localhost";
    private static final int PORT = 5050;

    public static void main(String[] args) throws Exception{
        String FILE_NAME = "input.txt";

        File file = new File(FILE_NAME);
        long fileLength = file.length();
        int totalChunks = (int) Math.ceil((double) fileLength / CHUNK_SIZE);

        byte[][] chunks = new byte[totalChunks][];
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

        RandomAccessFile randomAccessFile = new RandomAccessFile(file,"r");
        CountDownLatch latch = new CountDownLatch(totalChunks);

        for (int i =0; i< totalChunks; i++){
            final int chunkIndex = i;
            executorService.execute(() -> {
                try {
                    byte[] buffer;
                    synchronized (randomAccessFile){
                        randomAccessFile.seek((long) chunkIndex * CHUNK_SIZE);
                        int bytesToRead = (int) Math.min(CHUNK_SIZE, fileLength - ((long) chunkIndex * CHUNK_SIZE));
                        buffer = new byte[bytesToRead];
                        randomAccessFile.readFully(buffer);
                    }
                    chunks[chunkIndex] = buffer;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();
        randomAccessFile.close();

        Socket socket = new Socket(HOST, PORT);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeInt(totalChunks);

        for (int i= 0; i< totalChunks; i++){
            byte[] chunk = chunks[i];
            dataOutputStream.writeInt(i);
            dataOutputStream.writeInt(chunk.length);
            dataOutputStream.write(chunk);
            System.out.println("Sent chunk "+ i);
        }

        dataOutputStream.close();
        socket.close();
    }

}
