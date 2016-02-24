package zlab.offload.treeA5;

import java.util.Dictionary;
import java.util.Hashtable;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import ch.ethz.iks.r_osgi.RemoteOSGiService;
import zlab.offload.treeA5.api.UsePoint5;
import zlab.offload.treeA5.impl.UsePoint5Impl;

public class Activator implements BundleActivator {

	public void start(BundleContext context) throws Exception {
		System.out.println("point5 start");
			
		//register treeA5
		Dictionary props = new Hashtable();
		props.put(RemoteOSGiService.R_OSGi_REGISTRATION, Boolean.TRUE);
		context.registerService(UsePoint5.class.getName(), new UsePoint5Impl(context), props); 		
	}
	
	public void stop(BundleContext context) throws Exception {
		System.out.println("point5 stop");
	}

}
