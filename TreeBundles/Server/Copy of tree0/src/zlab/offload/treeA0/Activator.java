package zlab.offload.treeA0;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import ch.ethz.iks.r_osgi.RemoteOSGiService;
import zlab.offload.treeA0.api.UsePoint0;
import zlab.offload.treeA0.impl.UsePoint0Impl;

public class Activator implements BundleActivator {

	public void start(BundleContext context) throws Exception {
		System.out.println("point0 start");

		//test inial
		UsePoint0Impl test =new UsePoint0Impl(context);
		
		//register treeA0
		Dictionary props = new Hashtable();
		props.put(RemoteOSGiService.R_OSGi_REGISTRATION, Boolean.TRUE);
		context.registerService(UsePoint0.class.getName(), test, props);
		
		//test
		int[] datatoall =new int[0];
		int time = 500;
		List offpoint =new ArrayList();
		
		
		long startTime = System.currentTimeMillis();
		test.call(datatoall, time, offpoint);
		System.out.println("App["+time+"] cost time"+ ":"+ (System.currentTimeMillis() - startTime) + "ms");
	}
	
	public void stop(BundleContext context) throws Exception {
		System.out.println("point0 stop");
	}

}
