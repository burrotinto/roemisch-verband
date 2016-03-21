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
	 * R�ckgabe der l�nge der Platte
	 * 
	 * @return l�nge in cm
	 */
	int getX();

	/**
	 * R�ckgabe seiner Breite
	 * 
	 * @return breite in cm
	 */
	int getY();

	/**
	 * Da jede Platte zu einem FliesenTyp geh�rt kann man sich hier die ID des
	 * Typs geben lassen
	 * 
	 * @return ID des verlegten FliesenTyps
	 */
	String getFliesenTypID();

	/**
	 * Gibt den Verlegepunkt oben links der Platte zur�ck
	 * @return ein Punkt
	 */
	Point getVerlegePunktObenLinks();
}
