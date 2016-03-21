package com.ess.validatorenStreams;

import java.awt.Point;
import java.util.List;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;
import com.ess.heuristiken.Heuristiker;
import com.ess.heuristiken.nextPosHeuristiken.AuswahlNaechsteFreieStelleAnhandFlaechenabmessung;
import com.ess.heuristiken.nextPosHeuristiken.NaechsteFreieStelleSpalte;
import com.ess.heuristiken.nextPosHeuristiken.NaechsteFreieStelleZeile;
import com.ess.heuristiken.nextPosHeuristiken.StandardNextPos;

/**
 * Implementation des {@link IStream} der alle FliesenTypen aus dem
 * stream filtert die durch groessere ersetzt werden koennen.
 * 
 * @author Florian Klinger
 *
 */
public class B4Stream extends AbstractFliesenStream {
	private final Heuristiker heu;
	private boolean isSpalten = false;
	private boolean isZeilen = false;

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
	public B4Stream(Heuristiker heuristiker, IStream<IFliesenTyp> vorGaenger, List<IFliesenTyp> listeAllerFliesen, IFlaecheK flaeche, Point p, int fugenLaenge) {
		super(vorGaenger, listeAllerFliesen, flaeche, p, fugenLaenge);
		heu = heuristiker;
		initialisiereAuswahl(getListeAllerFliesen(), getFlaeche(), getEinfuegePunkt(), getFugenLaenge());
	}

