package com.ess.validateAAS.validatoren;

import java.awt.Point;

import com.ess.entity.FlaecheK1D;
import com.ess.entity.IFlaecheK;
import com.ess.entity.NichtDasRichtigeFormatException;
import com.ess.parser.DummyParser;
import com.ess.parser.IParserReader;

public class FlaechenTest {


	
	public static IFlaecheK getGueltigeFlaecheOhne1(){
		IFlaecheK flaecheE = null;
		try {
			flaecheE = new FlaecheK1D(120, 120);
		} catch (NichtDasRichtigeFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IParserReader parser = new DummyParser();

		flaecheE.verlegeFliesenTypUngeprueft(new Point(0,0),parser.getFliesenTypen().get("_0"));
		flaecheE.verlegeFliesenTypUngeprueft(new Point(40,0),parser.getFliesenTypen().get("_2"));
		flaecheE.verlegeFliesenTypUngeprueft(new Point(60,0),parser.getFliesenTypen().get("_1"));
		flaecheE.verlegeFliesenTypUngeprueft(new Point(0,60),parser.getFliesenTypen().get("_3"));
		flaecheE.verlegeFliesenTypUngeprueft(new Point(80,40),parser.getFliesenTypen().get("_3"));
		flaecheE.verlegeFliesenTypUngeprueft(new Point(80,60),parser.getFliesenTypen().get("_0"));
		flaecheE.verlegeFliesenTypUngeprueft(new Point(0,80),parser.getFliesenTypen().get("_2"));
		return flaecheE;
	
	}
	public static IFlaecheK getGueltigeFlaeche(){
		IFlaecheK flaecheE = null;
		try {
			flaecheE = new FlaecheK1D(120, 120);
		} catch (NichtDasRichtigeFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IParserReader parser = new DummyParser();

		flaecheE.verlegeFliesenTypUngeprueft(new Point(0,0),parser.getFliesenTypen().get("_0"));
		flaecheE.verlegeFliesenTypUngeprueft(new Point(40,0),parser.getFliesenTypen().get("_2"));
		flaecheE.verlegeFliesenTypUngeprueft(new Point(60,0),parser.getFliesenTypen().get("_1"));
		flaecheE.verlegeFliesenTypUngeprueft(new Point(0,60),parser.getFliesenTypen().get("_3"));
		flaecheE.verlegeFliesenTypUngeprueft(new Point(40,40),parser.getFliesenTypen().get("_4"));
		flaecheE.verlegeFliesenTypUngeprueft(new Point(80,40),parser.getFliesenTypen().get("_3"));
		flaecheE.verlegeFliesenTypUngeprueft(new Point(80,60),parser.getFliesenTypen().get("_0"));
		flaecheE.verlegeFliesenTypUngeprueft(new Point(0,80),parser.getFliesenTypen().get("_2"));
		flaecheE.verlegeFliesenTypUngeprueft(new Point(20,80),parser.getFliesenTypen().get("_1"));
		return flaecheE;
	}
	
	public static IFlaecheK getUngueltigeFlaeche(){
		IFlaecheK flaecheE = null;
		try {
			flaecheE = new FlaecheK1D(120, 120);
		} catch (NichtDasRichtigeFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IParserReader parser = new DummyParser();

		flaecheE.verlegeFliesenTypUngeprueft(new Point(0,0),parser.getFliesenTypen().get("_4"));
		flaecheE.verlegeFliesenTypUngeprueft(new Point(40,0),parser.getFliesenTypen().get("_2"));
		flaecheE.verlegeFliesenTypUngeprueft(new Point(60,0),parser.getFliesenTypen().get("_2"));
		flaecheE.verlegeFliesenTypUngeprueft(new Point(0,40),parser.getFliesenTypen().get("_4"));
		flaecheE.verlegeFliesenTypUngeprueft(new Point(40,40),parser.getFliesenTypen().get("_4"));
		return flaecheE;
	}
}
