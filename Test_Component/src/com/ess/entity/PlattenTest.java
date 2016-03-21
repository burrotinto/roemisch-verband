package com.ess.entity;

import static org.junit.Assert.*;

import org.junit.Test;

public class PlattenTest {

	@Test
	public void testIdentitaet() throws NichtDasRichtigeFormatException{
		IFliesenTyp fliesenTyp = new FliesenTypE(20,20,"1");
		IPlatte p1 = new PlatteE(fliesenTyp,null);
		IPlatte p2 = new PlatteE(fliesenTyp,null);
		
		assertTrue("Test auf gleichheit", !p1.equals(p2) );
		assertTrue("Test auf Identitaet", p1 != p2);
	}
}
