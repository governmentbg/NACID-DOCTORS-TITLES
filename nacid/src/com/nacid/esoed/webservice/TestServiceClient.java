package com.nacid.esoed.webservice;


import javax.xml.namespace.QName;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.log4j.Logger;


/**
 * @author bily
 *
 */
public class TestServiceClient {
	
	private static Logger log = Logger.getLogger( TestServiceClient.class );

	/**
	 * @param args
	 */

		// TODO Auto-generated method stub
	 public static void main(String[] args1) throws AxisFault{
		 
		 TestServiceClient client = new TestServiceClient();
		 client.invokeService();
		 

     }
	    
	 public void invokeService() throws AxisFault  {
		 
		 RPCServiceClient serviceClient = new RPCServiceClient();

	        Options options = serviceClient.getOptions();

	        EndpointReference targetEPR = new EndpointReference("http://localhost:8080/dms/services/TestService");
	        options.setTo(targetEPR);
	        // QName of the target method 
	        QName opAddEntry = new QName("http://webservice.dms.sag.org", "findBooks");
	        // Constructing the arguments array for the method invocation
	        String[] opAddEntryArgs = new String[] { new String ("<Test/>") };
	        Class[] returnTypes = new Class[] { String.class };
	        // Invoking the method
	        //serviceClient.invokeRobust(opAddEntry, opAddEntryArgs);
	        log.debug("Result of TestService invoke = " + serviceClient.invokeBlocking(opAddEntry, opAddEntryArgs, returnTypes));

	 }

}
