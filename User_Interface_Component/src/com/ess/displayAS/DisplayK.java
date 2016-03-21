package com.ess.displayAS;

import java.awt.Point;
import java.util.List;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IPlatte;

/**
 * Kontrollklasse für den Anwendungsfall {@link DisplayAAS}
 * 
 * @author Florian Klinger
 *
 */
class DisplayK {
	private IFlaecheK flaeche;
	private List<IPlatte> platten = null;

	DisplayK(IFlaecheK flaeche) {
		this.flaeche = flaeche;
		if (flaeche != null)
			this.platten = flaeche.getSortierteListeDerPlatten();
	}

	/**
	 * Test ob es eine zu verlegende Flaeche gibt
	 * 
	 * @return true wenn es eine Fläcche gibt
	 */
	public boolean hasFlaeche() {
		return flaeche != null;
	}

	/**
	 * Gibt eine Liste der verlegten platten zurück
	 * 
	 * @return Liste mit Platten die auf der Flaeche verlegt wurden
	 */
	public List<IPlatte> getPlatten() {
		return platten;
	}

	/**
	 * Ermittelt den Punkt oben links der verlegten Platte
	 * 
	 * @param platte
	 *            von der man den Punkt erfahren will
	 * @return der ermittelte punkt, null wenn diese Platte nicht verlegt wurde
	 */
	public Point getPunktObenLinks(IPlatte platte) {
		return flaeche.getVerlegeStartPoint(platte);
	}

	/**
	 * Ermittelt den Punkt unten rechts der verlegten Platte
	 * 
	 * @param platte
	 *            von der man den Punkt erfahren will
	 * @return der ermittelte punkt, null wenn diese Platte nicht verlegt wurde
	 */
	public Point getPunktUntenRechts(IPlatte platte) {
		Point p = getPunktObenLinks(platte);
		if (p != null) {
			return new Point(p.x + platte.getX(), p.y + platte.getY());
		} else {
			return null;
		}
	}

	/**
	 * Gibt zurück um welche Platte es sich an dem Punkt handelt
	 * 
	 * @param point
	 *            der angefragte punkt
	 * @return Platte die dort verlegt wurde, null wenn es dort keine gibt
	 */
	public IPlatte getPlatteOnPoint(Point point) {
		return flaeche.getPlatteOnPoint(point);
	}

	/**
	 * Um zu erfahren wie lange ein Quadrant ist
	 * 
	 * @return die Quadrantenlaenge
	 */
	public int getFlaechenQuatrantenLaenge() {
		return flaeche.getQuadrantenLaenge();
	}
}
