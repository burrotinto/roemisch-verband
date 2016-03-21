package com.ess.validatorenStreams;

import java.awt.Point;
import java.util.List;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;

/**
 * Ein Stream der alle Fiesentypen ausgibt die an der entsprechende Stelle nicht
 * ueber anderen Fliesen liegen oder ueber den Rand schauen werden.
 * 
 * @author Florian Klinger
 *
 */
public class B0Stream implements IStream<IFliesenTyp> {
	private List<IFliesenTyp> listeDerFliesen;
	private IFlaecheK flaeche;
	private Point p;
	private int i = 0;

	/**
	 * Instanziiert den Stream
	 * 
	 * @param listeDerFliesen
	 *            Liste der zu testenden FliesenTypen
	 * @param flaeche
	 *            Die teilgeloesete Flaeche
	 * @param p
	 *            Der einfuegepunkt
	 */
	public B0Stream(List<IFliesenTyp> listeDerFliesen, IFlaecheK flaeche, Point p) {
		this.flaeche = flaeche;
		this.listeDerFliesen = listeDerFliesen;
		this.p = p;
	}

	@Override
	public IFliesenTyp read() {
		if (i >= listeDerFliesen.size()) {
			return null;
		} else {
			do {
				IFliesenTyp fliese = listeDerFliesen.get(i);
				if (flaeche.istFliesenVerlegenMoeglich(p, fliese)) {
					i++;
					return fliese;
				} else {
					i++;
				}

			} while (i < listeDerFliesen.size());
			return null;
		}
	}

	@Override
	public void read(IFliesenTyp[] array) {
		for(int i = 0;i< array.length;i++){
			array[i] = read();
		}		
	}

}
