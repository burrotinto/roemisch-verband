package com.ess.solveAS;

/**
 * Einen Loeser kann man starten und anhalten, Klassen die dieses Interface
 * implementieren sind in der Lage eine Loesung fuer die Probleminstanz zu
 * finden, sofern eine existiert.
 * 
 * @author Florian Klinger
 *
 */
interface ILoeser extends Runnable {
	/**
	 * Startet die Lösungsfindung
	 */
	@Override
	public void run();

	/**
	 * Stoppt den LoesungsThread
	 */
	void interrupt();
}
