package com.ess.heuristikUndFilterTests;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.ess.entity.FlaecheK1D;
import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;
import com.ess.entity.NichtDasRichtigeFormatException;
import com.ess.parser.DummyParser;
import com.ess.parser.IParserReader;
import com.ess.validatoren.B4KeineErsetzung;
import com.ess.validatoren.B4PunktuellOhneVerlegung;

public class B4FilterTest {

	
	@Test
	public void test1() throws NichtDasRichtigeFormatException{
		IParserReader parser = new DummyParser(120, 120);
		IFlaecheK flaeche = new FlaecheK1D(120, 120);
		B4KeineErsetzung pruefling = new B4PunktuellOhneVerlegung(null);
	
		
		pruefling.setAlleFliesenTypen(new LinkedList<IFliesenTyp>(parser.getFliesenTypen().values()));
		List<IFliesenTyp> erlaubteTypen = new LinkedList<IFliesenTyp>(parser.getFliesenTypen().values());
		flaeche.verlegeFliesenTypUngeprueft(new Point (0,0), parser.getFliesenTypen().get("_4"));
		flaeche.verlegeFliesenTypUngeprueft(new Point (40,0), parser.getFliesenTypen().get("_3"));
		flaeche.verlegeFliesenTypUngeprueft(new Point (80,0), parser.getFliesenTypen().get("_4"));
	
		
		erlaubteTypen = pruefling.filtereUnmoeglicheTypen(erlaubteTypen, flaeche, new Point(40,20), 120);

		
		assertTrue("Typ 40*20 darf nicht dabei sein",!erlaubteTypen.contains(parser.getFliesenTypen().get("_3")));
		assertTrue("Typ 40*40 darf nicht dabei sein",!erlaubteTypen.contains(parser.getFliesenTypen().get("_4")));

		assertTrue("Sonst sind alle erlaubt", erlaubteTypen.size() +2 == parser.getFliesenTypen().size());
		
	}
	@Test
	public void test2() throws NichtDasRichtigeFormatException{
		IParserReader parser = new DummyParser(120, 120);
		IFlaecheK flaeche = parser.getLeereFlaeche();
		B4KeineErsetzung pruefling = new  B4PunktuellOhneVerlegung(null);
	
		
		pruefling.setAlleFliesenTypen(new LinkedList<IFliesenTyp>(parser.getFliesenTypen().values()));
		List<IFliesenTyp> erlaubteTypen = new LinkedList<IFliesenTyp>(parser.getFliesenTypen().values());
		flaeche.verlegeFliesenTypUngeprueft(new Point (0,0), parser.getFliesenTypen().get("_2"));
	
		
		erlaubteTypen = pruefling.filtereUnmoeglicheTypen(erlaubteTypen, flaeche, new Point(20,0), 120);
		
		assertTrue("Typ 20*40 darf nicht dabei sein",!erlaubteTypen.contains(parser.getFliesenTypen().get("_2")));
		assertTrue("Typ 40*40 darf nicht dabei sein",!erlaubteTypen.contains(parser.getFliesenTypen().get("_4")));
		
		assertTrue("Sonst sind alle erlaubt", erlaubteTypen.size() +2 == parser.getFliesenTypen().size());
		
	}
}
