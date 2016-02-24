package zlab.offload.treeA5.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import ch.ethz.iks.r_osgi.RemoteOSGiService;
import ch.ethz.iks.r_osgi.RemoteServiceReference;
import ch.ethz.iks.r_osgi.URI;
import zlab.offload.treeA5.api.UsePoint5;
import zlab.offload.treeA6.api.UsePoint6;

public class UsePoint5Impl implements UsePoint5 {
	
	private BundleContext context;
	private RemoteOSGiService rosgi;
	private UsePoint6 remote6;
	private UsePoint6 local6;
	private int sizeofint;
	private Map offstrategy = new HashMap();
	private final int[] Usedpoint = new int[]{6};
	private final String ADDRESS = "192.168.100.2:9278";
	
	
	public UsePoint5Impl(BundleContext context) throws BundleException {
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
		
		//cost time
		costtime((int)(0.6*in),5);
		
		int sizeofMB = 1;
		int sizeofKB = sizeofMB*1024;
		if(indata.length>=1){sizeofint = sizeofKB*256;}else{sizeofint= 0;}
		
		int[] datato6 = new int[sizeofint];
	
		int into6 = (int)(0.4*in);
		
		//call point6
		if(offstrategy.get(Usedpoint[0]) == Boolean.TRUE){
			System.out.print("point5 call point6.remote...");
			remote6.call(datato6,into6,offpoint);
		}else {
			System.out.print("point5 call point6.mobile...");
			local6.call(datato6,into6,offpoint);
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
					RemoteServiceReference[] remote6refs = rosgi.getRemoteServiceReferences(new URI(ADDRESS), UsePoint6.class.getName(), null);					
					if (remote6refs == null){System.out.println("no remote point6 found");}
					remote6 = (UsePoint6) rosgi.getRemoteService(remote6refs[0]);
				} catch (Exception e){
					System.out.println("Can't connect for: " + e.toString());
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void getlocalservice(){
		ServiceReference local6ref =context.getServiceReference(UsePoint6.class.getName());
		if (local6ref == null){System.out.println("no local point6 found");}
		local6 = (UsePoint6) context.getService(local6ref);
	}
	
	private void costtime(int ms, int point) {
		long startTime = System.currentTimeMillis();
		
		for(int j=0;j<ms;j++){
			for(long i=1; i<Integer.MAX_VALUE/900; i++){
			}
		}
		
		System.out.println("point" + point + " cost time["+ ms+"]" +":"+ (System.currentTimeMillis() - startTime) + "ms");
		
	}
}
