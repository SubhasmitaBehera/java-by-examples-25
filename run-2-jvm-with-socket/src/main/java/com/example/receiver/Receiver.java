package com.example.receiver;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver {
    private static final int PORT = 5050;
    private static final String OUTPUT_FILE = "output.txt";

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Receiver listening on port " + PORT);
        Socket socket = serverSocket.accept();
        System.out.println("Connection accepted from: "+ socket.getInetAddress());

        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        int chunkCount = dataInputStream.readInt();

        byte[][] chunks = new byte[chunkCount][];
        for (int i = 0; i<chunkCount; i++) {
            int chunkIndex = dataInputStream.readInt();
            int chunkSize = dataInputStream.readInt();
            byte[] buffer = new byte[chunkSize];
            dataInputStream.readFully(buffer);
            chunks[chunkIndex] = buffer;
            System.out.println("Received chunk " + chunkIndex);

        }
        try(FileOutputStream fileOutputStream = new FileOutputStream(OUTPUT_FILE)){
            for (byte[] chunk : chunks){
                fileOutputStream.write(chunk);
            }
        }

        System.out.println("File received and reconstructed as "+ OUTPUT_FILE);
        dataInputStream.close();
        socket.close();
        serverSocket.close();

    }
}
