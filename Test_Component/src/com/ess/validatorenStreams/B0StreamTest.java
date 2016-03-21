package com.ess.validatorenStreams;

import static org.junit.Assert.*;

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
import com.ess.validatoren.AbstractBedingung;
import com.ess.validatoren.B0NurPassendeBedingung;
import com.ess.validatorenStreams.B0Stream;

public class B0StreamTest {

	
	@Test
	public void testPruefe() throws JDOMException, IOException, NichtDasRichtigeFormatException {
		IParserReader parser = new DummyParser(120,120);
		List<IFliesenTyp> l = new LinkedList<IFliesenTyp>(parser.getFliesenTypen().values());
		B0Stream b0 = new B0Stream(l, parser.getLeereFlaeche(), new Point (0,0));
		
		LinkedList<IFliesenTyp> erlaubt = new LinkedList<IFliesenTyp>();
		IFliesenTyp f = null;
		while((f = b0.read()) != null){
			erlaubt.add(f);
		}
		
		assertTrue("Alle erlaubt",erlaubt.size() == l.size());
	
		
		
	}
	
	@Test
	public void testPruefe2() throws JDOMException, IOException, NichtDasRichtigeFormatException {
		IParserReader parser = new DummyParser(40,40);
		List<IFliesenTyp> l = new LinkedList<IFliesenTyp>(parser.getFliesenTypen().values());
		B0Stream b0 = new B0Stream(l, parser.getLeereFlaeche(), new Point (0,0));
		
		LinkedList<IFliesenTyp> erlaubt = new LinkedList<IFliesenTyp>();
		IFliesenTyp f = null;
		while((f = b0.read()) != null){
			erlaubt.add(f);
		}
		
		assertTrue("2 nicht erlaubt",erlaubt.size() == l.size()-2);
	
		
		
		
	}
}