import java.util.ArrayList;

/* Quantidade de dispositivos: 7 10 12 15 */
/* Quantidade de pontos de acesso 2 4  */

public class Environment {
  private ArrayList<Float> interferences = new ArrayList<>();
  private ArrayList<Device> devices = new ArrayList<Device>();
  private ArrayList<Device> wifiDevs = new ArrayList<Device>();
  private ArrayList<Device> zigbeeDevs = new ArrayList<Device>();
  private ArrayList<Device> bluetoothDevs = new ArrayList<Device>();
  private ArrayList<AP> accessPoints = new ArrayList<AP>();
  private ArrayList<AP> wifiAPs = new ArrayList<AP>();
  private ArrayList<AP> zigbeeAPs = new ArrayList<AP>();
  private ArrayList<AP> bluetoothAPs = new ArrayList<AP>();
  private APsManagerInterface accessPointsManager;
  private DevicesManagerInterface devicesManager;
  private Util util;

 

  
  
  public int getChannelWifi(int x) {
		int a = 0;
		if (x == 0) {
			a = 1;
		} else if (x > 1) {
			a = x + 1;

		}
		return a;

	}

	public int getChannelZigBee(int y) {
		int a = y -13;
		return a;
	}

	public int setChannelWifi(int x) {
		int a = x - 1;
		return a;
	}

