package com.ess.validatoren;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;
import com.ess.entity.IPlatte;

/**
 * Prüft das keine Platten überlappen oder Felder nicht belegt sind. Sowie
 * filtert er alle {@link IFliesenTyp}'en heraus die diese Bedingung verletzen
 * würden
 * 
 * @author Florian Klinger
 *
 */
public class B0NurPassendeBedingung extends AbstractBedingung {

	@Override
	public List<IFliesenTyp> filtereUnmoeglicheTypen(List<IFliesenTyp> fliesenTypen, IFlaecheK flache, Point einfuegePunkt, int fugenlaenge) {
		LinkedList<IFliesenTyp> liste = new LinkedList<IFliesenTyp>();
		for (IFliesenTyp iFliesenTyp : fliesenTypen) {
			if (flache.istFliesenVerlegenMoeglich(einfuegePunkt, iFliesenTyp)) {
				liste.add(iFliesenTyp);
			}
		}
		return liste;
	}

	@Override
	public int compareTo(IPunktuelleBedingung o) {
		// Muss immer zuerst benutzt werden
		return -1;
	}

	@Override
	public int aufrufReihenfolge() {
		return Integer.MIN_VALUE;
	}

	@Override
	public boolean pruefe(IFlaecheK flaeche, Map<String, IFliesenTyp> fliesenTypen, int maxFugenLaenge) {
		if (flaeche != null) {
			for (IPlatte p : flaeche.getSortierteListeDerPlatten()) {
				// wenn eine Platte nicht auf allen feldern ihrer Abmessung
				// liegt ist die Bedingung nicht erfüllt
				for (int x = flaeche.getVerlegeStartPoint(p).x / flaeche.getQuadrantenLaenge(); x < (flaeche.getVerlegeStartPoint(p).x + p.getX()) / flaeche.getQuadrantenLaenge(); x++) {
					for (int y = flaeche.getVerlegeStartPoint(p).y / flaeche.getQuadrantenLaenge(); y < (flaeche.getVerlegeStartPoint(p).y + p.getY())
							/ flaeche.getQuadrantenLaenge(); y++) {

						if (p != flaeche.getPlatteOnQuadrant(x, y)) {
							return false;
						}
					}
				}
			}
			for (int x = 0; x < flaeche.getAnzahlQuadrateX(); x++) {
				for (int y = 0; y < flaeche.getAnzahlQuadrateY(); y++) {
					if (flaeche.getPlatteOnQuadrant(x, y) == null) {
						return false;
					}
				}
			}
		}
		return true;
	}
}
