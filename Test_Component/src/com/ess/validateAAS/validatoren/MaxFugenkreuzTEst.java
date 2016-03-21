package com.ess.validateAAS.validatoren;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ess.parser.DummyParser;
import com.ess.parser.IParserReader;
import com.ess.validatoren.B2KeineFugenkreuze;
import com.ess.validatoren.IBedingungKompletteFlaeche;

public class MaxFugenkreuzTEst extends FlaechenTest {
	@Test
	public void testPruefeFalsch() {
		IBedingungKompletteFlaeche bedingung = new B2KeineFugenkreuze();
		IParserReader parser = new DummyParser(120, 120);

		boolean test = bedingung.pruefe(getUngueltigeFlaeche(), parser.getFliesenTypen(), 120);
		assertFalse("Falsches nicht erkannt", test);
	}

	@Test
	public void testPruefeRichtig() {
		IBedingungKompletteFlaeche bedingung = new B2KeineFugenkreuze();
		IParserReader parser = new DummyParser(120, 120);

		boolean pruefe = bedingung.pruefe(getGueltigeFlaeche(), parser.getFliesenTypen(), 120);

		assertTrue("Richtiges nicht erkannt", pruefe);

	}

	@Test
	public void testPruefeRichtig2() {
		IBedingungKompletteFlaeche bedingung = new B2KeineFugenkreuze();
		IParserReader parser = new DummyParser(120, 120);

		boolean pruefe = bedingung.pruefe(getGueltigeFlaecheOhne1(), parser.getFliesenTypen(), 120);
	
		assertTrue("Richtiges nicht erkannt", pruefe);

	}
}
