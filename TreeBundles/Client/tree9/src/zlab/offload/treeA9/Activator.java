package zlab.offload.treeA9;

import java.util.Dictionary;
import java.util.Hashtable;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import ch.ethz.iks.r_osgi.RemoteOSGiService;
import zlab.offload.treeA9.api.UsePoint9;
import zlab.offload.treeA9.impl.UsePoint9Impl;

public class Activator implements BundleActivator {

	public void start(BundleContext context) throws Exception {
		System.out.println("point9 start");
			
		//register treeA9
		Dictionary props = new Hashtable();
		props.put(RemoteOSGiService.R_OSGi_REGISTRATION, Boolean.TRUE);
		context.registerService(UsePoint9.class.getName(), new UsePoint9Impl(context), props); 		
	}
	
	public void stop(BundleContext context) throws Exception {
		System.out.println("point9 stop");
	}

}
