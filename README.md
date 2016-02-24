# MCC-Offloading
This project is used to do experiments of offloading algorithms on the MCC system: https://github.com/bxs3514/AndroidFelix.
This repository contains bundles which are components of experiment application.

Praperation: 
1. Download MCC client, AndroidFelix: https://github.com/bxs3514/AndroidFelix
2. Download MCC server: https://github.com/bxs3514/FelixServer

Configuration: 
1. Install MCC client on Android and server on Windows
2. Save all necessary bundles (e.g. all bundles in ChainBundles) under /sdcard/AFelixData/Bundle on the client
3. Startup the client and server, configure the TCP IP socket address according to the server (try ipconfig on your pc and the default port is 8888)
4. Install all bundles into client and server (you can also offload them online)
5. Run the AndroidOffload monitor
6. Start the experiment now!

If you want to do these experiments by yourself, you should install the MCC system first then install all of thest bundles in to the MCC system. You also need some basic R-OSGi bundles for supporting the cloud computing.

Some shortcuts during experiments:
![alt tag](https://github.com/bxs3514/MCC-Offloading/blob/master/Examination%202/1/Low1.jpg?raw=true)
![alt tag](https://github.com/bxs3514/MCC-Offloading/blob/master/Examination%202/1/Screenshot_2015-07-28-20-10-40.png?raw=true)
