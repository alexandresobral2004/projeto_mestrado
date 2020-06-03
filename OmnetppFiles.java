import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class OmnetppFiles {

	private String strIni = new String();
	private String strNed = new String();
	private Environment env;
	private ChannelAssignerInterface assigner;
	private Util util;
    private String suffix;
    private String path;
    private float customInterference;
    private float sameInterference;
    private float randomInterference;
    String random;
    String custom;


	public OmnetppFiles(Environment env, Util util, ChannelAssignerInterface assigner, String path, String suffix) {
		this.env = env;
		this.util = util;
		this.assigner = assigner;
        this.path = path;
        this.suffix = suffix;

		System.out.println("Amount of APs: " + this.env.getAmountOfAPs());
		System.out.println("Amount Wi-Fi AP: " + this.env.getWifiAPs().size());
		System.out.println("Amount Zigbee AP: " + this.env.getZigbeeAPs().size());
		System.out.println("Amount Bluetooth AP: " + this.env.getBluetoothAPs().size());

		System.out.println("Amount of Devices: " + this.env.getAmountOfDevices());
		System.out.println("Amount Wi-Fi Dev: " + this.env.getWifiDevs().size());
		System.out.println("Amount Zigbee Dev: " + this.env.getZigbeeDevs().size());
        System.out.println("Amount Bluetooth Dev: " + this.env.getBluetoothDevs().size());
	}

  public void writeInterferencesFile(String config){
    String configFileName = path + config + suffix;
    try(FileWriter writer = new FileWriter(configFileName, true)){
      if(config.equals("CUSTOM")){
    	
        writer.write(Float.toString(customInterference));
        writer.write(System.getProperty("line.separator"));
        
      }
      else if(config.equals("RANDOM")){
    
        writer.write(Float.toString(randomInterference));
        writer.write(System.getProperty("line.separator"));
       
      }
      else{
        writer.write(Float.toString(sameInterference));
        writer.write(System.getProperty("line.separator"));
      }
    }
    catch(IOException e){
      e.printStackTrace();
    }
  }

  public void writeTimeElapsedFile(){
    String runtimeFile = path + "RUNTIME" + suffix;
    try(FileWriter writer  = new FileWriter(runtimeFile, true)){
      writer.write(Float.toString(assigner.getElapsedTime()));
      writer.write(System.getProperty("line.separator"));
    } catch(IOException e){
      e.printStackTrace();
    }
  }

  public void printINIFile(String output) {
    prepareOmnetppHeader();
    prepareOmnetppSAME();
    prepareOmnetppRANDOM();
    prepateOmnetppCustom();

    try {
      // File iniFile = new File(output);

      FileWriter writer = new FileWriter(output);
      writer.write(this.strIni);
      writer.close();

    } catch (IOException iox) {
      iox.printStackTrace();
    }
  }

	public void printINIFile() {
		prepareOmnetppHeader();
		prepareOmnetppSAME();
		prepareOmnetppRANDOM();
		prepateOmnetppCustom();

		try {
			File iniFile = new File("parameters.ini");

			FileWriter writer = new FileWriter(iniFile);
			writer.write(this.strIni);
			writer.close();

			System.out.println("Printed ini file");

		} catch (IOException iox) {
			iox.printStackTrace();
		}
	}

	public void printNEDFile(String output) {
	   prepareOmnetppNED();

	   try {
	    //  File iniFile = new File(output);

	     FileWriter writer = new FileWriter(output);
	     writer.write(this.strNed);
	     writer.close();

	   } catch (IOException iox) {
	     iox.printStackTrace();
	   }
	}

	public void printNEDFile() {
		prepareOmnetppNED();

		try {
			File iniFile = new File("network.ned");

			FileWriter writer = new FileWriter(iniFile);
			writer.write(this.strNed);
			writer.close();

			System.out.println("Printed ned file");

		} catch (IOException iox) {
			iox.printStackTrace();
		}
	}

	private void prepareOmnetppHeader() {
		int i;
		String str = new String();
		str += "[Config common]\n";
		str += "description = \"Wireless smarthome scenario\"\n";
		str += "network = smarthome_network\n";
		str += "sim-time-limit = 10s\n";
		str += "\n";
		str += "#--------------------------------------------------------------------------------\n";
		str += "# Configuring visualizer\n";
		str += "#--------------------------------------------------------------------------------\n";
		str += "\n";
		str += "*.visualizer.sceneVisualizer.descriptionFigure = \"title\"\n";
		str += "\n";
		str += "*.visualizer.mediumVisualizer.displaySignals = false\n";
		str += "\n";
		str += "*.visualizer.physicalLinkVisualizer.displayLinks = true\n";
		str += "*.visualizer.physicalLinkVisualizer.packetFilter = \"Wifi* Zigbee* Bluetooth*\"\n";
		str += "\n";
		str += "#--------------------------------------------------------------------------------\n";
		str += "# Configuring global arp\n";
		str += "#--------------------------------------------------------------------------------\n";
		str += "\n";
		str += "*.host*.ipv4.arp.typename = \"GlobalArp\"\n";
		str += "*.host*.forwarding = false\n";
		str += "\n";
		str += "#--------------------------------------------------------------------------------\n";
		str += "# Configuring radio\n";
		str += "#--------------------------------------------------------------------------------\n";
		str += "\n";
		str += "*.host*.wlan[*].radio.typename = \"Ieee80211DimensionalRadio\"\n";
		str += "*.host*.wlan[*].radio.transmitter.dimensions = \"time frequency\"\n";
		str += "**.backgroundNoise.dimensions = \"time frequency\"\n";
		str += "*.radioMedium.pathLoss.typename = \"RayleighFading\"\n";
		str += "\n";
		str += "#--------------------------------------------------------------------------------\n";
		str += "# Configuring AP and devices positions\n";
		str += "#--------------------------------------------------------------------------------\n";
		str += "\n";

		int countWifi = 0;
		int countZigbee = 0;
		int countBluetooth = 0;

		if (this.env.getAmountOfAPs() > 0) {
			str += "# AP\n";
			str += "\n";
			str += "*.hostAP_*.mobility.typename = \"LinearMobility\"\n";
			str += "*.hostAP_*.mobility.initFromDisplayString = false\n";
			str += "*.hostAP_*.mobility.speed = 0mps\n";
			str += "*.hostAP_*.mobility.initialZ = 1.7m\n";
			str += "\n";

			for (AP ap : this.env.getWifiAPs()) {
				str += "\n";
				str += "# AP_Wifi[" + countWifi + "]\n";
				str += "*.hostAP_Wifi[" + countWifi + "].mobility.initialX = " + ap.getX() + "m\n";
				str += "*.hostAP_Wifi[" + countWifi + "].mobility.initialY = " + ap.getY() + "m\n";
				countWifi++;
			}

			for (AP ap : this.env.getZigbeeAPs()) {
				str += "\n";
				str += "# AP_Zigbee[" + countZigbee + "]\n";
				str += "*.hostAP_Zigbee[" + countZigbee + "].mobility.initialX = " + ap.getX() + "m\n";
				str += "*.hostAP_Zigbee[" + countZigbee + "].mobility.initialY = " + ap.getY() + "m\n";
				countZigbee++;
			}

			for (AP ap : this.env.getBluetoothAPs()) {
				str += "\n";
				str += "# AP_Bluetooth[" + countBluetooth + "]\n";
				str += "*.hostAP_Bluetooth[" + countBluetooth + "].mobility.initialX = " + ap.getX() + "m\n";
				str += "*.hostAP_Bluetooth[" + countBluetooth + "].mobility.initialY = " + ap.getY() + "m\n";
				countBluetooth++;
			}
		}
		if (this.env.getAmountOfDevices() > 0) {
			if (this.env.getHowManyWifiDevices() > 0) {
				str += "\n";
				str += "#Device_Wifi\n";
				str += "*.hostDevice_Wifi[*].mobility.typename = \"LinearMobility\"\n";
				str += "*.hostDevice_Wifi[*].mobility.initFromDisplayString = false\n";
				str += "*.hostDevice_Wifi[*].mobility.speed = 0mps\n";
				str += "*.hostDevice_Wifi[*].mobility.initialZ = 1.7m\n";
				str += "\n";

				i = 0;
				for(Device dev : this.env.getWifiDevs()) {
					str += "\n";
					str += "# Device_Wifi[" + i + "]\n";
					str += "*.hostDevice_Wifi[" + i + "].mobility.initialX = " + dev.getX() + "m\n";
					str += "*.hostDevice_Wifi[" + i + "].mobility.initialY = " + dev.getY() + "m\n";
					i++;
				}
			}
			if (this.env.getHowManyZigbeeDevices() > 0) {
				str += "\n";
				str += "#Device_Zigbee\n";
				str += "*.hostDevice_Zigbee[*].mobility.typename = \"LinearMobility\"\n";
				str += "*.hostDevice_Zigbee[*].mobility.initFromDisplayString = false\n";
				str += "*.hostDevice_Zigbee[*].mobility.speed = 0mps\n";
				str += "*.hostDevice_Zigbee[*].mobility.initialZ = 1.7m\n";
				str += "\n";

				i = 0;
				for(Device dev : this.env.getZigbeeDevs()) {
					str += "\n";
					str += "# Device_Zigbee[" + i + "]\n";
					str += "*.hostDevice_Zigbee[" + i + "].mobility.initialX = " + dev.getX() + "m\n";
					str += "*.hostDevice_Zigbee[" + i + "].mobility.initialY = " + dev.getY() + "m\n";
					i++;
				}
			}
			if (this.env.getHowManyBluetoothDevices() > 0) {
				str += "\n";
				str += "#Device_Bluetooth\n";
				str += "*.hostDevice_Bluetooth[*].mobility.typename = \"LinearMobility\"\n";
				str += "*.hostDevice_Bluetooth[*].mobility.initFromDisplayString = false\n";
				str += "*.hostDevice_Bluetooth[*].mobility.speed = 0mps\n";
				str += "*.hostDevice_Bluetooth[*].mobility.initialZ = 1.7m\n";
				str += "\n";

				i = 0;
				for(Device dev : this.env.getBluetoothDevs()) {
					str += "\n";
					str += "# Device_Bluetooth[" + i + "]\n";
					str += "*.hostDevice_Bluetooth[" + i + "].mobility.initialX = " + dev.getX() + "m\n";
					str += "*.hostDevice_Bluetooth[" + i + "].mobility.initialY = " + dev.getY() + "m\n";
					i++;
				}
			}
		}
		str += "\n";
		str += "#--------------------------------------------------------------------------------\n";
		str += "# Configuring AP and devices radio parameters\n";
		str += "#--------------------------------------------------------------------------------\n";
		if (this.env.getAmountOfAPs() > 0) {
			str += "\n";
			str += "# AP_Wifi\n";
			str += "*.hostAP_Wifi[*].wlan[*].radio.bandwidth = 20MHz\n";
			str += "*.hostAP_Wifi[*].wlan[*].radio.transmitter.power = 100mW\n";
			str += "*.hostAP_Wifi[*].wlan[*].radio.bandName = \"2.4 GHz\"\n";
			str += "\n";
			str += "# AP_Zigbee\n";
			str += "\n";
			str += "*.hostAP_Zigbee[*].wlan[*].radio.bandwidth = 2MHz\n";
			str += "*.hostAP_Zigbee[*].wlan[*].radio.transmitter.power = 1mW\n";
			str += "*.hostAP_Zigbee[*].wlan[*].radio.bandName = \"ZigbeeBand\"\n";
			str += "\n";
			str += "# AP_Bluetooth\n";
			str += "\n";
			str += "*.hostAP_Bluetooth[*].wlan[*].radio.bandwidth = 1MHz\n";
			str += "*.hostAP_Bluetooth[*].wlan[*].radio.transmitter.power = 1mW\n";
			str += "*.hostAP_Bluetooth[*].wlan[*].radio.bandName = \"BluetoothBand\"\n";
		}
		if (this.env.getHowManyWifiDevices() > 0) {
			str += "\n";
			str += "# Device_Wifi\n";
			str += "\n";
			str += "*.hostDevice_Wifi[*].wlan[*].radio.bandwidth = 20MHz\n";
			str += "*.hostDevice_Wifi[*].wlan[*].radio.transmitter.power = 100mW\n";
			str += "*.hostDevice_Wifi[*].wlan[*].radio.bandName = \"2.4 GHz\"\n";
		}
		if (this.env.getHowManyZigbeeDevices() > 0) {
			str += "\n";
			str += "#Device_Zigbee\n";
			str += "\n";
			str += "*.hostDevice_Zigbee[*].wlan[*].radio.bandwidth = 2MHz\n";
			str += "*.hostDevice_Zigbee[*].wlan[*].radio.transmitter.power = 1mW\n";
			str += "*.hostDevice_Zigbee[*].wlan[*].radio.bandName = \"ZigbeeBand\"\n";
		}
		if (this.env.getHowManyBluetoothDevices() > 0) {
			str += "\n";
			str += "#Device_Bluetooth\n";
			str += "\n";
			str += "*.hostDevice_Bluetooth[*].wlan[*].radio.bandwidth = 1MHz\n";
			str += "*.hostDevice_Bluetooth[*].wlan[*].radio.transmitter.power = 1mW\n";
			str += "*.hostDevice_Bluetooth[*].wlan[*].radio.bandName = \"BluetoothBand\"\n";
		}
		str += "\n";
		str += "#--------------------------------------------------------------------------------\n";
		str += "# Configuring devices applications\n";
		str += "#--------------------------------------------------------------------------------\n";
		str += "\n";
		str += "# Wifi, Zigbee and Bluetooth\n";
		str += "\n";
		str += "*.hostDevice_*.numApps = 1\n";
		str += "*.hostDevice_*.app[0].typename = \"UdpBasicApp\"\n";
		str += "*.hostDevice_*.app[0].destPort = 5000\n";
		str += "*.hostDevice_*.app[0].messageLength = 500B\n";
		str += "*.hostDevice_*.app[0].startTime = 0s\n";
		str += "*.hostDevice_*.app[0].sendInterval = 1ms\n";
		str += "*.hostDevice_*.app[0].stopTime = 10s\n";
		str += "\n";
		str += "#--------------------------------------------------------------------------------\n";
		str += "# Configuring AP applications\n";
		str += "#--------------------------------------------------------------------------------\n";
		str += "\n";
		str += "# Wifi, Zigbee and Blueooth\n";
		str += "\n";
		str += "*.hostAP_*.numApps = 1\n";
		str += "*.hostAP_*.app[0].typename = \"UdpSink\"\n";
		str += "*.hostAP_*.app[0].localPort = 5000\n";
		str += "*.hostAP_*.app[0].startTime = 0s\n";
		str += "\n";

		this.strIni = str;
	}

	public void prepareOmnetppSAME() {

		strIni += "\n[Config SAME]\n";
		strIni += "extends = common\n";
		strIni += "#--------------------------------------------------------------------------------\n";
		strIni += "# Configuring AP and devices channels\n";
		strIni += "#--------------------------------------------------------------------------------\n";
		strIni += "\n";
		strIni += "# Device channels and connections\n";


		int deviceWifi = 0;
		int deviceZigbee = 0;
    	int deviceBluetooth = 0;
    	ArrayList<Device> sameConfig = new ArrayList<Device>();
    	util.copyDevices(this.env.getDevices(), sameConfig);

		for (Device dev : this.env.getDevices()) {
			if(dev.getType().equals(DeviceType.WIFI)) {
				strIni += "*.hostDevice_Wifi[" + deviceWifi + "].app[0].destAddresses = \"hostAP_Wifi["
						+ dev.getConnectedAPId() / 3 + "]\"\n";
				strIni += "*.hostDevice_Wifi[" + deviceWifi + "].app[0].packetName = \"Wifi_Device[" + deviceWifi
						+ "]-\"\n";
				strIni += "*.hostDevice_Wifi[" + deviceWifi + "].wlan[*].radio.channelNumber = 0\n";

				deviceWifi++;
			}
			if(dev.getType().equals(DeviceType.ZIGBEE)) {
				strIni += "*.hostDevice_Zigbee[" + deviceZigbee + "].app[0].destAddresses = \"hostAP_Zigbee["
						+ dev.getConnectedAPId() / 3 + "]\"\n";
				strIni += "*.hostDevice_Zigbee[" + deviceZigbee + "].app[0].packetName = \"Zigbee_Device["
						+ deviceZigbee + "]-\"\n";
				strIni += "*.hostDevice_Zigbee[" + deviceZigbee + "].wlan[*].radio.channelNumber = 0\n";

				deviceZigbee++;
			}
			if(dev.getType().equals(DeviceType.BLUETOOTH)) {
				strIni += "*.hostDevice_Bluetooth[" + deviceBluetooth
						+ "].app[0].destAddresses = \"hostAP_Bluetooth[" + dev.getConnectedAPId() / 3
						+ "]\"\n";
				strIni += "*.hostDevice_Bluetooth[" + deviceBluetooth + "].app[0].packetName = \"Bluetooth_Device["
						+ deviceBluetooth + "]-\"\n";
				strIni += "*.hostDevice_Bluetooth[" + deviceBluetooth + "].wlan[*].radio.channelNumber = 0\n";

				deviceBluetooth++;
			}
		}

		strIni += "#AP channels\n";
		strIni += "*.hostAP_Wifi[*].wlan[*].radio.channelNumber = 0\n";
		strIni += "*.hostAP_Zigbee[*].wlan[*].radio.channelNumber = 0\n";
		strIni += "*.hostAP_Bluetooth[*].wlan[*].radio.channelNumber = 0\n";

    for(Device device: sameConfig){
      device.setChannel(0);
	}
    sameInterference = this.util.getDevicesInterference(sameConfig);
    System.out.println("Same config interference: " + sameInterference);
  }

	public void prepareOmnetppRANDOM() {
		strIni += "[Config RANDOM]\n";
		strIni += "extends = common\n";
		strIni += "#--------------------------------------------------------------------------------\n";
		strIni += "# Configuring AP and devices channels\n";
		strIni += "#--------------------------------------------------------------------------------\n";
		strIni += "\n";
		strIni += "# Device channels and connections\n";

		// TODO: Check if channel conversion is right

		int deviceWifi = 0;
		int deviceZigbee = 0;
		int deviceBluetooth = 0;
    int ch = 0;

    ArrayList<AP> copyAPs = new ArrayList<AP>();
    ArrayList<Device> copyDevices = new ArrayList<Device>();
		util.copyAPs(this.env.getAPs(), copyAPs);
    util.copyDevices(this.env.getDevices(), copyDevices);

		for (AP ap : copyAPs) {
			DeviceType type = ap.getType();

			switch (type) {
			case WIFI:
				ch = generateRandomChannel(0, 12);
				ap.setChannel(ch);
				break;
			case ZIGBEE:
				ch = generateRandomChannel(13, 28);
				ap.setChannel(ch);
				break;
			case BLUETOOTH:
				ch = generateRandomChannel(29, 65);
				ap.setChannel(ch);
				break;

			default:
				break;

			}
		}

		AP auxAP = new AP();

		for (Device dev : this.env.getDevices()) {
			for (AP ap : copyAPs) {
				if(dev.getConnectedAPId() == ap.getId()) {
					auxAP = ap;
					break;
				}
			}


			if(dev.getType().equals(DeviceType.WIFI)) {
				strIni += "*.hostDevice_Wifi[" + deviceWifi + "].app[0].destAddresses = \"hostAP_Wifi["
						+ dev.getConnectedAPId() / 3 + "]\"\n";
				strIni += "*.hostDevice_Wifi[" + deviceWifi + "].app[0].packetName = \"Wifi_Device[" + deviceWifi
						+ "]-\"\n";
				strIni += "*.hostDevice_Wifi[" + deviceWifi + "].wlan[*].radio.channelNumber = " + auxAP.getChannel() + "\n";

				deviceWifi++;
			}
			if(dev.getType().equals(DeviceType.ZIGBEE)) {
				strIni += "*.hostDevice_Zigbee[" + deviceZigbee + "].app[0].destAddresses = \"hostAP_Zigbee["
						+ dev.getConnectedAPId() / 3 + "]\"\n";
				strIni += "*.hostDevice_Zigbee[" + deviceZigbee + "].app[0].packetName = \"Zigbee_Device["
						+ deviceZigbee + "]-\"\n";
				strIni += "*.hostDevice_Zigbee[" + deviceZigbee + "].wlan[*].radio.channelNumber = "
						+ (auxAP.getChannel() - 13) + "\n";

				deviceZigbee++;
			}
			if(dev.getType().equals(DeviceType.BLUETOOTH)) {
				strIni += "*.hostDevice_Bluetooth[" + deviceBluetooth
						+ "].app[0].destAddresses = \"hostAP_Bluetooth[" + dev.getConnectedAPId() / 3
						+ "]\"\n";
				strIni += "*.hostDevice_Bluetooth[" + deviceBluetooth + "].app[0].packetName = \"Bluetooth_Device["
						+ deviceBluetooth + "]-\"\n";
				strIni += "*.hostDevice_Bluetooth[" + deviceBluetooth + "].wlan[*].radio.channelNumber = "
						+ (auxAP.getChannel() - 29) + "\n";

				deviceBluetooth++;
      }
		}

		strIni += "#AP channels\n";

		int apID = 0;
		for(AP ap : copyAPs) {
			if(ap.getAmountOfDevicesConnected() == 0) {
				apID++;
				continue; // AP not used
			}

			if (apID % 3 == 0) { // Wifi AP
				strIni += "*.hostAP_Wifi[" + apID / 3 + "].wlan[*].radio.channelNumber = " + ap.getChannel() + "\n";
			}
			if (apID % 3 == 1) { // Zigbee AP
				strIni += "*.hostAP_Zigbee[" + apID / 3 + "].wlan[*].radio.channelNumber = " + (ap.getChannel() - 13) + "\n";
			}
			if (apID % 3 == 2) { // Bluetooth AP
				strIni += "*.hostAP_Bluetooth[" + apID / 3 + "].wlan[*].radio.channelNumber = " + (ap.getChannel() - 29) + "\n";
			}
			apID++;
    }

    for(Device device: copyDevices){
      for(AP ap: copyAPs){
        if(device.getConnectedAPId() == ap.getId()){
          device.setChannel(ap.getChannel());
          break;
        }
      }
    }
    randomInterference = this.util.getDevicesInterference(copyDevices);
    System.out.println("Random config interference: " + randomInterference);
	}

	public void prepateOmnetppCustom() {
		strIni += "[Config CUSTOM]\n";
		strIni += "extends = common\n";
		strIni += "#--------------------------------------------------------------------------------\n";
		strIni += "# Configuring AP and devices channels\n";
		strIni += "#--------------------------------------------------------------------------------\n";
		strIni += "\n";
		strIni += "# Device channels and connections\n";

		// TODO: Check if channel conversion is right

		int deviceWifi = 0;
		int deviceZigbee = 0;
    int deviceBluetooth = 0;

		for (Device dev : this.env.getDevices()) {
			if(dev.getType().equals(DeviceType.WIFI)) {
				strIni += "*.hostDevice_Wifi[" + deviceWifi + "].app[0].destAddresses = \"hostAP_Wifi["
						+ dev.getConnectedAPId() / 3 + "]\"\n";
				strIni += "*.hostDevice_Wifi[" + deviceWifi + "].app[0].packetName = \"Wifi_Device[" + deviceWifi
						+ "]-\"\n";
				strIni += "*.hostDevice_Wifi[" + deviceWifi + "].wlan[*].radio.channelNumber = " + dev.getChannel() + "\n";

				deviceWifi++;
			}
			if(dev.getType().equals(DeviceType.ZIGBEE)) {
				strIni += "*.hostDevice_Zigbee[" + deviceZigbee + "].app[0].destAddresses = \"hostAP_Zigbee["
						+ dev.getConnectedAPId() / 3 + "]\"\n";
				strIni += "*.hostDevice_Zigbee[" + deviceZigbee + "].app[0].packetName = \"Zigbee_Device["
						+ deviceZigbee + "]-\"\n";
				strIni += "*.hostDevice_Zigbee[" + deviceZigbee + "].wlan[*].radio.channelNumber = "
						+ (dev.getChannel() - 13) + "\n";

				deviceZigbee++;
			}
			if(dev.getType().equals(DeviceType.BLUETOOTH)) {
				strIni += "*.hostDevice_Bluetooth[" + deviceBluetooth
						+ "].app[0].destAddresses = \"hostAP_Bluetooth[" + dev.getConnectedAPId() / 3
						+ "]\"\n";
				strIni += "*.hostDevice_Bluetooth[" + deviceBluetooth + "].app[0].packetName = \"Bluetooth_Device["
						+ deviceBluetooth + "]-\"\n";
				strIni += "*.hostDevice_Bluetooth[" + deviceBluetooth + "].wlan[*].radio.channelNumber = "
						+ (dev.getChannel() - 29) + "\n";

				deviceBluetooth++;
      }
		}

		strIni += "#AP channels\n";

		int apID = 0;
		for(AP ap : this.env.getAPs()) {
			if(ap.getAmountOfDevicesConnected() == 0) {
				apID++;
				continue; // AP not used
			}

			if (apID % 3 == 0) { // Wifi AP
				strIni += "*.hostAP_Wifi[" + apID / 3 + "].wlan[*].radio.channelNumber = " + ap.getChannel() + "\n";
			}
			if (apID % 3 == 1) { // Zigbee AP
				strIni += "*.hostAP_Zigbee[" + apID / 3 + "].wlan[*].radio.channelNumber = " + (ap.getChannel() - 13) + "\n";
			}
			if (apID % 3 == 2) { // Bluetooth AP
				strIni += "*.hostAP_Bluetooth[" + apID / 3 + "].wlan[*].radio.channelNumber = " + (ap.getChannel() - 29) + "\n";
			}
			apID++;
    }
    customInterference = this.util.getDevicesInterference(this.env.getDevices());
    System.out.println("Custom config interference: " + customInterference);
	}

	public void prepareOmnetppNED() {

		int countAP = this.env.getAmountOfAPs() / 3;
		strNed += "import inet.networklayer.configurator.ipv4.Ipv4NetworkConfigurator;\n";
		strNed += "import inet.node.inet.AdhocHost;\n";
		strNed += "import inet.node.inet.WirelessHost;\n";
		strNed += "import inet.physicallayer.ieee80211.packetlevel.Ieee80211DimensionalRadioMedium;\n";
		strNed += "import inet.visualizer.contract.IIntegratedVisualizer;\n";
		strNed += "\n";
		strNed += "\n";
		strNed += "network smarthome_network\n";
		strNed += "{\n";
		strNed += "    parameters:\n";
		strNed += "      int numAP = " + countAP + ";\n";
		if (this.env.getHowManyWifiDevices() > 0)
			strNed += "      int numDeviceWifi = " + this.env.getHowManyWifiDevices() + ";\n";
		if (this.env.getHowManyZigbeeDevices() > 0)
			strNed += "      int numDeviceZigbee = " + this.env.getHowManyZigbeeDevices() + ";\n";
		if (this.env.getHowManyBluetoothDevices() > 0)
			strNed += "      int numDeviceBluetooth = " + this.env.getHowManyBluetoothDevices() + ";\n";
		strNed += "      @display(\"bgb=100,100;bgg=100,1,grey95\");\n";
		strNed += "      @figure[title](type=label; pos=0,-1; anchor=sw; color=darkblue);\n";
		strNed += "\n";
		strNed += "    submodules:\n";
		strNed += "      visualizer: <default(\"IntegratedCanvasVisualizer\")> like IIntegratedVisualizer if hasVisualizer() {\n";
		strNed += "        @display(\"p=0,0\");\n";
		strNed += "      }\n";
		strNed += "      configurator: Ipv4NetworkConfigurator {\n";
		strNed += "        @display(\"p=0,0\");\n";
		strNed += "      }\n";
		strNed += "      radioMedium: Ieee80211DimensionalRadioMedium {\n";
		strNed += "        @display(\"p=0,0\");\n";
		strNed += "      }\n";
		strNed += "      hostAP_Wifi[numAP]: AdhocHost {\n";
		strNed += "        parameters:\n";
		strNed += "          @display(\"p=0,0;i=device/accesspoint\");\n";
		strNed += "      }\n";
		strNed += "      hostAP_Zigbee[numAP]: AdhocHost {\n";
		strNed += "        parameters:\n";
		strNed += "          @display(\"p=0,0;i=device/accesspoint\");\n";
		strNed += "      }\n";
		strNed += "      hostAP_Bluetooth[numAP]: AdhocHost {\n";
		strNed += "        parameters:\n";
		strNed += "          @display(\"p=0,0;i=device/accesspoint\");\n";
		strNed += "      }\n";
		if (this.env.getHowManyWifiDevices() > 0) {
			strNed += "      hostDevice_Wifi[numDeviceWifi]: AdhocHost {\n";
			strNed += "        parameters:\n";
			strNed += "          @display(\"p=0,0;i=device/wifilaptop\");\n";
			strNed += "      }\n";
		}
		if (this.env.getHowManyZigbeeDevices() > 0) {
			strNed += "      hostDevice_Zigbee[numDeviceZigbee]: AdhocHost {\n";
			strNed += "        parameters:\n";
			strNed += "          @display(\"p=0,0;i=device/wifilaptop\");\n";
			strNed += "      }\n";
		}
		if (this.env.getHowManyBluetoothDevices() > 0) {
			strNed += "      hostDevice_Bluetooth[numDeviceBluetooth]: AdhocHost {\n";
			strNed += "        parameters:\n";
			strNed += "          @display(\"p=0,0;i=device/wifilaptop\");\n";
			strNed += "      }\n";
		}
		strNed += "}\n";
	}

	private int generateRandomChannel(int min, int max) {
		Random foo = new Random();
		int randomNumber = foo.nextInt((max + 1) - min) + min;

		return randomNumber;
	}

	public String getRandom() {
		return random;
	}

	

	public String getCustom() {
		return custom;
	}

	public void setRandom(String random) {
		this.random = random;
	}

	public void setCustom(String custom) {
		this.custom = custom;
	}
	

	
	
}
