package com.ess.solveAS;

import java.util.Map;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;
import com.ess.heuristiken.Heuristiker;

/**
 * Als Subtyp von {@link AbstraktLoeser} nutzt dieser dessen Lösungsalgorithmus
 * und erweitert ihn um die Fähigkeit meherere parallel laufen zu lasssen.
 * 
 * Mehrere MultiThreadLoeser können gekoppelt, über einen {@link FlaechenPuffer},
 * gemeinsam einen Römischen Verbund zu einer angegebenen Fläche finden. Ist der
 * Puffer voll, schalten die MultiThreadLoeser auf einen rekursiven Modus um.
 * 
 * Da alle {@link ILoeser} {@link Runnable} sind, muss er gestartet werden.
 * 
 * @author Florian Klinger
 *
 */
class MultiThreadLoeser extends AbstractThreadLoeser {

	private IPuffer puffer;
	private IFlaecheK next = null;
	private boolean loesungAbgegeben = false;

	/**
	 * Konstruktor zum Instanzieren der {@link MultiThreadLoeser}
	 * 
	 * @param puffer
	 *            Der Puffer, von dem Teillösungen geholt und abgegeben werden
	 * @param heuristiken
	 *            Die zu Erfüllenden Bedingungen und Heuristiken
	 * @param loesungsEmpfaenger
	 *            Wer die Lösung bekommen soll
	 * @param fliesenTypen
	 *            Die erlaubten FliesenTypen
	 * @param fugenLaenge
	 *            Die maximale Fugenlänge
	 */
	MultiThreadLoeser(IPuffer puffer, Heuristiker heuristiken, ILoesungsEmpfaenger loesungsEmpfaenger, Map<String, IFliesenTyp> fliesenTypen, int fugenLaenge) {
		super(heuristiken, loesungsEmpfaenger, fliesenTypen, fugenLaenge);
		this.puffer = puffer;
	}

	/**
	 * Begint mit der Lösungsfindung
	 */
	@Override
	public void run() {
		// So lange der Puffer nicht leer ist, und keine Lösung gefunden wurde,
		// probiere.
		while (!isInterrupted() && (next = puffer.entnehmen()) != null) {
			legeVerbund(next);
		}
		// Übergabe, dass dieser MultiThreadloeser nichts gefunden hat.
		if (!loesungAbgegeben)
			gibLoesungAnSolveKontroller(null);
	}

	@Override
	/**
	 * Übergabe der lösung
	 */
	protected void loesungGefunden(IFlaecheK flaeche) {
		gibLoesungAnSolveKontroller(flaeche);
		loesungAbgegeben = true;
	}

	@Override
	/**
	 * Ist der Puffer voll, wird rekursiv weitergearbeitet.
	 */
	protected void teilFlaecheGefunden(IFlaecheK flaeche) {
		if (!puffer.ablegen(flaeche)) {
			legeVerbund(flaeche);
		}
	}
}
