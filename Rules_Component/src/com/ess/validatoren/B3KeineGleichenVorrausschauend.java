package com.ess.validatoren;

import java.awt.Point;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;
import com.ess.heuristiken.Heuristiker;
import com.ess.heuristiken.nextPosHeuristiken.AuswahlNaechsteFreieStelleAnhandFlaechenabmessung;
import com.ess.heuristiken.nextPosHeuristiken.NaechsteFreieStelleSpalte;
import com.ess.heuristiken.nextPosHeuristiken.NaechsteFreieStelleZeile;

/**
 * Erweitert die Klasse {@link B3KeineGleichen} um die Faehingkeit das feld
 * unter und neben den einfügepunkt zu betrachten. So kann schon eine Zeile /
 * Spalte im vorraus erkannt werden ob dies zu einen Bedingungsbruch führt
 * 
 * @author Florian Klinger
 *
 */
public class B3KeineGleichenVorrausschauend extends B3KeineGleichen {
	private Collection<IFliesenTyp> alleFliesenTypen;
	private int minX, minY;
	private boolean testX = true, testY = true;
	private Heuristiker heuristiken = null;
	private boolean init = false;

	public B3KeineGleichenVorrausschauend(Collection<IFliesenTyp> alleFliesenTypen) {
		super();
		this.alleFliesenTypen = alleFliesenTypen;
	}

	public B3KeineGleichenVorrausschauend(Collection<IFliesenTyp> alleFliesenTypen, Heuristiker heuristiken) {
		this(alleFliesenTypen);
		this.heuristiken = heuristiken;
		init = false;

	}

	private void init(IFlaecheK flaeche) {

		// Gibt es von den Kleinsten Seitenlaengen mehr als 1?
		minX = Integer.MAX_VALUE;
		minY = Integer.MAX_VALUE;
		for (IFliesenTyp iFliesenTyp : alleFliesenTypen) {
			minX = iFliesenTyp.getX() < minX ? iFliesenTyp.getX() : minX;
			minY = iFliesenTyp.getY() < minY ? iFliesenTyp.getY() : minY;
		}
		int anzX = 0;
		int anzY = 0;
		for (IFliesenTyp iFliesenTyp : alleFliesenTypen) {
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
		}
		init = true;
	}

	@Override
	public List<IFliesenTyp> filtereUnmoeglicheTypen(List<IFliesenTyp> fliesen, IFlaecheK flache, Point einfuegePunkt, int fugenlaenge) {
		// Lazy initiation
		if (!init) {
			init(flache);
		}
		LinkedList<IFliesenTyp> list = new LinkedList<IFliesenTyp>();
		for (IFliesenTyp iFliesenTyp : fliesen) {
			if (checkObenUndLinksOhneVerlegung(flache, einfuegePunkt, iFliesenTyp)) {
				// Prüfung ob daneben oder darüber noch ein verlegen anderer
				// FliesenTypen
				// möglich ist

				boolean s1 = true;
				if (flache.getHoehe() > einfuegePunkt.y + iFliesenTyp.getY()
						&& flache.getPlatteOnQuadrant(einfuegePunkt.x / flache.getQuadrantenLaenge(), (einfuegePunkt.y + iFliesenTyp.getY()) / flache.getQuadrantenLaenge()) == null
						&& flache.getPlatteOnQuadrant((einfuegePunkt.x / flache.getQuadrantenLaenge()) - 1, (einfuegePunkt.y + iFliesenTyp.getY()) / flache.getQuadrantenLaenge()) != null) {
					s1 = false;
					for (IFliesenTyp f : alleFliesenTypen) {
						if (flache.istFliesenVerlegenMoeglich(new Point(einfuegePunkt.x, einfuegePunkt.y + iFliesenTyp.getY()), f) && !f.equals(iFliesenTyp)) {
							s1 = true;
							break;
						}
					}
				}
				boolean s2 = true;

				if (s1
						&& flache.getLaenge() > einfuegePunkt.x + iFliesenTyp.getX()
						&& flache.getPlatteOnQuadrant((einfuegePunkt.x + iFliesenTyp.getX()) / flache.getQuadrantenLaenge(), einfuegePunkt.y / flache.getQuadrantenLaenge()) == null
						&& flache.getPlatteOnQuadrant((einfuegePunkt.x + iFliesenTyp.getX()) / flache.getQuadrantenLaenge(), (einfuegePunkt.y / flache.getQuadrantenLaenge()) - 1) != null) {
					s2 = false;
					for (IFliesenTyp f : alleFliesenTypen) {
						if (flache.istFliesenVerlegenMoeglich(new Point(einfuegePunkt.x + iFliesenTyp.getX(), einfuegePunkt.y), f) && !f.equals(iFliesenTyp)) {
							s2 = true;
							break;
						}
					}
				}

				// Pruefe die vorherigen felder
				if ((testX || testY) && s1 && s2) {
					// finde Start der Zeile
					if (testX) {
						Point untenLinksFlaeche = new Point(einfuegePunkt.x / flache.getQuadrantenLaenge() - 1, (iFliesenTyp.getY() + einfuegePunkt.y)
								/ flache.getQuadrantenLaenge() - 1);
						// Es muss nur diese Stelle untersucht werden wenn die
						// vorherige Fliese kleiner war
						if (untenLinksFlaeche.x >= 0 && flache.getPlatteOnQuadrant(untenLinksFlaeche.x, untenLinksFlaeche.y) == null) {
							// Suche anfang
							while (untenLinksFlaeche.y - 1 >= 0 && flache.getPlatteOnQuadrant(untenLinksFlaeche.x, untenLinksFlaeche.y - 1) == null) {
								untenLinksFlaeche.y--;
							}
							// Check ob das Freie Feld groeßer ist
							if (flache.getPlatteOnQuadrant(untenLinksFlaeche.x - (minX / flache.getQuadrantenLaenge()), untenLinksFlaeche.y) != null) {
								s1 = s2 = false;
							}
						}
					}
					if (s1 && s2 && testY) {
						// finde Start der Zeile
						Point obenRechtsFlaeche = new Point((iFliesenTyp.getX() + einfuegePunkt.x) / flache.getQuadrantenLaenge() - 1, einfuegePunkt.y
								/ flache.getQuadrantenLaenge() - 1);
						// Es muss nur diese Stelle untersucht werden wenn die
						// vorherige Fliese kleiner war
						if (obenRechtsFlaeche.y >= 0 && flache.getPlatteOnQuadrant(obenRechtsFlaeche.x, obenRechtsFlaeche.y) == null) {
							// Suche anfang
							while (obenRechtsFlaeche.x - 1 >= 0 && flache.getPlatteOnQuadrant(obenRechtsFlaeche.x - 1, obenRechtsFlaeche.y) == null) {
								obenRechtsFlaeche.x--;
							}
							// Check ob das Freie Feld groeßer ist
							if (flache.getPlatteOnQuadrant(obenRechtsFlaeche.x, obenRechtsFlaeche.y - (minY / flache.getQuadrantenLaenge())) != null) {
								s1 = s2 = false;
							}
						}
					}
				}
				if (s1 && s2) {
					list.add(iFliesenTyp);
				}
			}
		}
		return list;
	}

	@Override
	public int aufrufReihenfolge() {
		return 1;
	}
}
