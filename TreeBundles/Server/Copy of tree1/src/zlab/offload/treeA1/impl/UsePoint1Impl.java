package zlab.offload.treeA1.impl;

import java.util.List;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import zlab.offload.treeA1.api.UsePoint1;
import zlab.offload.treeA2.api.UsePoint2;
import zlab.offload.treeA4.api.UsePoint4;

public class UsePoint1Impl implements UsePoint1 {
	
	private BundleContext context;
	private UsePoint2 local2;
	private UsePoint4 local4;
	private int sizeofint2;
	private int sizeofint4;
	
	public UsePoint1Impl(BundleContext context) throws BundleException {
		this.context = context;
		getlocalservice();
		
	}

	@Override
	public int[] call(int[] indata, int in, List offpoint) {
		
		//cost time
		costtime((int)(0.1*in),1);
		
		int sizeofMB2 = 5;
		int sizeofKB2 = sizeofMB2*1024;
		if(indata.length>=1){sizeofint2 = sizeofKB2*256;}else{sizeofint2= 0;}
		
		int sizeofMB4 = 5;
		int sizeofKB4 = sizeofMB4*1024;
		if(indata.length>=1){sizeofint4 = sizeofKB4*256;}else{sizeofint4= 0;}
		
		int[] datato2 = new int[sizeofint2];
		int[] datato4 = new int[sizeofint4];
		
		int into2 = (int)(0.3*in);
		int into4 = (int)(0.6*in);

		
		//call point2
		System.out.print("point1 call point2.pc...");
		local2.call(datato2,into2,offpoint);
		
		
		//call point4
		System.out.print("point1 call point4.pc...");
		local4.call(datato4,into4,offpoint);
		
		
		return null;
	}
	
	


	private void getlocalservice(){
		ServiceReference local2ref =context.getServiceReference(UsePoint2.class.getName());
		ServiceReference local4ref =context.getServiceReference(UsePoint4.class.getName());
		if (local2ref == null){System.out.println("no local point2 found");}
		if (local4ref == null){System.out.println("no local point4 found");}
		local2 = (UsePoint2) context.getService(local2ref);
		local4 = (UsePoint4) context.getService(local4ref);
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
