package com.ess.validatorenStreams;

import java.awt.Point;
import java.util.List;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;
import com.ess.entity.IPlatte;
import com.ess.heuristiken.Heuristiker;
import com.ess.heuristiken.nextPosHeuristiken.AuswahlNaechsteFreieStelleAnhandFlaechenabmessung;
import com.ess.heuristiken.nextPosHeuristiken.NaechsteFreieStelleSpalte;
import com.ess.heuristiken.nextPosHeuristiken.NaechsteFreieStelleZeile;

/**
 * Implementation des {@link IStream} der alle FliesenTypen aus dem
 * stream filtert die die Regel Keine Gleichen Fliesentypen nebeneinander
 * verletzen oder verletzen werden.
 * 
 * @author Florian Klinger
 *
 */
public class B3Stream extends AbstractFliesenStream {

	private static int minX;
	private static int minY;
	private static IFliesenTyp minXFliese, minYFliese;
	private static Heuristiker heuristiken;
	private static boolean testX;
	private static boolean testY;

	/**
	 * Erzeugt den Stream
	 * 
	 * @param heuristiken
	 *            Die verwendeten Heuristiken
	 * @param vorGaenger
	 *            Der {@link IStream} der diesen Stream speisst
	 * @param listeAllerFliesen
	 *            Liste aller erlaubten Fliesen Typen
	 * @param flaeche
	 *            Die zu Loesende teilverlegte Flaeche
	 * @param p
	 *            die Position wo die Fliesen eingefuegt werden sollen
	 * @param fugenLaenge
	 * 
	 */
	public B3Stream(Heuristiker heuristiken, IStream<IFliesenTyp> vorGaenger, List<IFliesenTyp> listeAllerFliesen, IFlaecheK flaeche, Point p, int fugenLaenge) {
		super(vorGaenger, listeAllerFliesen, flaeche, p, fugenLaenge);
		if (heuristiken != B3Stream.heuristiken || heuristiken == null) {
			B3Stream.heuristiken = heuristiken;
			init(getFlaeche());
		}
	}

	private void init(IFlaecheK flaeche) {
		// Was sind die mit den Kleinsten Seitenlaengen?
		minX = Integer.MAX_VALUE;
		minY = Integer.MAX_VALUE;
		for (IFliesenTyp iFliesenTyp : getListeAllerFliesen()) {
			if (iFliesenTyp.getX() < minX) {
				minX = iFliesenTyp.getX();
				minXFliese = iFliesenTyp;
			}
			if (iFliesenTyp.getY() < minY) {
				minY = iFliesenTyp.getY();
				minYFliese = iFliesenTyp;
			}

		}
		// Gibt es von den Kleinsten Seitenlaengen mehr als 1?
		int anzX = 0;
		int anzY = 0;
		for (IFliesenTyp iFliesenTyp : getListeAllerFliesen()) {
			anzX = minX == iFliesenTyp.getX() ? anzX + 1 : anzX;
			anzY = minY == iFliesenTyp.getY() ? anzY + 1 : anzY;
		}

		if (heuristiken != null && (anzX == 1 || anzY == 1)) {
			if (heuristiken.getNextPosHeu() instanceof NaechsteFreieStelleZeile) {
				testX = anzX == 1;
				testY = false;
			} else {
				if (heuristiken.getNextPosHeu() instanceof NaechsteFreieStelleSpalte) {
					testY = anzY == 1;
					testX = false;
				} else {
					if (heuristiken.getNextPosHeu() instanceof AuswahlNaechsteFreieStelleAnhandFlaechenabmessung) {
						if (flaeche.getHoehe() < flaeche.getLaenge()) {
							testY = anzY == 1;
							testX = false;
						} else {
							testX = anzX == 1;
							testY = false;
						}
					} else {
						testY = anzY == 1;
						testX = anzX == 1;
					}
				}
			}
		} else {
			testY = anzY == 1;
			testX = anzX == 1;
		}
	}

