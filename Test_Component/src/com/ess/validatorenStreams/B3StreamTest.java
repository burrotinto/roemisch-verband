package com.ess.validatorenStreams;

import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jdom2.JDOMException;
import org.junit.Test;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;
import com.ess.entity.NichtDasRichtigeFormatException;
import com.ess.parser.DummyParser;
import com.ess.parser.IParserReader;

public class B3StreamTest {

	@Test
	public void testRandSenkrecht() throws JDOMException, IOException, NichtDasRichtigeFormatException {
		IParserReader parser = new DummyParser(100, 80);
		List<IFliesenTyp> l = new LinkedList<IFliesenTyp>(parser.getFliesenTypen().values());
		IFlaecheK fl = parser.getLeereFlaeche();

		fl.verlegeFliesenTypUngeprueft(new Point(20, 60), parser.getFliesenTypen().get("_3"));
		fl.verlegeFliesenTypUngeprueft(new Point(0, 0), parser.getFliesenTypen().get("_0"));

		Point einfuegepunkt = new Point(40, 40);
		B0Stream b0 = new B0Stream(l, fl, einfuegepunkt);
		B3Stream b3 = new B3Stream(null, b0, l, fl, einfuegepunkt, 140);
		LinkedList<IFliesenTyp> erlaubt = new LinkedList<IFliesenTyp>();
		IFliesenTyp f = null;
		while ((f = b3.read()) != null) {
			erlaubt.add(f);
		}

		assertTrue("Nichts erlaubt", erlaubt.size() == 0);
	}

	@Test
	public void testRandSenkrecht2() throws JDOMException, IOException, NichtDasRichtigeFormatException {
		IParserReader parser = new DummyParser(100, 80);
		List<IFliesenTyp> l = new LinkedList<IFliesenTyp>(parser.getFliesenTypen().values());

		IFlaecheK fl = parser.getLeereFlaeche();

		fl.verlegeFliesenTypUngeprueft(new Point(20, 60), parser.getFliesenTypen().get("_3"));
		fl.verlegeFliesenTypUngeprueft(new Point(0, 0), parser.getFliesenTypen().get("_0"));
		Point einfuegepunkt = new Point(40, 20);
		B0Stream b0 = new B0Stream(l, fl, einfuegepunkt);
		B3Stream b3 = new B3Stream(null, b0, l, fl, einfuegepunkt, 140);
		LinkedList<IFliesenTyp> erlaubt = new LinkedList<IFliesenTyp>();
		IFliesenTyp f = null;

		while ((f = b3.read()) != null) {

			erlaubt.add(f);
		}

		assertTrue("Einer erlaubt", erlaubt.size() == 1);

	}

	@Test
	public void testRandSenkrecht3() throws JDOMException, IOException, NichtDasRichtigeFormatException {
		IParserReader parser = new DummyParser(200, 60);
		List<IFliesenTyp> l = new LinkedList<IFliesenTyp>(parser.getFliesenTypen().values());

		IFlaecheK fl = parser.getLeereFlaeche();

		fl.verlegeFliesenTypUngeprueft(new Point(0, 0), parser.getFliesenTypen().get("_0"));
		fl.verlegeFliesenTypUngeprueft(new Point(40, 0), parser.getFliesenTypen().get("_1"));

		Point einfuegepunkt = new Point(40, 40);
		B0Stream b0 = new B0Stream(l, fl, einfuegepunkt);
		B3Stream b3 = new B3Stream(null, b0, l, fl, einfuegepunkt, 140);
		LinkedList<IFliesenTyp> erlaubt = new LinkedList<IFliesenTyp>();
		IFliesenTyp f = null;

		while ((f = b3.read()) != null) {
			erlaubt.add(f);
		}

		assertTrue("Keiner erklaubt", erlaubt.size() == 0);

	}

