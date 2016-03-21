package com.ess.validatorenStreams;

import java.awt.Point;
import java.util.List;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;

/**
 * Stellt das Grundgeruest fuer {@link IStream} bereit. Davon abgeleitete
 * Klassen muessen nur eine implementation der Methode istErlaubt()
 * bereitstellen um funktionsfaehig zu sein
 * 
 * @author Florian Klinger
 *
 */
public abstract class AbstractFliesenStream implements IStream<IFliesenTyp> {
	private IStream<IFliesenTyp> vorGaenger;
	private List<IFliesenTyp> listeAllerFliesen;
	private IFlaecheK flaeche;
	private Point p;
	private int fugenLaenge;

	/**
	 * Constructor fuer die benötigten Attribute.
	 * 
	 * @param vorGaenger
	 *            Der {@link IStream} der diesen Stream speisst
	 * @param listeAllerFliesen
	 *            Liste aller erlaubten Fliesen Typen
	 * @param flaeche
	 *            Die zu Loesende teilverlegte Flaeche
	 * @param p
	 *            die Position wo die Fliesen eingefuegt werden sollen
	 * @param fugenLaenge
	 *            Maximal zulaessig
	 */
	public AbstractFliesenStream(IStream<IFliesenTyp> vorGaenger, List<IFliesenTyp> listeAllerFliesen, IFlaecheK flaeche, Point p, int fugenLaenge) {
		this.flaeche = flaeche;
		this.fugenLaenge = fugenLaenge;
		this.listeAllerFliesen = listeAllerFliesen;
		this.p = p;
		this.vorGaenger = vorGaenger;
	}

	@Override
	public IFliesenTyp read() {
		IFliesenTyp fliese = vorGaenger.read();
		if (fliese == null) {
			return null;
		} else {
			if (istErlaubt(fliese)) {
				return fliese;
			} else {
				return read();
			}
		}
	}

	@Override
	public void read(IFliesenTyp[] array) {
		for(int i = 0;i< array.length;i++){
			array[i] = read();
		}		
	}
	
	public IFlaecheK getFlaeche() {
		return flaeche;
	}

	public int getFugenLaenge() {
		return fugenLaenge;
	}

	public List<IFliesenTyp> getListeAllerFliesen() {
		return listeAllerFliesen;
	}

	public Point getEinfuegePunkt() {
		return p;
	}

	/**
	 * Gibt eine Aussage darueber ob die Fliese an diesen Punkt verlegt werden
	 * kann ohne das es die Bedingung verletzt die der Stream implementiert
	 * 
	 * @param fliese der Pruefling
	 * @return true wenn es zu keiner Bedingungsverletzung kommen wird, false sonst
	 */
	protected abstract boolean istErlaubt(IFliesenTyp fliese);

}
