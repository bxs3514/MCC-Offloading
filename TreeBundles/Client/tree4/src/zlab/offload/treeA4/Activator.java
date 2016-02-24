package zlab.offload.treeA4;

import java.util.Dictionary;
import java.util.Hashtable;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import ch.ethz.iks.r_osgi.RemoteOSGiService;
import zlab.offload.treeA4.api.UsePoint4;
import zlab.offload.treeA4.impl.UsePoint4Impl;

public class Activator implements BundleActivator {

	public void start(BundleContext context) throws Exception {
		System.out.println("point4 start");
			
		//register treeA4
		Dictionary props = new Hashtable();
		props.put(RemoteOSGiService.R_OSGi_REGISTRATION, Boolean.TRUE);
		context.registerService(UsePoint4.class.getName(), new UsePoint4Impl(context), props); 		
	}
	
	public void stop(BundleContext context) throws Exception {
		System.out.println("point4 stop");
	}

}
