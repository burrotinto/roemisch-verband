package com.ess.main;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.ess.displayAS.DisplayAAS;
import com.ess.entity.IFliesenTyp;
import com.ess.entity.NichtDasRichtigeFormatException;
import com.ess.heuristiken.Heuristiker;
import com.ess.parser.IParserReader;
import com.ess.parser.XMLParser;

import ess.algorithm.RoemischerVerbund;
import ess.algorithm.RoemischerVerbund.Validation;

/*
 * Haupteinstiegspunkt der Anwendung. Dies ist ein Singelton der mit der Klassenmethode getMainInstance() bekommen werden kann.
 * 
 */
public class Main {
	private static Main instance = new Main();

	/**
	 * Factory Methode die es ermöglicht ein Singelton der {@link Main} Instanz
	 * zu bekommen.
	 * 
	 * @return die einzig wahre {@link Main}
	 */
	public static Main getMainInstance() {
		return instance;
	}

	/*
	 * Die Main...
	 */
	public static void main(String[] args) {
		Main haupt = Main.instance;

		// Eingaben einlesen
		for (String string : args) {
			try {
				haupt.addParameter(string.split("=")[0], string.split("=")[1]);
			} catch (ArrayIndexOutOfBoundsException e) {
				haupt.addFehlerText("\"" + string + "\" - Kann damit nichts anfangen");
			}
		}

		System.out.println(haupt.begruessungsText());
		if (!haupt.checkParameter()) {
			System.out.println("------------------------------\n");
			System.out.println(haupt.getFehlerText());
			System.out.println("------------------------------\n");
			System.out.println(haupt.getAnleitung());
		} else {
			try {
				haupt.erledigeAnwendungsfall();
			} catch (NichtDasRichtigeFormatException e) {
				System.out
						.println("Eine gültige Römische Verbundverlegung ist nicht möglich,\nda die angegebene Datei fehlerhaft ist oder mit den angegebenen Daten kein gueltiger Roemischer Verbund gelegt werden kann.");
			} catch (IOException e) {
				System.out.println("Beim Einlesen der Datei kam es zu einen Abbruch, bitte versuchen sie es erneut");
				System.exit(42);
			} catch (OutOfMemoryError e) {
				System.out.println("Leider ist die Probleminstanz zu gross. Eventuell mit der Einstellung \"MultiThreading=false\" nochmals versuchen.");
				System.exit(42);
			}
		}
	}

	private HashMap<String, String> eingaben;

	private LinkedList<String> fehler = new LinkedList<>();
	private List<Validation> errors;
	private Long time = System.currentTimeMillis();

	private Main() {
		initEingabenMap();
	}

	/**
	 * Es wird eine Fehlermeldung zu der Fehlerliste hinzugefuegt
	 * 
	 * @param s
	 *            die anzuzeigende Fehlermeldung
	 */
	public void addFehlerText(String s) {
		fehler.add(s);
	}

	/**
	 * Hier werden die Parameter ausgewertet und der Fehlertext erstellt
	 */
	private boolean addParameter(String parameter, String wert) {
		if (eingaben.get(parameter) != null) {
			addFehlerText("\"" + parameter + "\" - Parameter wurde doppelt angegeben");
			return false;
		}
		if (parameter.equals("r") && !(wert.equals("s") || wert.equals("sd") || wert.equals("v") || wert.equals("vd") || wert.equals("d"))) {
			fehler.add("Fuer den Parameter \"r\" ist der Wert " + wert + " nicht definiert.");
			return false;
		} else {
			if (eingaben.containsKey(parameter)) {
				eingaben.put(parameter, wert);
				return true;
			} else {
				addFehlerText("\"" + parameter + "\" - Parameter ist unbekannt");
				return false;
			}
		}
	}

	private String begruessungsText() {
		return "Roemischer Verbund von Florian Klinger     MatNr.:9215646\nFernuni in Hagen, ProPra WS 15/16\n";
	}

