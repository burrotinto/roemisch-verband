package com.ess.solveAS;

import com.ess.entity.IFlaecheK;

interface IPuffer {
	/**
	 * Die MultiThreads legen hier ihre teilverlegten Flaechen ab.
	 * 
	 * @param flaeche
	 *            Eine Flaeche, die die Bedingungen erf�llt
	 * @return wurde die {@link IFlaecheK} erfolgreich in den Speicher
	 *         geschrieben, wird <code>true</code> zur�ckgegeben, sonst <code>false</code>
	 */
	boolean ablegen(IFlaecheK flaeche);

	/**
	 * Hier k�nnen sich die MultiThreadLoeser ihre zu l�sende Flaeche holen.
	 * 
	 * @return Eine teilgeloeste Fl�che, die alle Bedingungen erfuellt, oder NUll
	 *         wenn es keine mehr gibt.
	 */
	IFlaecheK entnehmen();
}
