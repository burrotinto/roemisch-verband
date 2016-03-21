package com.ess.solveAS;

import com.ess.entity.IFlaecheK;
import com.ess.parser.IParserReader;

/**
 * Implementierung des Akteursanwendungsfalles Solve. Gibt einen Römischen
 * Verbund zurück, wenn dieser existiert.
 * 
 * @author Florian Klinger
 *
 */
public class SolveAAS {
	private final IParserReader parser;
	private SolveK contoller;
	private IFlaecheK flaeche;
	private boolean loesungGesucht = false;

	/**
	 * Instanziert den Anwendungsfall
	 * 
	 * @param parser
	 *            Enthält die Probleminstanz
	 * @param fugenLaenge
	 *            Die maximale Fugenlänge
	 */
	public SolveAAS(IParserReader parser, int fugenLaenge) {
		this.parser = parser;
		contoller = new SolveK(fugenLaenge);
	}

	@Override
	public String toString() {
		IFlaecheK flaeche = getRoemischenVerbund();
		if (flaeche == null) {
			return "Keine Loesung";
		} else
			return flaeche.toString();

	}

	/**
	 * Methode, die einen Römischen Verbund zurückgibt, sofern es denn auch einen
	 * gibt.
	 * 
	 * @return Wenn es eine mögliche Lösung des Problems gibt, wird diese Fläche
	 *         zurückgegeben, ansonsten Null
	 */
	public IFlaecheK getRoemischenVerbund() {
		if (!loesungGesucht) {
			flaeche = contoller.findeLoesung(parser);
		}
		return flaeche;
	}
}
