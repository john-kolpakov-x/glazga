package kz.greetgo.glazga.graphics_probe.forms2;

import kz.greetgo.glazga.graphics_probe.fonts.Fonts;

import javax.swing.SwingUtilities;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Point;
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
    String windowName = getClass().getSimpleName();

    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    GlazgaSettings glazgaSettings = new GlazgaSettings();
    PaintPanel paintPanel = new PaintPanel();

    Frame window = new Frame("glazga probes");
    window.setUndecorated(false);
    AtomicBoolean skipSizeSaving = new AtomicBoolean(true);

    final AtomicBoolean working = new AtomicBoolean(true);

    window.addComponentListener(new ComponentAdapter() {
      boolean skipMoved = true;
      ScheduledFuture<?> moveSaver = null;

      @Override
      public void componentMoved(ComponentEvent e) {
        if (skipMoved) {
          skipMoved = false;
          return;
        }
        if (moveSaver != null) moveSaver.cancel(false);
        moveSaver = scheduler.schedule(
          () -> glazgaSettings.saveWindowLocation(windowName, e.getComponent().getLocation()),
          100, MILLISECONDS
        );
      }

      ScheduledFuture<?> sizeSaver = null;

      @Override
      public void componentResized(ComponentEvent e) {
        if (skipSizeSaving.get()) return;
        if (sizeSaver != null) sizeSaver.cancel(false);
        sizeSaver = scheduler.schedule(
          () -> glazgaSettings.saveWindowSize(windowName, e.getComponent().getSize()),
          100, MILLISECONDS
        );
      }
    });

    Canvas canvas = new Canvas();
    window.add(canvas);
    window.pack();

    canvas.createBufferStrategy(2);
    BufferStrategy bufferStrategy = canvas.getBufferStrategy();

    SwingUtilities.invokeLater(() -> {
      window.setSize(glazgaSettings.loadWindowSize(getClass().getSimpleName(), new Dimension(1024, 600)));
      window.setLocation(glazgaSettings.loadWindowLocation(getClass().getSimpleName(), new Point(100, 100)));
      window.setVisible(true);

      scheduler.schedule(() -> skipSizeSaving.set(false), 100, MILLISECONDS);
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

    window.addWindowListener(new WindowAdapter() {
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
        window.dispose();
      }
    });
  }
}
