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
 * bedeutet, dass diese Klasse als Bibliothek f�r andere Applikationen verwendet
 * werden kann.
 * 
 * Bitte achten Sie darauf, am bereits implementierten Rahmen (Klassenname,
 * Package, Methodensignaturen) !!KEINE!! Ver�nderungen vorzunehmen.
 * Selbstverst�ndlich k�nnen und m�ssen Sie innerhalb einer Methode �nderungen
 * vornehmen.
 */
public class RoemischerVerbund implements IRoemischerVerbund {
	/**
	 * Fehlertypen, die bei der Validierung auftreten k�nnen
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
				return "Die Maximale Fugenlaenge wurde �berschritten";
			default:
				return "Sonstige Fehler";
			}
		}
	}

	/**
	 * �berpr�ft die eingegebene L�sung auf Korrektheit
	 * 
	 * @param xmlFile
	 *            Dokument, das validiert werden soll.
	 * @param maxFugenLaenge
	 *            maximale Fugenl�nge der zu berechnenden L�sung.
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
	 * Ermittelt eine L�sung zu den eingegebenen Daten
	 * 
	 * @param xmlFile
	 *            Eingabedokument, das die Probleminstanzen enth�lt.
	 * @param maxFugenLaenge
	 *            maximale Fugenl�nge der zu berechnenden L�sung.
	 * @return konnte eine L�sung gefunden werden? true = ja, false = nein.
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
	 * Ohne Beschraenkung bedeutet, Es sind auch Seitenl�ngen die keine vielfachen von 20 sind erlaubt.
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
	 * Pr�ft die �bergebene Probleminstanz auf fehler, gibt die Fehler zur�ck.
	 * 
	 * @param parser
	 *            Parser der die Problemistanz enth�lt
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
