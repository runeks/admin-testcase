package no.evote.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import no.evote.constants.EvoteConstants;
import no.evote.dto.FileReaderResultsDto;

import org.apache.log4j.Logger;

public class CSVReader {
	private static final String CSV_IMPORT_DELIM = ",";
	private static final String CSV_QUOTE = "\"";
	private static final String CSV_REGEX = "\"([^\"]+?)\",?|([^,]+),?|,";
	private static Pattern compiledPattern = Pattern.compile(CSV_REGEX);

	private static final Logger LOG = Logger.getLogger(CSVReader.class);
	private FileReaderResultsDto fileReaderResults = null;

	public Map<String, String> getRowDataFromFile(final InputStream inStream) {

		InputStreamReader inStreamReader;
		String line;
		Map<String, String> propertyMap = new HashMap<String, String>();
		fileReaderResults = new FileReaderResultsDto();

		try {
			inStreamReader = new InputStreamReader(inStream, EvoteConstants.CHARACTER_SET);
			BufferedReader bufReader = new BufferedReader(inStreamReader);
			while ((line = bufReader.readLine()) != null) {
				LOG.debug("Reading line=" + line);
				regexCSVTokenizing(line, propertyMap);
			}
		} catch (UnsupportedEncodingException uee) {
			LOG.error("CSVReaderService: getPropertyMapFromFile: Charset not supported", uee);
			fileReaderResults.addExceptionMessage(new StringBuffer("Configuration error: Charset not supported"));
		} catch (IOException ioe) {
			LOG.error("CSVReaderService: getPropertyMapFromFile: StringTokenizer failed", ioe);
			fileReaderResults.addExceptionMessage(new StringBuffer("Configuration error: StringTokenizer failed"));
		}

		return propertyMap;
	}

	private void regexCSVTokenizing(final String line, final Map<String, String> propertyMap) {
		Matcher matcher = compiledPattern.matcher(line);
		LOG.debug("Line:" + line);
		List<String> strList = new ArrayList<String>();
		while (matcher.find()) {
			String strElem = matcher.group();

			if (strElem == null) {
				break;
			}
			if (strElem.endsWith(CSV_IMPORT_DELIM)) {
				strElem = strElem.substring(0, strElem.length() - 1);
			}
			strList.add(trimQuote(strElem, CSV_QUOTE));
		}
		LOG.debug("Before trim: Key =<" + strList.get(0) + "> Old value=<" + strList.get(1) + "> New Value= <" + strList.get(2) + ">");
		// CSOFF: MagicNumber
		if (strList.size() == 3) {
			// CSON: MagicNumber
			LOG.debug("Key =<" + strList.get(0) + "> Old value=<" + strList.get(1) + "> New Value= <" + strList.get(2) + ">");
			propertyMap.put(strList.get(0), strList.get(2));
			fileReaderResults.incrementReadEntries();
		} else {
			LOG.error("Illegal data for line(size=" + strList.size() + "): " + line);
			fileReaderResults.incrementNotReadEntries();
		}

	}

	public String trimQuote(final String s, final String quote) {
		int startIndex = 0;
		int endIndex = s.length();
		int qLength = quote.length();

		if (s.startsWith(quote)) {
			startIndex = qLength;
		}
		if (s.endsWith(quote)) {
			endIndex = endIndex - qLength;
		}
		return s.substring(startIndex, endIndex);
	}

	public FileReaderResultsDto getFileReaderResults() {
		return fileReaderResults;
	}
}
