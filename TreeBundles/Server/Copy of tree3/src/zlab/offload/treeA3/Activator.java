package zlab.offload.treeA3;

import java.util.Dictionary;
import java.util.Hashtable;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import ch.ethz.iks.r_osgi.RemoteOSGiService;
import zlab.offload.treeA3.api.UsePoint3;
import zlab.offload.treeA3.impl.UsePoint3Impl;

public class Activator implements BundleActivator {

	public void start(BundleContext context) throws Exception {
		System.out.println("point3 start");
			
		//register treeA3
		Dictionary props = new Hashtable();
		props.put(RemoteOSGiService.R_OSGi_REGISTRATION, Boolean.TRUE);
		context.registerService(UsePoint3.class.getName(), new UsePoint3Impl(context), props); 		
	}
	
	public void stop(BundleContext context) throws Exception {
		System.out.println("point3 stop");
	}

}
