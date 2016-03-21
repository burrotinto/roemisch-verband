package com.ess.validatoren;

import java.awt.Point;
import java.util.Collection;

import com.ess.entity.IFlaecheK;

/**
 * Diese abstarkte Klasse implementiert teilweise das Interfaface
 * {@link IBedingungKompletteFlaeche} und erweitert es um eine Innere Abstrakte Parametrische
 * Klasse mit deren hilfe man die Bedingungspr�fer parallelisieren kann. Aber
 * auch hier hat es sich gezeigt das die Laufzeit dadurch ansteigt.
 *  Deshalb ist die einzige Klasse die das benutzt {@link B0NurPasssendeBedingungMultiThread}
 * 
 * @author Florian Klinger
 *
 */
public abstract class AbstractBedingung implements IBedingungKompletteFlaeche , IPunktuelleBedingung{

	@Override
	public int compareTo(IPunktuelleBedingung o) {
		return aufrufReihenfolge() - o.aufrufReihenfolge();
	}

	/**
	 * Dr Abstrakte Pr�fer schreibt sein ergebnis in die ihm �bergebene Liste.
	 * Da ein Thread muss er gestartet werden.
	 * 
	 * @author Florian Klinger
	 *
	 */
	protected abstract class Pruefer<T> extends Thread {
		Collection<T> coll;
		IFlaecheK flaeche;
		Point einfuegePunkt;
		T pruefObjekt;
		Object lock;

		public Pruefer(Collection<T> liste2, IFlaecheK flaeche, Point einfuegePunkt, T pruefObjekt, Object lock) {
			super();
			this.coll = liste2;
			this.flaeche = flaeche;
			this.einfuegePunkt = einfuegePunkt;
			this.pruefObjekt = pruefObjekt;
			this.lock = lock;
		}

		@Override
		public void run() {
			if (isGueltig()) {
				synchronized (lock) {
					coll.add(pruefObjekt);
					lock.notifyAll();
				}
			} else {
				synchronized (lock) {
					coll.add(null);
					lock.notifyAll();
				}
			}
		}

		protected abstract boolean isGueltig();
	}
}