	@Test
	public void testRandWaagrecht() throws JDOMException, IOException, NichtDasRichtigeFormatException {
		IParserReader parser = new DummyParser(80, 100);
		List<IFliesenTyp> l = new LinkedList<IFliesenTyp>(parser.getFliesenTypen().values());
		IFlaecheK fl = parser.getLeereFlaeche();

		fl.verlegeFliesenTypUngeprueft(new Point(60, 20), parser.getFliesenTypen().get("_2"));
		fl.verlegeFliesenTypUngeprueft(new Point(0, 0), parser.getFliesenTypen().get("_1"));

		Point einfuegepunkt = new Point(40, 40);
		B0Stream b0 = new B0Stream(l, fl, einfuegepunkt);
		B3Stream b3 = new B3Stream(null, b0, l, fl, einfuegepunkt, 140);
		LinkedList<IFliesenTyp> erlaubt = new LinkedList<IFliesenTyp>();
		IFliesenTyp f = null;
		while ((f = b3.read()) != null) {
			erlaubt.add(f);
		}

		assertTrue("Nichts erlaubt", erlaubt.size() == 0);
	}

	@Test
	public void testRandWaagrecht2() throws JDOMException, IOException, NichtDasRichtigeFormatException {
		IParserReader parser = new DummyParser(80, 100);
		List<IFliesenTyp> l = new LinkedList<IFliesenTyp>(parser.getFliesenTypen().values());

		IFlaecheK fl = parser.getLeereFlaeche();

		fl.verlegeFliesenTypUngeprueft(new Point(60, 20), parser.getFliesenTypen().get("_2"));
		fl.verlegeFliesenTypUngeprueft(new Point(0, 0), parser.getFliesenTypen().get("_1"));
		Point einfuegepunkt = new Point(20, 40);
		B0Stream b0 = new B0Stream(l, fl, einfuegepunkt);
		B3Stream b3 = new B3Stream(null, b0, l, fl, einfuegepunkt, 140);
		LinkedList<IFliesenTyp> erlaubt = new LinkedList<IFliesenTyp>();
		IFliesenTyp f = null;

		while ((f = b3.read()) != null) {
			erlaubt.add(f);
		}
		assertTrue("Einer erlaubt", erlaubt.size() == 1);

	}

	@Test
	public void testRandWaagrecht3() throws JDOMException, IOException, NichtDasRichtigeFormatException {
		IParserReader parser = new DummyParser(60, 200);
		List<IFliesenTyp> l = new LinkedList<IFliesenTyp>(parser.getFliesenTypen().values());

		IFlaecheK fl = parser.getLeereFlaeche();

		fl.verlegeFliesenTypUngeprueft(new Point(0, 0), parser.getFliesenTypen().get("_1"));
		fl.verlegeFliesenTypUngeprueft(new Point(0, 40), parser.getFliesenTypen().get("_0"));

		Point einfuegepunkt = new Point(40, 40);
		B0Stream b0 = new B0Stream(l, fl, einfuegepunkt);
		B3Stream b3 = new B3Stream(null, b0, l, fl, einfuegepunkt, 140);
		LinkedList<IFliesenTyp> erlaubt = new LinkedList<IFliesenTyp>();
		IFliesenTyp f = null;

		while ((f = b3.read()) != null) {
			erlaubt.add(f);
		}

		assertTrue("Keiner erklaubt", erlaubt.size() == 0);

	}

