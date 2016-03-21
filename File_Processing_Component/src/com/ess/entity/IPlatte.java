package com.ess.entity;

import java.awt.Point;
import java.io.Serializable;

/**
 * Eine Platte ist ein verlegter FliesenTyp
 * 
 * @author Florian Klinger
 *
 */
public interface IPlatte extends Serializable{

	/**
	 * Rückgabe der länge der Platte
	 * 
	 * @return länge in cm
	 */
	int getX();

	/**
	 * Rückgabe seiner Breite
	 * 
	 * @return breite in cm
	 */
	int getY();

	/**
	 * Da jede Platte zu einem FliesenTyp gehört kann man sich hier die ID des
	 * Typs geben lassen
	 * 
	 * @return ID des verlegten FliesenTyps
	 */
	String getFliesenTypID();

	/**
	 * Gibt den Verlegepunkt oben links der Platte zurück
	 * @return ein Punkt
	 */
	Point getVerlegePunktObenLinks();
}
