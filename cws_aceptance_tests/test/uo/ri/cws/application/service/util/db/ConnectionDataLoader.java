package uo.ri.cws.application.service.util.db;

public class ConnectionDataLoader {

	public static class ConnectionData {
		public String url;
		public String driver;
		public String user;
		public String pass;
	}
	
	public static ConnectionData getConnectionData() {
		return isJpaPresent() 
				? forJpa() 
				: forJdbc();
	}

	private static ConnectionData forJpa() {
		return PersistenceXmlScanner.scan();
	}

	private static ConnectionData forJdbc() {
		ConnectionData res = new ConnectionData();
////		res.url = "jdbc:hsqldb:jdbc:hsqldb:hsql://localhost";
//		res.url = "jdbc:hsqldb:file:./data/test;files_readonly=true";
//		res.driver = "org.hsqldb.jdbcDriver";
//		res.user = "sa";
//		res.pass = "";
		
		res.url = JdbcConf.getProperty("DB_URL").trim();
		res.driver = JdbcConf.getProperty("DB_DRIVER").trim();
		res.user = JdbcConf.getProperty("DB_USER").trim();
		res.pass = JdbcConf.getProperty("DB_PASS").trim();
		return res;
	}
	
    public static boolean isJpaPresent() {
        try {
            Class.forName("jakarta.persistence.Entity");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
