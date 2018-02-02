package kz.greetgo.glazga.graphics_probe.forms;

import kz.greetgo.glazga.graphics_probe.forms.events.FormExit;
import kz.greetgo.glazga.graphics_probe.forms.events.MouseMove;
import kz.greetgo.glazga.graphics_probe.util.UtilForm;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormProbe extends JComponent {

  public static void main(String[] args) {
    new FormProbe().execute();
  }

  final Form form = new FormImpl();

  private void execute() {

    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setSize(800, 400);
    frame.setLocation(100, 100);
    frame.setTitle("FormProbe");
    UtilForm.trackPos(frame);

    frame.setUndecorated(true);
    this.setOpaque(true);
    frame.setContentPane(this);

    form.attachRepaint(this::repaint);

    this.addMouseMotionListener(new MouseAdapter() {
      @Override
      public void mouseMoved(MouseEvent e) {
        form.goEvent(new MouseMove(e.getX(), e.getY()));
      }
    });

    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseExited(MouseEvent e) {
        form.goEvent(new FormExit());
      }
    });

    frame.setVisible(true);
  }

  @Override
  public void paint(Graphics g) {
    form.paint((Graphics2D) g, getWidth(), getHeight());
  }
}
