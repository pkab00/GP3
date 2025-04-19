package org.gp3.parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class WavParser implements IParser {
    private double calculateDuration(String filePath) throws IOException {
        try(FileInputStream fis = new FileInputStream(new File(filePath))){
            byte[] header = new byte[44]; // Минимальный размер WAV-заголовка
            fis.read(header);

            // Извлекаем параметры из заголовка (little-endian)
            int sampleRate = bytesToInt(header, 24, 4);
            int channels = bytesToInt(header, 22, 2);
            int bitsPerSample = bytesToInt(header, 34, 2);
            int dataSize = bytesToInt(header, 40, 4); // Размер данных в байтах

            return (double) dataSize / (sampleRate * channels * (bitsPerSample / 8));
        }
    }

    private static int bytesToInt(byte[] bytes, int offset, int length) {
        int value = 0;
        for (int i = 0; i < length; i++) {
            value |= (bytes[offset + i] & 0xFF) << (8 * i);
        }
        return value;
    }

    @Override
    public SongMetadata parse(String filePath) {
        try{
            return SongMetadata.getInstance(null,null, null, null, null,
                    calculateDuration(filePath));
        } catch(Exception e){
            System.out.println("Parsing failed: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
