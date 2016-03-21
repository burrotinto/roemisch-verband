package com.ess.validatoren;

import java.awt.Point;
import java.util.Collection;
import java.util.List;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;

/**
 * Davon abgeleitete klassen k�nnen dar�ber Aussagen treffen ob das einf�gen an
 * einem Bestimmten Punkt zu einer Regelverletzung kommt der nicht. Auch sind
 * sie vergleichbar um sie in effizienter Reihenfolge ausf�hren zu k�nnen. So
 * ist die Bedingngung das keine Fliesen �ber den Rand liegen und nicht
 * �berlappend sein d�rfen immer an erster Stelle
 * 
 * @author Florian Klinger
 *
 */
public interface IPunktuelleBedingung extends Comparable<IPunktuelleBedingung> {
	/**
	 * Der ganze L�sungs Algorithmus ist darauf aufgebaut das nur FliesenTypen
	 * verlegt werden die keine Bedingung verletzen. Dies wird in dieser Methode
	 * getan
	 * 
	 * @param fliesenTypen
	 *            {@link Collection} der "noch" m�glichen {@link IFliesenTyp}'en
	 * @param flache
	 *            die Flache auf der die {@link IFliesenTyp}'en verlegt werden
	 *            soll
	 * @param einfuegePunkt
	 *            einf�gepunkt der {@link IFliesenTyp}'en
	 * @param fugenlaenge
	 *            die Maximale Fugenl�nge
	 * @return Eine Liste mit {@link IFliesenTyp}'en. Zu beachten ist dass es
	 *         sich in den Eintr�gen auch um
	 *         {@link WrapperFlaechenKCopyMitVerlegtemFliesenTyp} Instanzen
	 *         handeln kann. Dies sind Wrapper an der der {@link IFliesenTyp}
	 *         auf der {@link IFlaecheK} an der Einf�geposition schon verlegt
	 *         wurde. Dieses Wissen kann benutzt werden um ein erneutes Kopieren
	 *         der Fl�che mit anschlie�enden einf�gen einer {@link IFliesenTyp}
	 *         zu umgehen.
	 * 
	 *         Ein Beispiel aus {@link B4KeineErsetzung}:
	 * 
	 *         FlaechenKCopyMitVerlegtemFliesenTyp copy;
	 *
	 *
	 *         if (fliesentyp instanceof FlaechenKCopyMitVerlegtemFliesenTyp) {
	 * 
	 *         copy = (FlaechenKCopyMitVerlegtemFliesenTyp) fliesentyp;
	 * 
	 *         } else {
	 * 
	 *         copy = new
	 *         FlaechenKCopyMitVerlegtemFliesenTyp(flache.getCopy(),fliesentyp);
	 *         copy.verlegeFliesenTypUngeprueft(einfuegePunkt, fliesentyp);
	 * 
	 *         }
	 */
	List<IFliesenTyp> filtereUnmoeglicheTypen(List<IFliesenTyp> fliesenTypen, IFlaecheK flache, Point einfuegePunkt, int fugenlaenge);

	/**
	 * Gibt an an welcher stelle die Bedingung aufgerufen werden soll, je
	 * kleiner umso fr�her
	 * 
	 * Da z.B. die Bedingung {@link B0NurPassendeBedingung} die wichtigste ist
	 * hat diese den Wert @Integer.MIN_VALUE
	 * 
	 * @return ein Integer der angibt wann die Bedingung zu pr�fen ist
	 */
	int aufrufReihenfolge();
}
