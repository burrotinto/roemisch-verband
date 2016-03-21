package com.ess.heuristiken;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;
import com.ess.heuristiken.multiThreadingStackHeuristiken.IMultiThreadingStackHeuristik;
import com.ess.heuristiken.multiThreadingStackHeuristiken.MultiThreadingStackChange;
import com.ess.heuristiken.multiThreadingStackHeuristiken.MultiThreadingStackFIFO;
import com.ess.heuristiken.multiThreadingStackHeuristiken.MultiThreadingStackLIFO;
import com.ess.heuristiken.multiThreadingStackHeuristiken.MultiThreadingStackMiddle;
import com.ess.heuristiken.multiThreadingStackHeuristiken.MultiThreadingStackNextToFull;
import com.ess.heuristiken.multiThreadingStackHeuristiken.MultiThreadingStackRandom;
import com.ess.heuristiken.nextPosHeuristiken.AuswahlNaechsteFreieStelleAnhandFlaechenabmessung;
import com.ess.heuristiken.nextPosHeuristiken.INaechsteFreieStelleHeuristik;
import com.ess.heuristiken.nextPosHeuristiken.NaechsteFreieStelleIntelligent;
import com.ess.heuristiken.nextPosHeuristiken.NaechsteFreieStelleSpalte;
import com.ess.heuristiken.nextPosHeuristiken.NaechsteFreieStelleZeile;
import com.ess.heuristiken.nextPosHeuristiken.StandardNextPos;
import com.ess.heuristiken.sortierHeuristiken.AutoHeuristik;
import com.ess.heuristiken.sortierHeuristiken.GroessteZuErst;
import com.ess.heuristiken.sortierHeuristiken.ISortierHeuristik;
import com.ess.heuristiken.sortierHeuristiken.KleinsteZuerst;
import com.ess.heuristiken.sortierHeuristiken.RandomHeuristik;
import com.ess.parser.IParserReader;
import com.ess.validatoren.B0NurPassendeBedingung;
import com.ess.validatoren.B1MaximaleFugenlaenge;
import com.ess.validatoren.B2KeineFugenkreuze;
import com.ess.validatoren.B3KeineGleichen;
import com.ess.validatoren.B3KeineGleichenVorrausschauend;
import com.ess.validatoren.B4KeineErsetzung;
import com.ess.validatoren.B4PunktuellOhneVerlegung;
import com.ess.validatoren.IBedingungKompletteFlaeche;
import com.ess.validatoren.IPunktuelleBedingung;
import com.ess.validatorenStreams.B0Stream;
import com.ess.validatorenStreams.B1Stream;
import com.ess.validatorenStreams.B2Stream;
import com.ess.validatorenStreams.B3Stream;
import com.ess.validatorenStreams.B4Stream;
import com.ess.validatorenStreams.IStream;

/**
 * Die Heuristiker Klasse liest die "config.properties" Datei ein, und erstellt
 * aus ihr die Bedingungen und Heuristiken. Befindet sich keine config im
 * Ausfuehrungspfad wird eine Muster config dort erstellt.
 * 
 * 
 * @author Florian Klinger
 *
 */
public class Heuristiker implements IMultiThreadingHeuristik {
	/**
	 * Standartpfad der config.properties, dieser liegt im Ordner der
	 * auszuführenden Datei
	 */
	public final static String STANDARTPROP = System.getProperty("user.home") + "/9215646_Config.properties";
	private final static String ORIGINALCONFIGPATH = "config.properties.ORGINAL";

	private ArrayList<IBedingungKompletteFlaeche> filter = new ArrayList<IBedingungKompletteFlaeche>();
	private ArrayList<IPunktuelleBedingung> filterPunktuell = new ArrayList<IPunktuelleBedingung>();
	private Boolean[] validatoren = { false, false, false, false };
	private ISortierHeuristik sortierHeuristik = null;
	private IMultiThreadingStackHeuristik multiStackHeuristiken = new MultiThreadingStackRandom();
	private IBedingungKompletteFlaeche[] bedingungen = { null, null, null, null, null };
	private INaechsteFreieStelleHeuristik nextPosHeu = new StandardNextPos();
	private B0NurPassendeBedingung nurPassend;
	private LoeserEnums loeser = LoeserEnums.SINGLETHREADBACKTRACK;
	private boolean pufferMitSpeicher = false;
	private boolean bedingungenAlsStream = false;

