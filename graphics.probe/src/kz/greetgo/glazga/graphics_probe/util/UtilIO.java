package kz.greetgo.glazga.graphics_probe.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class UtilIO {
  public static String readStr(File file) {
    if (!file.exists()) return null;
    return new String(readBytes(file), StandardCharsets.UTF_8);
  }

  @SuppressWarnings("unused")
  public static String readAllAsStr(InputStream in) {
    if (in == null) return null;
    return new String(readAllBytes(in), StandardCharsets.UTF_8);
  }

  public static byte[] readBytes(File file) {
    try {
      return readAllBytes(new FileInputStream(file));
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }

  }

  public static byte[] readAllBytes(InputStream inputStream) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    copyStreams(inputStream, out);
    return out.toByteArray();
  }

  @SuppressWarnings("UnusedReturnValue")
  public static long copyStreams(InputStream in, ByteArrayOutputStream out) {
    return copyStreams(in, out, 1024 * 8);
  }

  public static long copyStreams(InputStream in, ByteArrayOutputStream out, int bufferSize) {
    try {

      byte buffer[] = new byte[bufferSize];
      long totalCopyBytes = 0;

      while (true) {
        int count = in.read(buffer);
        if (count < 0) return totalCopyBytes;
        out.write(buffer, 0, count);
        totalCopyBytes += count;
      }

    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        in.close();
      } catch (IOException e) {
        //noinspection ThrowFromFinallyBlock
        throw new RuntimeException(e);
      }
    }
  }
}
