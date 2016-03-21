package ess.algorithm;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;
import com.ess.entity.NichtDasRichtigeFormatException;
import com.ess.heuristiken.Heuristiker;
import com.ess.heuristiken.FehlerEnums;
import com.ess.parser.IParserReader;
import com.ess.parser.XMLParser;
import com.ess.solveAS.SolveAAS;

/**
 * Diese Klasse wird als API (Application Programming Interface) verwendet. Das
 * bedeutet, dass diese Klasse als Bibliothek für andere Applikationen verwendet
 * werden kann.
 * 
 * Bitte achten Sie darauf, am bereits implementierten Rahmen (Klassenname,
 * Package, Methodensignaturen) !!KEINE!! Veränderungen vorzunehmen.
 * Selbstverständlich können und müssen Sie innerhalb einer Methode Änderungen
 * vornehmen.
 */
public class RoemischerVerbund implements IRoemischerVerbund {
	/**
	 * Fehlertypen, die bei der Validierung auftreten können
	 */
	public enum Validation {
		FLIESEN_AUSTAUSCHBAR, GLEICHE_FLIESEN, MAX_FUGENLAENGE, FUGENKREUZE, FLIESE_UNPASSEND;
		public String getFehlertext() {
			switch (this) {
			case FLIESEN_AUSTAUSCHBAR:
				return "Bereich der Flaeche durch eine groessere Fliese austauschbar";
			case FUGENKREUZE:
				return "Fugenkreuz vorhanden";
			case GLEICHE_FLIESEN:
				return "Gleiche Fliesentypen liegen exakt nebeneinander";
			case MAX_FUGENLAENGE:
				return "Die Maximale Fugenlaenge wurde überschritten";
			default:
				return "Sonstige Fehler";
			}
		}
	}

	/**
	 * Überprüft die eingegebene Lösung auf Korrektheit
	 * 
	 * @param xmlFile
	 *            Dokument, das validiert werden soll.
	 * @param maxFugenLaenge
	 *            maximale Fugenlänge der zu berechnenden Lösung.
	 * @return Liste von Fehlern, die fehlgeschlagen sind.
	 */
	@Override
	public List<Validation> validateSolution(String xmlFile, int maxFugenLaenge) {
		List<Validation> errorList = new LinkedList<Validation>();
		try {
			errorList = validateSolution(XMLParser.getInstance(xmlFile), maxFugenLaenge);
		} catch (NichtDasRichtigeFormatException e) {
			errorList.add(Validation.FLIESEN_AUSTAUSCHBAR);
			errorList.add(Validation.GLEICHE_FLIESEN);
			errorList.add(Validation.MAX_FUGENLAENGE);
			errorList.add(Validation.FUGENKREUZE);
			errorList.add(Validation.FLIESE_UNPASSEND);
		}
		return errorList;
	}

	/**
	 * Ermittelt eine Lösung zu den eingegebenen Daten
	 * 
	 * @param xmlFile
	 *            Eingabedokument, das die Probleminstanzen enthält.
	 * @param maxFugenLaenge
	 *            maximale Fugenlänge der zu berechnenden Lösung.
	 * @return konnte eine Lösung gefunden werden? true = ja, false = nein.
	 */
	@Override
	public boolean solve(String xmlFile, int maxFugenLaenge) {
		boolean loesbar;
		try {

			// Parser Instanziieren
			XMLParser parser = XMLParser.getInstance(xmlFile);

			// Laut Aufgabenstellung darf die API nur mit Quadrantenseiten von
			// vielfachen von 20 eine Loesung finden.
			if (parser.getLeereFlaeche().getQuadrantenLaenge() % 20 != 0) {
				loesbar = false;
			} else {
				// Starten des Solve Akteuranwendungsfalles
				loesbar = loesung(parser, maxFugenLaenge);
			}

		} catch (Exception e) {
			loesbar = false;
		}
		return loesbar;
	}

	private boolean loesung(XMLParser parser, int maxFugenLaenge) throws IOException {
		SolveAAS solver = new SolveAAS(parser, maxFugenLaenge);
		IFlaecheK loesung = solver.getRoemischenVerbund();
		// Schreiben in den Parser
		parser.write(loesung);
		return loesung != null;
	}

	/**
	 * Ohne Beschraenkung bedeutet, Es sind auch Seitenlängen die keine vielfachen von 20 sind erlaubt.
	 * 
	 * @param xmlFile
	 *            Probleminstanz
	 * @param maxFugenLaenge
	 *            maximale Fugenlaenge
	 * @return true wenn es eine Loesung gibt und diese in die XML geschrieben
	 *         wurde, false sonst.
	 */
	public boolean solveOhneBeschraenkung(String xmlFile, int maxFugenLaenge) {
		boolean loesbar = false;
		try {

			// Parser Instanziieren und starten des Solve Akteuranwendungsfalles
			loesbar = loesung(XMLParser.getInstance(xmlFile), maxFugenLaenge);

		} catch (Exception e) {
			loesbar = false;
		}
		return loesbar;
	}

	/**
	 * Prüft die übergebene Probleminstanz auf fehler, gibt die Fehler zurück.
	 * 
	 * @param parser
	 *            Parser der die Problemistanz enthält
	 * @param maxFugenLaenge
	 *            maximal
	 * @return die Fehlerliste
	 */
	public List<Validation> validateSolution(IParserReader parser, int maxFugenLaenge) {
		List<Validation> errorList = new LinkedList<Validation>();
		try {
			if (parser.getVerlegungsPlan() != null) {
				// Ummappen von der Internen Darstellung zur externen
				List<FehlerEnums> errorListIntern = new Heuristiker(new LinkedList<IFliesenTyp>(parser.getFliesenTypen().values())).testeAlles(parser, maxFugenLaenge);
				for (FehlerEnums fehlerEnums : errorListIntern) {
					errorList.add(Validation.values()[fehlerEnums.ordinal()]);
				}
			} else {
				errorList.add(Validation.FLIESEN_AUSTAUSCHBAR);
				errorList.add(Validation.GLEICHE_FLIESEN);
				errorList.add(Validation.MAX_FUGENLAENGE);
				errorList.add(Validation.FUGENKREUZE);
				errorList.add(Validation.FLIESE_UNPASSEND);
			}
		} catch (Exception e) {
			errorList.add(Validation.FLIESEN_AUSTAUSCHBAR);
			errorList.add(Validation.GLEICHE_FLIESEN);
			errorList.add(Validation.MAX_FUGENLAENGE);
			errorList.add(Validation.FUGENKREUZE);
			errorList.add(Validation.FLIESE_UNPASSEND);
		}
		return errorList;
	}
}
