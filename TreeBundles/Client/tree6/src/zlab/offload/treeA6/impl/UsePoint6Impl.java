package zlab.offload.treeA6.impl;

import java.util.List;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import zlab.offload.treeA6.api.UsePoint6;

public class UsePoint6Impl implements UsePoint6 {
	

	
	public UsePoint6Impl(BundleContext context) throws BundleException {
	}

	@Override
	public int[] call(int[] indata, int in, List offpoint) {

		//cost time
		costtime(in,6);
		
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