	private boolean checkParameter() {
		if (!containsParameter("r")) {
			addFehlerText("\"r\" - Ablaufparameter fehlt");
		}
		if (!containsParameter("if")) {
			addFehlerText("\"if\" - InputFile Parameter fehlt");
		} else {
			if (!Paths.get(eingaben.get("if")).toFile().exists()) {
				addFehlerText("\"if\" - Die Datei " + eingaben.get("if") + " existiert nicht.");

			} else {
				if (Paths.get(eingaben.get("if")).toFile().isDirectory()) {
					addFehlerText("\"if\" - " + eingaben.get("if") + " beizechnet einen Pfad.");
				} else {
					if (!(Paths.get(eingaben.get("if")).toFile().canRead() && Paths.get(eingaben.get("if")).toFile().canWrite())) {
						addFehlerText("\"if\" - " + eingaben.get("if") + " Lese- UND Schreibrechte sind erforderlich.");
					} else {
						try {
							XMLParser.getInstance(eingaben.get("if"));
						} catch (NichtDasRichtigeFormatException e) {
							addFehlerText("\"if\" - " + eingaben.get("if") + " Datei ist fehlerhaft. Grund: " + e.getMessage());
						}
					}
				}
			}
		}
		if (!containsParameter("l")) {
			addFehlerText("\"l\" - Maximale Fugenlaengen Parameter fehlt");
		} else {
			try {
				int wert = Integer.parseInt(eingaben.get("l"));
				if (wert <= 0) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				addFehlerText("\"l\" - Ist ungueltig, da " + eingaben.get("l") + " keine natuerlich Zahl ( > 0 ) darstellt");
				return false;
			}
		}

		return fehler.size() == 0;
	}

	private boolean containsParameter(String parameter) {
		return eingaben.get(parameter) != null;
	}

	/**
	 * Auswahl des AkteursAnwendungsfalls
	 * 
	 * @throws NichtDasRichtigeFormatException
	 *             Parser enthält unzulässige Größen
	 * @throws IOException
	 *             Sonstige IOExceptions
	 */
	private void erledigeAnwendungsfall() throws NichtDasRichtigeFormatException, IOException {
		String pfad = eingaben.get("if");
		XMLParser parser = XMLParser.getInstance(pfad);
		int maxFugenLange = Integer.parseInt(eingaben.get("l"));

		switch (eingaben.get("r")) {
		case "s":
			// solve
			initSolveAAS(pfad, maxFugenLange);
			break;
		case "sd":
			// solve & display
			initSolveAAS(pfad, maxFugenLange);
			initDisplayAAS(parser);
			break;
		case "v":
			// validate
			initValidateAAS(pfad, maxFugenLange);
			break;
		case "vd":
			// validate & display
			initValidateAAS(pfad, maxFugenLange);
			initDisplayAAS(parser, maxFugenLange);
			break;
		case "d":
			// display
			initDisplayAAS(parser);
			break;
		default:
			break;
		}
		System.out.println("\nHave a nice day");

	}

	/**
	 * Erzeugt einen String der die Anleitung enthaelt
	 * 
	 * @return die Anleitung zu diesem Programm
	 */
	private String getAnleitung() {

		StringBuilder sb = new StringBuilder();
		sb.append("Anleitung\n\n");
		sb.append("\"r\" -> Für den Ablaufparameter r wird folgende Festlegung getroffen:\n");
		sb.append("	„s“ (solve): für die durch die XML-Datei beschriebene Probleminstanz wird ein Verlegungsplan ermittelt, falls ein solcher existiert, ansonsten wird das Programm mit einer Fehlermeldung beendet.\n");
		sb.append("	„sd“ (solve & display): wie „s“, nur dass der ermittelte Verlegungsplan nach der Lösung der Probleminstanz zusätzlich in der graphischen Oberfläche angezeigt wird. \n");
		sb.append("	„v“ (validate): durch diese Option wird der in der angegebenen XML-Datei enthaltene Verlegungsplan auf die Einhaltung der Bedingungen B1 – B4 hin überprüft.\n");
		sb.append("	„vd“ (validate & display): wie „v“, nur dass der ermittelte Verlegungsplan nach der Lösung der Probleminstanz zusätzlich in der graphischen Oberfläche angezeigt wird. \n");
		sb.append("	„d“ (display): der in der XML-Datei enthaltene Verlegungsplan wird in der graphischen Oberfläche angezeigt. Falls die angegebene XML-Datei keinen Verlegungsplan enthält, wird eine Fehlermeldung ausgegeben. \n");
		sb.append("\n\"if\" -> Der Eingabedateiparameter if (Input File) ist ein String, der den Pfad der Eingabedatei beinhaltet. \n");
		sb.append("\n\"l\" -> Der Parameter für die maximale Fugenlänge l ist eine positive natürliche Zahl, welche die Fugenlänge in cm angibt. ");
		sb.append("\n\nEin Beispielparameteraufruf kann demnach wie folgt aussehen: r=s if=\"bin\\verbund1.xml\" l=1200.");

		return sb.toString();
	}

