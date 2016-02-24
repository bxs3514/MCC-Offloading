package zlab.offload.treeA9.impl;

import java.util.List;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import zlab.offload.treeA9.api.UsePoint9;

public class UsePoint9Impl implements UsePoint9 {
	
	public UsePoint9Impl(BundleContext context) throws BundleException {
	}

	@Override
	public int[] call(int[] indata, int in, List offpoint) {
		
		//cost time
		costtime(in,9);
		
		return null;
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
