package com.ess.entity;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 * Flaechenkotroller der IFlaecheK implementiert, benutzt intern als
 * Datenstruktur der Flaeche ein Array.
 * 
 * Im Vergleich zum {@link FlaecheK} ist er viel schlanker und schneller in der
 * Erstellung einer Kopie, wie auch im finden und einfuegen.
 * 
 * @author Florian Klinger
 *
 */
public class FlaecheK1D implements IFlaecheK {
	private final int MOD;
	private PlatteE[] flaeche;
	private final int length1;
	private final int length2;
	private final int anzahlX;
	private final int anzahlY;
	private int anzahlPlatten;
	private int anzahlFelder, belegteFelder;
	private int letzterFreierPunkt = 0;

	private FlaecheK1D(int mOD, int length1, int length2, int anzahlX, int anzahlY, int anzahlFelder, int belegteFelder, int letzterFreierPunkt, int anzahlPlatten) {
		super();
		MOD = mOD;
		this.length1 = length1;
		this.length2 = length2;
		this.anzahlX = anzahlX;
		this.anzahlY = anzahlY;
		this.anzahlFelder = anzahlFelder;
		this.belegteFelder = belegteFelder;
		this.letzterFreierPunkt = letzterFreierPunkt;
		this.anzahlPlatten = anzahlPlatten;
	}

	public FlaecheK1D(int length1, int length2) throws NichtDasRichtigeFormatException {
		this(length1,length2,20);
	}
	/**
	 * Zur Erzeugung dieser Wunderbaren Klasse.
	 * 
	 * @param length1
	 *            die Laenge
	 * @param length2
	 *            die Breite
	 * @param ggt
	 *            der groesste gemeinsame Teiler der dort einzufuegenden Fliesen
	 * @throws NichtDasRichtigeFormatException
	 *             wird geworfen wenn eine verlegung unmoeglich ist
	 */
	public FlaecheK1D(int length1, int length2, int ggt) throws NichtDasRichtigeFormatException {
		MOD = ggt;
		// Groessentest
		if (((long) length1 * (long) length2) % (MOD * MOD) != 0 || length1 % MOD != 0 || length2 % MOD != 0) {
			throw new NichtDasRichtigeFormatException("Laenge1(" + length1 + ") * Lange2(" + length2 + ") ist kein vielfaches von " + MOD * MOD);
		}
		flaeche = new PlatteE[(length1 / MOD) * (length2 / MOD)];
		this.length1 = length1;
		this.length2 = length2;
		anzahlX = length1 / MOD;
		anzahlY = length2 / MOD;
		anzahlFelder = getAnzahlQuadrateX() * getAnzahlQuadrateY();
		belegteFelder = 0;
		anzahlPlatten = 0;
	}

	@Override
	public int compareTo(IFlaecheK o) {
		int x = -o.getFuellstand() + getFuellstand();
		return (x != 0 ? x : ((getAnzahlVerlegtePlatten() - o.getAnzahlVerlegtePlatten() != 0) ? (getAnzahlVerlegtePlatten() - o.getAnzahlVerlegtePlatten()) : 0));
	}

	@Override
	public void verlegeFliesenTypUngeprueft(Point point, IFliesenTyp fliesenTyp) {
		if (point != null && fliesenTyp != null) {
			int x = getIndexForLength(point.x);
			int y = getIndexForLength(point.y);
			int xL = (fliesenTyp.getX() / MOD) + x;
			int yL = (fliesenTyp.getY() / MOD) + y;
			PlatteE platte = new PlatteE(fliesenTyp, point);
			anzahlPlatten++;
			for (int i = x; i < xL && i < anzahlX && i < getAnzahlQuadrateX(); i++) {
				for (int j = y; j < yL && y < anzahlY && j < getAnzahlQuadrateY(); j++) {
					flaeche[(j * anzahlX) + i] = platte;
					belegteFelder++;
				}
			}
		}
	}

	@Override
	public boolean verlegeFliesenTypGeprueft(Point point, IFliesenTyp fliesenTyp) {
		if (!istFliesenVerlegenMoeglich(point, fliesenTyp)) {
			return false;
		} else {
			verlegeFliesenTypUngeprueft(point, fliesenTyp);
			return true;
		}
	}

	@Override
	public boolean istFliesenVerlegenMoeglich(Point point, IFliesenTyp fliesenTyp) {
		// Nur Eckpunkte sind erlaubte Startpunkte
		if (point == null || (point.x * point.y) % MOD != 0)
			return false;
		else {
			int x = getIndexForLength(point.x);
			int y = getIndexForLength(point.y);

			// Platte wuerde ueber den Rand stehen
			if (x < 0 || y < 0 || (x + getIndexForLength(fliesenTyp.getX()) > getAnzahlQuadrateX()) || (y + getIndexForLength(fliesenTyp.getY()) > getAnzahlQuadrateY())) {
				return false;
			} else {
				// Zu testen ob Platte teilweise oder ganz auf einer anderen
				// liegt
				int xL = getIndexForLength(fliesenTyp.getX()) + x;
				int yL = getIndexForLength(fliesenTyp.getY()) + y;
				for (int i = x; i < xL; i++) {
					for (int j = y; j < yL; j++) {
						if (getPlatteOnQuadrant(i, j) != null) {
							return false;
						}
					}
				}

				// Da bis hier alles geklappt hat passt ja alles
				return true;
			}
		}
	}

