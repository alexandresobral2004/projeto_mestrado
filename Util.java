import java.util.ArrayList;
import java.util.Random;

public class Util{
    private float min_dBm = -80.0f;
    private float max_dBm = +35.0f;
    private float[][] w;

    public Util(){};

    public Util(float[][] w){
        this.w = w;
    }

    public float getDistance(Object node1, Object node2) {
        float distance = 0.0f;
        if (node1 instanceof Device && node2 instanceof Device) {
          Device device1 = (Device) node1;
          Device device2 = (Device) node2;
          distance = (float) Math.pow((
            Math.pow((device1.getX() - device2.getX()), 2) +
            Math.pow((device1.getY() - device2.getY()), 2)),
          0.5);
        }
        else if(node1 instanceof Device && node2 instanceof AP){
          Device device = (Device) node1;
          AP ap = (AP) node2;
          distance = (float) Math.pow((
            Math.pow((device.getX() - ap.getX()), 2) +
            Math.pow((device.getY() - ap.getY()), 2)),
          0.5);
        }
        else {
          AP ap1 = (AP) node1;
          AP ap2 = (AP) node2;
          distance = (float) Math.pow((
            Math.pow((ap1.getX() - ap2.getX()), 2) +
            Math.pow((ap1.getY() - ap2.getY()), 2)),
          0.5);
        }
        return distance;
      }

      public float getAPsInterference(ArrayList<AP> accessPoints) {
        float interference = 0.0f;
        for (int i = 0; i < accessPoints.size(); i++) {
          for (int j = 0; j < accessPoints.size(); j++) {
            if (i != j) {
              interference = interference + this.getNormalizedInterference(
                accessPoints.get(i),
                accessPoints.get(i).getChannel(),
                accessPoints.get(j),
                accessPoints.get(j).getChannel()
              );
            }
          }
        }
        return interference;
      }

      public float getDevicesInterference(ArrayList<Device> devices) {
        float interference = 0.0f;
        for (int i = 0; i < devices.size(); i++) {
          for (int j = 0; j < devices.size(); j++) {
            if (i != j) {
              interference = interference + this.getNormalizedInterference(
                devices.get(i),
                devices.get(i).getChannel(),
                devices.get(j),
                devices.get(j).getChannel()
              );
            }
          }
        }
        return interference;
      }

      public float getPathLoss(float distance) {
        float pl = 1.0f;
        if (0.5f < distance && distance <= 8.0f)
          pl = 40.2f + (20.0f * (float) Math.log10(distance));
        if (distance > 8.0f)
          pl = 58.5f + 33.0f * ((float) Math.log10((distance / 8.0f)));
        return pl;
      }

      public float getNormalizedInterference(AP ap1, int ap1Channel,
                                             AP ap2, int ap2Channel) {
        float antennaPower,
              antennaPowerDBM,
              pathLoss,
              interferenceFactor,
              distance,
              interference;

        distance = getDistance(ap1, ap2);
        interferenceFactor = getInterferenceFactor(ap1Channel, ap2Channel);

        antennaPowerDBM = getAntennaPowerFromAP(ap2);

        antennaPower = getAntennaPowerNormalization(antennaPowerDBM);

        pathLoss = getPathLoss(distance);

        interference = antennaPower * interferenceFactor / pathLoss;

        if (ap1.equals(ap2))
          interference = 0.0f;

        return interference;
      }

      public float getNormalizedInterference(Device device1, int device1Channel,
                                             Device device2, int device2Channel) {
        float antennaPower,
              antennaPowerDBM,
              pathLoss,
              interferenceFactor,
              distance,
              interference;

        distance = getDistance(device1, device2);
        interferenceFactor = getInterferenceFactor(device1Channel, device2Channel);

        antennaPowerDBM = getAntennaPowerFromDevice(device2);

        antennaPower = getAntennaPowerNormalization(antennaPowerDBM);

        pathLoss = getPathLoss(distance);

        interference = antennaPower * interferenceFactor / pathLoss;

        if (device1.equals(device2))
          interference = 0.0f;

        return interference;
      }

      public float getAntennaPowerFromAP(AP ap) {
        return ap.getAntennaPowerAP();
      }

      public float getAntennaPowerFromDevice(Device device) {
        return device.getAntennaPowerDevice();
      }

