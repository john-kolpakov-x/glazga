package kz.greetgo.glazga.graphics_probe.forms2;

import javax.swing.SwingUtilities;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.concurrent.atomic.AtomicBoolean;

public class MultiBufferingWindow {
  public static void main(String[] args) {
    new MultiBufferingWindow().exec();
  }

  private void exec() {

    Frame f = new Frame("glazga probes buffering paint");
    f.setIgnoreRepaint(true);

    final AtomicBoolean working = new AtomicBoolean(true);

    f.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosed(WindowEvent e) {
        working.set(false);
      }

      @Override
      public void windowClosing(WindowEvent e) {
        f.dispose();
        working.set(false);
      }
    });

    Canvas canvas = new Canvas() {
      @Override
      public void paint(Graphics g) {
//        super.paint(g);
      }
    };
    canvas.setIgnoreRepaint(true);
    f.add(canvas);
    f.pack();

    canvas.createBufferStrategy(2);
    BufferStrategy bufferStrategy = canvas.getBufferStrategy();

    SwingUtilities.invokeLater(() -> {
      f.setSize(1024, 600);
      f.setLocation(4300, 100);
      f.setVisible(true);
    });


    long startedAt = System.currentTimeMillis();

    new Thread(() -> {

      while (working.get()) {

        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();

        float seconds = (float) (System.currentTimeMillis() - startedAt) / 1000.0f;

        int x = 10 + Math.round(seconds * 10f);

        g.setColor(new Color(179, 199, 214));
        g.fillRect(0, 0, canvas.getWidth() - 1, canvas.getHeight() - 1);

        g.setColor(new Color(255, 36, 45));
        g.drawLine(x, 10, 100, 100);

        g.setColor(new Color(27, 182, 40));
        g.drawRect(1, 1, canvas.getWidth() - 3, canvas.getHeight() - 3);

        g.dispose();

        bufferStrategy.show();

        try {
          Thread.sleep(1000 / 24);
        } catch (InterruptedException ignore) {}
      }

    }).start();

  }
}
