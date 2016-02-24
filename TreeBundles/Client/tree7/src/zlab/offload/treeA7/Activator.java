package zlab.offload.treeA7;

import java.util.Dictionary;
import java.util.Hashtable;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import ch.ethz.iks.r_osgi.RemoteOSGiService;
import zlab.offload.treeA7.api.UsePoint7;
import zlab.offload.treeA7.impl.UsePoint7Impl;

public class Activator implements BundleActivator {

	public void start(BundleContext context) throws Exception {
		System.out.println("point7 start");
			
		//register treeA7
		Dictionary props = new Hashtable();
		props.put(RemoteOSGiService.R_OSGi_REGISTRATION, Boolean.TRUE);
		context.registerService(UsePoint7.class.getName(), new UsePoint7Impl(context), props); 		
	}
	
	public void stop(BundleContext context) throws Exception {
		System.out.println("point7 stop");
	}

}
