package com.ess.solveAS;

import java.util.LinkedList;
import java.util.Map;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;
import com.ess.heuristiken.Heuristiker;

/**
 * Ein {@link AbstractThreadLoeser} implementiert das Interface {@link ILoeser}
 * und stellt die Methode zur Uebergabe gefundener Loesungen an den {@link SolveK}
 * bereit.
 * 
 * @author Florian Klinger
 *
 */
abstract class AbstractThreadLoeser extends AbstraktLoeser implements ILoeser {

	private boolean interrupted = false;

	private ILoesungsEmpfaenger loesungsEmpfaenger;

	/**
	 * Konstruiert die Grundfunktionen
	 * 
	 * @param heuristiken die verwendeten Heuristiken
	 * @param loesungsEmpfaenger wer die Lösung bekommen soll
	 * @param fliesenTypen die erlaubten Fliesentypen
	 * @param fugenLaenge die maximal erlaubte Fugenlaenge
	 */
	public AbstractThreadLoeser(Heuristiker heuristiken, ILoesungsEmpfaenger loesungsEmpfaenger, Map<String, IFliesenTyp> fliesenTypen, int fugenLaenge) {
		super(heuristiken, fugenLaenge, new LinkedList<IFliesenTyp>(fliesenTypen.values()));
		this.loesungsEmpfaenger = loesungsEmpfaenger;
	}

	/**
	 * Gibt dem Solver die gefundene Loesung
	 * 
	 * @param flaeche eine Fläche die dem {@link SolveK} übergeben werden soll
	 */
	protected void gibLoesungAnSolveKontroller(IFlaecheK flaeche) {
		loesungsEmpfaenger.gebeLoesung(flaeche, this);
	}

	@Override
	public void interrupt() {
		interrupted = true;
	}

	@Override
	protected boolean isInterrupted() {
		return interrupted;
	}
	
	
}
