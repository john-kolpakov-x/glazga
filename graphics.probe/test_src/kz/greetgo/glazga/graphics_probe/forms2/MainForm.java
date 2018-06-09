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
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class MainForm {
  public static void main(String[] args) {
    new MainForm().exec();
  }

  private void exec() {

    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    GlazgaSettings glazgaSettings = new GlazgaSettings();
    PaintPanel paintPanel = new PaintPanel();

    Frame f = new Frame("glazga probes");

    final AtomicBoolean working = new AtomicBoolean(true);

    f.addComponentListener(new ComponentAdapter() {
      boolean skip = true;
      ScheduledFuture<?> saver = null;

      @Override
      public void componentMoved(ComponentEvent e) {
        if (skip) {
          skip = false;
          return;
        }
        if (saver != null) saver.cancel(false);
        saver = scheduler.schedule(
          () -> glazgaSettings.saveGameWindowLocation(e.getComponent().getLocation()),
          100, MILLISECONDS
        );
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

    paintPanel.startPaint();

    ScheduledFuture<?> drawTask = scheduler.scheduleWithFixedDelay(new Runnable() {
      BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
      long startedAt = System.currentTimeMillis();
      long count = 0;
      boolean justStarted = true;

      @Override
      public void run() {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        if (width < 1) return;
        if (height < 1) return;

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
          count++;

          long millis = System.currentTimeMillis() - startedAt;
          if (millis > 0) {
            int fps = (int) Math.round((double) count / (double) millis * 1000.0);
            g.setColor(new Color(255, 255, 255));
            g.drawString("FPS " + fps + " : " + count, 3, 11);
          }

          g.dispose();
        }

        bufferStrategy.show();

        if (justStarted && count > 200) {
          count = 0;
          justStarted = false;
          startedAt = System.currentTimeMillis();
        }
      }
    }, 0, 1000000 / 79, MICROSECONDS);

    f.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosed(WindowEvent e) {
        scheduler.shutdown();
      }

      @Override
      public void windowClosing(WindowEvent e) {
        working.set(false);
        drawTask.cancel(false);
        try {
          drawTask.get();
        } catch (InterruptedException | ExecutionException e1) {
          throw new RuntimeException(e1);
        } catch (CancellationException ignore) {}
        f.dispose();
      }
    });
  }
}
