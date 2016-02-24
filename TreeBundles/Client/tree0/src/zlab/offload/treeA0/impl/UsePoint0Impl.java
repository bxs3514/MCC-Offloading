package zlab.offload.treeA0.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import ch.ethz.iks.r_osgi.RemoteOSGiService;
import ch.ethz.iks.r_osgi.RemoteServiceReference;
import ch.ethz.iks.r_osgi.URI;
import zlab.offload.treeA0.api.UsePoint0;
import zlab.offload.treeA1.api.UsePoint1;
import zlab.offload.treeA9.api.UsePoint9;

public class UsePoint0Impl implements UsePoint0 {
	
	private BundleContext context;
	private RemoteOSGiService rosgi;
	private UsePoint1 remote1;
	private UsePoint9 remote9;
	private UsePoint1 local1;
	private UsePoint9 local9;
	private int sizeofint1;
	private int sizeofint9;
	private Map offstrategy = new HashMap();
	private final int[] Usedpoint = new int[]{1,9};
	private String ADDRESS = "192.168.100.2:9278";
	
	
	public UsePoint0Impl(BundleContext context) throws BundleException {
		this.context = context;
		for(int i : Usedpoint){
			offstrategy.put(i,Boolean.FALSE);}
		getrosgi();
		getremoteservice();
		getlocalservice();
		
	}

	@Override
	public int[] call(int[] indata, int in, List offpoint) {
		//initialize offload point
		for(int i : Usedpoint){
			if(offpoint.contains(i)) offstrategy.put(i, Boolean.TRUE);
		}		

		int sizeofMB1 = 18;
		int sizeofKB1 = sizeofMB1*1024;
		if(indata.length>=1){sizeofint1 = sizeofKB1*256;}else{sizeofint1= 0;}
		
		int sizeofMB9 = 2;
		int sizeofKB9 = sizeofMB9*1024;
		if(indata.length>=1){sizeofint9 = sizeofKB9*256;}else{sizeofint9= 0;}
		
		int[] datato1 = new int[sizeofint1];
		int[] datato9 = new int[sizeofint9];
		
		int into1 = (int)(in*0.9);
		int into9 = (int)(in*0.1);
		
		//call point1
		if(offstrategy.get(Usedpoint[0]) == Boolean.TRUE){
			System.out.print("point0 call point1.remote...");
			remote1.call(datato1,into1,offpoint);
		}else {
			System.out.print("point0 call point1.mobile...");
			local1.call(datato1,into1,offpoint);
		}
		
		//call point9
		if(offstrategy.get(Usedpoint[1]) == Boolean.TRUE){
			System.out.print("point0 call point9.remote...");
			remote9.call(datato9,into9,offpoint);
		}else {
			System.out.print("point0 call point9.mobile...");
			local9.call(datato9,into9,offpoint);
		}
		
		return null;
	}
	
	
	//get R-OSGi service
	private void getrosgi() throws BundleException {
		ServiceReference rosgiref = context.getServiceReference(RemoteOSGiService.class.getName());
		if (rosgiref == null){throw new BundleException("no R-OSGi found");}
		rosgi = (RemoteOSGiService) context.getService(rosgiref);
	}
	
	private void getremoteservice(){
		new Thread(){
			public void run(){
				try {
					RemoteServiceReference[] remote1refs = rosgi.getRemoteServiceReferences(new URI(ADDRESS), UsePoint1.class.getName(), null);
					RemoteServiceReference[] remote9refs = rosgi.getRemoteServiceReferences(new URI(ADDRESS), UsePoint9.class.getName(), null);
					if (remote1refs == null){System.out.println("no remote point1 found");}
					if (remote9refs == null){System.out.println("no remote point9 found");}
					remote1 = (UsePoint1) rosgi.getRemoteService(remote1refs[0]);
					remote9 = (UsePoint9) rosgi.getRemoteService(remote9refs[0]);
				} catch (Exception e){
					System.out.println("Can't connect for: " + e.toString());
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void getlocalservice(){
		ServiceReference local1ref =context.getServiceReference(UsePoint1.class.getName());
		ServiceReference local9ref =context.getServiceReference(UsePoint9.class.getName());
		if (local1ref == null){System.out.println("no local point1 found");}
		if (local9ref == null){System.out.println("no local point9 found");}
		local1 = (UsePoint1) context.getService(local1ref);
		local9 = (UsePoint9) context.getService(local9ref);
	}
	
	
}
