package fibonacci1;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Properties;

import loop3.service.LoopService3;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;

import ch.ethz.iks.r_osgi.RemoteOSGiException;
import ch.ethz.iks.r_osgi.RemoteOSGiService;
import ch.ethz.iks.r_osgi.RemoteServiceReference;
import ch.ethz.iks.r_osgi.URI;
import fibonacci.service.FibonacciService1;

public class FibonacciActivator implements BundleActivator {
	
	private ServiceReference<?>[] refs;
	private BundleContext bundleContext;
	private RemoteServiceReference[] srefs;
	private RemoteOSGiService remote;
	//private FibonacciService2 rFibonacci2;
	private Properties props=System.getProperties();
	
	public void start(BundleContext bundleContext) throws Exception {
		System.out.println("Fibonacci 1");
		this.bundleContext = bundleContext;
		Dictionary props = new Hashtable();
		props.put(RemoteOSGiService.R_OSGi_REGISTRATION, Boolean.TRUE);//R-OSGi Register
		bundleContext.registerService(FibonacciService1.class.getName(), 
				new FibonacciImpl1(), props);
		//refs = bundleContext.getServiceReferences(FibonacciService2.class.getName(), null);
		refs = bundleContext.getServiceReferences(LoopService3.class.getName(), null);
		ServiceReference sref = bundleContext.getServiceReference(RemoteOSGiService.class.getName());

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
						long startTime = System.currentTimeMillis();
						//srefs = remote.getRemoteServiceReferences(
								//new URI("192.168.1.102:9278"), FibonacciService2.class.getName(),
								//null);
						srefs = remote.getRemoteServiceReferences(
								new URI("192.168.1.102:9278"), LoopService3.class.getName(),
								null);
						//while(rFibonacci2 == null && (System.currentTimeMillis() - startTime <= 10000))
							//rFibonacci2 = (FibonacciService2) remote.getRemoteService(srefs[0]);
					} catch (Exception be) {
						System.out.println("Can't connect for: " + be.toString());
						be.printStackTrace();
					}
				}
			}.start();
		}
		
	}

	public void stop(BundleContext bundleContext) throws Exception {
		for(RemoteServiceReference rService : srefs)
			remote.ungetRemoteService(rService);
		System.out.println("Fibonacci 1 Stop");
	}

	private class FibonacciImpl1 implements FibonacciService1{
		int res;
		int num;
		int[] fibonacciNum;
		int[] loopCount;
		int offloadNum;
		int[] data;
		
		
		@Override
		public int offload(int res, int[] data, int[] loopCount, int[] fibonacciNum, int num, int offloadNum) {
			this.res = res;
			this.num = num;
			this.fibonacciNum = fibonacciNum;
			this.loopCount = loopCount;
			this.offloadNum = offloadNum;
			//this.data = data;
			
			this.data = new int[loopCount[num]];
			int length = loopCount[num];
			for(int i = 0; i < length ; i++){
				this.data[i] = i;
			}
			
			//this.data = data;
			
			System.out.println(num);
			//System.out.println("???" + data[1024]);
			System.out.println(props.getProperty("os.name"));
			System.out.println("Fibonacci 1" + ": " + fibonacciNum[num]);
			if(num + 1 != offloadNum){
				//FibonacciService2 fb2 = (FibonacciService2)bundleContext.getService(refs[0]);
				LoopService3 loop3 = (LoopService3)bundleContext.getService(refs[0]);
				long startTime = System.currentTimeMillis();
				res = Fibonacci(fibonacciNum[num]);

				System.out.println("Fibonacci1 Res:" + res);
				if(props.getProperty("os.name").split("\\s+")[0].equals("Linux"))
				System.out.println("Local Fibonacci1 Time: " + (System.currentTimeMillis() - startTime) + "ms"); 
				else System.out.println("Server Fibonacci1 Time: " + (System.currentTimeMillis() - startTime) + "ms");
				System.out.println("transfer data " + num +": " + 4 * this.data.length + "byte"); 
				//System.out.println(num + "length: " + data.length);
				//startTime = System.currentTimeMillis() - startTime;
				res += loop3.offload(res, this.data, loopCount, fibonacciNum, ++num, offloadNum);
				
				return res;
			}
			else{
				return RemoteCalculator(fibonacciNum[num]);
			}
		}
		
		public int Fibonacci(int n) {
		    if(n < 0) return -1;
			else if(n == 0) return 0;
			else if(n == 1) return 1;
			else return Fibonacci(n - 1) + Fibonacci(n - 2);
		}

		public int RemoteCalculator(int n) {
			long startTime = System.currentTimeMillis();
			res = Fibonacci(n);
			
			System.out.println(props.getProperty("os.name"));
			if(props.getProperty("os.name").split("\\s+")[0].equals("Android"))
			System.out.println("Local Fibonacci1 Time: " + (System.currentTimeMillis() - startTime) + "ms"); 
			else System.out.println("Server Fibonacci1 Time: " + (System.currentTimeMillis() - startTime) + "ms"); 
			if (srefs != null) {
				System.out.println("Remote Fibonacci Invoke.");
				long rStartTime = System.currentTimeMillis();
				//FibonacciService2 fb2 = (FibonacciService2)remote
						//.getRemoteService(srefs[0]);
				LoopService3 loop3 = (LoopService3)remote.getRemoteService(srefs[0]);
				System.out.println("Service connection:" + 
						(System.currentTimeMillis() - startTime) + "ms");
				System.out.println("transfer data " + num +": " + 4 * this.data.length + "byte");
				rStartTime = System.currentTimeMillis();
				res = loop3.offload(res, this.data, loopCount, fibonacciNum, ++num, offloadNum);
				System.out.println("Remote Fibonacci2 Time:" + 
						(System.currentTimeMillis() - rStartTime) + "ms");
				
			} else {
				System.out.println("Can't get service: Fiboncci2");
			}/**/
			return res;
			
		}
		
	}
}
