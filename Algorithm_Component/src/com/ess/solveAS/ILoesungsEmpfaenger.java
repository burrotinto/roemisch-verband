package com.ess.solveAS;

import com.ess.entity.IFlaecheK;

/**
 * 
 * Klassen, die dieses Interface implementieren, moechten Loesungen von einem oder mehreren
 * {@link ILoeser} bekommen.
 * 
 * @author Florian Klinger
 *
 */
interface ILoesungsEmpfaenger {

	/**
	 * Hier geben die {@link ILoeser} ihre gefundenen Loesungen ab. Die Bedingung
	 * an die abgegebenen {@link IFlaecheK} ist, dass nur gueltige Loesungen
	 * oder Null abgegeben werden duerfen.
	 * 
	 * @param loesung
	 *            Eine gueltige Loesung oder Null, wenn der {@link ILoeser} keine solche gefunden hat.
	 * @param loeser
	 *            der {@link ILoeser} der sie gefunden hat.
	 */
	void gebeLoesung(IFlaecheK loesung, ILoeser loeser);
}