	public int setChannelZigBee(int y) {
		int a = y + 13;
		return a;
	}
	
  
	/**
	 * TODO SETA O LOCAL DO DISP E MUDA O CANAL
	 *
	 * @param ap
	 * @return
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public ArrayList<AP> changeLocation(ArrayList<AP> ap) throws ArrayIndexOutOfBoundsException {

		int[] x_location = { 0, 15, 30, 45, 60, 75, 90, 105, 120, 135, 150, 165, 175, 190, 205, 220, 235, 250, 265,280, 295,310,325,340,355,370,385,400 };
		int[] y_location = { 0, 35 , 70, 95, 130, 165, 195, 230, 265, 300, 335, 370, 400 };
		//CANAIS WF,ZB
		
		int temp_y = 0;
	//	int max_tempY=y_location.length;
		int temp_x = 0;
//		int max_tempX=x_location.length;
		int ch=0;
		
		
		
	
		int[] canal = {1,16,11,11,6,22,1,18,6,23,11,17,1,19,11,17,1,20,6,24,11,26};
		
		//int[] canal2 = {11,26,6,24,1,20,11,17,1,19,11,17,6,23,1,18,6,22,11,11,1,16};
		
		for (int x = 0; x < ap.size(); x++) {

			for (int z = 0; z < ap.size(); z++) {
				try {	
				if ((ap.get(x).getType().toString() == "WIFI") && (ap.get(z).getType().toString() == "ZIGBEE")) {
					
			
				//	if (x_location[temp_x] <= 355 && y_location[temp_y] < 400 && temp_x <=max_tempX && temp_y  <= max_tempY) {
					 if ( temp_x < 26 && temp_y < 13 ) {
					
					
					
						// WF
						ap.get(x).setX(x_location[temp_x]);
						
						ap.get(x).setY(y_location[temp_y]);
					

						// ZB

						ap.get(z).setX(x_location[temp_x + 1]);
						

						ap.get(z).setY(y_location[temp_y]);
					

						// INCREMENTA O temp_x
						temp_x += 2;
						
							   //ATRIBUI CANAIS
								if(canal[ch+1] < 24 && ap.get(x)!= null && ap.get(z)!= null) {
								
								//ARIBUI CANAL WIFI E  IMPRIME LOCAL E CANAL
								ap.get(x).setChannel(setChannelWifi(canal[ch]));
								
								System.out.println("X_wf: "+x_location[temp_x] + "; Y_wf: "+y_location[temp_y]+ "; "+ canal[ch] );
									
								 
								//ATRIBUI CANAL ZIGBEE  IMPRIME LOCAL E CANAL
								 ap.get(z).setChannel(setChannelZigBee(canal[ch+1]));
								 System.out.println("X_zb: "+x_location[temp_x + 1]+ "; Y_zb: "+y_location[temp_y] + "; "+canal[ch+1]);
								 
								
								
								//incrementa canais
								 ch+=2;
								}
								
								
								//QUANDO CHEGA NO PENÚLTIMO CANAL ENTRA AQUI, ZERA O ch E SETA OS ÚLTIMOS CANAIS NOS 2 DEVICES
								else if (canal[ch+1] >= 24 && ap.get(x)!= null && ap.get(z)!= null ){
									
									 ap.get(x).setChannel(setChannelWifi(canal[ch] = 11));
									
									 System.out.println("X_wf: "+x_location[temp_x]+ "; Y_wf: "+y_location[temp_y] + "; "+canal[ch]);
									 
									 ap.get(z).setChannel(setChannelZigBee(canal[ch+1] = 26));
									
									 
									 System.out.println("X_zb: "+x_location[temp_x+1]+ "; Y_wf: "+y_location[temp_y] + "; "+canal[ch+1]);
									 
									 ch = 0;
									 
								}
								
						

					}

					else {
						
					//	temp_x < 26 && temp_y < 13 
						
						if(temp_x == 26) {
							temp_x = 0;
						}

						if ( temp_y == 13) {
							temp_y = 0;
							
						}
						temp_y += 1;// QUANDO X CHEGA A 400 INCREMENTA O Y
						

					}

				} else if ((ap.get(x).getType().toString() == "ZIGBEE") && (ap.get(z).getType().toString() == "WIFI") ) {
					
					//if (x_location[temp_x] <= 355 && y_location[temp_y] < 400  ) {
					  if( temp_x < 26 && temp_y < 13) {
						
					
						// ZB

						ap.get(x).setX(x_location[temp_x+1]);
					
						
						ap.get(x).setY(y_location[temp_y]);
					
						System.out.println("X_zb: "+x_location[temp_x+1] + "; Y_zb: "+y_location[temp_y] );

						
						// WF
						ap.get(z).setX(x_location[temp_x]);
					

						ap.get(z).setX(y_location[temp_y]);
				   


						// INCREMENTA O temp_x + 2 pra ele pular a casa do zigbee // wifi
						temp_x += 2;
						
						
						// INCREMENTA O temp_x
						
						//ATRIBUI CANAL WIFI
						if(canal[ch+1] < 24 && ap.get(x)!= null && ap.get(z)!= null ) {
						 ap.get(z).setChannel(setChannelWifi(canal[ch]));
						// System.out.println(canal2[ch]);
						System.out.println("X_wf: "+x_location[temp_x] + "; Y_wf: "+y_location[temp_y]+ "; "+ canal[ch] );
						 
						//ATRIBUI CANAL ZIGBEE
						 ap.get(x).setChannel(setChannelZigBee(canal[ch+1]));
						// System.out.println(canal2[ch+1]);
						 System.out.println("X_zb: "+x_location[temp_x + 1]+ "; Y_zb: "+y_location[temp_y] + "; "+canal[ch+1]);
						 
					
						
						 
						 ch+=2;
						 
						}
						
						//QUANDO CHEGA NO PENÚLTIMO CANAL ENTRA AQUI, ZERA O ch E SETA OS ÚLTIMOS CANAIS NOS 2 DEVICES
						else if (canal[ch+1] >= 24  && ap.get(x)!= null && ap.get(z)!= null ){
							
							 ap.get(x).setChannel(setChannelWifi(canal[ch] = 11));
							 
							 System.out.println("X_wf: "+x_location[temp_x]+ "; Y_wf: "+y_location[temp_y] + "; "+canal[ch]);
							 
							 
							 ap.get(x).setChannel(setChannelZigBee(canal[ch+1] = 26));
							 System.out.println("X_zb: "+x_location[temp_x+1]+ "; Y_wf: "+y_location[temp_y] + "; "+canal[ch+1]);
							 ch = 0;
							 
						}
						
						
						
						
						
						//################ TRATA OS CANAIS ##############################
					
				
					   // #########################################################################


					}

					else {

						 

						if(temp_x == 26) {
							temp_x = 0;
						}

						if ( temp_y == 13) {
							temp_y = 0;
							
						}
						temp_y += 1;// QUANDO X CHEGA A 400 INCREMENTA O Y

					}
				}
				}
				catch (Exception exception) {
					System.out.println("ERRO :"+exception);
					
				
				
				}


			}
		}
		return ap;

	}
	
	
	

/*	public ArrayList<Device> changeLocationDevice(ArrayList<Device> dev) {

		int[] x_location = { 0, 20, 40, 60, 80, 100, 120, 140, 160, 180, 200, 220, 240, 260, 280, 290, 310, 330, 350,
				370, 390 };
		int[] y_location = { 0, 30, 60, 90, 120, 150, 180, 210, 230, 270, 300, 330, 360, 400 };
		AP temp;
		int temp_y = 0;
		int temp_x = 0;

		for (int x = 0; x < dev.size(); x++) {

			for (int z = 0; z < dev.size(); z++) {

				if ((dev.get(x).getType().toString() == "WIFI") && (dev.get(z).getType().toString() == "ZIGBEE")) {
					System.out.println("VALOR ATUAL X: " + x_location[temp_x]);
					System.out.println("VALOR ATUAL Y: " + y_location[temp_y]);
					if (x_location[temp_x] < 370) {

						// WF
						dev.get(x).setX(x_location[temp_x]);
						System.out.println(x_location[temp_x]);// X

						dev.get(x).setY(y_location[temp_y]);
						System.out.println(y_location[temp_y]);// Y

						// ZB

						dev.get(z).setX(x_location[temp_x + 1]);
						System.out.println(x_location[temp_x + 1]);// X

						dev.get(z).setY(y_location[temp_y]);
						System.out.println(y_location[temp_y]);// X

						// INCREMENTA O temp_x
						temp_x += 2;

					}

					else {

						if (y_location[temp_y] == 400) {
							temp_y = 0;
						}
						temp_y += 1;// QUANDO X CHEGA A 400 INCREMENTA O Y
						temp_x = 0;

					}

				} else if ((dev.get(x).getType().toString() == "ZIGBEE") && (dev.get(z).getType().toString() == "WIFI")) {
					System.out.println("VALOR ATUAL X: " + x_location[temp_x]);
					System.out.println("VALOR ATUAL Y: " + y_location[temp_y]);
					if (x_location[temp_x] < 370) {
						// ZB

						dev.get(x).setX(x_location[temp_x]);
						System.out.println(x_location[temp_x]);

						dev.get(x).setY(y_location[temp_y]);
						System.out.println(y_location[temp_y]);

						// WF
						dev.get(z).setX(x_location[temp_x]);
						System.out.println(x_location[temp_x]);

						dev.get(z).setX(y_location[temp_y]);
						System.out.println(y_location[temp_y]);

						// INCREMENTA O temp_x + 2 pra ele pular a casa do zigbee // wifi
						temp_x += 2;

					}

					else {

						if (y_location[temp_y] == 400) {
							temp_y = 0;
						}
						temp_y += 1;// QUANDO X CHEGA A 400 INCREMENTA O Y
						temp_x = 0;

					}
				}


			}
		}
		return dev;

	}*/
  
/*  public void changeLocation(AP ap, AP ap1) {
	  
		
	  int[] x_location = {0,10,20,30,40,50,60,70,80,90,100,110,120,130,140,150,160,170,180,190,200,210,220,230,240,250,260,270,280,290,300,310,320,330,340,350,360,370,380,390,400};
	  int[] y_location =  {0,30,60,90,120,150,180,210,230,270,300,330,360,400};
	
	  int temp_y=0;
	  int temp_x=0;
	 
	  
	
 
	  
	//	for (int x = 0; x < ap.size(); x++) {

		//	for (int z = 0; z < ap.size(); z++) {
				
			

				if ((ap.getType().toString() == "WIFI") && (ap1.getType().toString() == "ZIGBEE")) {
					if (x_location[temp_x+1] < 400 && y_location[temp_y+1] < 400) {
						// WF
						ap.setX(x_location[temp_x]);
						System.out.println(x_location[temp_x]);// X

						ap.setY(y_location[temp_y]);
						System.out.println(y_location[temp_y]);// Y
						
					

						// ZB

						ap.setX(x_location[temp_x+1]);
						System.out.println(x_location[temp_x+1]);// X
							
						ap.setY(y_location[temp_y]);
						System.out.println(y_location[temp_y]);// X
						
						//INCREMENTA O temp_x
						temp_x+=2;
						
						

						}

				} else if ((ap.getType().toString() == "ZIGBEE") && (ap1.getType().toString() == "WIFI")) {

					if (x_location[temp_x+1] < 400 && y_location[temp_y+1] < 400) {
						// ZB

					     	ap.setX(x_location[temp_x]);
							System.out.println(x_location[temp_x]);
							
							ap.setY(y_location[temp_y]);
							System.out.println(y_location[temp_y]);
						
						// WF
						ap.setX(x_location[temp_x]);
						System.out.println(x_location[temp_x]);
						
						ap.setX(y_location[temp_y]);
						System.out.println(y_location[temp_y]);
						
						//INCREMENTA O temp_x + 2 pra ele pular a casa do zigbee // wifi
						temp_x+=2;
						

					
				}
				else if(x_location[temp_x] == 400) {
					temp_y+=1;// QUANDO X CHEGA A 400 INCREMENTA O Y
					temp_x = 0;
				}
				else if(y_location[temp_y] == 400) {
					temp_x=0;// SE O Y CHEGAR A 400 SERÁ ZERADO
					temp_y =0;
				}
				

			}
		//}
		 
		//}

	}*/

