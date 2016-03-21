package com.ess.parser;

import java.util.HashMap;
import java.util.Map;

import com.ess.entity.FlaecheK1D;
import com.ess.entity.FliesenTypE;
import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;
import com.ess.entity.NichtDasRichtigeFormatException;
/**
 * Eine Implementierung des IParserReaders. Wird nur zu TestZwecken gebraucht
 * @author Florian Klinger
 *
 */
public class DummyParser implements IParserReader{

	private int i = 120;
	private int j = 120;

	/**
	 * Es wird von einer Fläche 120x120 ausgegangen
	 */
	public DummyParser() {
	}

	/**
	 * Man kann die Abmessungen angeben
	 * 
	 * @param i laenge
	 * @param j breite
	 */
	public DummyParser(int i, int j) {
		this.i = i;
		this.j = j;
	}

	@Override
	public Map<String, IFliesenTyp> getFliesenTypen() {
		HashMap<String, IFliesenTyp> set = new HashMap<>();
			set.put("_0", new FliesenTypE(40, 60, "_0"));
			set.put("_1", new FliesenTypE(60, 40, "_1"));
			set.put("_2", new FliesenTypE(20, 40, "_2"));
			set.put("_3", new FliesenTypE(40, 20, "_3"));
			set.put("_4", new FliesenTypE(40, 40, "_4"));
		return set;
	}

	@Override
	public IFlaecheK getVerlegungsPlan() {
		return null;
	}

	@Override
	public IFlaecheK getLeereFlaeche() {
		try {
			return new FlaecheK1D(i, j,20);
		} catch (NichtDasRichtigeFormatException e) {
			return null;
		}
	}

}
