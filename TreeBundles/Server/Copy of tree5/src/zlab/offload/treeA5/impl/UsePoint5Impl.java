package zlab.offload.treeA5.impl;

import java.util.List;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import zlab.offload.treeA5.api.UsePoint5;
import zlab.offload.treeA6.api.UsePoint6;

public class UsePoint5Impl implements UsePoint5 {
	
	private BundleContext context;
	private UsePoint6 local6;
	private int sizeofint;

	public UsePoint5Impl(BundleContext context) throws BundleException {
		this.context = context;
		getlocalservice();
		
	}

	@Override
	public int[] call(int[] indata, int in, List offpoint) {
	
		//cost time
		costtime((int)(0.6*in),5);
		
		int sizeofMB = 1;
		int sizeofKB = sizeofMB*1024;
		if(indata.length>=1){sizeofint = sizeofKB*256;}else{sizeofint= 0;}
		
		int[] datato6 = new int[sizeofint];
		
		int into6 = (int)(0.4*in);
		
		//call point6
		System.out.print("point5 call point6.pc...");
		local6.call(datato6,into6,offpoint);
		
			
		return null;
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
