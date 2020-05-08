import java.util.ArrayList;

public interface DevicesManagerInterface {
    public void createWifiDevice(int id, ArrayList<Device> devices, AP ap);
    public void createZigbeeDevice(int id, ArrayList<Device> devices, AP ap);
    public void createBluetoothDevice(int id, ArrayList<Device> devices, AP ap);
    public ArrayList<Device> addDev(ArrayList<Device> devices, ArrayList<AP> accessPoints);
    public void setDevicesReachablesAPs(int[] aux, ArrayList<Device> devices, ArrayList<AP> accessPoints);
    public void setAPsReachablesDevices(ArrayList<Device> devices, ArrayList<AP> accessPoints);
    public void fillDevicesInfo(ArrayList<Device> devices);
    public void connectDevicesToAPs(int[] aux, ArrayList<Device> devices);
    public AP findSuitableAP(ArrayList<AP> accessPoints, DeviceType type, int index);
}
