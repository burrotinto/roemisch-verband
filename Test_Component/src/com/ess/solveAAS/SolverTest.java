package com.ess.solveAAS;

import org.junit.Test;

import static org.junit.Assert.*;

import com.ess.entity.IFlaecheK;
import com.ess.parser.DummyParser;
import com.ess.solveAS.SolveAAS;


public class SolverTest {
	@Test
	public void testSolve() {
		IFlaecheK f = new SolveAAS(new DummyParser(200, 200), 140).getRoemischenVerbund();
	
		assertTrue("Gibt Eine Loesung", f != null);
	}
	
	@Test
	public void testSolve2() {
		IFlaecheK f = new SolveAAS(new DummyParser(340, 480), 140).getRoemischenVerbund();
	
		assertTrue("Gibt Eine Loesung", f != null);
	}
	@Test
	public void testSolveRiesig() {
		IFlaecheK f = new SolveAAS(new DummyParser(1400, 400), 240).getRoemischenVerbund();
	
		assertTrue("Gibt Eine Loesung", f != null);
	}
	@Test
	public void testKeineLoesungWegenUngeraderAnzahlFelderBeiNurGeraderFelderAnzahlFliesen() {
		IFlaecheK f = new SolveAAS(new DummyParser(500, 500), 240).getRoemischenVerbund();
	
		assertTrue("Gibt Keine Loesung", f == null);
	}
	@Test
	public void testSolveQuadratGross() {
		IFlaecheK f = new SolveAAS(new DummyParser(520, 520), 240).getRoemischenVerbund();
	
		assertTrue("Gibt Keine Loesung", f != null);
	}
	@Test
	public void testKeineLoesung() {
		IFlaecheK f = new SolveAAS(new DummyParser(200, 260), 100).getRoemischenVerbund();
	
		assertTrue("Gibt Eine Loesung", f == null);
	}


}
