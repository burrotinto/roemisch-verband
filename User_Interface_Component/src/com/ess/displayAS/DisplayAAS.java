package com.ess.displayAS;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IPlatte;
import com.ess.parser.IParserReader;

import ess.algorithm.RoemischerVerbund;
import ess.algorithm.RoemischerVerbund.Validation;

/**
 * Diese Klasse erzeugt ein Fenster in dem ein Verbund aus Fliesen dargestellt
 * wird
 * 
 * @author Florian Klinger
 *
 */
public class DisplayAAS extends JFrame {

	private static final long serialVersionUID = 1L;
	private List<Validation> fehler;

	/**
	 * Erzeugt ein Display von der Flaeche inklusive der Ausgabe aller
	 * enthaltenen Fehler
	 * 
	 * @param parser
	 *            der Parser der die Probleminstanz enthält
	 * @param maxFugenLaenge
	 *            die Maximal gueltige FugenLänge
	 */
	public DisplayAAS(IParserReader parser, int maxFugenLaenge) {
		fehler = new RoemischerVerbund().validateSolution(parser, maxFugenLaenge);
		initFrame(parser.getVerlegungsPlan());

	}

	/**
	 * Erzeugt ein Display von der Flaeche ohne der Ausgabe der enthaltenen
	 * Fehler
	 * 
	 * @param parser
	 *            der Parser der die Probleminstanz enthält
	 */
	public DisplayAAS(IParserReader parser) {
		fehler = null;
		initFrame(parser.getVerlegungsPlan());
	}

	private void initFrame(IFlaecheK flaeche) {
		setLayout(new BorderLayout());
		JPanel fliesenPanel = new JPanel();

		fliesenPanel.add(new JLabel(" "));
		JPanel verlegungsPanel = new VerlegungsPanel(flaeche, fliesenPanel);
		if (flaeche != null) {
			JPanel size = new JPanel();
			size.add(new JLabel("Größe: " + flaeche.getLaenge() + " x " + flaeche.getHoehe()));
			add(size, BorderLayout.NORTH);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

			setSize(Math.max(((int) screenSize.getWidth()) / 2, flaeche.getLaenge() < screenSize.getWidth() ? flaeche.getLaenge() : 0),
					Math.max(((int) screenSize.getHeight()) / 2, flaeche.getHoehe() < screenSize.getHeight() ? flaeche.getHoehe() : 0));
			// setExtendedState(JFrame.MAXIMIZED_BOTH);
		} else {
			setSize(320, 240);
		}
		setTitle("Florian Klingers Römischer Verbund");
		add(verlegungsPanel, BorderLayout.CENTER);
		add(fliesenPanel, BorderLayout.SOUTH);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		repaint();
	}

	/**
	 * Panel das die Verlegung zeichnet.
	 * 
	 * @author Florian Klinger
	 *
	 */
	private class VerlegungsPanel extends JPanel implements MouseListener, MouseMotionListener {
		private static final long serialVersionUID = 1L;

		private DisplayK controll;
		private JPanel fliesenPanel;
		private IPlatte ausgewaelt = null;
		private IPlatte hoover = null;
		private final IFlaecheK flaeche;

		private Stroke stroke = new BasicStroke(4);

