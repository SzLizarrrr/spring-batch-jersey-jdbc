package hello.cxf;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CxfClient {
	
	/*** function to set log display level **/
	public static void setLoggingLevel(Level level) {
		Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		root.setLevel(level);
	}

	public static void main(String[] args) throws Exception {

		setLoggingLevel(Level.INFO);

		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		Client client = dcf.createClient("http://localhost:8080/soap/user?wsdl");
		Object[] objects;
		objects = client.invoke("getUser", 10002L);
		log.info("{}", objects[0].getClass());
		log.info("{}", objects[0].toString());

	}

}
