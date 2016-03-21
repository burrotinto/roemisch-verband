package com.ess.parser;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.jdom2.JDOMException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.ess.entity.FliesenTypE;
import com.ess.entity.NichtDasRichtigeFormatException;

public class XMLParserTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	private String WriteFileToTempDirectory(String fileName) throws IOException {
		// In Unit-Tests braucht kein fortgeschrittenes Exceptionhandling
		// integriert werden.
		// Der Grund dafür ist, dass der Test genau ein Szenario abbildet.
		byte[] bytes = Files.readAllBytes(Paths.get(fileName));
		String xmlAsString = new String(bytes, "UTF8");
		String tempFileName = String.format("%s.xml", UUID.randomUUID().toString());
		File testFile = folder.newFile(tempFileName);

		FileWriter fw = null;
		try {
			fw = new FileWriter(testFile);
			fw.write(xmlAsString);
		} finally {
			if (fw != null) {
				fw.close();
			}
		}

		return testFile.getPath();
	}

	@Test
	public void testFalscheLaenge() throws JDOMException, IOException {

		try {
			XMLParser.getInstance(WriteFileToTempDirectory("instances/ParserInstances/WrongLength.xml"));
			assertTrue("Falsche Laengen", false);
		} catch (NichtDasRichtigeFormatException e) {
		}

	}

	@Test
	public void testAllesRichtig() throws JDOMException, IOException {
		try {
			XMLParser.getInstance(WriteFileToTempDirectory("instances/ParserInstances/test1.xml"));

		} catch (NichtDasRichtigeFormatException e) {
			assertTrue("Aber alles ist doch richtig", false);
		}

	}

	@Test
	public void testGrossePLatten() throws JDOMException, IOException {
		try {
			XMLParser.getInstance(WriteFileToTempDirectory("instances/ParserInstances/grossePlatten.xml"));

		} catch (NichtDasRichtigeFormatException e) {
			assertTrue("Aber alles ist doch richtig", false);
		}

	}

	@Test
	public void testGroessen() throws JDOMException, IOException, NichtDasRichtigeFormatException {
		IParserReader xmlParser = XMLParser.getInstance(WriteFileToTempDirectory("instances/ParserInstances/test1.xml"));

		assertTrue("Fliese 0 falsch", xmlParser.getFliesenTypen().containsValue(new FliesenTypE(40, 60, "_0")));
		assertTrue("Fliese 1 falsch", xmlParser.getFliesenTypen().containsValue(new FliesenTypE(60, 40, "_1")));
		assertTrue("Fliese 2 falsch", xmlParser.getFliesenTypen().containsValue(new FliesenTypE(20, 40, "_2")));
		assertTrue("Fliese 3 falsch", xmlParser.getFliesenTypen().containsValue(new FliesenTypE(40, 20, "_3")));
		assertTrue("Fliese 4 falsch", xmlParser.getFliesenTypen().containsValue(new FliesenTypE(40, 40, "_4")));

	}

	@Test
	public void testFalscheXML() throws NichtDasRichtigeFormatException, IOException {
		// Die XML datei enthält mehrere Fehler
		boolean bestanden = true;

		try {
			XMLParser.getInstance(WriteFileToTempDirectory("instances/ParserInstances/WrongXML.xml"));
		} catch (NichtDasRichtigeFormatException e) {
			bestanden = false;
		}

		assertFalse("FalscheFlaeche nicht erkannt", bestanden);
	}

	@Test
	public void testFalscheFlaeche() throws JDOMException, IOException {
		// Test soll herausfinden das eine Seitenlänge kein vielfaches des GGT
		// der FliesenTypen sind
		boolean bestanden = true;
		try {

			XMLParser.getInstance(WriteFileToTempDirectory("instances/ParserInstances/TestFalscheFliesen.xml"));
		} catch (NichtDasRichtigeFormatException e) {
			bestanden = false;
		}
		assertFalse("FalscheFlaeche nicht erkannt", bestanden);
	}

}
