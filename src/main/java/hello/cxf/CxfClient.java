package hello.cxf;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

public class CxfClient {

	public static void main(String[] args) throws Exception {
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		Client client = dcf.createClient("http://localhost:8080/soap/user?wsdl");
		Object[] objects;
		objects = client.invoke("getUser", 10002L);
		System.out.println(objects[0].getClass());
		System.out.println(objects[0].toString());
	}

}