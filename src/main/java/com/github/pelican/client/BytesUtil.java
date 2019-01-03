package com.github.pelican.client;

import java.nio.ByteBuffer;

public class BytesUtil {

    public static byte[] append(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];

        int i = 0;
        for (; i < a.length; i++) {
            result[i] = a[i];
        }

        int bIndex = 0;
        for (; i < result.length ; i++) {
            result[i] = b[bIndex++];
        }

        return result;
    }

    public static byte[] toBytes(int data) {
        return ByteBuffer.allocate(4).putInt(data).array();
    }

    public static int toInt(byte[] data) {
        return ByteBuffer.wrap(data).getInt();
    }
}
