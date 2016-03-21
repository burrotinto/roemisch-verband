package com.ess.validatoren;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;
import com.ess.entity.IPlatte;

/**
 * Implementierung der Bedingung B3 - Keine gleichen nebeneinander.
 * 
 * Ist gleichzeitig auch eine Filternde Heuristik.
 * 
 * @author Florian Klinger
 *
 */
public class B3KeineGleichen extends AbstractBedingung {


	@Override
	public boolean pruefe(IFlaecheK flaeche, Map<String, IFliesenTyp> fliesenTypen, int maxFugenLaenge) {
		// Die Idee ist wie folgt: Wenn das
		if (flaeche != null) {
			List<IPlatte> platten = flaeche.getSortierteListeDerPlatten();
			if (platten != null) {
				for (IPlatte platte : platten) {
					// Hier recht es wenn man von jeder Platte nur zwei Seiten
					// betrachtet
					if (!checkUntenUndRechts(platte, flaeche)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private boolean checkUntenUndRechts(IPlatte platte, IFlaecheK flaeche) {
		Point aktuell = flaeche.getVerlegeStartPoint(platte);
		Point rPoint = new Point(platte.getX() + aktuell.x, aktuell.y);
		Point uPoint = new Point(aktuell.x, platte.getY() + aktuell.y);

		IPlatte rPlatte = flaeche.getPlatteOnPoint(rPoint);
		IPlatte uPlatte = flaeche.getPlatteOnPoint(uPoint);
		rPoint = flaeche.getVerlegeStartPoint(rPlatte);
		uPoint = flaeche.getVerlegeStartPoint(uPlatte);

		if (rPlatte != null && rPlatte.getFliesenTypID().equals(platte.getFliesenTypID()) && rPoint.y == aktuell.y) {
			return false;
		}
		if (uPlatte != null && uPlatte.getFliesenTypID().equals(platte.getFliesenTypID()) && uPoint.x == aktuell.x) {
			return false;
		}

		return true;
	}

	@Override
	public List<IFliesenTyp> filtereUnmoeglicheTypen(List<IFliesenTyp> fliesen, IFlaecheK flache, Point einfuegePunkt, int fugenlaenge) {
		LinkedList<IFliesenTyp> list = new LinkedList<IFliesenTyp>();
		for (IFliesenTyp iFliesenTyp : fliesen) {
			if (checkObenUndLinksOhneVerlegung(flache, einfuegePunkt, iFliesenTyp)) {
				list.add(iFliesenTyp);
			}
		}
		return list;
	}

	protected boolean checkObenUndLinksOhneVerlegung(IFlaecheK flaeche, Point einfuegePunkt, IFliesenTyp fliesenTyp) {
		Point lPoint = new Point(einfuegePunkt.x - flaeche.getQuadrantenLaenge(), einfuegePunkt.y);
		Point oPoint = new Point(einfuegePunkt.x, einfuegePunkt.y - flaeche.getQuadrantenLaenge());
		IPlatte oPlatte = flaeche.getPlatteOnPoint(oPoint);
		IPlatte lPlatte = flaeche.getPlatteOnPoint(lPoint);

		oPoint = flaeche.getVerlegeStartPoint(oPlatte);
		lPoint = flaeche.getVerlegeStartPoint(lPlatte);

		return !((oPlatte != null && oPlatte.getFliesenTypID().equals(fliesenTyp.getID()) && oPoint.x == einfuegePunkt.x) || (lPlatte != null
				&& lPlatte.getFliesenTypID().equals(fliesenTyp.getID()) && lPoint.y == einfuegePunkt.y));
	}

	@Override
	public int aufrufReihenfolge() {
		return 3;
	}
}
