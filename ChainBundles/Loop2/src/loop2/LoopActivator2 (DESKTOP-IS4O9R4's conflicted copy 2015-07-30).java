package loop2;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Properties;

import loop2.service.LoopService2;
import loop3.service.LoopService3;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;

import ch.ethz.iks.r_osgi.RemoteOSGiService;
import ch.ethz.iks.r_osgi.RemoteServiceReference;
import ch.ethz.iks.r_osgi.URI;
import fibonacci.service.FibonacciService1;

public class LoopActivator2 implements BundleActivator {

	private ServiceReference<?>[] refs;
	private BundleContext bundleContext;
	private RemoteServiceReference[] srefs;
	private RemoteOSGiService remote;
	private Properties props=System.getProperties();
	
	public void start(BundleContext bundleContext) throws Exception {
		System.out.println("Loop2 start.");
		this.bundleContext = bundleContext;
		Dictionary props = new Hashtable();
		props.put(RemoteOSGiService.R_OSGi_REGISTRATION, Boolean.TRUE);//R-OSGi Register
		bundleContext.registerService(LoopService2.class.getName(), new Loop2(), props);
		refs = bundleContext.getServiceReferences(FibonacciService1.class.getName(), null);
		//refs = bundleContext.getServiceReferences(LoopService3.class.getName(), null);
		
		ServiceReference sref = bundleContext
				.getServiceReference(RemoteOSGiService.class.getName());

		if (sref == null) {
			System.out.println("No R-OSGi found");
			throw new BundleException("No R-OSGi found");
		}
		else{
			remote = (RemoteOSGiService) bundleContext.getService(sref);
			
			new Thread() {
				public void run() {
					try {
						remote.connect(new URI("192.168.1.102:9278"));
						srefs = remote.getRemoteServiceReferences(
								new URI("192.168.1.102:9278"), FibonacciService1.class.getName(), null);
						//srefs = remote.getRemoteServiceReferences(
								//new URI("192.168.1.102:9278"), LoopService3.class.getName(),null);
						long startTime = System.currentTimeMillis();
						//synchronized (rFibonacciService1){
							//while(rFibonacciService1 == null && (System.currentTimeMillis() - startTime <= 10000))
								//rFibonacciService1 = (FibonacciService1) remote.getRemoteService(srefs[0]);
						//}
					} catch (Exception be) {
						System.out.println("Can't connect for: " + be.toString());
						be.printStackTrace();
					}
				}
			}.start();
		}
		System.out.println("Loop start successfully.");
	}

	public void stop(BundleContext bundleContext) throws Exception {
		for(RemoteServiceReference rService : srefs)
			remote.ungetRemoteService(rService);
		System.out.println("Loop2 stop.");
	}

	private class Loop2 implements LoopService2{

		int res;
		int num;
		int[] fibonacciNum;
		int[] loopCount;
		int offloadNum;
		//int[] sendData;
		int[] data;
		
		@Override
		public int offload(int res, int[] data, int[] loopCount, int[] fibonacciNum, int num, int offloadNum) {
			this.res = res;
			this.num = num;
			this.fibonacciNum = fibonacciNum;
			this.loopCount = loopCount;
			this.offloadNum = offloadNum;
			this.data = data;
			//sendData = new int[loopCount[num]];
			//int length = sendData.length;
			//for(int i = 0; i < length ; i++){
				//sendData[i] = i;
			//}
			
			
			if(num + 1 != offloadNum) {
				System.out.println(num);
				System.out.println(props.getProperty("os.name"));
				res += Calculate(data);
				
				//System.out.println(refs.length);
				FibonacciService1 fb1 = (FibonacciService1)bundleContext.getService(refs[0]);
				
				//res = fb1.offload(res % 10, num, ifOffload, null);
				System.out.println("transfer data " + num +": " + 4 * this.data.length + "byte");
				res = fb1.offload(res, this.data, loopCount, fibonacciNum, ++num, offloadNum);
				
				//LoopService3 loop3 = (LoopService3)bundleContext.getService(refs[0]);
				//res = loop3.offload(res, data, loopCount, fibonacciNum, ++num, offloadNum);
			}
			else{
				res = RemoteCalculate(data);
			}
			return res;
		}

		@Override
		public int Calculate(int[] data) {
			//System.out.println("Local Loop2.");
			long startTime = System.currentTimeMillis();
			int length = data.length;
			for(int i = 0; i < loopCount[num] ; i++){
				res += data[i % length];
			}
			if(res <= 0) res = Integer.MAX_VALUE / 2;
			if(props.getProperty("os.name").split("\\s+")[0].equals("Linux"))
				System.out.println("Local Loop2.\n Local Loop2 Time:" + (System.currentTimeMillis() - startTime) + "ms");
			else System.out.println("Server Loop2.\n Server Loop2 Time:" + (System.currentTimeMillis() - startTime) + "ms");
			System.out.println("Result after loop 2: " + res); 
			
			this.data = new int[loopCount[num]];
			length = data.length;
			//System.out.println("length: " + length);
			for(int i = 0; i < length ; i++){
				data[i] = i;
			}
			
			return res;
		}

		@Override
		public int RemoteCalculate(int[] data) {
			res = Calculate(data);
			if (srefs != null) {
				System.out.println("Loop2 Remote Invoke.");
				
				long startTime = System.currentTimeMillis();
				FibonacciService1 rFibonacci = (FibonacciService1) remote
						.getRemoteService(srefs[0]);
				//LoopService3 rloop3 = (LoopService3)remote.getRemoteService(srefs[0]);
				System.out.println("transfer data " + num +": " + 4 * this.data.length + "byte");
				System.out.println("Service connection:" + 
						(System.currentTimeMillis() - startTime) + "ms");
				startTime = System.currentTimeMillis();
				
				res = rFibonacci.offload(res, this.data, loopCount, 
						fibonacciNum, ++num, offloadNum);
				//rloop3.offload(res, data, loopCount, fibonacciNum, ++num, offloadNum);
				System.out.println("Remote Fibonacci1 Time:" + 
						(System.currentTimeMillis() - startTime) + "ms");
				
			} else {
				System.out.println("Can't get service.");
			}
			return res;
		}
		
	}
	
}
