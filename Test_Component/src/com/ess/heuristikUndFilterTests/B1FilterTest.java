package com.ess.heuristikUndFilterTests;

import static org.junit.Assert.assertTrue;

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
import com.ess.validatoren.B1MaximaleFugenlaenge;

public class B1FilterTest {
	
	@Test
	public void test1() throws NichtDasRichtigeFormatException{
		IParserReader parser = new DummyParser(120, 120);
		IFlaecheK flaeche = new FlaecheK1D(120, 120,20);
		B1MaximaleFugenlaenge pruefling = new B1MaximaleFugenlaenge();
	
		
		List<IFliesenTyp> erlaubteTypen = new LinkedList<IFliesenTyp>(parser.getFliesenTypen().values());
		
		flaeche.verlegeFliesenTypUngeprueft(new Point (0,0), parser.getFliesenTypen().get("_1"));
		flaeche.verlegeFliesenTypUngeprueft(new Point (60,0), parser.getFliesenTypen().get("_1"));
		flaeche.verlegeFliesenTypUngeprueft(new Point (0,40), parser.getFliesenTypen().get("_1"));
	
		
		erlaubteTypen = pruefling.filtereUnmoeglicheTypen(erlaubteTypen, flaeche, new Point(60,40), 80);

		
		assertTrue("Keine sind erlaubt", erlaubteTypen.size() == 0);
		
	}
	@Test
	public void test2() throws NichtDasRichtigeFormatException{
		IParserReader parser = new DummyParser(140, 140);
		IFlaecheK flaeche = new FlaecheK1D(140, 140);
		B1MaximaleFugenlaenge pruefling = new B1MaximaleFugenlaenge();
	
		
		List<IFliesenTyp> erlaubteTypen = new LinkedList<IFliesenTyp>(parser.getFliesenTypen().values());
		
		flaeche.verlegeFliesenTypUngeprueft(new Point (0,0), parser.getFliesenTypen().get("_0"));
		flaeche.verlegeFliesenTypUngeprueft(new Point (40,0), parser.getFliesenTypen().get("_1"));
		flaeche.verlegeFliesenTypUngeprueft(new Point (100,00), parser.getFliesenTypen().get("_0"));
	
		
		erlaubteTypen = pruefling.filtereUnmoeglicheTypen(erlaubteTypen, flaeche, new Point(40,40), 80);

		
		assertTrue("Typ 40*60 darf nicht dabei sein",!erlaubteTypen.contains(parser.getFliesenTypen().get("_0")));
	
		assertTrue("einer ist nicht erlaubt", erlaubteTypen.size() == 4);
		
	}
	@Test
	public void test3() throws NichtDasRichtigeFormatException{
		IParserReader parser = new DummyParser(120, 120);
		IFlaecheK flaeche = new FlaecheK1D(120, 60);
		B1MaximaleFugenlaenge pruefling = new B1MaximaleFugenlaenge();
	
		
		List<IFliesenTyp> erlaubteTypen = new LinkedList<IFliesenTyp>(parser.getFliesenTypen().values());
		
		flaeche.verlegeFliesenTypUngeprueft(new Point (0,0), parser.getFliesenTypen().get("_1"));		
		erlaubteTypen = pruefling.filtereUnmoeglicheTypen(erlaubteTypen, flaeche, new Point(60,0), 120);

	
		assertTrue("alle sind erlaubt", erlaubteTypen.size() == 5);
		
	}
	
	@Test
	public void test4() throws NichtDasRichtigeFormatException{
		IParserReader parser = new DummyParser(140, 140);
		IFlaecheK flaeche = new FlaecheK1D(140, 140);
		B1MaximaleFugenlaenge pruefling = new B1MaximaleFugenlaenge();
	
		
		List<IFliesenTyp> erlaubteTypen = new LinkedList<IFliesenTyp>(parser.getFliesenTypen().values());
		
		flaeche.verlegeFliesenTypUngeprueft(new Point (0,0), parser.getFliesenTypen().get("_0"));
		flaeche.verlegeFliesenTypUngeprueft(new Point (0,60), parser.getFliesenTypen().get("_4"));
		flaeche.verlegeFliesenTypUngeprueft(new Point (40,00), parser.getFliesenTypen().get("_0"));
	
		erlaubteTypen = pruefling.filtereUnmoeglicheTypen(erlaubteTypen, flaeche, new Point(40,60), 100);
		
		assertTrue("Typ 40*60 darf nicht dabei sein",!erlaubteTypen.contains(parser.getFliesenTypen().get("_0")));
	
		assertTrue("einer ist nicht erlaubt", erlaubteTypen.size() == 4);
		
	}
	@Test
	public void test5() throws NichtDasRichtigeFormatException{
		IParserReader parser = new DummyParser(140, 140);
		IFlaecheK flaeche = new FlaecheK1D(140, 140);
		B1MaximaleFugenlaenge pruefling = new B1MaximaleFugenlaenge();
	
		
		List<IFliesenTyp> erlaubteTypen = new LinkedList<IFliesenTyp>(parser.getFliesenTypen().values());
		
		flaeche.verlegeFliesenTypUngeprueft(new Point (20,40), parser.getFliesenTypen().get("_3"));
		flaeche.verlegeFliesenTypUngeprueft(new Point (60,40), parser.getFliesenTypen().get("_3"));
		flaeche.verlegeFliesenTypUngeprueft(new Point (0,60), parser.getFliesenTypen().get("_4"));
		flaeche.verlegeFliesenTypUngeprueft(new Point (0,40), parser.getFliesenTypen().get("_2"));
	
		erlaubteTypen = pruefling.filtereUnmoeglicheTypen(erlaubteTypen, flaeche, new Point(40,60),90);
		
		assertTrue("Typ 60*40 darf nicht dabei sein",!erlaubteTypen.contains(parser.getFliesenTypen().get("_1")));
	
		assertTrue("einer ist nicht erlaubt", erlaubteTypen.size() == 4);
		
	}
}