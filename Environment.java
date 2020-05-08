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
