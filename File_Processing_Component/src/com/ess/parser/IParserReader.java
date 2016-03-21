package com.ess.parser;

import java.util.Map;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;

/**
 * Klassen die dieses Interface implementieren stellen die Funktionalitaet
 * bereit, Probleminstanzen einzulesen und in die interne Struktur umzuwandeln.
 * 
 * @author Florian Klinger
 * 
 */
public interface IParserReader {
	
	/**
	 * Hier kann erfragt werden welche FliesenTypen der Parser ausgelesen hat.
	 * 
	 * @return Map mit den erlaubten FliesenTypen, der Key ist die ID der FliesenTypen
	 */
	Map<String, IFliesenTyp> getFliesenTypen();

	/**
	 * Gibt den Verlegungsplan zurück, wenn dieser existiert.
	 * 
	 * @return <tt>null</tt> wenn es keinen gibt, ansonsten den eingelesenen Plan, auch wenn dieser keine gueltige Verlegung darstellt.
	 */
	IFlaecheK getVerlegungsPlan();

	/**
	 * Mit den vom Parser ausgelesenen Größen wird ein {@link IFlaecheK} instanziiert.
	 * 
	 * @return ein Flaechenkontroller der eine Flaeche von length1 * length2 verwaltet
	 */
	IFlaecheK getLeereFlaeche();
}
