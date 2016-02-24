package zlab.offload.treeA2.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import ch.ethz.iks.r_osgi.RemoteOSGiService;
import ch.ethz.iks.r_osgi.RemoteServiceReference;
import ch.ethz.iks.r_osgi.URI;
import zlab.offload.treeA2.api.UsePoint2;
import zlab.offload.treeA3.api.UsePoint3;

public class UsePoint2Impl implements UsePoint2 {
	
	private BundleContext context;
	private RemoteOSGiService rosgi;
	private UsePoint3 remote3;
	private UsePoint3 local3;
	private int sizeofint;
	private Map offstrategy = new HashMap();
	private final int[] Usedpoint = new int[]{3};
	private final String ADDRESS = "192.168.100.2:9278";
	
	
	public UsePoint2Impl(BundleContext context) throws BundleException {
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
		costtime((int)(0.4*in),2);
		
		int sizeofMB = 3;
		int sizeofKB = sizeofMB*1024;
		if(indata.length>=1){sizeofint = sizeofKB*256;}else{sizeofint= 0;}
		
		int[] datato3 = new int[sizeofint];
		int into3 = (int)(0.6*in);

		
		//call point3
		if(offstrategy.get(Usedpoint[0]) == Boolean.TRUE){
			System.out.print("point2 call point3.remote...");
			remote3.call(datato3,into3,offpoint);
		}else {
			System.out.print("point2 call point3.mobile...");
			local3.call(datato3,into3,offpoint);
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
					RemoteServiceReference[] remote3refs = rosgi.getRemoteServiceReferences(new URI(ADDRESS), UsePoint3.class.getName(), null);
					if (remote3refs == null){System.out.println("no remote point3 found");}
					remote3 = (UsePoint3) rosgi.getRemoteService(remote3refs[0]);
				} catch (Exception e){
					System.out.println("Can't connect for: " + e.toString());
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void getlocalservice(){
		ServiceReference local3ref =context.getServiceReference(UsePoint3.class.getName());
		if (local3ref == null){System.out.println("no local point3 found");}
		local3 = (UsePoint3) context.getService(local3ref);
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
