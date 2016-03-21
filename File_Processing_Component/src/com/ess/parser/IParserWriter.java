package com.ess.parser;

import java.io.IOException;

import com.ess.entity.IFlaecheK;

/**
 * Ein IParserWriter schreibt eine Flaeche wo hin.
 * 
 * @author Florian Klinger
 *
 */
public interface IParserWriter {

	/**
	 * Der Parser �bernimmt die Ausgabe der ihm �bergebenen Flaeche.
	 * 
	 * @param flaeche
	 *            die auszugebene Flaeche
	 * @throws IOException
	 *             Kann vorkommen
	 */
	void write(IFlaecheK flaeche) throws IOException;
}
