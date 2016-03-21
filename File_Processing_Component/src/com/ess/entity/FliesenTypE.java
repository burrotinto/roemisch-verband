package com.ess.entity;

/**
 * Implementierung des {@link IFliesenTyp}
 * 
 * @author Florian Klinger
 *
 */
public class FliesenTypE implements IFliesenTyp {

	private static final long serialVersionUID = 1L;
	
	private final String id;
	private final int x;
	private final int y;

	/**
	 * Konstruiert einen FliesenTyp
	 * 
	 * @param x
	 *            Die Laenge der Fliese
	 * @param y
	 *            Die Breite der Fliese
	 * @param id
	 *            Die ID der Fliese
	 */
	public FliesenTypE(int x, int y, String id) {
		this.x = x;
		this.y = y;
		this.id = id;
	}

	/**
	 * Gleich sind Typen Wenn die Größe und die ID Übereinstimmt stimmt
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof IFliesenTyp){
			IFliesenTyp f = (IFliesenTyp) obj;
			return f.getID().equals(getID());
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "ID=" + getID() + " x=" + getX() + " y=" + getY();
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}
}
