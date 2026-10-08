package com.finonyx.api;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import com.finonyx.log.webServiceLogger;
import com.finonyx.smsservice.AccessJavaEmail1;

@Path("/v1")
public class AccesssmsEmailWebservice1 {
	
	@POST
	@Path("/STMT")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String email1(String name,@Context HttpServletRequest requestContext) throws JSONException, AddressException {
		//info("Request Enters");
		JSONObject obj = null;
		try {
			JSONObject obj1 = new JSONObject(name);		
			//info("API CALLER IP:"+requestContext.getRemoteAddr()+"MAILID:"+obj1.getString("MAILID")+"AC_ENTRY_SR_NO:"+obj1.getString("AC_ENTRY_SR_NO"));
			new Thread(new AccessEmailThread1(obj1)).start();
			obj = new JSONObject();
			obj.put("status", "Success");
		} catch (Exception e) {
			obj = new JSONObject();
			obj.put("status", "Failure");
			info("Json Failure Request Message:"+name);
		}
		return obj.toString();

	}
	private void info(String line) {

		webServiceLogger.log.info("INFO->" + line);

	}

}

class AccessEmailThread1 implements Runnable {

	private JSONObject Details;

	public AccessEmailThread1(JSONObject Details) throws JSONException {
		this.Details = Details;

	}

	public void run() {

		AccessJavaEmail1 javaEmail1 = new AccessJavaEmail1();
		try {
			
			javaEmail1.setMailServerProperties1();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			synchronized (javaEmail1) {
				javaEmail1.wait(1000);
				javaEmail1.sendEmail1(Details);
				}
		} catch (MessagingException e) {

			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
