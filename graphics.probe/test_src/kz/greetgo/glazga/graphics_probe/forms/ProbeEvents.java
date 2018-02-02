package kz.greetgo.glazga.graphics_probe.forms;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ProbeEvents extends JPanel {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      JFrame f = new JFrame();
      f.setSize(800, 400);
      f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      f.setTitle("Wow");

      ProbeEvents pane = new ProbeEvents();
      pane.setOpaque(true);
      f.setContentPane(pane);

      pane.addMouseMotionListener(new MouseAdapter() {
        @Override
        public void mouseMoved(MouseEvent e) {
          System.out.println('.');
        }
      });

      pane.addMouseListener(new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {
          System.out.println("mouseReleased");
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
      });

      f.setVisible(true);
    });


  }
}
