package zlab.offload.treeA0.impl;

import java.util.List;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import zlab.offload.treeA0.api.UsePoint0;
import zlab.offload.treeA1.api.UsePoint1;
import zlab.offload.treeA9.api.UsePoint9;

public class UsePoint0Impl implements UsePoint0 {
	
	private BundleContext context;
	private UsePoint1 local1;
	private UsePoint9 local9;
	private int sizeofint1;
	private int sizeofint9;
	
	
	public UsePoint0Impl(BundleContext context) throws BundleException {
		this.context = context;
		getlocalservice();
		
	}

	@Override
	public int[] call(int[] indata, int in, List offpoint) {
		
		
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
		System.out.print("point0 call point1.pc...");
		local1.call(datato1,into1,offpoint);
		
		//call point9
		System.out.print("point0 call point9.pc...");
		local9.call(datato9,into9,offpoint);
		
		return null;
		
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
