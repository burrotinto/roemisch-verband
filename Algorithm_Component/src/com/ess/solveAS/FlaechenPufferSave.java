package com.ess.solveAS;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;
import com.ess.parser.SerializiseParser;

/**
 * Dieser Puffer lagert die gefundenen Teilflächen auf die Festplatte aus.
 * 
 * 
 * @author Florian Klinger
 *
 */
class FlaechenPufferSave implements IPuffer {
	private final int HAUPTSPEICHERGROESSE = 100;

	private volatile LinkedList<IFlaecheK> speicher = new LinkedList<>();
	private volatile LinkedList<File> unwahrscheinlich = new LinkedList<>();
	private int runningThreads, wartendeThreads;
	private File path;
	private Collection<IFliesenTyp> fliesentypen;
	private Random random = new Random();

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
	FlaechenPufferSave(int threadAnzahl, IFlaecheK startEintrag, Collection<IFliesenTyp> fliesentypen) {
		this.runningThreads = threadAnzahl;
		this.fliesentypen = fliesentypen;
		speicher.add(startEintrag);
		wartendeThreads = 0;
		path = new File("./tmp");
		path.mkdir();
		path.deleteOnExit();
	}

	@Override
	public synchronized boolean ablegen(IFlaecheK flaeche) {

		speicher.addLast(flaeche);
		if (speicher.size() > HAUPTSPEICHERGROESSE) {
			Collections.sort(speicher);
			// 1/5 der Eintäge, die am Weitesten von der Lösung entfernt sind,
			// werden exportiert. Mit dem Rest wird weitergearbeitet
			for (IFlaecheK fl : speicher.subList(0, speicher.size() / 5)) {
				try {
					File f = File.createTempFile((fl.getFuellstand() < 10 ? "0" : "") + fl.getFuellstand() + "-" + fl.hashCode(), ".ser", path);
					f.deleteOnExit();
					new SerializiseParser(f, fliesentypen).write(fl);
					unwahrscheinlich.add(f);
				} catch (Exception e) {

				}
			}
			speicher = new LinkedList<>(speicher.subList(speicher.size() / 5, speicher.size() - 1));
		}

		if (wartendeThreads > 0)
			notifyAll();

		return true;
	}

	@Override
	public synchronized IFlaecheK entnehmen() {
		wartendeThreads++;
		while (speicher.isEmpty()) {
			// Ist der Puffer leergelaufen, wird er durch einen anderen ersetzt.
			if (unwahrscheinlich.size() != 0) {

				for (int i = 0; i < HAUPTSPEICHERGROESSE / 10 && unwahrscheinlich.size() > 0; i++) {
					File f = unwahrscheinlich.remove(random.nextInt(unwahrscheinlich.size()));
					speicher.add(new SerializiseParser(f, fliesentypen).getVerlegungsPlan());
					f.delete();
				}
				notifyAll();
			} else {
				// Wenn alle Threads warten, bedeutet dies, dass es keine
				// Flaeche
				// mehr
				// gibt, welche die Bedingungen erfüllt, somit ist die
				// ursprüngliche
				// Fläche nicht lösbar und es wird NULL zurückgegeben.
				runningThreads--;

				// Wenn der Puffer jetzt wirklich leer ist, wird gewartet, ob
				// ein
				// Thread noch ein neue Flaeche bringt, sind alle Threads hier
				// wartend, gibt es keine Lösung.
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
		return speicher.remove(random.nextInt(speicher.size()));
	}
}