      public float getInterferenceFactor(int channel_z, int channel_m) {
        return this.w[channel_z][channel_m];
      }

      public float getAntennaPowerNormalization(float value_dBm) {
        return 1 - ((this.max_dBm - value_dBm) / (this.max_dBm - this.min_dBm));
      }

      public void generateAPsRandomChannels(ArrayList<AP> accessPoints) {
        for (AP ap : accessPoints) {
          if (ap.getType().toString() == "WIFI") {
            int channel = getRandomNumberFrom(0, 12);
            ap.setChannel(channel);
          } else if (ap.getType().toString() == "ZIGBEE") {
            int channel = getRandomNumberFrom(13, 28);
            ap.setChannel(channel);
          } else {
            int channel = getRandomNumberFrom(29, 65);
            ap.setChannel(channel);
          }
        }
      }

      public int getRandomNumberFrom(int min, int max) {
        Random foo = new Random();
        int randomNumber = foo.nextInt((max + 1) - min) + min;

        return randomNumber;
      }

      public float[] fillProportion(int numberOfDevices,
                                  float currentAmount,
                                  float[] proportions,
                                  float[] fractionalParts
                                  ){
        int turn = 2;
        while(currentAmount < numberOfDevices){
          if(turn == 2 && fractionalParts[turn] > 0f){
            proportions[turn]++;
            currentAmount++;
            turn = 0;
          }
          else if(turn == 0 && fractionalParts[turn] > 0f){
            proportions[turn]++;
            currentAmount++;
            turn = 1;
          }
          else if(turn == 1 && fractionalParts[turn] > 0f){
            proportions[turn]++;
            currentAmount++;
            turn = 2;
          }
        }
        return proportions;
      }

      public float[] setProportion(int numberOfDevices, float[] proportions){

        float wifiIntegerPart;
        float zigbeeIntegerPart;
        float bluetoothIntegerPart;
        float[] fractionalParts = {0, 0, 0};
        float fractionalPart;

        proportions[0] = 40f / 100f * numberOfDevices;
        wifiIntegerPart = (int) proportions[0];
        fractionalPart = proportions[0] - wifiIntegerPart;
        proportions[0] = wifiIntegerPart;
        fractionalParts[0] = fractionalPart;

        proportions[1] = 50f / 100f * numberOfDevices;
        zigbeeIntegerPart = (int) proportions[1];
        fractionalPart = proportions[1] - zigbeeIntegerPart;
        proportions[1] = zigbeeIntegerPart;
        fractionalParts[1] = fractionalPart;

        proportions[2] = 10f / 100f * numberOfDevices;
        bluetoothIntegerPart = (int) proportions[2];
        fractionalPart = proportions[2] - bluetoothIntegerPart;
        proportions[2] = bluetoothIntegerPart;
        fractionalParts[2] = fractionalPart;

        float currentAmount = proportions[0] + proportions[1] + proportions[2];
        proportions = fillProportion(numberOfDevices,
                                     currentAmount,
                                     proportions,
                                     fractionalParts);
        return proportions;
      }

      public void copyAPs(ArrayList<AP> source, ArrayList<AP> target) {
        AP auxAP = null;
        target.clear();

        for (AP ap : source) {
          auxAP = new AP();
          auxAP.setId(ap.getId());
          auxAP.setType(ap.getType());
          auxAP.setChannel(ap.getChannel());
          auxAP.setConnectedDevices(ap.getConnectedDevices());
          auxAP.setX(ap.getX());
          auxAP.setY(ap.getY());
          auxAP.setAntennaPowerAP(ap.getAntennaPowerAP());
          target.add(auxAP);
        }
      }

      public void copyDevices(ArrayList<Device> source, ArrayList<Device> target) {

        Device auxDevice = null;
        target.clear();

        for(Device dev : source) {
          auxDevice = new Device();

          auxDevice.setId(dev.getId());
          auxDevice.setType(dev.getType());
          auxDevice.setChannel(dev.getChannel());
          auxDevice.setReachableAPs(dev.getReachableAPs());
          auxDevice.setConnectedAP(dev.getConnectedAP());
          auxDevice.setX(dev.getX());
          auxDevice.setY(dev.getY());
          auxDevice.setAntennaPowerDevice(dev.getAntennaPowerDevice());
          target.add(auxDevice);
        }
      }
}