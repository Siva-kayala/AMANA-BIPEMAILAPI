package com.finonyx.smsservice;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.json.JSONObject;

import com.finonyx.database.DatabasePRD1;
import com.finonyx.log.webServiceLogger;
import com.finonyx.statement.GenerateAccountStatement;


public class AccessJavaEmail1 {

	Session mailSession;
	final Logger logger = Logger.getLogger(AccessJavaEmail1.class);
	String account_number = "";
	String filename = "";


	public void setMailServerProperties1() {
		Properties emailProperties = System.getProperties();
		emailProperties.put("mail.smtp.auth", "false");
		emailProperties.put("mail.smtp.starttls.enable", "false");
		emailProperties.put("mail.smtp.host", "10.0.5.4");
		emailProperties.put("mail.smtp.port", 25);
		mailSession = Session.getDefaultInstance(emailProperties,null);

	}

	public void sendEmail1(JSONObject Details) throws Exception {

		InputStream in = this.getClass().getClassLoader().getResourceAsStream("config.properties");
		Properties prop = null;
		prop = new Properties();
		prop.load(in);
		boolean flag = false;
		String[] toEmails = { Details.getString("EMAIL") };
		String emailSubject = Details.getString("SUB");
		String emailBody = "";
		String ID=Details.getString("ID");
		
		//logger.info("Request  " + Details.toString());

		String url="";
		Map<String, String> status = new HashMap<String, String>();
		MimeMessage emailMessage = new MimeMessage(mailSession);
		PreparedStatement pstmt = null;
		Connection conn = null;
		PreparedStatement pstmt1 = null;
		Connection conn1 = null;
		int count = 0;
		out3: if (count == 0) {
			try {


				String fromUser = "Amana Bank";
				String username = "ebanking@amanabank.co.tz";
				String fromUserEmailPassword = "Ama3banking31";
				String Event = "";
			
				MimeMultipart contentPart = new MimeMultipart("related");
				MimeBodyPart attachmentPart1 = new MimeBodyPart();
				MimeBodyPart bodyHtml = new MimeBodyPart();
				if (Details.has("EVENT")) {

					Event = Details.getString("EVENT");
				}
				if(Event.equals("DLYSTMT") || Event.equals("MONSTMT") || Event.equals("WKLYSTMT"))
				{
					  
	                    String from_date = "";
	                    String to_date = "";
	                    final String Report_name = "";
	                    String acc = "";



					if (Details.has("FLD1")) {

						account_number   = Details.getString("FLD1");

					}
					if (Details.has("FLD2")) {

						from_date = Details.getString("FLD2");
					}
					if (Details.has("FLD3")) {
						to_date = Details.getString("FLD3");

					}
					 acc = account_number.replace(account_number.substring(3, 7), "XXXX");
	                    filename = Details.getString("ID")+"_"+String.valueOf(acc) + ".pdf";
	                   
	                    
	                    final GenerateAccountStatement gns = new GenerateAccountStatement();

	                    try {
	                         gns.runReport(prop, account_number, from_date, to_date, filename, Report_name);
	                        encryptPdf(prop.getProperty("STATEMENTFOLDER")+ filename);
	                    }
	                   

				    catch (Exception e) {
				    	
						status.put("errormsg", e.getMessage());
						logger.info("Error at calling runReport"+ e.getMessage());
						/*status.put("place", "Mail Template Path");*/
						flag = true;
						count++;
						break out3;
					}
				}


				emailBody = new String(Files.readAllBytes(
						Paths.get("//u01//AMS//htm_files//"+ Details.getString("FILE") + ".html")));



				if (emailBody != null) {

					if (Details.has("FLD1")) {
						try {
							if (emailBody.indexOf("^" + "FLD1" + "^") > 0) {
								emailBody = emailBody.replace("^" + "FLD1" + "^", Details.getString("FLD1"));
							}
						} catch (Exception e) {
							status.put("errormsg", e.getMessage());
							/*status.put("place", "Name");*/
							flag = true;
							count++;
							break out3;
						}
					}


					if (Details.has("FLD2")) {
						try {
							if (emailBody.indexOf("^" + "FLD2" + "^") > 0) {
								emailBody = emailBody.replace("^" + "FLD2" + "^", Details.getString("FLD2"));
							}
						} catch (Exception e) {
							status.put("errormsg", e.getMessage());
							/*status.put("place", "Name");*/
							flag = true;
							count++;
							break out3;
						}
					}


					if (Details.has("FLD3")) {
						try {
							if (emailBody.indexOf("^" + "FLD3" + "^") > 0) {
								emailBody = emailBody.replace("^" + "FLD3" + "^", Details.getString("FLD3"));
							}
						} catch (Exception e) {
							status.put("errormsg", e.getMessage());
							/*status.put("place", "Name");*/
							flag = true;
							count++;
							break out3;
						}
					}


					if (Details.has("FLD4")) {
						try {
							if (emailBody.indexOf("^" + "FLD4" + "^") > 0) {
								emailBody = emailBody.replace("^" + "FLD4" + "^", Details.getString("FLD4"));
							}
						} catch (Exception e) {
							status.put("errormsg", e.getMessage());
							/*status.put("place", "Name");*/
							flag = true;
							count++;
							break out3;
						}
					}


					if (Details.has("FLD5")) {
						try {
							if (emailBody.indexOf("^" + "FLD5" + "^") > 0) {
								emailBody = emailBody.replace("^" + "FLD5" + "^", Details.getString("FLD5"));
							}
						} catch (Exception e) {
							status.put("errormsg", e.getMessage());
							/*status.put("place", "Name");*/
							flag = true;
							count++;
							break out3;
						}
					}
					if (Details.has("FLD6")) {
						try {
							if (emailBody.indexOf("^" + "FLD6" + "^") > 0) {
								emailBody = emailBody.replace("^" + "FLD6" + "^", Details.getString("FLD6"));
							}
						} catch (Exception e) {
							status.put("errormsg", e.getMessage());
							/*status.put("place", "Name");*/
							flag = true;
							count++;
							break out3;
						}
					}
					if (Details.has("FLD7")) {
						try {
							if (emailBody.indexOf("^" + "FLD7" + "^") > 0) {
								emailBody = emailBody.replace("^" + "FLD7" + "^", Details.getString("FLD7"));
							}
						} catch (Exception e) {
							status.put("errormsg", e.getMessage());
							/*status.put("place", "Name");*/
							flag = true;
							count++;
							break out3;
						}
					}


					if(Event.equals("DLYSTMT") || Event.equals("MONSTMT") || Event.equals("WKLYSTMT"))
					{

						bodyHtml.setContent(emailBody, "text/html");
						contentPart.addBodyPart(bodyHtml);
						emailMessage.setContent(contentPart);
						emailMessage.saveChanges();
						final String attachementFile = String.valueOf(prop.getProperty("STATEMENTFOLDER")) + filename;
						attachmentPart1.attachFile(attachementFile);
						attachmentPart1.setFileName(filename);
						contentPart.addBodyPart(attachmentPart1);
					//	logger.info("Inside attachment Block");
						
					
					}
					else {
						emailMessage.setContent(emailBody, "text/html; charset=UTF-8");
					}
				}

				
				 for (int i = 0; i < toEmails.length; ++i) {
	                    emailMessage.addRecipient(Message.RecipientType.TO, (Address)new InternetAddress(toEmails[i]));
	                }
	                emailMessage.setFrom((Address)new InternetAddress(username, fromUser));
	                emailMessage.setSubject(emailSubject,"UTF-8");
	                emailMessage.saveChanges();
	                
	               // logger.info("emailSubject " + emailSubject);
	               
	              
				try {

					 final String emailHost = "10.0.5.4";
							Transport transport = mailSession.getTransport("smtp");
					//transport.connect(emailHost, username, fromUserEmailPassword);
					transport.connect(emailHost,username,fromUserEmailPassword);
					transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
					transport.close();	
				//logger.info("Email sent Successfully");

				} catch (Exception e) {
					logger.info("EMAIL:" + Details.getString("EMAIL") + "ID:" + Details.getString("ID") + "Status:Failure");
					logger.info("SMTP Transport protocol Error Message:" + e.getMessage());
					status.put("errormsg", e.getMessage());
					flag = true;
					count++;
					break out3;

				}


			}

			catch (Exception fnf) {
				logger.info("ID: " + Details.getString("ID") + "EMAIL: " +Details.getString("EMAIL") +  "Mail Exception:" +fnf.getMessage());
				status.put("errormsg", fnf.getMessage());
				flag = true;

			}


		}


		

		if(flag) {
			
			String Sql = " update  TB_STMT_ALERT_LOG  set EMAIL_STATUS =?,EMAIL_FAIL_REASON =?  where ID =?";
			
			try
			{	
					conn1 = DatabasePRD1.getDataSource().getConnection();
					pstmt1 = conn1.prepareStatement(Sql);
					pstmt1.setString(1,"F");
					pstmt1.setString(2,status.get("errormsg"));
					pstmt1.setBigDecimal(3,new BigDecimal(Details.getString("ID")));   
					int i = pstmt1.executeUpdate();  
					pstmt1.close();
					conn1.close();
					
				}
				
			catch (Exception e) {
				logger.info("ID: " + Details.getString("ID")  + "Exception:" +e.getMessage());
				if(pstmt1 != null)
				{
					pstmt1.close();
				}
				if(conn1 != null)
				{
					conn1.close();
				}

			}

			finally {
				
				if(pstmt1 != null)
				{
					pstmt1.close();
				}
				if(conn1 != null)
				{
					conn1.close();
				}
				
			}

		}
		}
	


	private void encryptPdf(String source) {
		try {
			String pdfFilePath = source;
			String password=account_number.substring(9, 15);

			File f = new File(source);
			PDDocument pdd = PDDocument.load(f);

			AccessPermission ap = new AccessPermission();

			// step 3. Creating instance of
			// StandardProtectionPolicy
			StandardProtectionPolicy stpp
			= new StandardProtectionPolicy(password, password, ap);

			// step 4. Setting the length of Encryption key
			stpp.setEncryptionKeyLength(128);

			// step 5. Setting the permission
			stpp.setPermissions(ap);

			// step 6. Protecting the PDF file
			pdd.protect(stpp);

			// step 7. Saving and closing the the PDF Document
			pdd.save(source);
			pdd.close();



		} catch (Throwable e) {
			e.printStackTrace();
			logger.info("Encryption Exception"+e.getMessage());
		}
		
	}

	private void info(String line) {

		webServiceLogger.log.info("INFO->" + line);

	}
}
