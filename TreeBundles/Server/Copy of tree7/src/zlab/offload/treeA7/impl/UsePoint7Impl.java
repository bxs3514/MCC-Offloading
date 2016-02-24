package zlab.offload.treeA7.impl;

import java.util.List;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import zlab.offload.treeA7.api.UsePoint7;
import zlab.offload.treeA8.api.UsePoint8;

public class UsePoint7Impl implements UsePoint7 {
	
	private BundleContext context;
	private UsePoint8 local8;
	private int sizeofint;

	public UsePoint7Impl(BundleContext context) throws BundleException {
		this.context = context;
		getlocalservice();
		
	}

	@Override
	public int[] call(int[] indata, int in, List offpoint) {
		
		//cost time
		costtime((int)(0.2*in),7);
		
		int sizeofMB = 2;
		int sizeofKB = sizeofMB*1024;
		if(indata.length>=1){sizeofint = sizeofKB*256;}else{sizeofint= 0;}
		
		int[] datato8 = new int[sizeofint];
		int into8 = (int)(0.8*in);
		
		//call point8
		System.out.print("point7 call point8.pc...");
		local8.call(datato8,into8,offpoint);
		
		
		return null;
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