	/**
	 * Schreibt die Fehler, die bei der Eingabe aufgetreten sind, und eine
	 * Überschrift in einen String
	 * 
	 * @return alle Fehler die aufgetreten sind.
	 */
	private String getFehlerText() {
		StringBuilder sb = new StringBuilder("Folgende Fehler wurden bei der Eingabe gemacht:\n\n");
		for (String string : fehler) {
			sb.append(string + "\n");
		}
		return sb.toString();
	}

	private void initDisplayAAS(IParserReader reader) {
		System.out.println("Anzeigen der Flaeche");
		new DisplayAAS(reader);
	}

	private void initDisplayAAS(IParserReader reader, int maxFugenlaenge) {
		System.out.println("Anzeigen der Flaeche mit Fehlerpruefung");
		new DisplayAAS(reader, maxFugenlaenge);
	}

	private void initEingabenMap() {
		eingaben = new HashMap<String, String>();
		eingaben.put("l", null);
		eingaben.put("if", null);
		eingaben.put("r", null);
	}

	private void initSolveAAS(String pfad, int maxFugenLange) {

		XMLParser parser = null;
		try {
			parser = XMLParser.getInstance(pfad);
		} catch (NichtDasRichtigeFormatException e) {
		}

		System.out.println("Finde eine Loesung der Probleminstanz " + pfad);
		System.out.print("Bei einer maximalen Fugenlaenge von " + maxFugenLange);
		System.out.println(" ist eine Flaeche von " + parser.getLeereFlaeche().getLaenge() + "x" + parser.getLeereFlaeche().getHoehe()
				+ "\nmit folgenden FliesenTypen zu verlegen:\n");

		for (IFliesenTyp fliese : parser.getFliesenTypen().values()) {
			System.out.println(fliese.getID() + " = " + fliese.getX() + "x" + fliese.getY());
		}
		System.out.println("\nDabei wird intern auf eine Quadrantenlaenge von " + parser.getLeereFlaeche().getQuadrantenLaenge() + " skaliert\n");
		System.out.println("Es wird die Konigurationsdatei benutzt: " + Heuristiker.STANDARTPROP);

		System.out.println();
		System.out.println("\nInitialisierung dauerte " + ((System.currentTimeMillis() - time) / 1000.0) + " s\n");
		time = System.currentTimeMillis();
		System.out.println("Start der Loesungsfindung");
		String ausgabe = null;
		if (new RoemischerVerbund().solveOhneBeschraenkung(pfad, maxFugenLange)) {
			ausgabe = "|   Loesung gefunden und wird in \"" + pfad + "\" geschrieben   |";
		} else {
			ausgabe = "|   Es gibt keine Moeglichkeit diese Flaeche zu verlegen   |";
		}
		for (int i = 0; i < ausgabe.length(); i++) {
			System.out.print("-");
		}
		System.out.println("\n" + ausgabe);
		for (int i = 0; i < ausgabe.length(); i++) {
			System.out.print("-");
		}

		System.out.println("\n\nBerechnung dauerte " + ((System.currentTimeMillis() - time) / 1000.0) + " s\n");
	}

	private void initValidateAAS(String pfad, int maxFugenLange) throws IOException {
		System.out.println("Validieren der Flaeche");
		try {
			if (!XMLParser.getInstance(pfad).hasVerlegungsplan()) {
				System.out.println("Kein gueltiger Verlegungsplan in der Datei " + pfad);
			} else {
				errors = new RoemischerVerbund().validateSolution(pfad, maxFugenLange);
				if (errors.size() == 0) {
					System.out.println("Keine Fehler");
				} else {
					System.out.println("Liste der Fehler:");
					for (Validation v : errors) {
						System.out.println(v.getFehlertext());
					}
				}
			}
			System.out.println();
		} catch (NichtDasRichtigeFormatException e) {
			System.out.println("Fehler: " + e.getLocalizedMessage());
		}
	}
}
