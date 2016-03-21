package com.ess.validateAAS.validatoren;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ess.parser.DummyParser;
import com.ess.parser.IParserReader;
import com.ess.validatoren.B4PunktuellOhneVerlegung;

public class VertauschungTest {

	@Test
	public void testRichtig1() {
		IParserReader parser = new DummyParser(120, 120);

		boolean pruefe = new B4PunktuellOhneVerlegung(null).pruefe(FlaechenTest.getGueltigeFlaeche(), parser.getFliesenTypen(), 120);
		
		assertTrue("Richtige Verlegung", pruefe);
	}

	@Test
	public void testRichtig2() {
		IParserReader parser = new DummyParser(120, 120);

		boolean pruefe = new B4PunktuellOhneVerlegung(null).pruefe(FlaechenTest.getGueltigeFlaecheOhne1(), parser.getFliesenTypen(), 120);
		
		assertTrue("Richtige Verlegung", pruefe);
	}

	@Test
	public void testFalsch() {
		IParserReader parser = new DummyParser(120, 120);

		boolean pruefe = new B4PunktuellOhneVerlegung(null).pruefe(FlaechenTest.getUngueltigeFlaeche(), parser.getFliesenTypen(), 120);
	
		assertFalse("Falsche Verlegung", pruefe);
	}
}
