package fibonacci2;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;

import ch.ethz.iks.r_osgi.RemoteOSGiException;
import ch.ethz.iks.r_osgi.RemoteOSGiService;
import ch.ethz.iks.r_osgi.RemoteServiceReference;
import ch.ethz.iks.r_osgi.URI;
import fibonacci2.service.FibonacciService2;

public class FibonacciActivator2 implements BundleActivator {
	
	private ServiceReference<?>[] refs;
	private BundleContext bundleContext;
	//private RemoteServiceReference[] srefs;
	//private RemoteOSGiService remote;
	private Properties props=System.getProperties();
	
	public void start(BundleContext bundleContext) throws Exception {
		System.out.println("Fibonacci 2");
		this.bundleContext = bundleContext;
		Dictionary props = new Hashtable();
		props.put(RemoteOSGiService.R_OSGi_REGISTRATION, Boolean.TRUE);//R-OSGi Register
		bundleContext.registerService(FibonacciService2.class.getName(), 
				new FibonacciImpl2(), props);
		/*refs = bundleContext.getServiceReferences(FibonacciService2.class.getName(), null);
		
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
						//while(rFibonacci2 == null && (System.currentTimeMillis() - startTime <= 10000))
							//rFibonacci2 = (FibonacciService2) remote.getRemoteService(srefs[0]);
					} catch (Exception be) {
						System.out.println("Can't connect for: " + be.toString());
						be.printStackTrace();
					}
				}
			}.start();
		}*/
		
	}

	public void stop(BundleContext bundleContext) throws Exception {
		//for(RemoteServiceReference rService : srefs)
			//remote.ungetRemoteService(rService);
		System.out.println("Fibonacci 2 Stop");
	}

	private class FibonacciImpl2 implements FibonacciService2{
		int res;
		int num;
		int[] fibonacciNum;
		int[] loopCount;
		int offloadNum;
		
		@Override
		public int offload(int res, int[] data, int[] loopCount, int[] fibonacciNum, int num, int offloadNum) {
			this.res = res;
			this.num = num;
			this.fibonacciNum = fibonacciNum;
			this.loopCount = loopCount;
			this.offloadNum = offloadNum;
			
			System.out.println("\n" + num);
			System.out.println(props.getProperty("os.name"));
			System.out.println("Fibonacci 2" + ": " + fibonacciNum[num]);
			if(num + 1 != offloadNum){
				//FibonacciService2 fb2 = (FibonacciService2)bundleContext.getService(refs[0]);
				long startTime = System.currentTimeMillis();
				res = Fibonacci(fibonacciNum[num]);
				
				
				System.out.println("Fibonacci2 Result: " + res);
				if(props.getProperty("os.name").split("\\s+")[0].equals("Linux"))
				System.out.println("Local Fibonacci2 Time: " + (System.currentTimeMillis() - startTime) + "ms"); 
				else System.out.println("Server Fibonacci2 Time: " + (System.currentTimeMillis() - startTime) + "ms"); 
				
				//startTime = System.currentTimeMillis() - startTime;
				//res += fb2.offload(res, data, loopCount, fibonacciNum, ++num, offloadNum);
				
				
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
			
			/*System.out.println(props.getProperty("os.name"));
			if(props.getProperty("os.name").split("\\s+")[0].equals("Android"))
			System.out.println("Local Fibonacci1 Time: " + (System.currentTimeMillis() - startTime) + "ms"); 
			else System.out.println("Server Fibonacci1 Time: " + (System.currentTimeMillis() - startTime) + "ms"); 
			if (srefs != null) {
				System.out.println("Remote Fibonacci Revoke.");
				
				long rStartTime = System.currentTimeMillis();
				FibonacciService1 rFibonacci = (FibonacciService1) remote
						.getRemoteService(srefs[0]);
				System.out.println("Service connection:" + 
						(System.currentTimeMillis() - startTime) + "ms");
				rStartTime = System.currentTimeMillis();
				res = rFibonacci2.offload(res, new int[]{1}, new int[]{1}, fibonacciNum, ++num, offloadNum);
				System.out.println("Remote Fibonacci2 Time:" + 
						(System.currentTimeMillis() - rStartTime) + "ms");
				
			} else {
				System.out.println("Can't get service: Fiboncci2");
			}*/
			return res;
			
		}
		
	}
}