	@Override
	public IPlatte getPlatteOnPoint(Point point) {
		// da z.B. -1 % MOD = MOD -1
		if (point.x < 0 || point.y < 0 || point.x >= length1 || point.y >= length2) {
			return null;
		}
		return flaeche[getIndexForLength(point.x) + (getIndexForLength(point.y) * getAnzahlQuadrateX())];
	}

	@Override
	public int getAnzahlQuadrateX() {
		return anzahlX;
	}

	@Override
	public int getAnzahlQuadrateY() {
		return anzahlY;
	}

	@Override
	public IPlatte getPlatteOnQuadrant(int x, int y) {
		if (x >= 0 && y >= 0 && x < getAnzahlQuadrateX() && y < getAnzahlQuadrateY())
			return flaeche[x + (y * getAnzahlQuadrateX())];
		else {
			return null;
		}
	}

	@Override
	public int getQuadrantenLaenge() {
		return MOD;
	}

	@Override
	public int getLaenge() {
		return length1;
	}

	@Override
	public int getHoehe() {
		return length2;
	}

	@Override
	public IFlaecheK getCopy() {

		FlaecheK1D copy = new FlaecheK1D(MOD, length1, length2, anzahlX, anzahlY, anzahlFelder, belegteFelder, letzterFreierPunkt, anzahlPlatten);

		copy.flaeche = new PlatteE[flaeche.length];
		System.arraycopy(flaeche, 0, copy.flaeche, 0, flaeche.length);

		return copy;
	}

	@Override
	public int getAnzahlVerlegtePlatten() {
		return anzahlPlatten;
	}

	@Override
	public Point getVerlegeStartPoint(IPlatte platte) {
		return platte == null ? null : platte.getVerlegePunktObenLinks();
	}

	@Override
	public List<IPlatte> getSortierteListeDerPlatten() {

		// Hashset befuellen
		HashSet<IPlatte> set = new HashSet<>();
		for (IPlatte p : flaeche) {
			if (p != null)
				set.add(p);
		}

		// Liste erstellen
		List<IPlatte> list = new ArrayList<>(set);

		// Liste sortieren
		Collections.sort(list, new Comparator<IPlatte>() {

			@Override
			public int compare(IPlatte o1, IPlatte o2) {
				if (o1.getVerlegePunktObenLinks().y > o2.getVerlegePunktObenLinks().y) {
					return 1;
				} else {
					if (o1.getVerlegePunktObenLinks().y == o2.getVerlegePunktObenLinks().y) {
						return o1.getVerlegePunktObenLinks().x - o2.getVerlegePunktObenLinks().x;
					} else {
						return -1;
					}
				}
			}

		});

		return list;
	}

	@Override
	public Point getNaechsteFreieStelle() {
		while (letzterFreierPunkt < flaeche.length && flaeche[letzterFreierPunkt] != null) {
			letzterFreierPunkt++;
		}
		if (letzterFreierPunkt >= flaeche.length) {
			return null;
		} else {
			// Umwandeln in das Koordinatensystem ausserhalb des Kontrollers
			return new Point((letzterFreierPunkt % getAnzahlQuadrateX()) * getQuadrantenLaenge(), (letzterFreierPunkt / getAnzahlQuadrateX()) * getQuadrantenLaenge());
		}
	}

	@Override
	public int getFuellstand() {
		return (belegteFelder * 100) / anzahlFelder;
	}

	private int getIndexForLength(int length) {
		return length / MOD;

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		int i = 0;
		for (PlatteE platteE : flaeche) {
			if (i % getAnzahlQuadrateX() == 0 && i != 0) {
				sb.append("\n");
			}
			sb.append(((platteE != null) ? platteE.getFliesenTypID() : "XX") + " ");
			i++;
		}
		sb.append("\n");
		return sb.toString();
	}

	@Override
	public void removePlatte(Point point) {
		IPlatte platte = getPlatteOnPoint(point);
		if (point != null && platte != null) {
			int x = getIndexForLength(point.x);
			int y = getIndexForLength(point.y);
			int xL = (platte.getX() / MOD) + x;
			int yL = (platte.getY() / MOD) + y;
			anzahlPlatten--;
			for (int i = x; i < xL && i < anzahlX && i < getAnzahlQuadrateX(); i++) {
				for (int j = y; j < yL && y < anzahlY && j < getAnzahlQuadrateY(); j++) {
					flaeche[(j * anzahlX) + i] = null;
					belegteFelder--;
				}
			}
		}
	}
}
