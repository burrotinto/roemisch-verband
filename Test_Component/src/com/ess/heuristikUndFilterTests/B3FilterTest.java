package com.ess.heuristikUndFilterTests;

import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jdom2.JDOMException;
import org.junit.Test;

import com.ess.entity.FlaecheK1D;
import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;
import com.ess.entity.NichtDasRichtigeFormatException;
import com.ess.parser.DummyParser;
import com.ess.parser.IParserReader;
import com.ess.validatoren.B3KeineGleichen;
import com.ess.validatoren.B3KeineGleichenVorrausschauend;

public class B3FilterTest {
	@Test
	public void test1() throws NichtDasRichtigeFormatException {
		IParserReader parser = new DummyParser(120, 120);
		IFlaecheK flaeche = new FlaecheK1D(120, 120);
		B3KeineGleichen pruefling = new B3KeineGleichen();

		List<IFliesenTyp> erlaubteTypen = new LinkedList<IFliesenTyp>(parser.getFliesenTypen().values());
		flaeche.verlegeFliesenTypUngeprueft(new Point(0, 0), parser.getFliesenTypen().get("_4"));
		erlaubteTypen = pruefling.filtereUnmoeglicheTypen(erlaubteTypen, flaeche, new Point(40, 0), 80);

		assertTrue("_4 darf nicht sein", !erlaubteTypen.contains(parser.getFliesenTypen().get("_4")));
		assertTrue("Sonst sind alle erlaubt", erlaubteTypen.size() + 1 == parser.getFliesenTypen().size());

	}

	@Test
	public void test2() throws NichtDasRichtigeFormatException {
		IParserReader parser = new DummyParser(120, 120);
		IFlaecheK flaeche = new FlaecheK1D(120, 120);
		B3KeineGleichen pruefling = new B3KeineGleichen();

		List<IFliesenTyp> erlaubteTypen = new LinkedList<IFliesenTyp>(parser.getFliesenTypen().values());
		flaeche.verlegeFliesenTypUngeprueft(new Point(0, 0), parser.getFliesenTypen().get("_1"));
		flaeche.verlegeFliesenTypUngeprueft(new Point(60, 0), parser.getFliesenTypen().get("_4"));
		flaeche.verlegeFliesenTypUngeprueft(new Point(40, 40), parser.getFliesenTypen().get("_2"));

		erlaubteTypen = pruefling.filtereUnmoeglicheTypen(erlaubteTypen, flaeche, new Point(60, 40), 80);

		assertTrue("_4 darf nicht sein", !erlaubteTypen.contains(parser.getFliesenTypen().get("_4")));
		assertTrue("_2 darf nicht sein", !erlaubteTypen.contains(parser.getFliesenTypen().get("_2")));
		assertTrue("Sonst sind alle erlaubt", erlaubteTypen.size() + 2 == parser.getFliesenTypen().size());

	}

	@Test
	public void testRueckschauZeile() throws JDOMException, IOException, NichtDasRichtigeFormatException {
		IParserReader parser = new DummyParser(120, 120);
		IFlaecheK flaeche = parser.getLeereFlaeche();
		B3KeineGleichen pruefling = new B3KeineGleichenVorrausschauend(parser.getFliesenTypen().values());
		List<IFliesenTyp> erlaubteTypen = new LinkedList<IFliesenTyp>(parser.getFliesenTypen().values());

		flaeche.verlegeFliesenTypUngeprueft(new Point(0, 0), parser.getFliesenTypen().get("_0"));
		flaeche.verlegeFliesenTypUngeprueft(new Point(40, 0), parser.getFliesenTypen().get("_2"));

		erlaubteTypen = pruefling.filtereUnmoeglicheTypen(erlaubteTypen, flaeche, new Point(60, 0), 80);

		
		assertTrue("_0 nicht erlaubt", !erlaubteTypen.contains(parser.getFliesenTypen().get("_0")) );
	}
	@Test
	public void testRueckschauSpalte() throws JDOMException, IOException, NichtDasRichtigeFormatException {
		IParserReader parser = new DummyParser(120, 120);
		IFlaecheK flaeche = parser.getLeereFlaeche();
		B3KeineGleichen pruefling = new B3KeineGleichenVorrausschauend(parser.getFliesenTypen().values());
		List<IFliesenTyp> erlaubteTypen = new LinkedList<IFliesenTyp>(parser.getFliesenTypen().values());

		flaeche.verlegeFliesenTypUngeprueft(new Point(0, 0), parser.getFliesenTypen().get("_1"));
		flaeche.verlegeFliesenTypUngeprueft(new Point(0, 40), parser.getFliesenTypen().get("_3"));

		erlaubteTypen = pruefling.filtereUnmoeglicheTypen(erlaubteTypen, flaeche, new Point(0, 60), 80);

		assertTrue("_1 nicht erlaubt", !erlaubteTypen.contains(parser.getFliesenTypen().get("_1")) );

	}
}
