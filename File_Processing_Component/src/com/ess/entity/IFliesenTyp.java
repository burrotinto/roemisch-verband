package com.ess.entity;

import java.io.Serializable;

/**
 * Dieses Interface beschreibt was ein Fliesentyp bereitstellen soll
 * 
 * @author Florian Klinger
 *
 */
public interface IFliesenTyp extends Serializable {

	/**
	 * Die Laenge des Fliesentyps
	 * 
	 * @return Integerwert der Laenge in cm.
	 */
	public int getX();

	/**
	 * Die Breite des Fliesentyps
	 * 
	 * @return Integerwert der Breite in cm.
	 */
	public int getY();

	/**
	 * Die ID ist dazu da um den Fliesentyp eindeutig zuordnen zu können
	 * 
	 * @return Die ID
	 */
	public String getID();

}