  Environment(
    DevicesManagerInterface devicesManager,
    APsManagerInterface accessPointsManager) {
    this.devicesManager = devicesManager;
    this.accessPointsManager = accessPointsManager;
 
   
    setAPs();
    setDevices();
  }

  public void setAPs(){
    accessPoints = accessPointsManager.addAP(accessPoints);
    initWifiAPs();
    initZigbeeAPs();
    initBluetoothAPs();
  }

  public void setDevices(){
    devices = devicesManager.addDev(devices, accessPoints);
    initWifiDevs();
    initZigbeeDevs();
    initBluetoothDevs();
  }

  public void initWifiAPs() {
    for (AP ap : accessPoints) {
      if (ap.getType() == DeviceType.WIFI)
        wifiAPs.add(ap);
    }
  }

  public void initZigbeeAPs() {
    for (AP ap : accessPoints) {
      if (ap.getType() == DeviceType.ZIGBEE)
        zigbeeAPs.add(ap);
    }
  }

  public void initBluetoothAPs() {
    for (AP ap : accessPoints) {
      if (ap.getType() == DeviceType.BLUETOOTH)
        bluetoothAPs.add(ap);
    }
  }

  public void initWifiDevs() {
    for (Device device : devices) {
      if (device.getType() == DeviceType.WIFI) {
        wifiDevs.add(device);
      }
    }
  }

