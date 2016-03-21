package com.ess.heuristiken.sortierHeuristiken;

import java.awt.Point;
import java.util.List;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;
import com.ess.heuristiken.Heuristiker;
import com.ess.heuristiken.LoeserEnums;

/**
 * Automatische Auswahl der zu verwendenden Heuristik
 * 
 * @author Florian Klinger
 *
 */
public class AutoHeuristik implements ISortierHeuristik {

	private final Heuristiker heu;
	private ISortierHeuristik heuristik;
	private boolean init = false;

	public AutoHeuristik(Heuristiker heuristiker) {
		heu = heuristiker;
	}

	@Override
	public List<IFliesenTyp> sortiereMittelsHeuristik(List<IFliesenTyp> fliesenTypen, IFlaecheK flache, Point einfuegePunkt, int fugenlaenge) {
		if (!init) {
			initHeuristik();
		}
		if (heuristik == null) {
			return fliesenTypen;
		} else {
			return heuristik.sortiereMittelsHeuristik(fliesenTypen, flache, einfuegePunkt, fugenlaenge);
		}
	}

	private void initHeuristik() {
		
		switch (heu.getGewuenschtesLoesungsVerfahren()) {
		case SINGLETHREADBACKTRACK:
			heuristik = new GroessteZuErst();
			break;
		case AUTO:
			if ((heu.getGewuenschtesLoesungsVerfahren() == LoeserEnums.AUTO && Runtime.getRuntime().availableProcessors() > 2)
					|| heu.getGewuenschtesLoesungsVerfahren() == LoeserEnums.MULTITHREADBACKTRACK) {
				// Bei diesem Loeser geht es allein um Geschwindigkeit, darum
				// wird
				// keine Rechenleistung zum sortieren verschwendet
				heuristik = null;
			} else {
				heuristik = new GroessteZuErst();
			}
			break;
		default:
			heuristik = null;
			break;
		}
		
		init = true;
	}

}
