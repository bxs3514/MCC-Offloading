package zlab.offload.treeA6;

import java.util.Dictionary;
import java.util.Hashtable;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import ch.ethz.iks.r_osgi.RemoteOSGiService;
import zlab.offload.treeA6.api.UsePoint6;
import zlab.offload.treeA6.impl.UsePoint6Impl;

public class Activator implements BundleActivator {

	public void start(BundleContext context) throws Exception {
		System.out.println("point6 start");
			
		//register treeA6
		Dictionary props = new Hashtable();
		props.put(RemoteOSGiService.R_OSGi_REGISTRATION, Boolean.TRUE);
		context.registerService(UsePoint6.class.getName(), new UsePoint6Impl(context), props); 		
	}
	
	public void stop(BundleContext context) throws Exception {
		System.out.println("point6 stop");
	}

}
