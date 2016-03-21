package com.ess.validateAAS.validatoren;

import static org.junit.Assert.*;

import java.io.IOException;

import org.jdom2.JDOMException;
import org.junit.Test;

import com.ess.entity.NichtDasRichtigeFormatException;
import com.ess.parser.DummyParser;
import com.ess.parser.IParserReader;
import com.ess.parser.XMLParser;
import com.ess.validatoren.B3KeineGleichen;
import com.ess.validatoren.IBedingungKompletteFlaeche;

public class KeineGleichenTest{

	@Test
	public void testGueltig() throws JDOMException, IOException, NichtDasRichtigeFormatException {
		IBedingungKompletteFlaeche pruefling = new B3KeineGleichen();
		IParserReader parser = new DummyParser(120,120);
		
		boolean test =  pruefling.pruefe(FlaechenTest.getGueltigeFlaeche(), parser.getFliesenTypen(), 120);
		
		assertTrue("Richtige Verlegung nicht erkannt",test);

		
		
	}
	@Test
	public void testGueltig2() throws JDOMException, IOException, NichtDasRichtigeFormatException {
		IBedingungKompletteFlaeche pruefling = new B3KeineGleichen();
		IParserReader parser = new DummyParser(120,120);
		
		boolean test =  pruefling.pruefe(FlaechenTest.getGueltigeFlaecheOhne1(), parser.getFliesenTypen(), 120);
		
		assertTrue("Richtige Verlegung nicht erkannt",test);
		
		
	}
	@Test
	public void testUnGueltig() throws JDOMException, IOException, NichtDasRichtigeFormatException {
		IBedingungKompletteFlaeche pruefling = new B3KeineGleichen();
		IParserReader parser = new DummyParser(120,120);
		
		boolean test =  pruefling.pruefe(XMLParser.getInstance("instances/validationInstances/B3Falsch.xml").getVerlegungsPlan(), parser.getFliesenTypen(), 120);
		
		assertFalse("Falsche Verlegung nicht erkannt", test);

		
		
	}
}
