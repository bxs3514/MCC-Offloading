package zlab.offload.treeA4.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import ch.ethz.iks.r_osgi.RemoteOSGiService;
import ch.ethz.iks.r_osgi.RemoteServiceReference;
import ch.ethz.iks.r_osgi.URI;
import zlab.offload.treeA4.api.UsePoint4;
import zlab.offload.treeA5.api.UsePoint5;
import zlab.offload.treeA7.api.UsePoint7;

public class UsePoint4Impl implements UsePoint4 {
	
	private BundleContext context;
	private RemoteOSGiService rosgi;
	private UsePoint5 remote5;
	private UsePoint7 remote7;
	private UsePoint5 local5;
	private UsePoint7 local7;
	private int sizeofint5;
	private int sizeofint7;
	private Map offstrategy = new HashMap();
	private final int[] Usedpoint = new int[]{5,7};
	private final String ADDRESS = "192.168.100.2:9278";
	
	
	public UsePoint4Impl(BundleContext context) throws BundleException {
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
		costtime((int)(0.2*in),4);
		
		int sizeofMB5 = 2;
		int sizeofKB5 = sizeofMB5*1024;
		if(indata.length>=1){sizeofint5 = sizeofKB5*256;}else{sizeofint5= 0;}
		
		int sizeofMB7 = 2;
		int sizeofKB7 = sizeofMB7*1024;
		if(indata.length>=1){sizeofint7 = sizeofKB7*256;}else{sizeofint7= 0;}
		
		int[] datato5 = new int[sizeofint5];
		int[] datato7 = new int[sizeofint7];
		int into5 = (int)(0.4*in);
		int into7 = (int)(0.4*in);

		
		//call point5
		if(offstrategy.get(Usedpoint[0]) == Boolean.TRUE){
			System.out.print("point4 call point5.remote...");
			remote5.call(datato5,into5,offpoint);
		}else {
			System.out.print("point4 call point5.mobile...");
			local5.call(datato5,into5,offpoint);
		}
		
		//call point7
		if(offstrategy.get(Usedpoint[1]) == Boolean.TRUE){
			System.out.print("point4 call point7.remote...");
			remote7.call(datato7,into7,offpoint);
		}else {
			System.out.print("point4 call point7.mobile...");
			local7.call(datato7,into7,offpoint);
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
					RemoteServiceReference[] remote5refs = rosgi.getRemoteServiceReferences(new URI(ADDRESS), UsePoint5.class.getName(), null);
					RemoteServiceReference[] remote7refs = rosgi.getRemoteServiceReferences(new URI(ADDRESS), UsePoint7.class.getName(), null);
					if (remote5refs == null){System.out.println("no remote point5 found");}
					if (remote7refs == null){System.out.println("no remote point7 found");}
					remote5 = (UsePoint5) rosgi.getRemoteService(remote5refs[0]);
					remote7 = (UsePoint7) rosgi.getRemoteService(remote7refs[0]);
				} catch (Exception e){
					System.out.println("Can't connect for: " + e.toString());
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void getlocalservice(){
		ServiceReference local5ref =context.getServiceReference(UsePoint5.class.getName());
		ServiceReference local7ref =context.getServiceReference(UsePoint7.class.getName());
		if (local5ref == null){System.out.println("no local point5 found");}
		if (local7ref == null){System.out.println("no local point7 found");}
		local5 = (UsePoint5) context.getService(local5ref);
		local7 = (UsePoint7) context.getService(local7ref);
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
