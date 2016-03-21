package com.ess.entity;

import java.awt.Point;
import java.util.List;

/**
 * IFlaecheK kontollieren den Zugriff auf Flaechen Entit�ten und stellen
 * n�tzliche Zugriffe bereit und regeln das Einf�gen von Fliesen
 * 
 * @author Florian Klinger
 *
 */
public interface IFlaecheK extends Comparable<IFlaecheK> {
	/**
	 * Legt die Platte auf die Fl�che, zu bachten ist hier das KEINE pr�fung
	 * durchgef�hrt wird!!!!
	 * 
	 * @param point
	 *            Punkz auf dem die Fliese gelegt werden soll
	 * 
	 * @param fliesenTyp
	 *            Der Typ der dort hin gelegt wird
	 */
	void verlegeFliesenTypUngeprueft(Point point, IFliesenTyp fliesenTyp);

	/**
	 * Legt die Platte nach einer Pr�fung auf die Fl�che.
	 * 
	 * @param point
	 *            Punkt auf dem die Fliese gelegt werden soll
	 * @param fliesenTyp
	 *            fliesenTyp Der Typ der dort hin gelegt wird
	 * @return true wenn verlegt, false sonst
	 */
	boolean verlegeFliesenTypGeprueft(Point point, IFliesenTyp fliesenTyp);

	/**
	 * Testet ob einer Verlegung Grundsaetzlich nichts im Wege steht
	 * 
	 * @param x
	 *            in cm vom Linken Rand
	 * @param y
	 *            in cm von Oben
	 * @param fliesenTyp
	 *            Der Typ der dort getestet werden soll
	 * @return Wahr wenn dort Platz ist, unwahr wenn nicht
	 */
	boolean istFliesenVerlegenMoeglich(Point point, IFliesenTyp fliesenTyp);

	/**
	 * Will man die Platte haben die an diesen Punkt liegt ist diese Methode
	 * genau richtig
	 * 
	 * @param x
	 *            in cm vom Linken Rand
	 * @param y
	 *            in cm von Oben
	 * @return die Platte die dort liegt, Null wenn leer
	 */
	IPlatte getPlatteOnPoint(Point point);

	/**
	 * Gibt die Anzahl der in Quadrate eingeteilten Laenge zurueck
	 * 
	 * @return Laenge % Quadratseitenlaenge
	 */
	int getAnzahlQuadrateX();

	/**
	 * Gibt die Anzahl der in Quadrate eingeteilten Breite zurueck
	 * 
	 * @return Breite % Quadratseitenlaenge
	 */
	int getAnzahlQuadrateY();

	/**
	 * Gibt die Platte auf dem angegebenen Quadranten zurueck
	 * 
	 * @param x
	 *            die X achse
	 * @param y
	 *            die Y achse
	 * @return Null wenn x oder y ausserhalb des Indexes oder dort keine Platte
	 *         verlegt ist, sonst das dort liegende Element
	 */
	IPlatte getPlatteOnQuadrant(int x, int y);

	/**
	 * Gibt die Seitenlaenge eines Quadranden zurück
	 * 
	 * @return Quadrantseitenlaenge
	 */
	int getQuadrantenLaenge();

	/**
	 * Die Laenge der Flaeche
	 * 
	 * @return laenge in cm
	 */
	int getLaenge();

	/**
	 * Die H�he der Flaeche
	 * 
	 * @return h�he in cm
	 */
	int getHoehe();

	/**
	 * Erstellt eine Kopie des Feldes mitsamt den darauf liegenden Platten
	 * 
	 * @return flache Kopie des Feldes
	 */
	IFlaecheK getCopy();

	/**
	 * Sagt an wie viele Platten schon auf die Flaeche verlegt wurden
	 * 
	 * @return Anzahl der verlegten Platten
	 */
	int getAnzahlVerlegtePlatten();

	/**
	 * Oft ist es n�tzlich zu wissen was der Obere Linke verlegePunkt einer
	 * Platte ist, diese Methode gibt diesen Punkt zur�ck
	 * 
	 * @param platte
	 *            Platte die man wissen will
	 * @return den verlegepunkt oder Null wenn diese Platte nicht verlegt wurde
	 */
	Point getVerlegeStartPoint(IPlatte platte);

	/**
	 * Erstellt eine Liste, aufsteigend Sortiert der Platten die verlegt wurden
	 * 
	 * @return Liste mit Platten
	 */
	List<IPlatte> getSortierteListeDerPlatten();

	/**
	 * Gibt die n�chste freie Stelle zur�ck
	 * 
	 * @return Point einer freien stelle oder Null wenn die Flaeche voll ist
	 */
	Point getNaechsteFreieStelle();

	/**
	 * Gibt den ungefaehren prozentualen Fullstand der Flaeche zur�ck. 0 ist
	 * leer, 100 ist voll.
	 * 
	 * @return ein Wert zwischen 0 und 100 der den F�llstand der Fl�che
	 *         repr�sentiert
	 */
	int getFuellstand();

	/**
	 * Entfernt die Platte die auf der Coordinate liegt vollst�ndig on der
	 * Flaeche
	 * 
	 * @param point
	 *            welche Platte entfernt werden soll
	 */
	void removePlatte(Point point);
}