	/**
	 * Konstuktor zum istanziieren des Heuristikers, erstellen der config wenn
	 * noetig, auslesen der config, instanziieren der Regelobjekte
	 * 
	 * @param fliesenTypen
	 *            Die in der Probleminstanz vorkommenden FliesenTypen
	 */
	public Heuristiker(List<IFliesenTyp> fliesenTypen) {
		initProperties(fliesenTypen, STANDARTPROP);
	}

	private void initProperties(List<IFliesenTyp> fliesenTypen, String propFile) {
		Properties prop = new Properties();
		InputStream input = null;

		// Bedingung die immer dabei ist
		nurPassend = new B0NurPassendeBedingung();
		bedingungen[FehlerEnums.FLIESE_UNPASSEND.ordinal()] = nurPassend;

		try {
			if (!new File(propFile).exists()) {
				makeNewproperties(propFile);
			}

			input = new FileInputStream(propFile);
			prop.load(input);

			Enumeration<?> e = prop.propertyNames();

			// Alle Argumente werden durchlaufen
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				try {
					// Aber nur die wahren werden betrachtet
					if (Boolean.parseBoolean(value)) {
						switch (key) {

						// Bedingungen
						case "B1MaximaleFugenlaenge":
							B1MaximaleFugenlaenge b1 = new B1MaximaleFugenlaenge();
							bedingungen[FehlerEnums.MAX_FUGENLAENGE.ordinal()] = b1;
							filter.add(b1);
							filterPunktuell.add(b1);
							validatoren[0] = true;
							break;
						case "B2KeineKreuze":
							B2KeineFugenkreuze b2 = new B2KeineFugenkreuze();
							bedingungen[FehlerEnums.FUGENKREUZE.ordinal()] = b2;
							filter.add(b2);
							filterPunktuell.add(b2);
							validatoren[1] = true;
							break;
						case "B3KeineGleichen":
							B3KeineGleichen b3a = new B3KeineGleichenVorrausschauend(fliesenTypen, this);
							bedingungen[FehlerEnums.GLEICHE_FLIESEN.ordinal()] = b3a;
							filter.add(b3a);
							filterPunktuell.add(b3a);
							validatoren[2] = true;
							break;
						case "B4KeineErsetzungen":
							B4KeineErsetzung b4 = new B4PunktuellOhneVerlegung(this);
							b4.setAlleFliesenTypen(fliesenTypen);
							bedingungen[FehlerEnums.FLIESEN_AUSTAUSCHBAR.ordinal()] = b4;
							filter.add(b4);
							filterPunktuell.add(b4);
							validatoren[3] = true;
							break;

						// Sortierende Heuristiken
						case "AutoHeuristik":
							sortierHeuristik = new AutoHeuristik(this);
							break;
						case "GroesseteZuerst":
							sortierHeuristik = new GroessteZuErst();
							break;
						case "KleinsteZuerst":
							sortierHeuristik = new KleinsteZuerst();
							break;
						case "Random":
							sortierHeuristik = new RandomHeuristik();
							break;
						case "StandardRekurisivBacktrack":
							loeser = LoeserEnums.SINGLETHREADBACKTRACK;
							break;
						case "MultiThreading":
							loeser = LoeserEnums.MULTITHREADBACKTRACK;
							break;
						case "AutoLoeserAuswahl":
							loeser = LoeserEnums.AUTO;
							break;
						case "Visual":
							loeser = LoeserEnums.VISUAL;
							break;
						case "MultiThreadingStackFIFO":
							multiStackHeuristiken = new MultiThreadingStackFIFO();
							break;
						case "MultiThreadingStackLIFO":
							multiStackHeuristiken = new MultiThreadingStackLIFO();
							break;
						case "MultiThreadingStackRandom":
							multiStackHeuristiken = new MultiThreadingStackRandom();
							break;
						case "MultiThreadingStackChange":
							multiStackHeuristiken = new MultiThreadingStackChange();
							break;
						case "MultiThreadingStackMiddle":
							multiStackHeuristiken = new MultiThreadingStackMiddle();
							break;
						case "MultiThreadingStackNextToFull":
							multiStackHeuristiken = new MultiThreadingStackNextToFull();
							break;
						// NextPos
						case "StandardNaechsterPunkt":
							nextPosHeu = new StandardNextPos();
							break;
						case "NaechsteFreieStelleSpalte":
							nextPosHeu = new NaechsteFreieStelleSpalte();
							break;
						case "NaechsteFreieStelleZeile":
							nextPosHeu = new NaechsteFreieStelleZeile();
							break;
						case "NaechsteFreieStelleIntelligent":
							nextPosHeu = new NaechsteFreieStelleIntelligent();
							break;
						case "AuswahlNaechsteFreieStelleAnhandFlaechenabmessung":
							nextPosHeu = new AuswahlNaechsteFreieStelleAnhandFlaechenabmessung();
							break;
						case "PufferMitSpeicher":
							pufferMitSpeicher = true;
							break;
						case "BedingungenAlsStream":
							bedingungenAlsStream = true;
							break;
						default:
							break;
						}
					}
				} catch (Exception ec) {

				}
			}

		} catch (IOException ex) {
			// Wenn es Keine prop gibt nimm dies als Standart
			filter.add(new B1MaximaleFugenlaenge());
			filter.add(new B2KeineFugenkreuze());
			filter.add(new B3KeineGleichenVorrausschauend(fliesenTypen, this));
			B4KeineErsetzung b4 = new B4PunktuellOhneVerlegung(this);
			b4.setAlleFliesenTypen(fliesenTypen);
			filter.add(b4);
			filterPunktuell.add(new B1MaximaleFugenlaenge());
			filterPunktuell.add(new B2KeineFugenkreuze());
			filterPunktuell.add(new B3KeineGleichen());
			filterPunktuell.add(b4);
			for (int i = 0; i < validatoren.length; i++) {
				validatoren[i] = true;
			}
			multiStackHeuristiken = new MultiThreadingStackRandom();

		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Collections.sort(filterPunktuell);
	}

