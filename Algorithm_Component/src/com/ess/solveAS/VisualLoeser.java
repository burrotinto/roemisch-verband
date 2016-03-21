package com.ess.solveAS;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;
import com.ess.entity.IPlatte;
import com.ess.heuristiken.Heuristiker;

/**
 * Erweitert den {@link Loeser} um ein Swing Frame, dass die Loesungfindung auf
 * den Bildschirm zeichnet.
 * Mittels 'w' kann die Ausgabe beschleunigt und bei 's' verlangsamt werden
 * 
 * @author Florian Klinger
 *
 */
class VisualLoeser extends Loeser implements KeyListener {

	private JFrame frame = new JFrame("The VisualSolver");
	private VerlegungsPanel panel;
	private int speed = 128;
	private int i = 0;
	private JLabel label = new JLabel("Speed:" + speed);

	VisualLoeser(IFlaecheK flaeche, Heuristiker heuristiken, ILoesungsEmpfaenger loesungsEmpfaenger, Map<String, IFliesenTyp> fliesenTypen, int fugenLaenge) {
		super(flaeche, heuristiken, loesungsEmpfaenger, fliesenTypen, fugenLaenge);
		frame.setLayout(new BorderLayout());
		panel = new VerlegungsPanel(flaeche);
		frame.addKeyListener(this);
		frame.add(panel, BorderLayout.CENTER);
		frame.add(label, BorderLayout.SOUTH);
		frame.setSize(320, 320);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	@Override
	public synchronized void run() {
		super.run();
	}

	@Override
	protected void teilFlaecheGefunden(IFlaecheK flaeche) {
		label.setText("Aufrufe: " + i++ + "    | Speed:" + speed);
		panel.updateFlaeche(flaeche);
		try {
			Thread.sleep(speed);
		} catch (InterruptedException e) {
		}
		super.teilFlaecheGefunden(flaeche);
	}

	/**
	 * Panel, dass die Verlegung zeichnet.
	 * 
	 * @author Florian Klinger
	 *
	 */
	private class VerlegungsPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private IFlaecheK flaeche;

		private Stroke stroke = new BasicStroke(4);

		public VerlegungsPanel(IFlaecheK flaeche) {
			this.flaeche = flaeche;
		}

		void updateFlaeche(IFlaecheK flaeche) {
			this.flaeche = flaeche;
			revalidate();
			repaint();
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			// Graphics2D scalieren und in die Mitte rücken.
			transformGraphicsToCenter(g2d);
			transformGraphicsToUserCoordinatesystem(g2d);
			List<IPlatte> platten = flaeche.getSortierteListeDerPlatten();
			g2d.setColor(Color.LIGHT_GRAY);
			g2d.setStroke(new BasicStroke(5));
			g2d.fillRect(0, 0, flaeche.getLaenge(), flaeche.getHoehe());
			g2d.setColor(Color.DARK_GRAY);
			g2d.drawRect(0, 0, flaeche.getLaenge(), flaeche.getHoehe());

			for (IPlatte platte : platten) {
				Point obenLinks = platte.getVerlegePunktObenLinks();
				g.setColor(Color.DARK_GRAY);
				g.fillRect(obenLinks.x, obenLinks.y, platte.getX(), platte.getY());
				g2d.setColor(Color.BLACK);
				g2d.setStroke(stroke);
				g.drawRect(obenLinks.x, obenLinks.y, platte.getX(), platte.getY());
				g.drawRect(obenLinks.x + 1, obenLinks.y + 1, platte.getX() - 2, platte.getY() - 2);

			}
		}

		private void transformGraphicsToCenter(Graphics2D g2d) {
			g2d.transform(AffineTransform.getTranslateInstance(getXShift(), getYShift()));
		}

		private void transformGraphicsToUserCoordinatesystem(Graphics2D g2d) {
			g2d.transform(AffineTransform.getScaleInstance(getScale(), getScale()));
		}

		private double getScale() {
			return Math.min(getWidth() / (double) flaeche.getLaenge(), getHeight() / (double) flaeche.getHoehe());
		}

		private double getXShift() {
			return 0.5 * getWidth() - (flaeche.getLaenge() * 0.5 * getScale());
		}

		private double getYShift() {
			return 0.5 * getHeight() - (flaeche.getHoehe() * 0.5 * getScale());
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyChar()) {
		case 's':
			// langsamer
			if (speed == 0) {
				speed = 1;
			} else {
				speed = Math.min(speed * 2, 65536);
			}
			break;
		case 'w':
			// schneller
			speed = speed / 2;
			break;
		default:
			break;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
