package kz.greetgo.glazga.graphics_probe.forms2;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import static java.util.Collections.singletonList;

public class GlazgaSettings {

  String dir = System.getProperty("user.home") + "/.glazga/component_places";

  private File windowLocationFileSaver(String windowName) {
    return new File(dir + "/window/" + windowName + "_location.txt");
  }

  private File windowSizeFileSaver(String windowName) {
    return new File(dir + "/window/" + windowName + "_size.txt");
  }

  public Point loadWindowLocation(String windowName, Point defaultValue) {
    File file = windowLocationFileSaver(windowName);
    if (!file.exists()) return defaultValue;

    try {
      List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

      String[] split = lines.get(0).split("\\s+");

      int x = Integer.parseInt(split[0]);
      int y = Integer.parseInt(split[1]);

      return new Point(x, y);

    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
      return defaultValue;
    }
  }

  public Dimension loadWindowSize(String windowName, Dimension defaultValue) {
    File file = windowSizeFileSaver(windowName);
    if (!file.exists()) return defaultValue;

    try {
      List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

      String[] split = lines.get(0).split("\\s+");

      int width = Integer.parseInt(split[0]);
      int height = Integer.parseInt(split[1]);

      return new Dimension(width, height);

    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
      return defaultValue;
    }
  }

  public void saveWindowLocation(String windowName, Point location) {
    saveStrToFile(location.x + " " + location.y, windowLocationFileSaver(windowName));
  }

  public void saveWindowSize(String windowName, Dimension dimension) {
    saveStrToFile(dimension.width + " " + dimension.height, windowSizeFileSaver(windowName));
  }

  private static void saveStrToFile(String str, File file) {
    try {
      file.getParentFile().mkdirs();
      Files.write(file.toPath(), singletonList(str), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
