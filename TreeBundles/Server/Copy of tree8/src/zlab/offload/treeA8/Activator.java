package zlab.offload.treeA8;

import java.util.Dictionary;
import java.util.Hashtable;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import ch.ethz.iks.r_osgi.RemoteOSGiService;
import zlab.offload.treeA8.api.UsePoint8;
import zlab.offload.treeA8.impl.UsePoint8Impl;

public class Activator implements BundleActivator {

	public void start(BundleContext context) throws Exception {
		System.out.println("point8 start");
			
		//register treeA8
		Dictionary props = new Hashtable();
		props.put(RemoteOSGiService.R_OSGi_REGISTRATION, Boolean.TRUE);
		context.registerService(UsePoint8.class.getName(), new UsePoint8Impl(context), props); 		
	}
	
	public void stop(BundleContext context) throws Exception {
		System.out.println("point8 stop");
	}

}
