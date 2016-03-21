package com.ess.entity;

/**
 * Erfüllt eine Länge oder Fläche nicht die gewünschten Abmessungen wie
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
