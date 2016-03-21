package com.ess.validateAAS.validatoren;

import static org.junit.Assert.*;

import java.awt.Point;
import java.io.IOException;

import org.jdom2.JDOMException;
import org.junit.Test;

import com.ess.entity.IFlaecheK;
import com.ess.entity.NichtDasRichtigeFormatException;
import com.ess.parser.DummyParser;
import com.ess.parser.IParserReader;
import com.ess.validatoren.AbstractBedingung;
import com.ess.validatoren.B0NurPassendeBedingung;

public class B0Test {

	
	@Test
	public void testPruefe() throws JDOMException, IOException, NichtDasRichtigeFormatException {
		AbstractBedingung b0 = new B0NurPassendeBedingung();
		IParserReader parser = new DummyParser(120,120);
		
		boolean test = b0.pruefe(FlaechenTest.getGueltigeFlaeche(), parser.getFliesenTypen(), 120);
		
		assertTrue("Gueltige Flaeche nicht erkannt",test);
		
		
		
	}
	
	@Test
	public void testPruefe2() throws JDOMException, IOException, NichtDasRichtigeFormatException {
		//Test mit einer falsch verlegten Platte
		AbstractBedingung b0 = new B0NurPassendeBedingung();
		IParserReader parser = new DummyParser(120,120);
		IFlaecheK flaeche = FlaechenTest.getGueltigeFlaeche();
		flaeche.verlegeFliesenTypUngeprueft(new Point (20,20), parser.getFliesenTypen().get("_0"));
	
		boolean test = !b0.pruefe(flaeche, parser.getFliesenTypen(), 120);
		
		assertTrue("Ungueltige Flaeche nicht erkannt",test);
		
		
		
	}
}
