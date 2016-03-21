package com.ess.entity;

import java.awt.Point;

/**
 * Implementierung als Entitätsklasse des Interfaces IPlatte.
 * 
 * Eine Platte ist eine verlegte Fliese eines bestimten FliesenTyps
 * 
 * @author Florian Klinger
 *
 */
class PlatteE implements IPlatte {

	private static final long serialVersionUID = 1L;
	private final IFliesenTyp fliesenTyp;
	private final Point verlegePunkt;
	
	/**
	 * Konstruiert einen verlegten {@link IFliesenTyp}
	 * 
	 * @param fliesenTyp der Typ der verlegt wurde
	 * @param verelgePunkt der Punkt oben links
	 */
	PlatteE(IFliesenTyp fliesenTyp,Point verlegePunkt) {
		this.fliesenTyp = fliesenTyp;
		this.verlegePunkt = verlegePunkt;
	}

	@Override
	public int getX() {
		return fliesenTyp.getX();
	}

	@Override
	public int getY() {
		return fliesenTyp.getY();
	}

	@Override
	public String getFliesenTypID() {
		return fliesenTyp.getID();
	}

	@Override
	public String toString() {
		return "TypID=" + getFliesenTypID() + " | x=" + getX() + " | y=" + getY() + " " + verlegePunkt.toString();
	}

	@Override
	public Point getVerlegePunktObenLinks() {
		return verlegePunkt;
	}
}
