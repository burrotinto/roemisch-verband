package com.ess.validatoren;

import java.util.Map;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;

/**
 * Klassen die dieses Interface bereitstellen pr�fen ob die Flaeche ihre
 * Bedingung erf�llen
 * 
 * @author Florian Klinger
 *
 */
public interface IBedingungKompletteFlaeche {

	/**
	 * Es wird der ganze Plan ueberprueft ob eine spezielle Bedingung verletzt wurde.
	 * 
	 * @param flaeche
	 *            Der Pr�fling
	 * @param maxFugenLaenge die maximale Fugenlaenge
	 * @param fliesenTypen alle FliesenTypen die zur auswahl stehen
	 * @return wahr wenn die Fl�che die pr�fung bestanden hat, falsch sonst.
	 */
	boolean pruefe(IFlaecheK flaeche, Map<String, IFliesenTyp> fliesenTypen, int maxFugenLaenge);

}
