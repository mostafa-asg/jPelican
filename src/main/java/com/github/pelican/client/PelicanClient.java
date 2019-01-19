package com.github.pelican.client;

import java.io.IOException;
import java.net.Socket;

import static com.github.pelican.client.BytesUtil.*;

public class PelicanClient {

    public enum Strategy {
        Absolute(0),
        Sliding(1);

        private int value;

        Strategy(int value) {
            this.value = value;
        }
    }

    private Socket socket;

    private PelicanClient(Socket socket) {
        if (socket == null) throw new IllegalArgumentException("socket is null");

        this.socket = socket;
    }

    public static PelicanClient connect(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);

        return new PelicanClient(socket);
    }

    public boolean put(String key, String value) throws IOException {
       return put(key, value.getBytes());
    }

    public boolean put(String key, byte[] value) throws IOException {
        String head = String.format("PUT %s %d\n", key, value.length);
        byte[] putCommand = append(head.getBytes(), value);
        byte[] command = append( toBytes(putCommand.length), putCommand );

        socket.getOutputStream().write(command);

        byte[] answer = new byte[3];
        socket.getInputStream().read(answer);
        return new String(answer).equals("OK.");
    }

    public boolean put(String key, byte[] value, String expiration, Strategy strategy) throws IOException {
        String head = String.format("PUTE %s %s %d %d\n", key, expiration, strategy.value, value.length);
        byte[] putCommand = append(head.getBytes(), value);
        byte[] command = append( toBytes(putCommand.length), putCommand );

        socket.getOutputStream().write(command);

        byte[] answer = new byte[3];
        socket.getInputStream().read(answer);
        return new String(answer).equals("OK.");
    }

    public long incCounter(String key, int change) throws IOException {
        String head = String.format("C %s %s %d\n", "Inc", key, change);
        byte[] command = append( toBytes(head.length()), head.getBytes() );

        socket.getOutputStream().write(command);

        byte[] answer = new byte[8];
        socket.getInputStream().read(answer);
        return BytesUtil.toLong(answer);
    }

    public long decCounter(String key, int change) throws IOException {
        String head = String.format("C %s %s %d\n", "Dec", key, change);
        byte[] command = append( toBytes(head.length()), head.getBytes() );

        socket.getOutputStream().write(command);

        byte[] answer = new byte[8];
        socket.getInputStream().read(answer);
        return BytesUtil.toLong(answer);
    }

    public byte[] get(String key) throws IOException {
        String getCommand = String.format("GET %s\n", key);
        byte[] command = append( toBytes(getCommand.length()), getCommand.getBytes() );

        socket.getOutputStream().write(command);

        byte[] valueSize = new byte[4];
        socket.getInputStream().read(valueSize);
        int size = toInt(valueSize);
        byte[] value = new byte[size];
        socket.getInputStream().read(value);

        return value;
    }

    public boolean del(String key) throws IOException {
        String getCommand = String.format("DEL %s\n", key);
        byte[] command = append( toBytes(getCommand.length()), getCommand.getBytes() );

        socket.getOutputStream().write(command);

        byte[] answer = new byte[3];
        socket.getInputStream().read(answer);
        return new String(answer).equals("OK.");
    }

    public void close() throws IOException {
        socket.getOutputStream().write(toBytes(0));
        socket.close();
    }

}
