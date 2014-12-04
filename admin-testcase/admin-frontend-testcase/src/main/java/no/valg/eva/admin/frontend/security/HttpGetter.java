package no.valg.eva.admin.frontend.security;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.log4j.Logger;

public class HttpGetter {
	private static final int MAX_CONNECTIONS_PER_HOST = 20;
	private final Logger logger = Logger.getLogger(HttpGetter.class);
	
	private HttpClient httpClient;
	private MultiThreadedHttpConnectionManager httpConnectionManager;

	public HttpGetter() {
		httpConnectionManager = new MultiThreadedHttpConnectionManager();
		HttpConnectionManagerParams params = new HttpConnectionManagerParams();
		params.setDefaultMaxConnectionsPerHost(MAX_CONNECTIONS_PER_HOST);
		httpConnectionManager.setParams(params);

		httpClient = new HttpClient(httpConnectionManager);
	}

	public byte[] getResponse(final String url) throws IOException {
		HttpMethod httpMethod = new GetMethod(url);
		
		try {
			int responseCode = httpClient.executeMethod(httpMethod);

			if (responseCode != HttpServletResponse.SC_OK) {
				throw new RuntimeException("Got response " + responseCode + " from " + url);
			}

			byte[] responseBody = httpMethod.getResponseBody();
			httpMethod.releaseConnection();

			return responseBody;
		} catch (RuntimeException e) {
			logger.error("Unable to connect to " + url, e);
			throw e;
		}
	}

	public void close() {
		httpConnectionManager.shutdown();
	}
}
