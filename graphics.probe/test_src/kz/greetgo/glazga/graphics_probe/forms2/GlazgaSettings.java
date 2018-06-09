package kz.greetgo.glazga.graphics_probe.forms2;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.util.Collections.singletonList;

public class GlazgaSettings {

  String dir = System.getProperty("user.home") + "/.glazga";

  private static final String MAIN_WINDOW_LOCATION = "main_window_location";

  public Point loadGameWindowLocation() {


    File file = new File(dir + "/" + MAIN_WINDOW_LOCATION);
    if (!file.exists()) return new Point(100, 100);

    try {
      List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

      String[] split = lines.get(0).split("\\s+");

      int x = Integer.parseInt(split[0]);
      int y = Integer.parseInt(split[1]);

      return new Point(x, y);

    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
      return new Point(100, 100);
    }
  }

  public void saveGameWindowLocation(Point location) {
    new File(dir).mkdirs();

    String str = location.x + " " + location.y;

    try {
      Files.write(Paths.get(dir + "/" + MAIN_WINDOW_LOCATION), singletonList(str), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }
}
