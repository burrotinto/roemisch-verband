package com.ess.entity;

/**
 * Erf�llt eine L�nge oder Fl�che nicht die gew�nschten Abmessungen wie
 * gefordert.
 * 
 * @author Florian Klinger
 *
 */
public class NichtDasRichtigeFormatException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public NichtDasRichtigeFormatException(String nachricht) {
		super(nachricht);
	}
}
