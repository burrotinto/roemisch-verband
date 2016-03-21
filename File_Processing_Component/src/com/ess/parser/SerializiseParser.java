package com.ess.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.ess.entity.FlaecheK1D;
import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;
import com.ess.entity.IPlatte;

/**
 * Umsetzung der Interfaces {@link IParserReader} und {@link IParserWriter}
 * indem die Probleminstanz als {@link java.lang.Object} gespeichert wird
 * 
 * @author Florian Klinger
 *
 */
public class SerializiseParser implements IParserReader, IParserWriter {

	private File file;
	private Collection<IFliesenTyp> fliesentypen;
	private Wrapper wrapper = null;

	/**
	 * Konstruiert den Parser anhand der erlaubten FliesenTypen
	 * 
	 * @param file
	 *            wo die Datei liegt
	 * @param fliesentypen
	 *            welche FliesenTypen erlaubt sind
	 */
	public SerializiseParser(File file, Collection<IFliesenTyp> fliesentypen) {
		this.file = file;
		this.fliesentypen = new LinkedList<>(fliesentypen);
	}

	public SerializiseParser(File file) {
		this.file = file;
		this.fliesentypen = getFliesenTypen().values();
	}

	@Override
	public void write(IFlaecheK flaeche) throws IOException {
		Wrapper w = new Wrapper();
		w.fliesentypen = fliesentypen;
		w.platten = flaeche.getSortierteListeDerPlatten();
		w.length1 = flaeche.getLaenge();
		w.length2 = flaeche.getHoehe();
		w.ggT = flaeche.getQuadrantenLaenge();
		ObjectOutput oO = new ObjectOutputStream(new FileOutputStream(file));
		oO.writeObject(w);
		oO.close();
	}

	private Wrapper readWrapper() throws Exception {
		if (wrapper == null) {
			ObjectInput oI = new ObjectInputStream(new FileInputStream(file));
			wrapper = (Wrapper) oI.readObject();
			oI.close();
		}
		return wrapper;
	}

	@Override
	public Map<String, IFliesenTyp> getFliesenTypen() {
		HashMap<String, IFliesenTyp> map = new HashMap<>();
		try {
			for (IFliesenTyp iFliesenTyp : readWrapper().fliesentypen) {
				map.put(iFliesenTyp.getID(), iFliesenTyp);
			}
		} catch (Exception e) {
			if (fliesentypen != null) {
				for (IFliesenTyp iFliesenTyp : fliesentypen) {
					map.put(iFliesenTyp.getID(), iFliesenTyp);
				}
			}
		}
		return map;
	}

	@Override
	public IFlaecheK getVerlegungsPlan() {
		IFlaecheK flaeche = getLeereFlaeche();
		if (flaeche == null) {
			return null;
		} else {
			try {
				for (IPlatte platte : readWrapper().platten) {
					flaeche.verlegeFliesenTypUngeprueft(platte.getVerlegePunktObenLinks(), getFliesenTypen().get(platte.getFliesenTypID()));
				}
			} catch (Exception e) {
			}
			return flaeche;
		}
	}

	@Override
	public IFlaecheK getLeereFlaeche() {
		try {
			return new FlaecheK1D(readWrapper().length1, readWrapper().length2, readWrapper().ggT);
		} catch (Exception e) {
			return null;
		}
	}
}

class Wrapper implements Serializable {

	private static final long serialVersionUID = 1L;
	Collection<IFliesenTyp> fliesentypen;
	Collection<IPlatte> platten;
	int length1, length2, ggT;
}