import java.util.ArrayList;

public interface APsManagerInterface{
    public void createZigbeeAP(int id, ArrayList<AP> accessPoints);
    public void createWifiAP(int id, ArrayList<AP> accessPoints);
    public void createBluetoothAP(int id, ArrayList<AP> accessPoints);
    public ArrayList<AP> addAP(ArrayList<AP> accessPoints);
    public int[] initializeAPs(int[] aux, ArrayList<AP> accessPoints);
    public void fillAPsInfo(ArrayList<Device> devices, ArrayList<AP> accessPoints);
}