	@Override
	protected boolean istErlaubt(IFliesenTyp fliese) {
		IFliesenTyp iFt = fliese;
		Point einfuegePunkt = getEinfuegePunkt();
		IFlaecheK flache = getFlaeche();
		int x = (iFt.getX() + einfuegePunkt.x) / flache.getQuadrantenLaenge();
		int y = (iFt.getY() + einfuegePunkt.y) / flache.getQuadrantenLaenge();

		for (IFliesenTyp testTyp : getListeAllerFliesen()) {
			// Auch sind nur größere interesanter
			if (testTyp.getX() >= iFt.getX() && testTyp.getY() >= iFt.getY()) {

				// Nur typen die nicht der gleiche sind werden getestet
				if (!testTyp.equals(iFt)) {

					// Moeglichen Startpunkt berechnen

					int xTest = x - (testTyp.getX() / flache.getQuadrantenLaenge());
					int yTest = y - (testTyp.getY() / flache.getQuadrantenLaenge());

					// Teste Mitte
					if (!testeZentraleErsetzung(flache, einfuegePunkt, iFt, testTyp, xTest, yTest)) {
						return false;
					}

					// benötigter Test für Zeilenweise Verlegung
					if (isZeilen) {
						if (!testeNachZeilenHeuristik(flache, einfuegePunkt, iFt, testTyp, yTest)) {
							return false;
						}
					}
					// benötigter Test für Spaltenweise Verlegung
					if (isSpalten) {
						if (!testeNachSpaltenHeuristik(flache, einfuegePunkt, iFt, testTyp, xTest)) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	protected boolean testeNachSpaltenHeuristik(IFlaecheK flache, Point einfuegePunkt, IFliesenTyp iFt, IFliesenTyp testTyp, int xTest) {
		int x3Test = xTest;
		int y3Test = einfuegePunkt.y / flache.getQuadrantenLaenge();
		if (innerhalbDerFlaeche(x3Test, y3Test, testTyp, flache)) {
			// Wenn der punkt nicht der Eckpunkt ist lohnt sich
			// das
			// betrachten auch nicht

			if (flache.getPlatteOnQuadrant(x3Test, y3Test) != null
					&& flache.getVerlegeStartPoint(flache.getPlatteOnQuadrant(x3Test, y3Test)).equals(
							new Point(x3Test * flache.getQuadrantenLaenge(), flache.getQuadrantenLaenge() * y3Test))) {
				// Alle unmöglichen werden aus der Liste
				// gelöscht
				if (kantenAblaufen(x3Test, y3Test, testTyp, einfuegePunkt, iFt, flache)) {
					return false;
				}
			}
		}
		return true;
	}

	protected boolean testeNachZeilenHeuristik(IFlaecheK flache, Point einfuegePunkt, IFliesenTyp iFt, IFliesenTyp testTyp, int yTest) {
		int x2Test = einfuegePunkt.x / flache.getQuadrantenLaenge();
		int y2Test = yTest;
		if (innerhalbDerFlaeche(x2Test, y2Test, testTyp, flache)) {
			// Wenn der punkt nicht der Eckpunkt ist lohnt sich
			// das
			// betrachten auch nicht

			if (flache.getPlatteOnQuadrant(x2Test, y2Test) != null
					&& flache.getVerlegeStartPoint(flache.getPlatteOnQuadrant(x2Test, y2Test)).equals(
							new Point(x2Test * flache.getQuadrantenLaenge(), flache.getQuadrantenLaenge() * y2Test))) {
				// Alle unmöglichen werden aus der Liste
				// gelöscht
				if (kantenAblaufen(x2Test, y2Test, testTyp, einfuegePunkt, iFt, flache)) {
					return false;
				}
			}
		}
		return true;
	}

	protected boolean testeZentraleErsetzung(IFlaecheK flache, Point einfuegePunkt, IFliesenTyp iFt, IFliesenTyp testTyp, int xTest, int yTest) {
		if (innerhalbDerFlaeche(xTest, yTest, testTyp, flache)) {
			// Wenn der punkt nicht der Eckpunkt ist lohnt sich
			// das
			// betrachten auch nicht
			if (flache.getPlatteOnQuadrant(xTest, yTest) != null
					&& flache.getVerlegeStartPoint(flache.getPlatteOnQuadrant(xTest, yTest)).equals(
							new Point(xTest * flache.getQuadrantenLaenge(), flache.getQuadrantenLaenge() * yTest))) {
				// Alle unmöglichen werden aus der Liste
				// gelöscht

				if (kantenAblaufen(xTest, yTest, testTyp, einfuegePunkt, iFt, flache)) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean innerhalbDerFlaeche(int x, int y, IFliesenTyp testTyp, IFlaecheK flache) {
		return x >= 0 && y >= 0 && x + (testTyp.getX() / flache.getQuadrantenLaenge()) <= flache.getAnzahlQuadrateX()
				&& y + (testTyp.getY() / flache.getQuadrantenLaenge()) <= flache.getAnzahlQuadrateY();
	}

	private boolean kantenAblaufen(int x, int y, IFliesenTyp fliesenTyp, Point einfuegePunkt, IFliesenTyp einfuegeTyp, IFlaecheK flaeche) {
		int einfuegeX = einfuegePunkt.x / flaeche.getQuadrantenLaenge();
		int einfuegeY = einfuegePunkt.y / flaeche.getQuadrantenLaenge();
		for (int x2 = x; x2 < (fliesenTyp.getX() / flaeche.getQuadrantenLaenge()) + x; x2++) {
			if (!istAufEinfuegeFlaeche(x2, y, einfuegeX, einfuegeY, einfuegeTyp, flaeche) && !istAufEinfuegeFlaeche(x2, y - 1, einfuegeX, einfuegeY, einfuegeTyp, flaeche)) {
				if (istAuchImFeldDarueber(flaeche, x2, y)) {
					return false;
				}
			}
			if (!istAufEinfuegeFlaeche(x2, y + (fliesenTyp.getY() / flaeche.getQuadrantenLaenge()), einfuegeX, einfuegeY, einfuegeTyp, flaeche)
					&& !istAufEinfuegeFlaeche(x2, y - 1 + (fliesenTyp.getY() / flaeche.getQuadrantenLaenge()), einfuegeX, einfuegeY, einfuegeTyp, flaeche)) {
				if (istAuchImFeldDarueber(flaeche, x2, y + (fliesenTyp.getY() / flaeche.getQuadrantenLaenge()))) {
					return false;

				}
			}
		}
		for (int y2 = y; y2 < (fliesenTyp.getY() / flaeche.getQuadrantenLaenge()) + y; y2++) {
			if (!istAufEinfuegeFlaeche(x, y2, einfuegeX, einfuegeY, einfuegeTyp, flaeche) && !istAufEinfuegeFlaeche(x - 1, y2, einfuegeX, einfuegeY, einfuegeTyp, flaeche)) {
				if (istAuchImFeldLinks(flaeche, x, y2)) {

					return false;
				}
			}
			if (!istAufEinfuegeFlaeche(x + (fliesenTyp.getX() / flaeche.getQuadrantenLaenge()), y2, einfuegeX, einfuegeY, einfuegeTyp, flaeche)
					&& !istAufEinfuegeFlaeche(x + (fliesenTyp.getX() / flaeche.getQuadrantenLaenge()) - 1, y2, einfuegeX, einfuegeY, einfuegeTyp, flaeche)) {
				if (istAuchImFeldLinks(flaeche, x + (fliesenTyp.getX() / flaeche.getQuadrantenLaenge()), y2)) {

					return false;
				}
			}
		}
		return true;
	}

	protected boolean istAufEinfuegeFlaeche(int xVerlege, int yVerlege, int xStartPunktFliese, int yStartPunktFliese, IFliesenTyp fliese, IFlaecheK flaeche) {

		return xVerlege >= xStartPunktFliese && xVerlege < (xStartPunktFliese + (fliese.getX() / flaeche.getQuadrantenLaenge())) && yVerlege >= yStartPunktFliese
				&& yVerlege < (yStartPunktFliese + (fliese.getY() / flaeche.getQuadrantenLaenge()));

	}

	protected boolean istAuchImFeldDarueber(IFlaecheK flaeche, int x, int y) {
		return flaeche.getPlatteOnQuadrant(x, y) == flaeche.getPlatteOnQuadrant(x, y - 1);
	}

	protected boolean istAuchImFeldLinks(IFlaecheK flaeche, int x, int y) {
		return flaeche.getPlatteOnQuadrant(x, y) == flaeche.getPlatteOnQuadrant(x - 1, y);
	}

	protected void initialisiereAuswahl(List<IFliesenTyp> fliesenTypen, IFlaecheK flaeche, Point einfuegePunkt, int fugenlaenge) {
		if (heu.getNextPosHeu() instanceof StandardNextPos || heu.getNextPosHeu() instanceof NaechsteFreieStelleZeile) {
			isZeilen = true;
		} else {
			if (heu.getNextPosHeu() instanceof NaechsteFreieStelleSpalte) {
				isSpalten = true;
			} else {
				if (heu.getNextPosHeu() instanceof AuswahlNaechsteFreieStelleAnhandFlaechenabmessung) {
					if (flaeche.getHoehe() < flaeche.getLaenge()) {
						isSpalten = true;
					} else {
						isZeilen = true;
					}
				} else {
					isSpalten = isZeilen = true;
				}
			}
		}
	}
}
