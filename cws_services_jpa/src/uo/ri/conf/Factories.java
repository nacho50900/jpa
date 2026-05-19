package uo.ri.conf;

import uo.ri.cws.application.repository.RepositoryFactory;
import uo.ri.cws.application.service.JpaServicesFactoryImpl;
import uo.ri.cws.application.service.ServiceFactory;
import uo.ri.cws.application.util.command.ComandExecutorFactory;
import uo.ri.cws.infrastructure.persistence.jpa.executor.JpaCommandExecutorFactory;
import uo.ri.cws.infrastructure.persistence.jpa.repository.JpaRepositoryFactory;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;

public class Factories {

	public static RepositoryFactory repository = new JpaRepositoryFactory();
	public static ServiceFactory service = new JpaServicesFactoryImpl();
	public static ComandExecutorFactory executor = new JpaCommandExecutorFactory();
	
	public static void close() {
		Jpa.close();
	}

}
