/**
 * 
 */
package com.ess.validatoren;

import java.awt.Point;
import java.util.List;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;
import com.ess.entity.IPlatte;
import com.ess.heuristiken.nextPosHeuristiken.INaechsteFreieStelleHeuristik;

/**
 * Wrapperklasse die gleichzeitig eine {@link IFlaecheK} und {@link IFliesenTyp}
 * ist. Wird teilweise von den Subklassen der {@link AbstractBedingung} genutzt
 * sowie von dem Lösungsalgorithmus in {@link AbstraktLoeser}. Der vorteil durch
 * das wissen der existenz dieser Klasse liegt darin das man das kopieren und
 * einfügen nur einmal machen muss. Da die entahltenden {@link IFlaecheK} eine
 * Kopie ist mit dem {@link IFliesenTyp} an der vom
 * {@link INaechsteFreieStelleHeuristik} ermittelten Stelle
 * 
 * @author Florian Klinger
 *
 */
public class WrapperFlaechenKCopyMitVerlegtemFliesenTyp implements IFlaecheK, IFliesenTyp {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final IFlaecheK flaeche;
	private final IFliesenTyp fliesentyp;

	/**
	 * Erstellt die Wrapperklasse. Darf nur innerhalb des com.ess.validatoren package instanziiert werden 
	 * 
	 * @param copy eine kopie
	 * @param fliesentyp der eingefuegte Fliesentyp
	 */
	WrapperFlaechenKCopyMitVerlegtemFliesenTyp(IFlaecheK copy, IFliesenTyp fliesentyp) {
		this.flaeche = copy;
		this.fliesentyp = fliesentyp;
	}

	@Override
	public int compareTo(IFlaecheK o) {
		return flaeche.compareTo(o);
	}

	@Override
	public String getID() {
		return fliesentyp.getID();
	}

	@Override
	public void verlegeFliesenTypUngeprueft(Point point, IFliesenTyp fliesenTyp) {
		flaeche.verlegeFliesenTypUngeprueft(point, fliesenTyp);

	}

	@Override
	public boolean verlegeFliesenTypGeprueft(Point point, IFliesenTyp fliesenTyp) {
		return flaeche.verlegeFliesenTypGeprueft(point, fliesenTyp);
	}

	@Override
	public boolean istFliesenVerlegenMoeglich(Point point, IFliesenTyp fliesenTyp) {
		return flaeche.istFliesenVerlegenMoeglich(point, fliesenTyp);
	}

	@Override
	public IPlatte getPlatteOnPoint(Point point) {
		return flaeche.getPlatteOnPoint(point);
	}

	@Override
	public int getAnzahlQuadrateX() {
		return flaeche.getAnzahlQuadrateX();
	}

	@Override
	public int getAnzahlQuadrateY() {
		return flaeche.getAnzahlQuadrateY();
	}

	@Override
	public IPlatte getPlatteOnQuadrant(int x, int y) {
		return flaeche.getPlatteOnQuadrant(x, y);
	}

	@Override
	public int getQuadrantenLaenge() {
		return flaeche.getQuadrantenLaenge();
	}

	@Override
	public int getLaenge() {
		return flaeche.getLaenge();
	}

	@Override
	public int getHoehe() {
		return flaeche.getHoehe();
	}

	@Override
	public int getX() {
		return fliesentyp.getX();
	}

	@Override
	public int getY() {
		return fliesentyp.getY();
	}

	@Override
	public IFlaecheK getCopy() {
		return flaeche.getCopy();
	}

	@Override
	public int getAnzahlVerlegtePlatten() {
		return flaeche.getAnzahlVerlegtePlatten();
	}

	@Override
	public Point getVerlegeStartPoint(IPlatte platte) {
		return flaeche.getVerlegeStartPoint(platte);
	}

	@Override
	public List<IPlatte> getSortierteListeDerPlatten() {
		return flaeche.getSortierteListeDerPlatten();
	}

	@Override
	public Point getNaechsteFreieStelle() {
		return flaeche.getNaechsteFreieStelle();
	}

	@Override
	public int getFuellstand() {
		return flaeche.getFuellstand();
	}

	public boolean equals(Object obj) {
		if (obj instanceof IFliesenTyp) {
			return fliesentyp.equals(obj);
		} else {
			return flaeche.equals(obj);
		}
	}

	@Override
	public String toString() {
		return flaeche.toString();
	}

	@Override
	public void removePlatte(Point point) {
		flaeche.removePlatte(point);		
	}
}
