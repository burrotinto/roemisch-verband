package com.ess.validateAAS.validatoren;

import static org.junit.Assert.*;

import org.junit.*;

import com.ess.parser.DummyParser;
import com.ess.parser.IParserReader;
import com.ess.validatoren.B1MaximaleFugenlaenge;
import com.ess.validatoren.IBedingungKompletteFlaeche;

public class MaximaleFugenlaengeTest{

	@Test
	public void testRichtig1() {
		IBedingungKompletteFlaeche test1 = new B1MaximaleFugenlaenge();
		IParserReader parser = new DummyParser(120, 120);

		boolean pruefe = test1.pruefe(FlaechenTest.getGueltigeFlaeche(), parser.getFliesenTypen(), 120);

		assertTrue("Muss bestehen", pruefe);

	}

	@Test
	public void testRichtig2() {
		IBedingungKompletteFlaeche test1 = new B1MaximaleFugenlaenge();
		IParserReader parser = new DummyParser(120, 120);

		boolean pruefe = test1.pruefe(FlaechenTest.getGueltigeFlaecheOhne1(), parser.getFliesenTypen(), 120);

		assertTrue("Muss bestehen", pruefe);

	}

	@Test
	public void testFalsch1() {
		IBedingungKompletteFlaeche test2 = new B1MaximaleFugenlaenge();
		IParserReader parser = new DummyParser(120, 120);

		boolean pruefe = test2.pruefe(FlaechenTest.getGueltigeFlaeche(), parser.getFliesenTypen(), 60);

		assertFalse("Darf Nicht bestehen", pruefe);

	}

	@Test
	public void testFalsch2() {
		IBedingungKompletteFlaeche test2 = new B1MaximaleFugenlaenge();
		IParserReader parser = new DummyParser(120, 120);

		boolean pruefe = test2.pruefe(FlaechenTest.getGueltigeFlaecheOhne1(), parser.getFliesenTypen(), 60);

		assertFalse("Darf Nicht bestehen", pruefe);

	}

}
