package zlab.offload.treeA7.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import ch.ethz.iks.r_osgi.RemoteOSGiService;
import ch.ethz.iks.r_osgi.RemoteServiceReference;
import ch.ethz.iks.r_osgi.URI;
import zlab.offload.treeA7.api.UsePoint7;
import zlab.offload.treeA8.api.UsePoint8;

public class UsePoint7Impl implements UsePoint7 {
	
	private BundleContext context;
	private RemoteOSGiService rosgi;
	private UsePoint8 remote8;
	private UsePoint8 local8;
	private int sizeofint;
	private Map offstrategy = new HashMap();
	private final int[] Usedpoint = new int[]{8};
	private final String ADDRESS = "192.168.100.2:9278";
	
	
	public UsePoint7Impl(BundleContext context) throws BundleException {
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
		costtime((int)(0.2*in),7);
		
		int sizeofMB = 2;
		int sizeofKB = sizeofMB*1024;
		if(indata.length>=1){sizeofint = sizeofKB*256;}else{sizeofint= 0;}
		
		int[] datato8 = new int[sizeofint];
		int into8 = (int)(0.8*in);

		//call point8
		if(offstrategy.get(Usedpoint[0]) == Boolean.TRUE){
			System.out.print("point7 call point8.remote...");
			remote8.call(datato8,into8,offpoint);
		}else {
			System.out.print("point7 call point8.mobile...");
			local8.call(datato8,into8,offpoint);
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
					RemoteServiceReference[] remote8refs = rosgi.getRemoteServiceReferences(new URI(ADDRESS), UsePoint8.class.getName(), null);
					if (remote8refs == null){System.out.println("no remote point8 found");}
					remote8 = (UsePoint8) rosgi.getRemoteService(remote8refs[0]);
				} catch (Exception e){
					System.out.println("Can't connect for: " + e.toString());
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void getlocalservice(){
		ServiceReference local8ref =context.getServiceReference(UsePoint8.class.getName());
		if (local8ref == null){System.out.println("no local point8 found");}
		local8 = (UsePoint8) context.getService(local8ref);
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
