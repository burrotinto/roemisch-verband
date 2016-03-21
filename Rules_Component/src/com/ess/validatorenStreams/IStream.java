package com.ess.validatorenStreams;


/**
 * Interface fuer einen Stream aus Elementen
 * 
 * @author Florian Klinger
 *
 */
public interface IStream<T> {
	/**
	 * Lieset das naechste Element
	 * @return das naechste Element aus dem Stream
	 */
	T read();
	
	/**
	 * Schreibt die naechsten Elemente in das Array
	 * @param array das zu fuellende Array
	 */
	void read(T[] array);
}