	@Override
	protected boolean istErlaubt(IFliesenTyp fliese) {
		IFlaecheK flache = getFlaeche();
		Point einfuegePunkt = getEinfuegePunkt();
		if (checkObenUndLinksOhneVerlegung(flache, einfuegePunkt, fliese)) {
			// Pruefung ob daneben oder darueber noch ein verlegen anderer
			// FliesenTypen
			// moeglich ist
			boolean s1 = true;
			Point nextSenkrecht = new Point(einfuegePunkt.x, einfuegePunkt.y + fliese.getY());
			if (flache.getHoehe() > nextSenkrecht.y) {
				IPlatte pl = flache.getPlatteOnPoint(new Point(nextSenkrecht.x - 1, nextSenkrecht.y));
				if (pl != null && (pl.getVerlegePunktObenLinks().x + pl.getX() != nextSenkrecht.x)) {
					String links = pl.getFliesenTypID();
					while (flache.getPlatteOnPoint(nextSenkrecht) != null) {
						nextSenkrecht.x += flache.getQuadrantenLaenge();
					}
					if (nextSenkrecht.x < flache.getLaenge() && nextSenkrecht.x != einfuegePunkt.x + fliese.getX()) {
						s1 = false;
						for (IFliesenTyp f : getListeAllerFliesen()) {
							if (flache.istFliesenVerlegenMoeglich(nextSenkrecht, f) && !f.getID().equals(links)) {
								s1 = true;
								break;
							}
						}
					}
				} else {
					if (fliese.equals(minYFliese)
							&& einfuegePunkt.x == nextSenkrecht.x
							&& (nextSenkrecht.y + minYFliese.getY() >= flache.getHoehe() || flache.getPlatteOnPoint(new Point(nextSenkrecht.x, nextSenkrecht.y + fliese.getY())) != null)) {
						return false;
					}
				}
			}
			boolean s2 = true;
			Point nextWaagrecht = new Point(einfuegePunkt.x + fliese.getX(), einfuegePunkt.y);
			if (s1 && flache.getLaenge() > nextWaagrecht.x) {
				IPlatte pr = flache.getPlatteOnPoint(new Point(nextWaagrecht.x, nextWaagrecht.y - flache.getQuadrantenLaenge()));
				if (pr != null && (pr.getVerlegePunktObenLinks().y + pr.getY() != nextWaagrecht.y)) {
					String oben = pr.getFliesenTypID();
					while (flache.getPlatteOnPoint(nextWaagrecht) != null) {
						nextWaagrecht.y += flache.getQuadrantenLaenge();
					}
					if (nextWaagrecht.y < flache.getHoehe() && nextWaagrecht.y != einfuegePunkt.y + fliese.getY()) {
						s2 = false;
						for (IFliesenTyp f : getListeAllerFliesen()) {
							if (flache.istFliesenVerlegenMoeglich(nextWaagrecht, f) && !f.getID().equals(oben)) {
								s2 = true;
								break;
							}
						}
					}
				} else {
					if (fliese.equals(minXFliese)
							&& einfuegePunkt.y == nextWaagrecht.y
							&& (nextWaagrecht.x + minXFliese.getX() >= flache.getLaenge() || flache
									.getPlatteOnPoint(new Point(nextWaagrecht.x + minXFliese.getX(), nextWaagrecht.y)) != null)) {
						return false;
					}
				}
			}
			// Pruefe die vorherigen felder
			if ((testX || testY) && s1 && s2) {
				// finde Start der Zeile
				if (testX) {

					Point untenLinksFlaeche = new Point(einfuegePunkt.x / flache.getQuadrantenLaenge(), (fliese.getY() + einfuegePunkt.y) / flache.getQuadrantenLaenge() - 1);
					// Es muss nur diese Stelle untersucht werden wenn die
					// vorherige Fliese kleiner war
					if (untenLinksFlaeche.x >= 0 && flache.getPlatteOnQuadrant(untenLinksFlaeche.x - 1, untenLinksFlaeche.y) == null) {
						untenLinksFlaeche.x--;
						// Suche anfang
						while (untenLinksFlaeche.y - 1 >= 0 && flache.getPlatteOnQuadrant(untenLinksFlaeche.x, untenLinksFlaeche.y - 1) == null) {
							untenLinksFlaeche.y--;
						}
						// Check ob das Freie Feld groesser ist
						if (flache.getPlatteOnQuadrant(untenLinksFlaeche.x - (minX / flache.getQuadrantenLaenge()), untenLinksFlaeche.y) != null) {
							return false;
						}
					} else {
						// oder Wenn es die Kleinste Fliese sein soll und diese
						// am Rand verlegt
						if (fliese.equals(minXFliese) && einfuegePunkt.x + fliese.getX() == flache.getLaenge()
								&& flache.getPlatteOnQuadrant(untenLinksFlaeche.x - 1, untenLinksFlaeche.y) != null) {
							return false;
						}
					}
				}
				if (s1 && s2 && testY) {
					// finde Start der Zeile
					Point obenRechtsFlaeche = new Point((fliese.getX() + einfuegePunkt.x) / flache.getQuadrantenLaenge() - 1, einfuegePunkt.y / flache.getQuadrantenLaenge());

					// Es muss die Stelle untersucht werden wenn die
					// vorherige Fliese kleiner war
					if (obenRechtsFlaeche.y >= 0 && flache.getPlatteOnQuadrant(obenRechtsFlaeche.x, obenRechtsFlaeche.y - 1) == null) {
						obenRechtsFlaeche.y--;
						// Suche anfang
						while (obenRechtsFlaeche.x - 1 >= 0 && flache.getPlatteOnQuadrant(obenRechtsFlaeche.x - 1, obenRechtsFlaeche.y) == null) {
							obenRechtsFlaeche.x--;
						}
						// Check ob das Freie Feld groe�er ist

						if (flache.getPlatteOnQuadrant(obenRechtsFlaeche.x, obenRechtsFlaeche.y - (minY / flache.getQuadrantenLaenge())) != null) {
							return false;
						}
					} else {
						// oder Wenn es die Kleinste Fliese sein soll und diese
						// am Rand verlegt
						if (fliese.equals(minYFliese) && einfuegePunkt.y + fliese.getY() == flache.getHoehe()
								&& flache.getPlatteOnQuadrant(obenRechtsFlaeche.x, obenRechtsFlaeche.y - 1) != null) {
							return false;
						}
					}

					// Test ob die Aktuelle Fliese
				}
			}
			return s1 && s2;

		}
		return false;
	}

	private boolean checkObenUndLinksOhneVerlegung(IFlaecheK flaeche, Point einfuegePunkt, IFliesenTyp fliesenTyp) {
		Point lPoint = new Point(einfuegePunkt.x - flaeche.getQuadrantenLaenge(), einfuegePunkt.y);
		Point oPoint = new Point(einfuegePunkt.x, einfuegePunkt.y - flaeche.getQuadrantenLaenge());
		IPlatte oPlatte = flaeche.getPlatteOnPoint(oPoint);
		IPlatte lPlatte = flaeche.getPlatteOnPoint(lPoint);

		oPoint = flaeche.getVerlegeStartPoint(oPlatte);
		lPoint = flaeche.getVerlegeStartPoint(lPlatte);

		return !((oPlatte != null && oPlatte.getFliesenTypID().equals(fliesenTyp.getID()) && oPoint.x == einfuegePunkt.x) || (lPlatte != null
				&& lPlatte.getFliesenTypID().equals(fliesenTyp.getID()) && lPoint.y == einfuegePunkt.y));
	}
}