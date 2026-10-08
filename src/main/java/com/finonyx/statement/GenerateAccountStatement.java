package com.finonyx.statement;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.finonyx.log.webServiceLogger;

import sun.misc.BASE64Decoder;

public class GenerateAccountStatement {
	private void info(String line) {
		webServiceLogger.log.info("INFO->" + line);
	}

	public void runReport(Properties prop, String ACC, String FROMDT,
			String TODT, String filename, String Report_name)
					throws Exception {
		boolean flag = false;
		Map<String, String> status = new HashMap<String, String>();
		final Logger logger = Logger.getLogger(GenerateAccountStatement.class);
		try {
			final String bipEndpoint = "http://" + prop.getProperty("BIPIP")
			+ ":" + prop.getProperty("BIPPORT")
		       + "/xmlpserver/services/PublicReportService?wsdl";
			
			final String SOAPAction = "http://xmlns.oracle.com/oxp/service/PublicReportService/runReport";
			final String  SOAPURL = bipEndpoint;
			//logger.info("SAOP URL " + SOAPURL);
			String SOAPResponse ="";

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("MM-dd-yyyy");
			Date d = dateFormat.parse(FROMDT);
			FROMDT = dateFormat1.format(d);
	        Date d1 = dateFormat.parse(TODT);
	        TODT = dateFormat1.format(d1);
			String xdofile = "";
			xdofile = prop.getProperty("ACCSTMTABSPATH");
			
			URL url = null;

			url = new URL(SOAPURL);

			URLConnection connection = null;

			connection = url.openConnection();

			HttpURLConnection httpConn = (HttpURLConnection) connection;
			String reqMsg ="";

			reqMsg = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:pub=\"http://xmlns.oracle.com/oxp/service/PublicReportService\">\r\n"
					  +"<soapenv:Header/>\r\n"
					   +"<soapenv:Body>\r\n"
					      +"<pub:runReport>\r\n"
					        +"<pub:reportRequest>\r\n"
					           +"<pub:attributeFormat>pdf</pub:attributeFormat>\r\n"
					            +"<pub:attributeLocale>en-US</pub:attributeLocale>\r\n"
					          + "<pub:attributeTemplate>Simple</pub:attributeTemplate>\r\n"
					           +"<pub:parameterNameValues>\r\n"
					           +"<pub:item>\r\n"
					             +"<pub:name>pm_account</pub:name>\r\n"
					              +"<pub:values>\r\n"
					                +"<pub:item>"+ACC+"</pub:item>\r\n"
					             +"</pub:values>\r\n"
					         +"</pub:item>\r\n"
					           +"<pub:item>\r\n"
					           +"<pub:name>pm_from_date</pub:name>\r\n"
					            +"<pub:values>\r\n"
					              +"<pub:item>"+FROMDT+"</pub:item>\r\n"
					            +"</pub:values>\r\n"
					           +"</pub:item>\r\n"
					           +"<pub:item>\r\n"
					             +"<pub:name>pm_to_date</pub:name>\r\n"
					               +"<pub:values>\r\n"
					              + "<pub:item>"+TODT+"</pub:item>\r\n"
					            +"</pub:values>\r\n"
					           +"</pub:item>\r\n"
					           +"</pub:parameterNameValues>\r\n"
					           +"<pub:reportAbsolutePath>"+xdofile+"</pub:reportAbsolutePath>\r\n"
					       + "</pub:reportRequest>\r\n"
					       + "<pub:userID>"+prop.getProperty("BIPUSERNAME")+"</pub:userID>\r\n"
					       + "<pub:password>"+prop.getProperty("BIPPASSWORD")+"</pub:password>\r\n"
					    + "</pub:runReport>\r\n"
					  +"</soapenv:Body>\r\n"
					+"</soapenv:Envelope>";
         
			//logger.info("request " + reqMsg);

			InputStream xin = new ByteArrayInputStream(reqMsg.getBytes());

			ByteArrayOutputStream bout = new ByteArrayOutputStream();

			copy(xin, bout);

			xin.close();


			byte[] b = bout.toByteArray();

		

			httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
			httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			httpConn.setRequestProperty("SOAPAction", SOAPAction);
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);

			OutputStream out = null;
			InputStreamReader isr = null;
			String inputLine;

			out = httpConn.getOutputStream();

			out.write(b);

			out.close();
			
			logger.info("ResponseCode----"+httpConn.getResponseCode());
			logger.info("ResponseMessage----"+httpConn.getResponseMessage());

			
			isr = new InputStreamReader(httpConn.getInputStream());

			BufferedReader in = new BufferedReader(isr);
			while ((inputLine = in.readLine()) != null) {
				SOAPResponse += inputLine;
			}

			in.close();


			String SOAPResponse1 = SOAPResponse.substring(SOAPResponse.indexOf("<reportBytes>")+13, SOAPResponse.indexOf("</reportBytes>"));

			byte[] binaryBytes = null;
			binaryBytes = new BASE64Decoder().decodeBuffer(SOAPResponse1);


			filename = filename.replaceAll("/", "-");
			String outputPath = prop.getProperty("STATEMENTFOLDER") + filename;
			OutputStream out1 = new FileOutputStream(outputPath);
			out1.write(binaryBytes);
			out1.close();
			//info("Success for Run Report");
			

		} catch (Exception e) {
			//info("catch "+e);
			status.put("errormsg", e.getMessage());
			logger.info("Exception at Generating Report " + e.getMessage());
			flag = true;
			
		}
	}
	public static void copy(InputStream in, OutputStream out) throws IOException {

		synchronized (in) {
			synchronized (out) {

				byte[] buffer = new byte[256];
				while (true) {
					int bytesRead = in.read(buffer);
					if (bytesRead == -1)
						break;
					out.write(buffer, 0, bytesRead);
				}
			}
		}
	}

}
