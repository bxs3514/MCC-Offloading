package zlab.offload.treeA2.impl;

import java.util.List;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import zlab.offload.treeA2.api.UsePoint2;
import zlab.offload.treeA3.api.UsePoint3;

public class UsePoint2Impl implements UsePoint2 {
	
	private BundleContext context;
	private UsePoint3 local3;
	private int sizeofint;
	
	public UsePoint2Impl(BundleContext context) throws BundleException {
		this.context = context;
		getlocalservice();
		
	}

	@Override
	public int[] call(int[] indata, int in, List offpoint) {

		//cost time
		costtime((int)(0.4*in),2);
		
		int sizeofMB = 3;
		int sizeofKB = sizeofMB*1024;
		if(indata.length>=1){sizeofint = sizeofKB*256;}else{sizeofint= 0;}
		
		int[] datato3 = new int[sizeofint];
		int into3 = (int)(0.6*in);
		
		//call point3
		System.out.print("point2 call point3.pc...");
		local3.call(datato3,into3,offpoint);
		
		
		return null;
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
