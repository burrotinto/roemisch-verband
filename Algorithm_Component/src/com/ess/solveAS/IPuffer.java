package com.ess.solveAS;

import com.ess.entity.IFlaecheK;

interface IPuffer {
	/**
	 * Die MultiThreads legen hier ihre teilverlegten Flaechen ab.
	 * 
	 * @param flaeche
	 *            Eine Flaeche, die die Bedingungen erfüllt
	 * @return wurde die {@link IFlaecheK} erfolgreich in den Speicher
	 *         geschrieben, wird <code>true</code> zurückgegeben, sonst <code>false</code>
	 */
	boolean ablegen(IFlaecheK flaeche);

	/**
	 * Hier können sich die MultiThreadLoeser ihre zu lösende Flaeche holen.
	 * 
	 * @return Eine teilgeloeste Fläche, die alle Bedingungen erfuellt, oder NUll
	 *         wenn es keine mehr gibt.
	 */
	IFlaecheK entnehmen();
}