		VerlegungsPanel(IFlaecheK flaeche, JPanel fliesenPanel) {
			super();
			this.flaeche = flaeche;
			this.fliesenPanel = fliesenPanel;
			controll = new DisplayK(flaeche);
			if (controll.hasFlaeche()) {
				addMouseListener(this);
				addMouseMotionListener(this);
			}
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			// Test ob es einen Gültigen verlegungsplan gibt
			if (!controll.hasFlaeche()) {
				g.setColor(Color.RED);
				g.drawString("Kein Verlegungsplan enthalten", getWidth() / 8, getHeight() / 2);
			} else {
				// Graphics2D scalieren und in die Mitte rücken.
				transformGraphicsToCenter(g2d);
				transformGraphicsToUserCoordinatesystem(g2d);
				List<IPlatte> platten = controll.getPlatten();
				g2d.setColor(Color.LIGHT_GRAY);
				g2d.setStroke(new BasicStroke(5));
				g2d.fillRect(0, 0, flaeche.getLaenge(), flaeche.getHoehe());
				g2d.setColor(Color.DARK_GRAY);
				g2d.drawRect(0, 0, flaeche.getLaenge(), flaeche.getHoehe());
				for (IPlatte platte : platten) {
					Point obenLinks = controll.getPunktObenLinks(platte);
					if (obenLinks != null) {
						if (ausgewaelt == platte) {
							if (hoover != null)
								g2d.setPaint(new GradientPaint(hoover.getVerlegePunktObenLinks().x + (hoover.getX() / 2),
										hoover.getVerlegePunktObenLinks().y + (hoover.getY() / 2), Color.red, platte.getVerlegePunktObenLinks().x + (platte.getX() / 2), platte
												.getVerlegePunktObenLinks().y + (platte.getY() / 2), Color.ORANGE, false));
							else
								g.setColor(Color.RED);

						} else {
							if (hoover == platte) {
								g.setColor(Color.WHITE);
							} else {
								if (hoover != null) {
									g2d.setPaint(new GradientPaint(hoover.getVerlegePunktObenLinks().x + (hoover.getX() / 2), hoover.getVerlegePunktObenLinks().y
											+ (hoover.getY() / 2), Color.white, obenLinks.x + (platte.getX() / 2), obenLinks.y + (platte.getY() / 2), Color.DARK_GRAY, false));
								} else
									g.setColor(Color.DARK_GRAY);
							}
						}
						g.fillRect(obenLinks.x, obenLinks.y, platte.getX(), platte.getY());
						g2d.setColor(Color.BLACK);
						g2d.setStroke(stroke);
						g.drawRect(obenLinks.x, obenLinks.y, platte.getX(), platte.getY());
						g.drawRect(obenLinks.x + 1, obenLinks.y + 1, platte.getX() - 2, platte.getY() - 2);
					}
				}

				if (fehler != null) {

					g2d.transform(AffineTransform.getScaleInstance(3 * getScale(), 3 * getScale()));
					if (fehler.size() != 0) {
						g2d.setColor(Color.RED);
						g2d.drawString("Folgende Fehler sind enthalten:", flaeche.getQuadrantenLaenge(), flaeche.getQuadrantenLaenge());
						int i = flaeche.getQuadrantenLaenge();
						for (Validation validation : fehler) {
							i += flaeche.getQuadrantenLaenge();
							g2d.drawString(validation.getFehlertext(), flaeche.getQuadrantenLaenge(), i);
						}
					} else {
						g2d.setColor(Color.GREEN);
						g2d.drawString("Fehlerfrei", flaeche.getQuadrantenLaenge(), flaeche.getQuadrantenLaenge());
					}
				}
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

		private IPlatte getPlatte(int x, int y) {
			return flaeche.getPlatteOnPoint(new Point((int) ((x - getXShift()) / getScale()), (int) ((y - getYShift()) / getScale())));
		}

		/**
		 * Durch die Auswahl einer Platte soll dessen Maße und Typ angezeigt und
		 * die Platte eine andere Farbe bekommen
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			ausgewaelt = getPlatte(e.getX(), e.getY());
			fliesenPanel.removeAll();
			if (ausgewaelt != null) {
				fliesenPanel.add(new JLabel("FliesenTyp: \"" + ausgewaelt.getFliesenTypID() + "\" || Laenge: " + ausgewaelt.getX() + " Breite: " + ausgewaelt.getY()
						+ " || Verlegepunkt Oben-Links: " + ausgewaelt.getVerlegePunktObenLinks().x + " x " + ausgewaelt.getVerlegePunktObenLinks().y));
			} else {
				fliesenPanel.add(new JLabel(" "));
			}
			fliesenPanel.revalidate();
			fliesenPanel.repaint();
			revalidate();
			repaint();
		}

		/**
		 * Wenn die Maus über eine Platte läuft soll diese leuchten
		 */
		@Override
		public void mouseMoved(MouseEvent e) {
			if (hoover != getPlatte(e.getX(), e.getY())) {
				hoover = getPlatte(e.getX(), e.getY());
				revalidate();
				repaint();
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {

		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mouseDragged(MouseEvent e) {
		}

	}
}
