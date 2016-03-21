package com.ess.solveAS;

import java.awt.Point;
import java.util.Map;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;
import com.ess.heuristiken.Heuristiker;

/**
 * Als Spezialisierung des {@link AbstraktLoeser} erbt er dessen Methode zum
 * legen eines Verbundes und nutzt diese, um das Problem rekursiv zu lösen.
 * 
 * Da der Solver auch ein runnable ist, muss dieser als Thread gestartet werden,
 * sonst startet die Loesungsfindung nicht.
 *
 * Er implementiert die Loesungsfindung nach der Aufgabenstellung.
 *
 * @author Florian Klinger
 *
 */
class Loeser extends AbstractThreadLoeser {
	private IFlaecheK flaeche;

	/**
	 * Zur Instanzierung des Solvers wird die zu loesende Flaeche, die zu
	 * nutzenden Heuristiken, der SolveKontroller, der die Loesung bekommen soll,
	 * sowie die erlaubten Fliesentypen und die Fugenlaenge mitgegeben.
	 * 
	 * Auch sei nochmal angemerkt, dass der Solver gestartet werden muss.
	 * 
	 * @param flaeche
	 *            Die zu loesende Flaeche
	 * @param heuristiken
	 *            Die Heuristiken und Bedingungen
	 * @param loesungsEmpfaenger
	 *            Wer die "Antwort haben will"
	 * @param fliesenTypen
	 *            Die FliesenTypen, die benutzt werden koennen
	 * @param fugenLaenge
	 *            Die maximale Fugenlaenge
	 */
	Loeser(IFlaecheK flaeche, Heuristiker heuristiken, ILoesungsEmpfaenger loesungsEmpfaenger, Map<String, IFliesenTyp> fliesenTypen, int fugenLaenge) {
		super(heuristiken, loesungsEmpfaenger, fliesenTypen, fugenLaenge);
		this.flaeche = flaeche;
	}

	@Override
	/**
	 * Startet mit der Lösungsfindung
	 */
	public synchronized void run() {
		legeVerbund(flaeche);
		// Da der SolveKontroller den Thread nur abbricht wenn eine Lösung
		// gefunden wurde, muss dem SolveKontroller mitgeteilt werden das keine
		// Lösung gefunden wurde
		if (!isInterrupted())
			gibLoesungAnSolveKontroller(null);
	}

	@Override
	protected void loesungGefunden(IFlaecheK flaeche) {
		gibLoesungAnSolveKontroller(flaeche);
	}

	@Override
	/**
	 * Einfacher rekursiver Aufruf
	 */
	protected void teilFlaecheGefunden(IFlaecheK flaeche) {
		legeVerbund(flaeche);
	}

	@Override
	protected void verlege(IFlaecheK flaeche, IFliesenTyp fliesenTyp, Point p) {
		//Fliese verlegen
		flaeche.verlegeFliesenTypUngeprueft(p, fliesenTyp);
		//Rekursiver Aufruf
		teilFlaecheGefunden(flaeche);
		// Wenn der Loeser nicht gestoppt wurde, wird die Platte wieder entfernt
		if (!isInterrupted())
			flaeche.removePlatte(p);
	}
}
