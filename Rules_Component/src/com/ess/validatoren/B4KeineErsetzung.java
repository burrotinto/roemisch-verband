package com.ess.validatoren;

import java.awt.Point;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;
import com.ess.entity.IPlatte;

/**
 * Implementierung der Bedingung B4 - Keine ersetzung.
 * 
 * Ist gleichzeitig auch eine Filternde Heuristik.
 * 
 * @author Florian Klinger
 *
 */
public abstract class B4KeineErsetzung extends AbstractBedingung {

	private List<IFliesenTyp> alleFliesenTypen;

	protected boolean istAuchImFeldDarueber(IFlaecheK flaeche, int x, int y) {
		return flaeche.getPlatteOnQuadrant(x, y) == flaeche.getPlatteOnQuadrant(x, y - 1);
	}

	protected boolean istAuchImFeldLinks(IFlaecheK flaeche, int x, int y) {
		return flaeche.getPlatteOnQuadrant(x, y) == flaeche.getPlatteOnQuadrant(x - 1, y);
	}

	/**
	 * Gibt zur�ck ob man Die Kanten ablaufen kann
	 * 
	 */
	private boolean kantenAblaufen(int x, int y, IFliesenTyp fliesenTyp, IFlaecheK flaeche) {
		for (int x2 = x; x2 < (fliesenTyp.getX() / flaeche.getQuadrantenLaenge()) + x; x2++) {
			if (istAuchImFeldDarueber(flaeche, x2, y) || istAuchImFeldDarueber(flaeche, x2, y + (fliesenTyp.getY() / flaeche.getQuadrantenLaenge()))) {
				return false;
			}
		}
		for (int y2 = y; y2 < (fliesenTyp.getY() / flaeche.getQuadrantenLaenge()) + y; y2++) {

			if (istAuchImFeldLinks(flaeche, x, y2) || istAuchImFeldLinks(flaeche, x + (fliesenTyp.getX() / flaeche.getQuadrantenLaenge()), y2)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int aufrufReihenfolge() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean pruefe(IFlaecheK flaeche, Map<String, IFliesenTyp> fliesenTypen, int maxFugenLaenge) {
		for (IPlatte platte : flaeche.getSortierteListeDerPlatten()) {
			Point eck = flaeche.getVerlegeStartPoint(platte);
			int x = eck.x / flaeche.getQuadrantenLaenge();
			int y = eck.y / flaeche.getQuadrantenLaenge();

			// Hier wird geschaut was und ob ersetzt werden kann
			for (Entry<String, IFliesenTyp> fliesenTypEntry : fliesenTypen.entrySet()) {
				IFliesenTyp fliesenTyp = fliesenTypEntry.getValue();
				// bei gleichen braucht man nicht zu schauen
				if (!fliesenTyp.getID().equals(platte.getFliesenTypID())) {
					// Auch lohnt sich ein betrachten nur bei größeren
					// Fliesen
					if (fliesenTyp.getX() >= platte.getX() && fliesenTyp.getY() >= platte.getY()) {
						// Auch wenn die Platte über den Rand schaut
						// interesiert es nicht
						if (((fliesenTyp.getX() + (x * flaeche.getQuadrantenLaenge()) <= flaeche.getLaenge()) || (fliesenTyp.getY() + (y * flaeche.getQuadrantenLaenge()) <= flaeche
								.getHoehe()))) {

							// Ablaufen der Kanten
							if (kantenAblaufen(x, y, fliesenTyp, flaeche)) {
								return false;
							}
						}
					}
				}
			}

		}
		return true;
	}

	public void setAlleFliesenTypen(List<IFliesenTyp> alleFliesenTypen) {
		this.alleFliesenTypen = alleFliesenTypen;
	}
	
	protected List<IFliesenTyp> getAlleFliesenTypen() {
		return alleFliesenTypen;
	}
}
