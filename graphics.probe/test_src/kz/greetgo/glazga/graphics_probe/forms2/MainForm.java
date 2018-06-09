package kz.greetgo.glazga.graphics_probe.forms2;

import kz.greetgo.glazga.graphics_probe.fonts.Fonts;

import javax.swing.SwingUtilities;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainForm {
  public static void main(String[] args) {
    new MainForm().exec();
  }

  private void exec() {

    Timer timer = new Timer();
    GlazgaSettings glazgaSettings = new GlazgaSettings();
    PaintPanel paintPanel = new PaintPanel();

    Frame f = new Frame("glazga probes");

    final AtomicBoolean working = new AtomicBoolean(true);

    f.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosed(WindowEvent e) {
        working.set(false);
        timer.cancel();
      }

      @Override
      public void windowClosing(WindowEvent e) {
        f.dispose();
        working.set(false);
      }
    });

    f.addComponentListener(new ComponentAdapter() {
      TimerTask tt = null;
      boolean skip = true;

      @Override
      public void componentMoved(ComponentEvent e) {
        if (skip) {
          skip = false;
          return;
        }
        if (tt != null) tt.cancel();
        tt = new TimerTask() {
          @Override
          public void run() {
            glazgaSettings.saveGameWindowLocation(e.getComponent().getLocation());
          }
        };
        timer.schedule(tt, 100);

      }
    });

    Canvas canvas = new Canvas();
    f.add(canvas);
    f.pack();

    canvas.createBufferStrategy(2);
    BufferStrategy bufferStrategy = canvas.getBufferStrategy();

    SwingUtilities.invokeLater(() -> {
      f.setSize(1024, 600);
      f.setLocation(glazgaSettings.loadGameWindowLocation());
      f.setVisible(true);
    });

    new Thread(() -> {

      paintPanel.startPaint();

      BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
      image.getType();

      while (working.get()) {

        int width = canvas.getWidth();
        int height = canvas.getHeight();
        if (width > 1 && height > 1) {

          if (image.getWidth() != width || image.getHeight() != height) {
            image = new BufferedImage(width, height, image.getType());
            System.out.println("Changed image size to " + width + "x" + height);
          }

          {
            Graphics2D g = image.createGraphics();
            Fonts.get().applyHints(g);
            g.setColor(new Color(180, 180, 180));
            g.fillRect(0, 0, width, height);
            try {
              paintPanel.paint(g, canvas.getWidth(), canvas.getHeight());
            } finally {
              g.dispose();
            }
          }

          {
            Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();
          }
          bufferStrategy.show();

        }
        try {
          Thread.sleep(1000 / 24);
        } catch (InterruptedException ignore) {}
      }

    }).start();
  }
}
