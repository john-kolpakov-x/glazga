package kz.greetgo.glazga.graphics_probe.util;

import javax.swing.JFrame;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class UtilForm {
  public static void trackPos(JFrame form) {
    File locationFile = new File(System.getProperty("user.home")
      + "/.cache/glazga/" + form.getTitle() + ".location.txt");

    Point p = readPoint(locationFile);
    if (p != null) form.setLocation(p);

    form.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentMoved(ComponentEvent e) {
        writePoint(locationFile, form.getLocationOnScreen());
      }
    });

  }

  public static void writePoint(File file, Point point) {
    if (point == null) {
      file.delete();
      return;
    }
    file.getParentFile().mkdirs();
    try (PrintStream pr = new PrintStream(file, "UTF-8")) {
      pr.print(point.x + " " + point.y);
    } catch (FileNotFoundException | UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  public static Point readPoint(File locationFile) {
    String str = UtilIO.readStr(locationFile);
    if (str == null || str.trim().length() == 0) return null;
    String[] split = str.trim().split("\\s+");
    if (split.length != 2) return null;

    try {
      Point ret = new Point();
      ret.x = Integer.parseInt(split[0]);
      ret.y = Integer.parseInt(split[1]);
      return ret;
    } catch (NumberFormatException e) {
      return null;
    }
  }


}