	private void makeNewproperties(String string) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(ORIGINALCONFIGPATH)));
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(string)));
			int x = -1;
			while ((x = reader.read()) >= 0) {
				writer.write((char) x);
			}
			reader.close();
			writer.flush();
			writer.close();
		} catch (IOException e) {
		}

	}

	/**
	 * Sortiert die übergebenen Fliesen anhand der Heuristiken
	 * 
	 * @param collection
	 *            Die FliesenTypen die zur auswahl stehen eingefügt zu werden
	 * @param flache
	 *            die Fläche in der sie eingefügt werden sollen
	 * @param einfuegePunkt
	 *            der Punkt an den sie verlegt werden sollen
	 * @param fugenlaenge
	 *            die Maximale Fugenlänge die erlaubt ist
	 * @return eine Liste mit FliesenTypen die die Bedingunen erfüllen
	 */
	public List<IFliesenTyp> sortiereFliesen(List<IFliesenTyp> collection, IFlaecheK flache, Point einfuegePunkt, int fugenlaenge) {
		return sortierHeuristik == null ? collection : sortierHeuristik.sortiereMittelsHeuristik(collection, flache, einfuegePunkt, fugenlaenge);
	}

	/**
	 * Methode zum testen der Bedingungen
	 * 
	 * @param parser
	 *            enthält den Verlegungsplan
	 * @param maxFugenLaenge
	 *            gibt die Maximal Fugenlänge an
	 * @return Eine Listen von Validation ENums mit den Bedingungsverletzungen
	 */
	public List<FehlerEnums> testeAlles(IParserReader parser, int maxFugenLaenge) {
		return testeAlles(parser.getVerlegungsPlan(), parser.getFliesenTypen(), maxFugenLaenge);
	}

	/**
	 * Methode zum testen der Bedingungen
	 * 
	 * @param flaeche
	 *            zu Testende Fläche
	 * @param fliesenTypen
	 *            erlaubte FliesenTypen
	 * @param maxFugenLaenge
	 *            Die maximale Fugenlänge
	 * @return
	 */
	public List<FehlerEnums> testeAlles(IFlaecheK flaeche, Map<String, IFliesenTyp> fliesenTypen, int maxFugenLaenge) {
		LinkedList<FehlerEnums> liste = new LinkedList<>();
		for (int i = 0; i < bedingungen.length; i++) {
			if (bedingungen[i] != null && !bedingungen[i].pruefe(flaeche, fliesenTypen, maxFugenLaenge)) {
				liste.add(FehlerEnums.values()[i]);
			}
		}
		return liste;
	}

	/**
	 * Schneller Test ob die flaeche die Bedingungen erfüllt werden, stoppt
	 * sobald die erste Bedingung verletzt wird
	 * 
	 * @param flaeche
	 *            die zu testende Flaeche
	 * @param fliesenTypen
	 *            die FliesenTypen die verendet werden dürfen
	 * @param maxFugenLaenge
	 *            Die maximale Fugenlänge
	 * @return true alle Bedingungen werden erfüllt, false mindestens eine wird
	 *         verletzt
	 */
	public boolean schnelltest(IFlaecheK flaeche, Map<String, IFliesenTyp> fliesenTypen, int maxFugenLaenge) {
		for (int i = 0; i < bedingungen.length; i++) {
			if (bedingungen[i] != null && !bedingungen[i].pruefe(flaeche, fliesenTypen, maxFugenLaenge)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Rueckgabe der naechsten Position
	 * 
	 * @param flaeche
	 *            Flaeche aus der die Stelle gefunden werden soll
	 * @return Punkt der frei ist oder null
	 */
	public Point getNaechsteFreieStelle(IFlaecheK flaeche) {
		return nextPosHeu.getNaechsteFreieStelle(flaeche);
	}

	/**
	 * Hier kann erfragt werden welches loesungsverfahren benutzt wird
	 * 
	 * @return Enum des verfahrens
	 */
	public LoeserEnums getGewuenschtesLoesungsVerfahren() {

		return loeser;
	}

	/**
	 * Getter der {@link INaechsteFreieStelleHeuristik}
	 * 
	 * @return benutzte {@link INaechsteFreieStelleHeuristik}
	 */
	public INaechsteFreieStelleHeuristik getNextPosHeu() {
		return nextPosHeu;

	}

	@Override
	public LinkedList<IFlaecheK> ermittleMittelsHeuristik(LinkedList<LinkedList<IFlaecheK>> stackListe) {
		return multiStackHeuristiken.gibDeineHeuristischErmittelteListe(stackListe);
	}

	/**
	 * Gibt zurueck ob die nutzung des Puffers der die zwischenergebnise auf die
	 * Festplatte schreibt erwuenscht ist
	 * 
	 * @return wahr wenn genuenscht, false sonst
	 */
	public boolean isPufferMitSpeicher() {
		return pufferMitSpeicher;
	}

	/**
	 * Wählt die Fliesen aus die an der Angegebenen Stelle verlegt werden dürfen
	 * ohne eine der angegebenen Bedingungen zu verletzen
	 * 
	 * @param fliesenTypen
	 *            Die FliesenTypen die zur auswahl stehen eingefügt zu werden
	 * @param flache
	 *            die Fläche in der sie eingefügt werden sollen
	 * @param einfuegePunkt
	 *            der Punkt an den sie verlegt werden sollen
	 * @param fugenlaenge
	 *            die Maximale Fugenlänge die erlaubt ist
	 * @return eine Liste mit FliesenTypen die die Bedingunen erfüllen
	 */
	private List<IFliesenTyp> waehleFliesen(List<IFliesenTyp> fliesenTypen, IFlaecheK flache, Point einfuegePunkt, int fugenlaenge) {
		// Wichtigste kommt immer am Anfang
		List<IFliesenTyp> list = nurPassend.filtereUnmoeglicheTypen(fliesenTypen, flache, einfuegePunkt, fugenlaenge);

		for (IPunktuelleBedingung f : filterPunktuell) {
			list = f.filtereUnmoeglicheTypen(list, flache, einfuegePunkt, fugenlaenge);
		}
		return list;
	}

	/**
	 * Erzeugt einen Stream aus FliesenTypen die an der gewünschten Stelle zu
	 * keinem Regelbruch fuehren, teilweise auch zu einem spaeteren Zeitpunkt.
	 * Veraendert die Reihenfolge der Fliesen nicht.
	 * 
	 * @param listeAllerFliesen
	 *            Alle FliesenTypen die an der Stelle geprueft werden sollen
	 * @param flaeche
	 *            Die Flaeche auf der die Pruefung erfolgen soll
	 * @param einfuegepunkt
	 *            Die Position der einzufuegenden Fliesen
	 * @param fugenLaenge
	 *            Die maximal erlaubte Fugenlaenge
	 * @return Einen Stream aus Fliesen
	 */
	private IStream<IFliesenTyp> getFliesenStream(List<IFliesenTyp> listeAllerFliesen, IFlaecheK flaeche, Point einfuegepunkt, int fugenLaenge) {
		IStream<IFliesenTyp> stream = new B0Stream(listeAllerFliesen, flaeche, einfuegepunkt);
		if (validatoren[1]) {
			// Fugenkreuze
			stream = new B2Stream(stream, listeAllerFliesen, flaeche, einfuegepunkt, fugenLaenge);
		}
		if (validatoren[0]) {
			// Fugenlaenge
			stream = new B1Stream(stream, listeAllerFliesen, flaeche, einfuegepunkt, fugenLaenge);
		}
		if (validatoren[3]) {
			// Ersetzung
			stream = new B4Stream(this, stream, listeAllerFliesen, flaeche, einfuegepunkt, fugenLaenge);
		}
		if (validatoren[2]) {
			// Gleiche
			stream = new B3Stream(this, stream, listeAllerFliesen, flaeche, einfuegepunkt, fugenLaenge);
		}
		return stream;
	}

	/**
	 * Erzeugt einen Iterator aus {@link IFliesenTyp} die an der gewuenschten
	 * Stelle ohne Regelbruch verlegt werden koennen, in der gewuenschten
	 * sortierten Reihenfolge
	 * 
	 * @param listeAllerFliesen
	 *            alle Fliesen die an dieser Stelle getestet werden sollen
	 * @param flaeche
	 *            die Flaeche die getestet werden soll
	 * @param p
	 *            der zu testende Einfuegepunkt
	 * @param fugenLaenge
	 *            die maximal zulaessige Fugenlaenge
	 * @return ein sortierter der ueber die erlaubten und sortierten
	 *         {@link IFliesenTyp} iteriert
	 */
	public Iterator<IFliesenTyp> getValidFliesenIterator(List<IFliesenTyp> listeAllerFliesen, IFlaecheK flaeche, Point p, int fugenLaenge) {
		if (!bedingungenAlsStream) {
			return sortiereFliesen(waehleFliesen(listeAllerFliesen, flaeche, p, fugenLaenge), flaeche, p, fugenLaenge).iterator();
		} else {
			final IStream<IFliesenTyp> stream = getFliesenStream(sortiereFliesen(listeAllerFliesen, flaeche, p, fugenLaenge), flaeche, p, fugenLaenge);

			return new Iterator<IFliesenTyp>() {

				IFliesenTyp next = stream.read();

				@Override
				public boolean hasNext() {
					return next != null;
				}

				@Override
				public IFliesenTyp next() {
					IFliesenTyp tmp = next;
					next = stream.read();
					return tmp;
				}
			};
		}
	}
}