	@Test
	public void testWaagrecht1() throws JDOMException, IOException, NichtDasRichtigeFormatException {
		IParserReader parser = new DummyParser(200, 200);
		List<IFliesenTyp> l = new LinkedList<IFliesenTyp>(parser.getFliesenTypen().values());

		IFlaecheK fl = parser.getLeereFlaeche();

		fl.verlegeFliesenTypUngeprueft(new Point(0, 0), parser.getFliesenTypen().get("_0"));
		fl.verlegeFliesenTypUngeprueft(new Point(40, 0), parser.getFliesenTypen().get("_1"));
		fl.verlegeFliesenTypUngeprueft(new Point(0, 60), parser.getFliesenTypen().get("_4"));
		fl.verlegeFliesenTypUngeprueft(new Point(40, 40), parser.getFliesenTypen().get("_0"));
		fl.verlegeFliesenTypUngeprueft(new Point(100, 0), parser.getFliesenTypen().get("_0"));
		fl.verlegeFliesenTypUngeprueft(new Point(80, 40), parser.getFliesenTypen().get("_2"));
		fl.verlegeFliesenTypUngeprueft(new Point(140, 40), parser.getFliesenTypen().get("_0"));

		Point einfuegepunkt = new Point(100, 60);
		B0Stream b0 = new B0Stream(l, fl, einfuegepunkt);
		B3Stream b3 = new B3Stream(null, b0, l, fl, einfuegepunkt, 140);
		LinkedList<IFliesenTyp> erlaubt = new LinkedList<IFliesenTyp>();
		IFliesenTyp f = null;

		while ((f = b3.read()) != null) {
			erlaubt.add(f);
		}

		assertTrue("Einer erlaubt", erlaubt.size() == 1);

	}
	@Test
	public void testWaagrecht2() throws JDOMException, IOException, NichtDasRichtigeFormatException {
		IParserReader parser = new DummyParser(100, 80);
		List<IFliesenTyp> l = new LinkedList<IFliesenTyp>(parser.getFliesenTypen().values());

		IFlaecheK fl = parser.getLeereFlaeche();

		fl.verlegeFliesenTypUngeprueft(new Point(0, 0), parser.getFliesenTypen().get("_1"));
		fl.verlegeFliesenTypUngeprueft(new Point(0, 40), parser.getFliesenTypen().get("_3"));
		fl.verlegeFliesenTypUngeprueft(new Point(60, 0), parser.getFliesenTypen().get("_3"));
		fl.verlegeFliesenTypUngeprueft(new Point(60, 20), parser.getFliesenTypen().get("_0"));

		Point einfuegepunkt = new Point(40, 40);
		B0Stream b0 = new B0Stream(l, fl, einfuegepunkt);
		B3Stream b3 = new B3Stream(null, b0, l, fl, einfuegepunkt, 140);
		LinkedList<IFliesenTyp> erlaubt = new LinkedList<IFliesenTyp>();
		IFliesenTyp f = null;

		while ((f = b3.read()) != null) {
			erlaubt.add(f);
		}

		assertTrue("Einer erklaubt", erlaubt.size() == 1);

	}
	@Test
	public void testWaagrecht3() throws JDOMException, IOException, NichtDasRichtigeFormatException {
		IParserReader parser = new DummyParser(80, 80);
		List<IFliesenTyp> l = new LinkedList<IFliesenTyp>(parser.getFliesenTypen().values());

		IFlaecheK fl = parser.getLeereFlaeche();

		fl.verlegeFliesenTypUngeprueft(new Point(0, 0), parser.getFliesenTypen().get("_0"));

		Point einfuegepunkt = new Point(40, 0);
		B0Stream b0 = new B0Stream(l, fl, einfuegepunkt);
		B3Stream b3 = new B3Stream(null, b0, l, fl, einfuegepunkt, 140);
		LinkedList<IFliesenTyp> erlaubt = new LinkedList<IFliesenTyp>();
		IFliesenTyp f = null;
		while ((f = b3.read()) != null) {
			erlaubt.add(f);
		}

		assertTrue("20 * 40 nicht erlaubt ", !erlaubt.contains(parser.getFliesenTypen().get("_2")));

	}
	@Test
	public void testWaagrecht4() throws JDOMException, IOException, NichtDasRichtigeFormatException {
		IParserReader parser = new DummyParser(80, 200);
		List<IFliesenTyp> l = new LinkedList<IFliesenTyp>(parser.getFliesenTypen().values());

		IFlaecheK fl = parser.getLeereFlaeche();

		fl.verlegeFliesenTypUngeprueft(new Point(40, 0), parser.getFliesenTypen().get("_4"));
		fl.verlegeFliesenTypUngeprueft(new Point(0, 20), parser.getFliesenTypen().get("_0"));
	
		Point einfuegepunkt = new Point(40, 40);
		B0Stream b0 = new B0Stream(l, fl, einfuegepunkt);
		B3Stream b3 = new B3Stream(null, b0, l, fl, einfuegepunkt, 140);
		LinkedList<IFliesenTyp> erlaubt = new LinkedList<IFliesenTyp>();
		IFliesenTyp f = null;
		while ((f = b3.read()) != null) {
			erlaubt.add(f);
		}

		assertTrue("20 * 40 nicht erlaubt ", !erlaubt.contains(parser.getFliesenTypen().get("_2")));

	}
}
