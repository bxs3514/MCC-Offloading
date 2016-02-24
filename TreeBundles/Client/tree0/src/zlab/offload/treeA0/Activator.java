package zlab.offload.treeA0;

import java.util.ArrayList;
import java.util.Arrays;
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

		int sizeofMB = 20;
		int sizeofKB = sizeofMB*1024;
		int sizeofint = sizeofKB*256;
		
		
		int[] datatoall = new int[sizeofint];
		int time = 1000;
		List offpoint =new ArrayList();
		offpoint.add(2);
		offpoint.add(4);
		//List offpoint =new ArrayList(Arrays.asList(1,9));
		System.out.println("time 1000 start");
		for(int p=0;p<=20;p++){
			System.out.println("point0 start");
			long startTime = System.currentTimeMillis();
			test.call(datatoall, time, offpoint);
			System.out.println("App["+time+"] cost time"+ ":"+ (System.currentTimeMillis() - startTime) + "ms");
			System.out.println("point0 stop");
		}
		
		/**time = 200;
		System.out.println("time 200 start");
		for(int p=0;p<=20;p++){
			System.out.println("point0 start");
			long startTime = System.currentTimeMillis();
			test.call(datatoall, time, offpoint);
			System.out.println();
			System.out.println("App["+time+"] cost time"+ ":"+ (System.currentTimeMillis() - startTime) + "ms");
			System.out.println("point0 stop");
			System.out.println();
		}
		
		time = 500;
		System.out.println("time 500 start");
		for(int p=0;p<=20;p++){
			System.out.println("point0 start");
			long startTime = System.currentTimeMillis();
			test.call(datatoall, time, offpoint);
			System.out.println();
			System.out.println("App["+time+"] cost time"+ ":"+ (System.currentTimeMillis() - startTime) + "ms");
			System.out.println("point0 stop");
			System.out.println();
		}
		
		time = 1000;
		System.out.println("time 1000 start");
		for(int p=0;p<=20;p++){
			System.out.println("point0 start");
			long startTime = System.currentTimeMillis();
			test.call(datatoall, time, offpoint);
			System.out.println();
			System.out.println("App["+time+"] cost time"+ ":"+ (System.currentTimeMillis() - startTime) + "ms");
			System.out.println("point0 stop");
			System.out.println();
			
		}**/
		

	}
	
	public void stop(BundleContext context) throws Exception {
		System.out.println("point0 stop");
	}

}
