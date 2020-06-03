import java.util.ArrayList;

public class DevicesManager implements DevicesManagerInterface {

    private int wifiIndex = 0;
    private int zigbeeIndex = 0;
    private int bluetoothIndex = 0;
    private int numberOfDevices;
    private float[] proportions = {0, 0, 0};
    private PositionerInterface positioner;
    private Util util;

    public DevicesManager(int numberOfDevices, PositionerInterface positioner){
        this.numberOfDevices = numberOfDevices;
        this.positioner = positioner;
        util = new Util();
    }

    @Override
    public void createWifiDevice(int id, ArrayList<Device> devices, AP ap){
      Device device = new Device(id, DeviceType.WIFI, 20.0f);
      positioner.setDevicePosition(DeviceType.WIFI, device, ap);
      devices.add(device);
    }

    @Override
    public void createZigbeeDevice(int id, ArrayList<Device> devices, AP ap){
        Device device = new Device(id, DeviceType.ZIGBEE, 0.0f);
        positioner.setDevicePosition(DeviceType.ZIGBEE, device, ap);
        devices.add(device);
    }

    @Override
    public void createBluetoothDevice(int id, ArrayList<Device> devices, AP ap){
        Device device = new Device(id, DeviceType.BLUETOOTH, 4.0f);
        positioner.setDevicePosition(DeviceType.BLUETOOTH, device, ap);
        devices.add(device);
    }

    @Override
    public ArrayList<Device> addDev(ArrayList<Device> devices, ArrayList<AP> accessPoints) {
        int id = 0;
        float wifiAmount, zigbeeAmount, bluetoothAmount;
        int index = numberOfDevices;
        proportions = util.setProportion(numberOfDevices, proportions);
        wifiAmount = proportions[0];
        zigbeeAmount = proportions[1];
        bluetoothAmount = proportions[2];
        while(index > 0){
            while(wifiAmount > 0)
            {
                wifiIndex++;
                AP ap = findSuitableAP(accessPoints, DeviceType.WIFI, wifiIndex);
                createWifiDevice(id, devices, ap);
                id++;
                wifiAmount--;
                index--;
            }
            while(zigbeeAmount > 0){
                zigbeeIndex++;
                AP ap = findSuitableAP(accessPoints, DeviceType.ZIGBEE, zigbeeIndex);
                createZigbeeDevice(id, devices, ap);
                id++;
                zigbeeAmount--;
                index--;
            }
            while(bluetoothAmount > 0){
                bluetoothIndex++;
                AP ap = findSuitableAP(accessPoints, DeviceType.BLUETOOTH, bluetoothIndex);
                createBluetoothDevice(id, devices, ap);
                id++;
                bluetoothAmount--;
                index--;
            }
        }
        return devices;
    }

    @Override
    public void setDevicesReachablesAPs(int[] aux, ArrayList<Device> devices, ArrayList<AP> accessPoints) {
      for(Device device: devices){
        for(AP ap: accessPoints){
            if(device.getType() == ap.getType()){
                if(device.getType() == DeviceType.WIFI){
                    if(util.getDistance(device, ap) <= positioner.getWifi_max())
                    {
                      device.addReachableAP(ap);
                    }
                }
                if(device.getType() == DeviceType.ZIGBEE){
                    if(util.getDistance(device, ap) <= positioner.getZigbee_max())
                    {
                      device.addReachableAP(ap);
                    }
                }
                if(device.getType() == DeviceType.BLUETOOTH){
                    if(util.getDistance(device, ap) <= positioner.getBluetooth_max())
                    {
                      device.addReachableAP(ap);
                    }
                }
            }
        }
        if(device.getReachableAPs().size() == 1){
          AP ap = device.getReachableAPs().get(0);
          device.setConnectedAP(ap);
          aux[ap.getId()] += 1;
        }
      }
    }

    @Override
    public void setAPsReachablesDevices(ArrayList<Device> devices, ArrayList<AP> accessPoints) {
      for(AP ap: accessPoints){
        for(Device device: devices){
          if(ap.getType() == DeviceType.WIFI){
            if(util.getDistance(device, ap) <= positioner.getWifi_max())
            {
              ap.getReachableDevices().add(device);
            }
          }
          if(ap.getType() == DeviceType.ZIGBEE){
            if(util.getDistance(device, ap) <= positioner.getZigbee_max())
            {
              ap.getReachableDevices().add(device);
            }
          }
          if(ap.getType() == DeviceType.BLUETOOTH){
            if(util.getDistance(device, ap) <= positioner.getBluetooth_max())
            {
              ap.getReachableDevices().add(device);
            }
          }
        }
      }
    }

    public void fillDevicesInfo(ArrayList<Device> devices) {
      for (Device device : devices) {
        int devChannel = device.getConnectedAP().getChannel();
        device.setChannel(devChannel);
      }
    }

    public void connectDevicesToAPs(int[] aux, ArrayList<Device> devices) {
      for (Device device : devices) {
        if (device.getReachableAPs().size() > 1) {
          AP bestAP = device.getReachableAPs().get(0);
          for (AP ap : device.getReachableAPs()) {
            if (aux[ap.getId()] < aux[bestAP.getId()])
              bestAP = ap;
          }
          device.setConnectedAP(bestAP);
          aux[bestAP.getId()] += 1;
        }
      }
    }

    public int getNumberOfDevices(){
    return this.numberOfDevices;
  }

  public AP findSuitableAP(ArrayList<AP> accessPoints, DeviceType type, int index){
    AP suitableAP = new AP();
    boolean suitable = false;
    while(!suitable){
      for(int i = 0; i < accessPoints.size(); i++){
        if(accessPoints.get(i).getType() == type){
          suitableAP = accessPoints.get(i);
          index--;
          if(index == 0){
            suitable = true;
            break;
          }
          if(i == accessPoints.size() - 1)
            i = 0;
        }
      }
    }
    return suitableAP;
  }

}