package com.ess.solveAS;

import com.ess.entity.IFlaecheK;
import com.ess.parser.IParserReader;

/**
 * Implementierung des Akteursanwendungsfalles Solve. Gibt einen R�mischen
 * Verbund zur�ck, wenn dieser existiert.
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
	 *            Enth�lt die Probleminstanz
	 * @param fugenLaenge
	 *            Die maximale Fugenl�nge
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
	 * Methode, die einen R�mischen Verbund zur�ckgibt, sofern es denn auch einen
	 * gibt.
	 * 
	 * @return Wenn es eine m�gliche L�sung des Problems gibt, wird diese Fl�che
	 *         zur�ckgegeben, ansonsten Null
	 */
	public IFlaecheK getRoemischenVerbund() {
		if (!loesungGesucht) {
			flaeche = contoller.findeLoesung(parser);
		}
		return flaeche;
	}
}