  public void initZigbeeDevs() {
    for (Device device : devices) {
      if (device.getType() == DeviceType.ZIGBEE) {
        zigbeeDevs.add(device);
      }
    }
  }

  public void initBluetoothDevs() {
    for (Device device : devices) {
      if (device.getType() == DeviceType.BLUETOOTH) {
        bluetoothDevs.add(device);
      }
    }
  }

  // getters e setters

  public ArrayList<Device> getDevices() {
    return this.devices;
  }

  public ArrayList<AP> getAPs() {
    return this.accessPoints;
  }

  public int getHowManyWifiDevices() {
    return wifiDevs.size();
  }

  public int getHowManyZigbeeDevices() {
    return zigbeeDevs.size();
  }

  public int getHowManyBluetoothDevices() {
    return bluetoothDevs.size();
  }

  public ArrayList<Device> getWifiDevs() {
    return wifiDevs;
  }

  public void setWifiDevs(ArrayList<Device> wifiDevs) {
    this.wifiDevs = wifiDevs;
  }

  public ArrayList<Device> getZigbeeDevs() {
    return zigbeeDevs;
  }

  public void setZigbeeDevs(ArrayList<Device> zigbeeDevs) {
    this.zigbeeDevs = zigbeeDevs;
  }

  public ArrayList<Device> getBluetoothDevs() {
    return bluetoothDevs;
  }

  public void setBluetoothDevs(ArrayList<Device> bluetoothDevs) {
    this.bluetoothDevs = bluetoothDevs;
  }

  public ArrayList<AP> getWifiAPs() {
    return wifiAPs;
  }

  public void setWifiAPs(ArrayList<AP> wifiAPs) {
    this.wifiAPs = wifiAPs;
  }

  public ArrayList<AP> getZigbeeAPs() {
    return zigbeeAPs;
  }

  public void setZigbeeAPs(ArrayList<AP> zigbeeAPs) {
    this.zigbeeAPs = zigbeeAPs;
  }

  public ArrayList<AP> getBluetoothAPs() {
    return bluetoothAPs;
  }

  public void setBluetoothAPs(ArrayList<AP> bluetoothAPs) {
    this.bluetoothAPs = bluetoothAPs;
  }

public ArrayList<Float> getInterferences(){
  return interferences;
}

  public void setInterferences(ArrayList<Float> interferences){
      this.interferences = interferences;
  }
  public Float getBestInterferences(){
    interferences.sort(null);
    return interferences.get(0);
  }

  public int getAmountOfDevices() {
    return devices.size();
  }

  public int getAmountOfAPs() {
    return accessPoints.size();
  }

 
  
}
