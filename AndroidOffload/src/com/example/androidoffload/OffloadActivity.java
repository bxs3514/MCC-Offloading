package com.example.androidoffload;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import afelix.service.interfaces.BundlePresent;
import afelix.service.interfaces.IAFelixService;
import android.support.v7.app.ActionBarActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class OffloadActivity extends ActionBarActivity {

	static int[] data = new int[1024];
	
	private static float ms[] = {12f, 11f, 142f, 1f, 1227};
	private static float ml[] = {76f, 65f, 1247f, 1f, 13151f};
	private static int d[] = {16 * 1024 * data.length,  8 * 1024 * data.length, 
		8 * 1024 * data.length, 8 * 1024 * data.length, 4 * 1024 * data.length, 0};
	
	private ServiceConnection mConnection;
	private IAFelixService mAFelixService;
	private BundlePresent bp;
	
	private Button CalculateBtn;
	private TextView resText;
	
	private Intent bindServiceIntent;
	
	private TimerTask refreshNetworkSpeed;
	private float[] uploadSpeed;
	//private float[] downloadSpeed;
	private float avgUploadSpeed;
	//private float avgDownloadSpeed;
    private int counter = 0;
    private Timer timer;
    
    private Object lock = new Object();
    private boolean lockBool = true;

	//int offloadNum = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offload);
		
		for(int i = 1; i <= data.length; i++){
			data[i - 1] = i;
		}
		timer = new Timer();
		uploadSpeed = new float[5];
		resText = (TextView)findViewById(R.id.resText);
		
		CalculateBtn = (Button)findViewById(R.id.start);
		CalculateBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				//synchronized(this){
					//measureUploadSpeed();
				//}
				
				
				//System.out.println(optimize());
				int offloadNum = optimize();
				//System.out.println(offloadNum);
				//resText.setText("Local\n");
				//resText.setText("Full Offload\n");
				//optimize();
				resText.setText("Optimize Offload\n");
				Object[] para = {data, new int[]{4 * 1024 * data.length, 2 * 1024 * data.length, 
						2 * 1024 * data.length, 2 * 1024 * data.length, 1024 * data.length, 0},
						new int[]{0, 0, 0, 36, 0, 41}, offloadNum};
				
				ArrayList<String> clazz = new ArrayList<String>();
				long[] resTime = new long[3];
				//String[] clazz = {String.class.getName()};
				bp = new BundlePresent();
				clazz.add(int[].class.getName());
				clazz.add(int[].class.getName());
				clazz.add(int[].class.getName());
				clazz.add(int.class.getName());
				
				bp.setParameters("", "AndroidFelixOffloadKernal_1.0.0.jar", 
						"offload.service.OffloadTestImpl", 
						"offload", "result", para, clazz);
				try {
					long startTime = System.currentTimeMillis();
					mAFelixService.executeBundle(bp);
					resTime[0] = System.currentTimeMillis() - startTime;
					
					ArrayList<Object> resultArray = 
							(ArrayList<Object>) bp.getBundleResult().get("result");
						//System.out.println(bp.getBundleResult().size());
					if(resultArray != null){
						for(Iterator<Object> i = resultArray.iterator(); i.hasNext(); ){
							String tempStr = i.next().toString();
							long resTime1 = System.currentTimeMillis() - startTime;
							resText.append("Result:" + tempStr + "\n" + "Offload Number: " + offloadNum + "\n"
							+ resTime1 + "ms\n" + avgUploadSpeed + "KB/s");
							//resText.append("Result:" + tempStr  + "\n"
									//+ resTime1 + "ms\n"); //+ avgUploadSpeed + "KB/s");
						}
							//String result = resultArray.get(0).toString();
							//Toast.makeText(this, Integer.toString(size), Toast.LENGTH_LONG).show();
					}
					else{
						resText.setText("No results.");
					}
					/*
					para[3] = 1;
					bp.setParameters("", "AndroidFelixOffloadKernal_1.0.0.jar", 
							"offload.service.OffloadTestImpl", 
							"offload", "result", para, clazz);
					
					startTime = System.currentTimeMillis();
					mAFelixService.executeExistBundle(bp);
					resTime[1] = System.currentTimeMillis() - startTime;
					
					para[3] = optimistic();
					bp.setParameters("", "AndroidFelixOffloadKernal_1.0.0.jar", 
							"offload.service.OffloadTestImpl", 
							"offload", "result", para, clazz);
					startTime = System.currentTimeMillis();
					mAFelixService.executeExistBundle(bp);
					resTime[2] = System.currentTimeMillis() - startTime;
					ArrayList<Object> resultArray = 
							(ArrayList<Object>) bp.getBundleResult().get("result");
						//System.out.println(bp.getBundleResult().size());
					//resText.setText("");
					if(resultArray != null){
						int n = 0;
						for(Iterator<Object> i = resultArray.iterator(); i.hasNext(); ){
							String tempStr = i.next().toString();
							//long resTime = System.currentTimeMillis() - startTime;
							resText.append(n + "\n" + tempStr + "\n" + resTime[n] + "ms\n\n");
							n++;
						}
						resText.append(avgUploadSpeed + "KB/s\n");
							//String result = resultArray.get(0).toString();
							//Toast.makeText(this, Integer.toString(size), Toast.LENGTH_LONG).show();
					}
					else{
						resText.setText("No results.");
					}*/
				} catch (RemoteException e) {
					e.printStackTrace();
				} finally{
					clazz.clear();
				}
				
			}
			
		});
		
		buildServiceConnection();

		bindServiceIntent = new Intent(IAFelixService.class.getName());
		
		bindServiceIntent = this.createExplicitFromImplicitIntent(
				getApplicationContext(), bindServiceIntent);

		bindService(bindServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		if(mConnection != null)
			unbindService(mConnection);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.offload, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void buildServiceConnection(){
		
		mConnection = new ServiceConnection(){

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				
				try{
					if(service.getInterfaceDescriptor().equals(IAFelixService.class.getName())){
						mAFelixService = IAFelixService.Stub.asInterface(service);
						/*mAFelixService.interpret("install Fibonacci2_1.0.0.jar");
						mAFelixService.interpret("install Fibonacci1_1.0.0.jar");
						mAFelixService.interpret("install Loop1_1.0.0.jar");
						mAFelixService.interpret("install Loop2_1.0.0.jar");
						mAFelixService.interpret("install Loop3_1.0.0.jar");
						mAFelixService.interpret("install AndroidFelixOffloadKernal_1.0.0.jar");
						
						mAFelixService.startBundle("Fibonacci2_1.0.0.jar");
						mAFelixService.startBundle("Fibonacci1_1.0.0.jar");
						mAFelixService.startBundle("Loop3.jar");
						mAFelixService.startBundle("Loop2.jar");
						mAFelixService.startBundle("Loop1.jar");*/
					}
				}catch (RemoteException re){
					re.printStackTrace();
				}
			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
				// TODO Auto-generated method stub
				Toast.makeText(OffloadActivity.this, 
						"Service has disconnected", Toast.LENGTH_LONG).show();
				mAFelixService = null;
			}
			
		};
	}
	
	//Change a implicit intent to a explicit one
	private Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
 
        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }
 
        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);
 
        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);
 
        // Set the component to be explicit
        explicitIntent.setComponent(component);
 
        return explicitIntent;
    }
	
	private void measureUploadSpeed(){
		counter = 0;
		avgUploadSpeed = 0;
		
		//if(refreshNetworkSpeed != null){
			//refreshNetworkSpeed.cancel();
			//refreshNetworkSpeed = new mNetworkSpeed();
		//}
		//lock.wait();
		
		refreshNetworkSpeed = new mNetworkSpeed();
		timer.schedule(refreshNetworkSpeed, 0, 1000);
	}
	
	private int optimize(){
		measureUploadSpeed();
		
		while(counter != 5);
		
		int length = ms.length;
		int offloadNum = 0;
		float optimizedTime = 0;
		
		float[] netTransTime = new float[]{d[0]/avgUploadSpeed, d[1] / avgUploadSpeed, d[2] / avgUploadSpeed,
				d[3] / avgUploadSpeed, d[4] / avgUploadSpeed, Integer.MAX_VALUE};
		//netSpeed = 2800;
		//System.out.println("???" + avgUploadSpeed + "\n><" + d[1]);
		float quickerTime = 0;
		optimizedTime = 0;
		
		for(int i = 0; i < length; i++){
			quickerTime = -netTransTime[i];
			
			for(int j = 0; j < i; j++){
				quickerTime -= ml[j] - ms[j];
			}
			for(int k = i; k < length; k++){
				quickerTime += ml[k] - ms[k];
			}
			System.out.println("quiker!" + quickerTime);
			if(quickerTime > optimizedTime){
				optimizedTime = quickerTime;
				offloadNum = i;
			}
			quickerTime = -quickerTime;
		}
		
		
		//float optimizedToFull = 0;
		
		/*if(offloadNum > 0){
			optimizedToFull = netTransTime[0] - netTransTime[--offloadNum];
			for(int i = 0; i < offloadNum; i++){
				optimizedToFull -= ml[i] - ms[i];
			}
		}
		else{
			optimizedToFull = netTransTime[0];
			for(int i = 0; i < length; i++){
				optimizedToFull -= ml[i] - ms[i];
			}
		}*/
		
		System.out.println(++offloadNum + ": "+ "Quicker " + optimizedTime + "ms");
				//+ "     Optimized: " + optimizedToFull + "ms");
		if(optimizedTime <= 0) return -1;	
		return offloadNum;
	}
	
	private class mNetworkSpeed extends TimerTask{

		@Override
		public void run(){
			try {
				mAFelixService.networkSpeed();
				uploadSpeed[counter] = mAFelixService.networkUploadSpeed();
				//System.out.println(uploadSpeed[counter] + "KB/s");
				//downloadSpeed[counter] = mAFelixService.networkDownloadSpeed();
				//avgUploadSpeed += uploadSpeed[counter];
				//avgDownloadSpeed += downloadSpeed[counter];
				counter++;
				System.out.println(counter);
				avgUploadSpeed = uploadSpeed[counter - 1];
				if(counter == 5) {
					//System.out.println("!!!" + avgUploadSpeed + "KB/s");
					//avgDownloadSpeed /= 10;
					//lockBool = false;
					//OffloadActivity.this.notify();
					//this.notifyAll();
					//speedText.setText("Upload Speed: " + avgUploadSpeed + "KB/s\n" 
					//+ "Download Speed: " + avgDownloadSpeed + "KB/s");
					refreshNetworkSpeed.cancel();
					//timer.cancel();
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	}
}
