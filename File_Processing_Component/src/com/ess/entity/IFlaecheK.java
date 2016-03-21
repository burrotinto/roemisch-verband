package com.ess.entity;

import java.awt.Point;
import java.util.List;

/**
 * IFlaecheK kontollieren den Zugriff auf Flaechen Entitäten und stellen
 * nützliche Zugriffe bereit und regeln das Einfügen von Fliesen
 * 
 * @author Florian Klinger
 *
 */
public interface IFlaecheK extends Comparable<IFlaecheK> {
	/**
	 * Legt die Platte auf die Fläche, zu bachten ist hier das KEINE prüfung
	 * durchgeführt wird!!!!
	 * 
	 * @param point
	 *            Punkz auf dem die Fliese gelegt werden soll
	 * 
	 * @param fliesenTyp
	 *            Der Typ der dort hin gelegt wird
	 */
	void verlegeFliesenTypUngeprueft(Point point, IFliesenTyp fliesenTyp);

	/**
	 * Legt die Platte nach einer Prüfung auf die Fläche.
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
	 * Gibt die Seitenlaenge eines Quadranden zurÃ¼ck
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
	 * Die Höhe der Flaeche
	 * 
	 * @return höhe in cm
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
	 * Oft ist es nützlich zu wissen was der Obere Linke verlegePunkt einer
	 * Platte ist, diese Methode gibt diesen Punkt zurück
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
	 * Gibt die nächste freie Stelle zurück
	 * 
	 * @return Point einer freien stelle oder Null wenn die Flaeche voll ist
	 */
	Point getNaechsteFreieStelle();

	/**
	 * Gibt den ungefaehren prozentualen Fullstand der Flaeche zurück. 0 ist
	 * leer, 100 ist voll.
	 * 
	 * @return ein Wert zwischen 0 und 100 der den Füllstand der Fläche
	 *         repräsentiert
	 */
	int getFuellstand();

	/**
	 * Entfernt die Platte die auf der Coordinate liegt vollständig on der
	 * Flaeche
	 * 
	 * @param point
	 *            welche Platte entfernt werden soll
	 */
	void removePlatte(Point point);
}
