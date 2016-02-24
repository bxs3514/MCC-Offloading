package zlab.offload.treeA1;

import java.util.Dictionary;
import java.util.Hashtable;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import ch.ethz.iks.r_osgi.RemoteOSGiService;
import zlab.offload.treeA1.api.UsePoint1;
import zlab.offload.treeA1.impl.UsePoint1Impl;

public class Activator implements BundleActivator {

	public void start(BundleContext context) throws Exception {
		System.out.println("point1 start");
			
		//register treeA1
		Dictionary props = new Hashtable();
		props.put(RemoteOSGiService.R_OSGi_REGISTRATION, Boolean.TRUE);
		context.registerService(UsePoint1.class.getName(), new UsePoint1Impl(context), props); 		
	}
	
	public void stop(BundleContext context) throws Exception {
		System.out.println("point1 stop");
	}

}
