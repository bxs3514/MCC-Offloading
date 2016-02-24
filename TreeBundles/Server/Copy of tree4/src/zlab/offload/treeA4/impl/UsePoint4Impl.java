package zlab.offload.treeA4.impl;

import java.util.List;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import zlab.offload.treeA4.api.UsePoint4;
import zlab.offload.treeA5.api.UsePoint5;
import zlab.offload.treeA7.api.UsePoint7;

public class UsePoint4Impl implements UsePoint4 {
	
	private BundleContext context;
	private UsePoint5 local5;
	private UsePoint7 local7;
	private int sizeofint5;
	private int sizeofint7;
	
	public UsePoint4Impl(BundleContext context) throws BundleException {
		this.context = context;
		getlocalservice();
		
	}

	@Override
	public int[] call(int[] indata, int in, List offpoint) {
		
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
		System.out.print("point4 call point5.pc...");
		local5.call(datato5,into5,offpoint);
		
		
		//call point7
		System.out.print("point4 call point7.pc...");
		local7.call(datato7,into7,offpoint);
				
		
		return null;
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
