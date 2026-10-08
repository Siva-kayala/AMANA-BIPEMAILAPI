package com.finonyx.database;
 

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
 
public class DatabasePRD1
{
	private static DataSource dataSourceprd1;
private static final String JNDI_LOOKUP_SERVICE = "BIPEMAILAPI";
static {
	try {

		Context context = new InitialContext();
		Object lookup = context.lookup(JNDI_LOOKUP_SERVICE);
		if (lookup != null) {
			dataSourceprd1 = (DataSource) lookup;

		} else {
			new RuntimeException("DataSource not found!!!");
		}
	} catch (NamingException e) {
		//e.printStackTrace();
		//info("DataSource error:"+e.getMessage());
	}
}

public static DataSource getDataSource() {
	/*info("getDataSource");*/
	return dataSourceprd1;
}

/*private static void info(String line) {

		webServiceLogger.log.info("INFO->" + line);

	}*/
}
 

