package com.ess.solveAS;

import java.awt.Point;
import java.util.Iterator;
import java.util.List;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;
import com.ess.heuristiken.Heuristiker;
import com.ess.validatoren.WrapperFlaechenKCopyMitVerlegtemFliesenTyp;

/**
 * Abstrakte Klasse zur L�sung des Roemischen Verbund Problems. Diese stellt das
 * Grundgeruest des Algorithmus bereit. Mittels offener Rekursion k�nnen
 * Spezialisationen dieser Klasse das Verhalten explizit steuern.
 * 
 * @author Florian Klinger
 *
 */
abstract class AbstraktLoeser {
	private final Heuristiker heuristiken;
	private final int fugenLaenge;
	private final List<IFliesenTyp> listeAllerFliesen;

	/**
	 * Damit alles funktioniert braucht der AbstraktLoeser folgende
	 * Informationen.
	 * 
	 * @param heuristiken
	 *            Die zu benutzenden Heuristiken
	 * @param fugenLaenge
	 *            Die maximale Fugenl�nge
	 * @param fliesenCollection
	 *            Die daf�r zu verwendenden Fliesentypen
	 */
	public AbstraktLoeser(Heuristiker heuristiken, int fugenLaenge, List<IFliesenTyp> listeAllerFliesen) {
		super();
		this.heuristiken = heuristiken;
		this.fugenLaenge = fugenLaenge;
		this.listeAllerFliesen = listeAllerFliesen;
	}

	/**
	 * Implementierung des Loesungsalgorithmus
	 * 
	 * @param flaeche
	 *            die zu l�sende Flaeche
	 */
	protected synchronized void legeVerbund(IFlaecheK flaeche) {
		if (!isInterrupted()) {
			Point p = heuristiken.getNaechsteFreieStelle(flaeche);
			if (p == null) {
				loesungGefunden(flaeche);
			} else {
				// Die m�glichen Fliesentypen werden anhand der Heuristiken und
				// Bedingungen ausgew�hlt.
				Iterator<IFliesenTyp> it = heuristiken.getValidFliesenIterator(listeAllerFliesen, flaeche, p, fugenLaenge);

				while (it.hasNext() && !isInterrupted()) {
					verlege(flaeche, it.next(), p);
				}
			}
		}
	}

	protected void verlege(IFlaecheK flaeche, IFliesenTyp fliesenTyp, Point p) {
		// Da die eingef�gten FliesenTypen die Bedingungen
		// erf�llen,
		// kann auf eine erneute Pr�fung verzichtet werden.

		// Test, ob eine Wrapperklasse vorliegt.
		if (fliesenTyp instanceof WrapperFlaechenKCopyMitVerlegtemFliesenTyp) {
			teilFlaecheGefunden((WrapperFlaechenKCopyMitVerlegtemFliesenTyp) fliesenTyp);
		} else {
			IFlaecheK copy = flaeche.getCopy();
			copy.verlegeFliesenTypUngeprueft(p, fliesenTyp);
			teilFlaecheGefunden(copy);
		}
	}

	/**
	 * Gibt an, ob die L�sungsfindung gestoppt werden soll.
	 * 
	 * @return wenn wahr dann Abbruch
	 */
	protected abstract boolean isInterrupted();

	/**
	 * Methode wird aufgerufen, wenn ein g�ltiger, vollst�ndig verlegter Verbund
	 * gefunden wurde.
	 * 
	 * @param flaeche
	 *            Vollverlegte Fl�che die alle Bedingungen erf�llt
	 */
	protected abstract void loesungGefunden(IFlaecheK flaeche);

	/**
	 * Wird eine teilverlegte Fl�che, die die Bedingungen erf�llt, gefunden wird
	 * diese Methode aufgerufen.
	 * 
	 * @param flaeche
	 *            Teilverlegte Fl�che die alle bedingungen erf�llt
	 */
	protected abstract void teilFlaecheGefunden(IFlaecheK flaeche);

	/**
	 * Heuristik Getter
	 * 
	 * @return verwendete Heuristiken
	 */
	public Heuristiker getHeuristiken() {
		return heuristiken;
	}

	/**
	 * Fliesentypen Listen Getter
	 * 
	 * @return LIste aller zu benutzenden Fliesentypen
	 */
	public List<IFliesenTyp> getListeAllerFliesen() {
		return listeAllerFliesen;
	}

	/**
	 * Maximal erlaubte Fugenlaenge Getter
	 * 
	 * @return maximal erlaute Fugenlaenge in cm
	 */
	public int getFugenLaenge() {
		return fugenLaenge;
	}
}
