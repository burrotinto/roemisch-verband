package com.ess.parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.ess.entity.FlaecheK1D;
import com.ess.entity.FliesenTypE;
import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;
import com.ess.entity.IPlatte;
import com.ess.entity.NichtDasRichtigeFormatException;

/**
 * Ein XMLParser implementiert die Schnittstellen IParserReader und
 * IParserWriter. Ein Singelton bekommt man ueber die Klassenmethode
 * getInstance(String).
 * 
 * @author Florian Klinger
 *
 */
public class XMLParser implements IParserReader, IParserWriter {

	private static HashMap<String, XMLParser> instances = new HashMap<>();
	private final static String ORGINALDTD = "RoemischerVerbund.dtd";

	private final IFlaecheK leereFlaeche;

	private int length1 = -1, length2 = -1;
	private HashMap<String, IFliesenTyp> typenMap = new HashMap<>();
	private File xmlFile;

	/**
	 * Factory Methode um an eine Instanz des XMLParsers zu gelangen. Fuer jede
	 * xml Datei wird eine neue instanz angelegt
	 * 
	 * @param xmlFile
	 *            Pfad zu der Datei
	 * @return "Singelton" eines XMLParsers
	 * @throws NichtDasRichtigeFormatException
	 *             es kann mit den enthaltenen Groessen keine Verlegung
	 *             bewerkstelligt werden
	 * @throws IOException
	 */
	public static XMLParser getInstance(String xmlFile) throws NichtDasRichtigeFormatException {
		if (xmlFile == null) {
			xmlFile = "";
		}
		XMLParser parser = instances.get(xmlFile);
		if (parser == null) {
			parser = new XMLParser(xmlFile);
		}
		return parser;
	}

	private XMLParser(String xmlFile) throws NichtDasRichtigeFormatException {
		this(new File(xmlFile));
	}

	private XMLParser(File xmlFile) throws NichtDasRichtigeFormatException {

		this.xmlFile = xmlFile;
		try {
			Document doc = getDocument(xmlFile);

			if (doc != null) {

				Element root = doc.getRootElement();
				Element fliesenTypen = root.getChild("Fliesentypen");

				initFliesenTypen(fliesenTypen);

				try {
					length1 = Integer.parseInt(root.getAttributeValue("length1"));
					length2 = Integer.parseInt(root.getAttributeValue("length2"));
				} catch (NumberFormatException e) {
					throw new NichtDasRichtigeFormatException(e.getLocalizedMessage());
				}
				leereFlaeche = new FlaecheK1D(length1, length2, getGGTDerFliesenTypen());
				getVerlegungsPlanV1();
			} else {
				throw new NichtDasRichtigeFormatException("Fehler beim Validieren der XML waehrend der Instanziierung, aufgerufen aufgrund falscher groessen");
			}
		} catch (Exception e) {
			StringBuilder sb = new StringBuilder();
			if (e.getLocalizedMessage().contains("file:")) {
				for (int i = 0; i < e.getLocalizedMessage().split(":").length; i++) {
					if (e.getLocalizedMessage().split(":")[i].contains(".xml")) {
						sb.append(new File(xmlFile.getAbsolutePath()));
					} else {
						sb.append(e.getLocalizedMessage().split(":")[i]);
					}
					if (i != e.getLocalizedMessage().split(":").length - 1)
						sb.append(":");
				}
			} else {
				sb.append(e.getLocalizedMessage());
			}
			throw new NichtDasRichtigeFormatException(sb.toString());
		}
	}

