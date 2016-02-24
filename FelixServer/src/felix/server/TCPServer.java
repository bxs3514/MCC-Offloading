package felix.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.felix.main.AutoProcessor;
import org.osgi.framework.BundleContext;


public class TCPServer
{

	private static ServerSocket serverSocket;
	private int tryBindTimes = 0;
	private ExecutorService executorService;
	private final static int POOL_SIZE = 4;
	private Map<String, String> configProps = null;
	private BundleContext bundleContext = null;
	private Properties props=System.getProperties();
	
	public TCPServer(Map<String, String> configProps , BundleContext bundleContext) throws Exception{
		try{
			this.configProps = configProps;
			this.bundleContext = bundleContext;
			this.bindtoServerPort(8888);
			executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*POOL_SIZE);
			//System.out.println("Os Name: " + props.getProperty("os.name").split(" ")[0]);
		}catch (Exception e){
			throw new Exception("launch TCPServer failed!");
		}
	}
	
	private void bindtoServerPort(int port) throws Exception{
		try{
			serverSocket = new ServerSocket(port);			
			new acceptRequest().start();
			System.out.println("bind to port: " + port +" successed!");
		}catch(Exception e){
			this.tryBindTimes +=1;
			port =port + this.tryBindTimes;
			if(this.tryBindTimes>=20){
				throw new Exception("have tried too many times");
			}
			this.bindtoServerPort(port);
		}
	}


	public void destoryTCP() {
		try{
			serverSocket.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		serverSocket = null;
	}
	
	public void installBundle(){
		AutoProcessor.process(configProps, bundleContext);		
	}


	class acceptRequest extends Thread {
		public void run() {
			while(true){
				try{
					Socket incoming = serverSocket.accept();
					executorService.execute(new RequestHandler(incoming));
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
		}
	}
	
	class RequestHandler implements Runnable{
		private Socket incoming;

		
		public RequestHandler(Socket incoming){
			this.incoming = incoming;
		}
		
		public void run(){
			DataInputStream dis = null;
			DataOutputStream dos =null;
			
			int bufferSize = 1024;
			byte[] bufBytes = new byte[bufferSize];
			
			String filename = null;
			try{
				dis = new DataInputStream(new BufferedInputStream(incoming.getInputStream()));
				filename = dis.readUTF();
				
				
				//Long lengthLong = dis.readLong();
				//long transTime = dis.readLong();
				
				//if(filename.equals("Speed.test")) {
					//long transTime = System.currentTimeMillis() - dis.readLong();
					//System.out.println("Time: " + (System.currentTimeMillis() - transTime));
					//return;
				//}
				
				if(props.getProperty("os.name").split("\\s+")[0].equals("Mac"))
					dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("/Users/sen/Documents/workspace/Felix/bundle/" + filename)));
				else if(props.getProperty("os.name").split("\\s+")[0].equals("Windows"))
					dos = new DataOutputStream(
							new BufferedOutputStream(
							new FileOutputStream(
							"G:\\Dropbox\\Graduate Design\\FelixServer\\bundle\\" + filename)));
				
				int byteRead = 0;
				while((byteRead=dis.read(bufBytes))!= -1){
					dos.write(bufBytes,0,byteRead);

				}

				//System.out.println("Time: " + (System.currentTimeMillis() - transTime) + "ms");
				//if(filename.equals("Speed.test")) {
					//long transTime = System.currentTimeMillis() - dis.readLong();
					//System.out.println("Time: " + (System.currentTimeMillis() - transTime));
					//return;
				//}
				System.out.println(filename +" received!");
				
			}catch (Exception e) {
					//if(filename.equals("Speed.test")) return;
					//e.printStackTrace();
					//System.out.println("receive file failed");
			}finally{
				try{
					if(dos != null){
						dos.close();	
					}
					if(dis != null){
						dis.close();
					}
					if(incoming != null){
						incoming.close();
					}
					//if(!filename.equals("Speed.test"))
					installBundle();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
}