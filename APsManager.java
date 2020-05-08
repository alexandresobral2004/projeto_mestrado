import java.util.ArrayList;

public class APsManager implements APsManagerInterface{

    private int numberOfAPs;
    private PositionerInterface positioner;

    public APsManager(int numberOfAPs, PositionerInterface positioner){
        this.numberOfAPs = numberOfAPs;
        this.positioner = positioner;
    }

    @Override
    public void createWifiAP(int id, ArrayList<AP> accessPoints){
      AP ap = new AP(id, DeviceType.WIFI, 20.0f);
      positioner.setAPPosition(DeviceType.WIFI, ap);
      accessPoints.add(ap);
    }

    @Override
    public void createZigbeeAP(int id, ArrayList<AP> accessPoints){
      AP ap = new AP(id, DeviceType.ZIGBEE, 0.0f);
      positioner.setAPPosition(DeviceType.ZIGBEE, ap);
      accessPoints.add(ap);
    }

    @Override
    public void createBluetoothAP(int id, ArrayList<AP> accessPoints){
      AP ap = new AP(id, DeviceType.BLUETOOTH, 4.0f);
      positioner.setAPPosition(DeviceType.BLUETOOTH, ap);
      accessPoints.add(ap);
    }

    @Override
    public ArrayList<AP> addAP(ArrayList<AP> accessPoints) {
        int id = 0;
        for(int j = 0; j < numberOfAPs; j++){
            createWifiAP(id, accessPoints);
            id++;

            createZigbeeAP(id, accessPoints);
            id++;

            createBluetoothAP(id, accessPoints);
            id++;
        }
        return accessPoints;
      }

      @Override
      public int[] initializeAPs(int[] aux, ArrayList<AP> accessPoints) {
        aux = new int[accessPoints.size()];
        for(int i = 0; i < aux.length; i++){
          aux[i] = 0;
        }
        return aux;
      }

      @Override
      public void fillAPsInfo(ArrayList<Device> devices, ArrayList<AP> accessPoints) {
        for (Device device : devices) {
          device.getConnectedAP().connectDeviceToAP(device);
        }
      }

}