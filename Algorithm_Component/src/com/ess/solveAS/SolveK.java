package com.ess.solveAS;

import java.util.LinkedList;
import java.util.Map;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;
import com.ess.heuristiken.Heuristiker;
import com.ess.parser.IParserReader;

/**
 * KontrollKlasse für das Akteurs Anwendungsfall Interface "Solve". Kümmert sich
 * um die Findung einer Lösung. Darunter fällt auch die Auswahl, ob im Single
 * oder MultiThread Modus gelöst werden soll.
 * 
 * @author Florian Klinger
 *
 */
class SolveK implements ILoesungsEmpfaenger {

	private int fugenLaenge;
	private IFlaecheK loesung = null;
	private Map<String, IFliesenTyp> fliesenTypen;
	private Heuristiker heuristiken;
	private LinkedList<ILoeser> threads = new LinkedList<>();
	private boolean loesungGefunden = false;

	SolveK(int fugenLaenge) {
		this.fugenLaenge = fugenLaenge;
	}

	/**
	 * Liest die Probleminstanz aus einem Parser und gibt die erste Lösung
	 * zurück, die gefunden wird.
	 * 
	 * @param parser
	 *            Enthält die Probleminstanz
	 * @return Eine Fläche, welche die Bedingungen erfüllt, oder Null wenn es keine
	 *         solche gibt
	 */
	synchronized IFlaecheK findeLoesung(IParserReader parser) {
		try {
			// Flaeche holen
			IFlaecheK flaeche = parser.getLeereFlaeche();

			// FliesenTypen lesen
			fliesenTypen = parser.getFliesenTypen();

			// Heuristiker instanzieren
			heuristiken = new Heuristiker(new LinkedList<IFliesenTyp>(fliesenTypen.values()));

			// Schnelltest, ob die Flaeche lueckenlos befuellt werden kann
			IFliesenTyp[] typArray = new IFliesenTyp[fliesenTypen.size()];
			if (!lueckenlosBefuellbar(fliesenTypen.values().toArray(typArray), flaeche)) {
				return null;
			}

			// Auswahl der Löser
			switch (heuristiken.getGewuenschtesLoesungsVerfahren()) {
			case AUTO:
				if (Runtime.getRuntime().availableProcessors() > 1
						&& (flaeche.getAnzahlQuadrateX() * flaeche.getAnzahlQuadrateY()) / (flaeche.getQuadrantenLaenge() / flaeche.getQuadrantenLaenge()) >= 600) {
					initThreads(flaeche);
				} else {
					ILoeser l = new Loeser(flaeche, heuristiken, this, fliesenTypen, fugenLaenge);
					threads.add(l);
					new Thread(l).start();
				}
				break;
			case SINGLETHREADBACKTRACK:
				ILoeser l = new Loeser(flaeche, heuristiken, this, fliesenTypen, fugenLaenge);
				threads.add(l);
				new Thread(l).start();
				break;

			case MULTITHREADBACKTRACK:
				initThreads(flaeche);
				break;
			case VISUAL:
				ILoeser v = new VisualLoeser(flaeche, heuristiken, this, fliesenTypen, fugenLaenge);
				threads.add(v);
				new Thread(v).start();
				break;
			default:
				ILoeser ll = new Loeser(flaeche, heuristiken, this, fliesenTypen, fugenLaenge);
				threads.add(ll);
				new Thread(ll).start();
				break;
			}

			// Auf Lösung warten
			while (!loesungGefunden) {
				wait();
			}
			// fertig
			return loesung;
		} catch (InterruptedException e) {
			return null;
		}
	}

	/**
	 * Startet mehrere Threads zum Effizienten Lösen des Problems
	 * 
	 * @param flaeche
	 *            die zu lösende Fläche
	 */
	private synchronized void initThreads(IFlaecheK flaeche) {
		int anzahlThreads = Runtime.getRuntime().availableProcessors();
		IPuffer puffer;
		if (heuristiken.isPufferMitSpeicher())
			puffer = new FlaechenPufferSave(anzahlThreads, flaeche, fliesenTypen.values());
		else
			puffer = new FlaechenPuffer(anzahlThreads, flaeche, heuristiken, fugenLaenge, fliesenTypen.values());
		for (int i = 1; i <= anzahlThreads; i++) {
			threads.add(new MultiThreadLoeser(puffer, heuristiken, this, fliesenTypen, fugenLaenge));
			Thread t = new Thread(threads.getLast());
			t.setName("LoesungsThread Nr " + i + "/" + anzahlThreads);
			t.start();
		}
	}

	@Override
	public synchronized void gebeLoesung(IFlaecheK loesung, ILoeser loeser) {
		// Der abgebende Thread wird unterbrochen und aus der Liste gelöscht
		loeser.interrupt();
		threads.remove(loeser);
		// Wenn eine Lösung gefunden wurde, müssen alle Threads gestoppt werden,
		// die Lösung gespeichert und die Lösungsfindung abgebrochen werden.
		if (loesung != null && this.loesung == null) {
			loesungGefunden = true;
			this.loesung = loesung;
			notifyAll();
			for (ILoeser mT : threads) {
				mT.interrupt();
			}
		}
		// Läuft kein Thread mehr, muss dies mitgeteilt werden um weitere
		// Schritte zu veranlassen.
		if (threads.isEmpty()) {
			loesungGefunden = true;
		}
		notifyAll();
	}

	private boolean lueckenlosBefuellbar(IFliesenTyp[] fliesenTypen, IFlaecheK flaeche) {
		if (fliesenTypen != null && fliesenTypen.length > 0) {
			int minAnzahlQuadrate = (fliesenTypen[0].getX() * fliesenTypen[0].getY()) / (flaeche.getQuadrantenLaenge() * flaeche.getQuadrantenLaenge());
			for (int i = 1; i < fliesenTypen.length; i++) {
				int quadrate = (fliesenTypen[i].getX() * fliesenTypen[i].getY()) / (flaeche.getQuadrantenLaenge() * flaeche.getQuadrantenLaenge());
				minAnzahlQuadrate = Math.min(minAnzahlQuadrate, quadrate);
				if ((flaeche.getAnzahlQuadrateX() * flaeche.getAnzahlQuadrateY()) % minAnzahlQuadrate == 0) {
					return true;
				}
			}
			return (flaeche.getAnzahlQuadrateX() * flaeche.getAnzahlQuadrateY()) % minAnzahlQuadrate == 0;
		} else {
			return false;
		}
	}
}
