package zlab.offload.treeA2;

import java.util.Dictionary;
import java.util.Hashtable;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import ch.ethz.iks.r_osgi.RemoteOSGiService;
import zlab.offload.treeA2.api.UsePoint2;
import zlab.offload.treeA2.impl.UsePoint2Impl;

public class Activator implements BundleActivator {

	public void start(BundleContext context) throws Exception {
		System.out.println("point2 start");
			
		//register treeA2
		Dictionary props = new Hashtable();
		props.put(RemoteOSGiService.R_OSGi_REGISTRATION, Boolean.TRUE);
		context.registerService(UsePoint2.class.getName(), new UsePoint2Impl(context), props); 		
	}
	
	public void stop(BundleContext context) throws Exception {
		System.out.println("point2 stop");
	}

}
