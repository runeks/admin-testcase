package no.evote.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import no.evote.constants.EvoteConstants;
import no.evote.exception.EvoteException;

import org.apache.log4j.Logger;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.LineSeparator;
import org.jdom2.output.XMLOutputter;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import static no.evote.util.LazyFormat.format;

public final class XMLUtil {

	private static final Logger LOG = Logger.getLogger(XMLUtil.class);

	private XMLUtil() {
	}

	public static String formatXml(final Document xmldoc) {
		StringWriter result = new StringWriter();
		StreamResult out = new StreamResult(result);
		try {
			DOMSource domSource = new DOMSource(xmldoc);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");

			// we want to pretty format the XML output
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			transformer.transform(domSource, out);
		} catch (TransformerException e) {
			LOG.warn("Feil ved formattering av XML output", e);
		}
		return result.toString();
	}

	public static boolean validateAgainstSchema(final String doc, final String xsdFilename) throws SAXException, IOException, ParserConfigurationException {
		// 1. Lookup a factory for the W3C XML Schema language
		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

		// 2. Compile the schema.
		// Here the schema is loaded from a java.io.File, but you could use
		// a java.net.URL or a javax.xml.transform.Source instead.
		Schema schema = factory.newSchema(XMLUtil.class.getClassLoader().getResource(xsdFilename));

		// 3. Get a validator from the schema.
		Validator validator = schema.newValidator();

		// 4. Parse the document you want to check.
		try (Reader docReader = new StringReader(doc)) {
			Source source = new StreamSource(docReader);
			// 5. Check the document
			validator.validate(source);
		}

		return true;
	}

	public static Element child(final Element parent, final String childName, final Namespace ns, final boolean allowNull) {
		return child(parent, childName, ns, format("Expected to find element with name %s as a child of %s", childName, parent.getName()), allowNull);
	}

	/**
	 * Get a child element, raise error message if it doesn't exist.
	 */
	public static Element child(final Element parent, final String childName, final Namespace ns) {
		return child(parent, childName, ns, format("Expected to find element with name %s as a child of %s", childName, parent.getName()), false);
	}

	/**
	 * Get a child element, raise error message if it doesn't exist.
	 */
	public static Element child(final Element parent, final String childName, final Namespace ns, final Object errorMessage, final boolean allowNull) {
		Element child = parent.getChild(childName, ns);
		if (!allowNull && child == null) {
			throw new EvoteException(errorMessage.toString());
		}
		return child;
	}

	/**
	 * Get an attribute value, raise an exception if it doesn't exist.
	 */
	public static String attribute(final Element element, final String attributeId) {
		return attribute(element, attributeId, format("Expected to find attribute %s on element %s", attributeId, element.getName()));
	}

	/**
	 * Get an attribute value, raise an exception if it doesn't exist.
	 */
	public static String attribute(final Element element, final String attributeId, final Object errorMessage) {
		String value = element.getAttributeValue(attributeId);
		if (value == null) {
			throw new EvoteException(errorMessage.toString());
		}
		return value;
	}

	public static String value(final Element element, final boolean allowNull) {
		return value(element, format("Expected value in %s", element.getName()), allowNull);
	}

	/**
	 * Get an element value, raise an exception if it doesn't exist.
	 */
	public static String value(final Element element) {
		return value(element, format("Expected value in %s", element.getName()), false);
	}

	/**
	 * Get an element value, raise an exception if it doesn't exist.
	 */
	public static String value(final Element element, final Object errorMessage, final boolean allowNull) {
		String value = element.getValue();
		if (!allowNull && value == null) {
			throw new EvoteException(errorMessage.toString());
		}
		return value;
	}

	/**
	 * @param document
	 *            the document to represent as bytes
	 * @return the document as an {@value no.evote.constants.EvoteConstants#CHARACTER_SET}-encoded byte-array
	 * @see #documentToString(org.jdom2.Document)
	 */
	public static byte[] documentToBytes(org.jdom2.Document document) {
		try {
			return documentToString(document).getBytes(EvoteConstants.CHARACTER_SET);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("This should never happen, since the encoding is UTF-8", e);
		}
	}

	/**
	 * @param document
	 *            the document to represent as an XML string
	 * @return the document as XML, using {@link org.jdom2.output.Format#getRawFormat()}
	 */
	public static String documentToString(org.jdom2.Document document) {
		XMLOutputter printer = new XMLOutputter();
		Format format = Format.getRawFormat();
		format.setLineSeparator(LineSeparator.NL);
		printer.setFormat(format);

		return printer.outputString(document);
	}

	/**
	 * @param xmlAsBytes
	 *            an XML document represented as a byte array
	 * @return the XML in document form
	 */
	public static org.jdom2.Document bytesToDocument(byte[] xmlAsBytes) {
		try {
			SAXBuilder builder = new SAXBuilder();
			return builder.build(new ByteArrayInputStream(xmlAsBytes));
		} catch (JDOMException | IOException e) {
			throw new RuntimeException("Unable to build document from byte representation", e);
		}
	}

	/**
	 * @param xml
	 *            an XML document represented as a string
	 * @return the XML in document form
	 */
	public static org.jdom2.Document stringToDocument(String xml) {
		return bytesToDocument(xml.getBytes());
	}
}
