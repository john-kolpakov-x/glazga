package kz.greetgo.glazga.graphics_probe.forms;

import kz.greetgo.glazga.graphics_probe.display.Display;
import kz.greetgo.glazga.graphics_probe.display.DisplayBuilder;
import kz.greetgo.glazga.graphics_probe.fonts.Fonts;
import kz.greetgo.glazga.graphics_probe.metric.DrawMetric;
import kz.greetgo.glazga.graphics_probe.util.UtilForm;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class FormProbe extends JPanel {

  public static void main(String[] args) {
    new FormProbe().execute();
  }

  private void execute() {

    JFrame form = new JFrame();
    form.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    form.setSize(800, 400);
    form.setLocation(100, 100);
    form.setTitle("FormProbe");
    UtilForm.trackPos(form);

    form.setUndecorated(true);
    form.setBackground(new Color(255, 255, 255));
    form.setContentPane(this);

    form.setVisible(true);
  }

  final DrawMetric drawMetric = Fonts.With.Merriweather_Light.drawMetric();
  final DisplayBuilder builder = new DisplayBuilder(drawMetric).baseHeight(() -> 80);

  @Override
  public void paint(Graphics g1) {
    Graphics2D g = (Graphics2D) g1;
    Display str = builder.str("Привет мир!!!", 1);
    Fonts.get().applyHints(g);
    str.paint(g, 100, 100);
  }

}
