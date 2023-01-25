package com.kaliv.myths.common.image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.springframework.stereotype.Component;

@Component
public class ImageCompressor {

    public static byte[] compressImage(byte[] data) throws IOException {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        byte[] result;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length)) {
            byte[] tempByteArray = new byte[4 * 1024];
            while (!deflater.finished()) {
                int size = deflater.deflate(tempByteArray);
                outputStream.write(tempByteArray, 0, size);
            }
            result = outputStream.toByteArray();
        }
        return result;
    }

    public static byte[] decompressImage(byte[] data) throws IOException, DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);

        byte[] tempByteArray = new byte[4 * 1024];
        byte[] result;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length)) {
            while (!inflater.finished()) {
                int count = inflater.inflate(tempByteArray);
                outputStream.write(tempByteArray, 0, count);
            }
            result = outputStream.toByteArray();
        }
        return result;
    }
}