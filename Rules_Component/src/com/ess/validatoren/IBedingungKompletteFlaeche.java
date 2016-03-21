package com.ess.validatoren;

import java.util.Map;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;

/**
 * Klassen die dieses Interface bereitstellen prüfen ob die Flaeche ihre
 * Bedingung erfüllen
 * 
 * @author Florian Klinger
 *
 */
public interface IBedingungKompletteFlaeche {

	/**
	 * Es wird der ganze Plan ueberprueft ob eine spezielle Bedingung verletzt wurde.
	 * 
	 * @param flaeche
	 *            Der Prüfling
	 * @param maxFugenLaenge die maximale Fugenlaenge
	 * @param fliesenTypen alle FliesenTypen die zur auswahl stehen
	 * @return wahr wenn die Fläche die prüfung bestanden hat, falsch sonst.
	 */
	boolean pruefe(IFlaecheK flaeche, Map<String, IFliesenTyp> fliesenTypen, int maxFugenLaenge);

}
