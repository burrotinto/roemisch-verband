package com.ess.solveAS;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;
import com.ess.heuristiken.Heuristiker;

/**
 * Damit mehrere Threads zusammen an der Loesung arbeiten koennen, ohne dass
 * eine schier endlose Anzahl an Threads angestossen wird, gibt es diesen
 * Puffer. Dadurch ist es moeglich mit einer definierten Anzahl an Threads zu
 * arbeiten.
 * 
 * Durch die Benutzung von Heuristiken zur Auswahl der naechsten Teilliste
 * konnte die Geschwindigkeit gesteigert werden. Mit der Implementierung eines
 * Ueberlaufs wird versucht eine {@link OutOfMemoryError} Exception zu
 * verhindern.
 * 
 * @author Florian Klinger
 *
 */
class FlaechenPuffer implements IPuffer {
	private final int hauptspeichergroesse;

	private int fenstergroesse;
	private volatile LinkedList<IFlaecheK> speicher = new LinkedList<>();
	private volatile LinkedList<LinkedList<IFlaecheK>> unwahrscheinlich = new LinkedList<>();
	private Heuristiker heuristiken;
	private int runningThreads, wartendeThreads;
	private Random random = new Random();
	private int abgelehnteFlaechen = 0;

	/**
	 * Zur Initialisierung des Puffers muss angegeben werden, wie viele Threads
	 * mit ihm arbeiten, sowie der erste Eintrag des Puffers und die zu
	 * benutzenden Heuristiken.
	 * 
	 * @param threadAnzahl
	 *            die genaue Anzahl der Threads, die diesen Puffer benutzen
	 * @param startEintrag
	 *            eine gueltige Flaeche, die der erste Dienstbenutzer bekommt
	 * @param heuristiken
	 *            zu benutzende Heuristiken
	 */
	FlaechenPuffer(int threadAnzahl, IFlaecheK startEintrag, Heuristiker heuristiken, int maxFugenLaenge, Collection<IFliesenTyp> fliesen) {
		this.runningThreads = threadAnzahl;
		this.heuristiken = heuristiken;
		this.hauptspeichergroesse = 75;
		fenstergroesse = startEintrag.getAnzahlQuadrateX() * startEintrag.getAnzahlQuadrateY();
		speicher.add(startEintrag);
		// anfangs wartet keiner...
		wartendeThreads = 0;
	}

	/**
	 * Die MultiThreads legen hier ihre teilverlegten Flaechen ab. Nach einer
	 * fest definierten Groesse des Stapels wird eine feste Anzahl in eine extra
	 * Liste geschoben, der Rest wird wieder hergenommen um weiterzumachen.
	 * 
	 * @param flaeche
	 *            Eine Flaeche die die Bedingungen erfuellt
	 * @return wurde die {@link IFlaecheK} erfolgreich in den Speicher
	 *         geschrieben wird <code>true</code> zurueckgegeben
	 */
	public synchronized boolean ablegen(IFlaecheK flaeche) {

		if (speicher.size() >= hauptspeichergroesse) {
			if (unwahrscheinlich.size() >= fenstergroesse) {
				abgelehnteFlaechen++;
				// Wurden mehr Flaechen abgelehnt als es Sublisten gibt wird
				// eine Ueberlaufsbehandlung angestossen
				if (abgelehnteFlaechen > fenstergroesse) {
					unwahrscheinlich.remove(random.nextInt(unwahrscheinlich.size()));
					unwahrscheinlich.remove(random.nextInt(unwahrscheinlich.size()));
					unwahrscheinlich.add(speicher);
					speicher = new LinkedList<>();
					speicher.add(flaeche);
					return true;
				}
				return false;

			} else {
				// Ein Teil wird in eine 2. Liste ausgelagert, mit dem Rest
				// weitergearbeitet
				speicher.add(flaeche);
				unwahrscheinlich.add(new LinkedList<>(speicher.subList(0, speicher.size() / 5)));
				speicher = new LinkedList<>(speicher.subList(speicher.size() / 5, speicher.size()));

			}

		} else {
			speicher.add(flaeche);
		}

		if (wartendeThreads > 0)
			notifyAll();

		return true;
	}

	/**
	 * Hier koennen sich die MultiThreadLoeser ihre zu loesende Flaeche holen.
	 * Wenn die Hauptliste abgearbeitet worden ist wird, wenn vorhanden, eine
	 * der abgelegten Nebenlisten mittels Heuristik ausgewaehlt und als
	 * Hauptliste gesetzt. Ist dies nicht moeglich, da keine der Listen noch
	 * Eintraege hat, wartet der Thread bis entweder wieder Eintraege in der
	 * Liste sind oder alle Threads warten, dann bekommen alle NULL.
	 * 
	 * 
	 * @return Eine Teilgeloeste Flaeche, die alle Bedingungen erfuellt, oder
	 *         NUll wenn es keine mehr gibt.
	 */
	public synchronized IFlaecheK entnehmen() {
		wartendeThreads++;

		while (speicher.isEmpty()) {

			// Ist der Puffer leergelaufen, wird er durch einen anderen ersetzt.
			// Dazu werden Heuristiken herangezogen.
			if (unwahrscheinlich.size() != 0) {
				speicher = heuristiken.ermittleMittelsHeuristik(unwahrscheinlich);
				notifyAll();
			} else {
				// Wenn alle Threads warten, bedeutet dies, dass es keine
				// Flaeche
				// mehr
				// gibt, welche die Bedingungen erfuellt, somit ist die
				// urspruengliche
				// Flaeche nicht loesbar und es wird NULL zurueckgegeben.
				runningThreads--;

				// Wenn der Puffer jetzt wirklich leer ist wird gewartet ob ein
				// Thread noch ein neue Flaeche bringt, sind alle Threads hier
				// wartend, gibt es keine Loesung.
				if (runningThreads < 1) {
					notifyAll();
					return null;
				}

				try {
					wait();
					runningThreads++;
				} catch (InterruptedException e) {
					notifyAll();
					return null;
				}
			}
		}

		wartendeThreads--;
		abgelehnteFlaechen = 0;
		return speicher.remove(random.nextInt(speicher.size()));
	}
}
