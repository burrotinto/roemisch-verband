package com.ess.entity;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

public class FlaecheTest1D {

	@Test
	public void testFalscheGroesse1() {
		boolean test = true;		
		try {
			new FlaecheK1D(120, 210,20);
			test = false;
		} catch (NichtDasRichtigeFormatException e) {
		}
		
		assertTrue("Falsche Grösse nicht erkannt", test);
		
	}

	@Test
	public void testFalscheGroesse2() {
		boolean test = true;
		try {
			new FlaecheK1D(210, 120,20);
			test = false;
		} catch (NichtDasRichtigeFormatException e) {
		}
		
		assertTrue("Falsche Grösse nicht erkannt", test);
		
	}
	
	@Test
	public void testRichtig() {
		boolean test = true;
		try {
			new FlaecheK1D(120, 120,20);
		} catch (NichtDasRichtigeFormatException e) {
			test = false;
		}
		assertTrue("Richtige Grösse nicht erkannt", test);
		
	}

	@Test
	public void testVerlegeFliesenTyp() {
		try {
			IFlaecheK flaeche = new FlaecheK1D(100, 100,20);
			IFliesenTyp passenderFliesenTyp = new FliesenTypE(40, 60, "1");
			IFliesenTyp passenderFliesenTyp2 = new FliesenTypE(20, 40, "3");
			IFliesenTyp zuGrosserFliesenTyp = new FliesenTypE(140, 40, "2");

			assertFalse("Zugross", flaeche.verlegeFliesenTypGeprueft(new Point(0, 0),
					zuGrosserFliesenTyp));
			
			assertFalse("Uebern Rand", flaeche.verlegeFliesenTypGeprueft(
					new Point(80, 0), passenderFliesenTyp));

			assertTrue("Erstes Einfuegen", flaeche.verlegeFliesenTypGeprueft(new Point(
					0, 0), passenderFliesenTyp));
			
			assertFalse("Teilweiseueberdeckt", flaeche.verlegeFliesenTypGeprueft(
					new Point(20, 20), passenderFliesenTyp));

			assertTrue("Zweites Einfuegen", flaeche.verlegeFliesenTypGeprueft(new Point(
					40, 0), passenderFliesenTyp));

			assertTrue("Legen bis zum Rand", flaeche.verlegeFliesenTypGeprueft(
					new Point(80, 0), passenderFliesenTyp2));
			
			assertTrue("Zusammenhaengende Fliese nicht erkannt", flaeche.getPlatteOnPoint(new Point(0, 0))
					.getFliesenTypID() == flaeche.getPlatteOnPoint(
					new Point(20, 20)).getFliesenTypID());
	
		} catch (NichtDasRichtigeFormatException e) {
			assertTrue("Exceptions nicht erlaubt", false);
		}

	}
}