	/**
	 * XML wird geparst und als Document zurueckgegeben
	 * 
	 * @param xmlFile
	 * @return das {@link Document} der XMLdatei oder null wenn es keines gibt
	 */
	private Document getDocument(File xmlFile) throws Exception {
		// Die xml wird anhand der DTD gepueft
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();

		// dtd tmp file erstellen
		int rand = Math.abs(new Random().nextInt());
		File dM = File.createTempFile(rand + "-dtd", ".dtd");
		BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(ORGINALDTD)));
		BufferedWriter writer = new BufferedWriter(new FileWriter(dM.getAbsoluteFile()));
		int x = -1;
		while ((x = reader.read()) >= 0) {
			writer.write((char) x);
		}
		reader.close();
		writer.flush();
		writer.close();

		// dtd Pfad angeben
		transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dM.getAbsolutePath());

		// TempDateierst erstellen
		File tmp = File.createTempFile(rand + "-xml", ".xml");

		// XML mit DTD Pruefung erstellen
		transformer.transform(new StreamSource(new ByteArrayInputStream(Files.readAllBytes(xmlFile.toPath()))), new StreamResult(new FileWriter(tmp)));

		// SAXBuilder INstanzieren, ist die XML nicht gueltig wird die
		// Exception geworfen
		new SAXBuilder(XMLReaders.DTDVALIDATING).build(tmp);

		// tmps wieder loeschen
		tmp.delete();
		dM.delete();

		// Wenn alles geklappt hat. Wird die Orginale XML eingelesen
		return new SAXBuilder().build(xmlFile);
	}

	/**
	 * Hier wird der GGT Aller Fliesen bestimmt. Dies ist notwendig um die
	 * Flaeche zu skalieren.
	 * 
	 * @return der GGT aller FliesenTypen
	 */
	private int getGGTDerFliesenTypen() {
		int ggt = -1;
		for (IFliesenTyp typ : typenMap.values()) {
			if (ggt != -1) {
				ggt = gGT(ggt, gGT(typ.getX(), typ.getY()));
			} else {
				ggt = gGT(typ.getX(), typ.getY());
			}
		}
		return ggt;
	}

	private int gGT(int a, int b) {
		return b == 0 ? a : gGT(b, a % b);
	}

	private void initFliesenTypen(Element fliesenTypen) throws NichtDasRichtigeFormatException {
		List<Element> typen = fliesenTypen.getChildren("Fliesentyp");
		for (Element element : typen) {
			String id = element.getAttributeValue("ident").toString();
			if (typenMap.containsKey(id)) {
				throw new NichtDasRichtigeFormatException("Die FliesenID: \"" + id + "\" ist mehrfach vorhanden");
			} else {
				int x = -1;
				int y = -1;
				for (Element laengen : element.getChildren()) {
					if (laengen.getName().equals("length1")) {
						x = Integer.parseInt(laengen.getValue());
					} else {
						if (laengen.getName().equals("length2")) {
							y = Integer.parseInt(laengen.getValue());
						}
					}
				}
				if (x <= 0 || y <= 0) {
					throw new NichtDasRichtigeFormatException("Laenge oder Breite der FliesenID: \"" + id + "\"  <= 0 ");
				}
				typenMap.put(id, new FliesenTypE(x, y, id));
			}
		}
	}

	@Override
	public Map<String, IFliesenTyp> getFliesenTypen() {
		return typenMap;
	}

	/**
	 * Es wird die Flaeche des {@link IFlaecheK} in die, bei der Instanziierung
	 * angegebene, XML Datei geschrieben. Dabei wird ein evtl. enthaltener
	 * Verlegungsplan ersetzt.
	 * 
	 * @param flaeche
	 *            ist sie null wird nichts geschrieben aber der bereits
	 *            enthaltene geloescht, ansonsten wird ganz normal in die Datei
	 *            geschrieben
	 */
	@Override
	public synchronized void write(IFlaecheK flaeche) throws IOException {
		try {
			Document doc = getDocument(xmlFile);
			if (doc != null) {
				Element root = doc.getRootElement();
				if (root.getChild("Verlegungsplan") != null) {
					root.removeChild("Verlegungsplan");
				}
				if (flaeche != null) {
					root.addContent(new Element("Verlegungsplan"));
					Element verlegungsplan = root.getChild("Verlegungsplan");

					int i = 1;

					for (IPlatte platte : flaeche.getSortierteListeDerPlatten()) {
						Element plattenElement = new Element("Platte");
						plattenElement.setAttribute("fliesenId", platte.getFliesenTypID());

						Element nr = new Element("Nr");
						nr.addContent(i + "");

						plattenElement.addContent(nr);
						verlegungsplan.addContent(plattenElement);

						i++;
					}
				}

				// Speichern
				XMLOutputter xmlOutput = new XMLOutputter();

				xmlOutput.setFormat(Format.getPrettyFormat());
				xmlOutput.output(doc, new FileWriter(xmlFile));
			}
		} catch (Exception e) {
			throw new IOException(e.getLocalizedMessage());
		}
	}

	@Override
	public synchronized IFlaecheK getVerlegungsPlan() {
		try {
			return getVerlegungsPlanV1();
		} catch (NichtDasRichtigeFormatException e) {
			return null;
		}
	}

	private IFlaecheK getVerlegungsPlanV1() throws NichtDasRichtigeFormatException {
		try {
			Document doc = getDocument(xmlFile);

			if (doc == null) {
				return null;
			} else {
				Element root = doc.getRootElement();
				Element verlegungsplan = root.getChild("Verlegungsplan");
				IFlaecheK flaeche = null;
				if (verlegungsplan != null) {
					flaeche = getLeereFlaeche();
					int last = -1;
					for (Element element : verlegungsplan.getChildren()) {
						try {
							if (Integer.parseInt(element.getChild("Nr").getContent(0).getValue()) <= last) {
								throw new NichtDasRichtigeFormatException("Elemente des Verlegungsplanes nicht aufsteigend soriert: \""
										+ element.getChild("Nr").getContent(0).getValue() + "\"");
							} else {
								flaeche.verlegeFliesenTypUngeprueft(flaeche.getNaechsteFreieStelle(), getFliesenTypen().get(element.getAttribute("fliesenId").getValue()));
							}
						} catch (Exception e) {
							throw new NichtDasRichtigeFormatException(e.getMessage());
						}
					}
				}
				return flaeche;
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Gibt zurueck ob der Parser einen Verlegungsplan hat.
	 * 
	 * @return true wenn die xml einen gueltigen Verlegungsplan enthaelt
	 */
	public boolean hasVerlegungsplan() {
		return getVerlegungsPlan() != null;
	}

	@Override
	public IFlaecheK getLeereFlaeche() {
		return leereFlaeche.getCopy();
	}
}