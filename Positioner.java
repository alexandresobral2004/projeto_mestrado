import java.util.Random;

public class Positioner implements PositionerInterface {
    private float wifi_max = 42;
    private float zigbee_max = 30f;
    private float bluetooth_max = 10f;
    private float max_reach = 400;

    Positioner(Util util){}

    public void setDevicePosition(DeviceType deviceType, Device device, AP ap){

        double maxX, minX, maxY, minY;
        double wifi_reach = wifi_max / 1.5;
        double zigbee_reach = zigbee_max / 1.5;
        double bluetooth_reach = bluetooth_max / 1.5;
        Random random = new Random();
        double x, y;

        if(deviceType.equals("WIFI")){
            if(ap.getX() + wifi_reach < max_reach)

                maxX = ap.getX() + wifi_reach;
            else
                maxX = max_reach;
            if(ap.getX() - wifi_reach > 0)
                minX = ap.getX() - wifi_reach;
            else
                minX = 0;
        }
        else if(deviceType.equals("ZIGBEE")){
            if(ap.getX() + zigbee_reach < max_reach)
                maxX = ap.getX() + zigbee_reach;
            else
                maxX = max_reach;
            if(ap.getX() - zigbee_reach > 0)
                minX = ap.getX() - zigbee_reach;
            else
                minX = 0;
        }
        else{
            if(ap.getX() + bluetooth_reach < max_reach)
                maxX = ap.getX() + bluetooth_reach;
            else
                maxX = max_reach;
            if(ap.getX() - bluetooth_reach > 0)
                minX = ap.getX() - bluetooth_reach;
            else
                minX = 0;
        }

        x = random.nextInt((int) ((maxX - minX) + 1)) + minX;

        if(deviceType.equals("WIFI")){
            if(ap.getY() + wifi_reach < max_reach)
                maxY = ap.getY() + wifi_reach;
            else
                maxY = max_reach;
            if(ap.getY() - wifi_reach > 0)
                minY = ap.getY() - wifi_reach;
            else
                minY = 0;
        }
        else if(deviceType.equals("ZIGBEE")){
            if(ap.getY() + zigbee_reach < max_reach)
                maxY = ap.getY() + zigbee_reach;
            else
                maxY = max_reach;
            if(ap.getY() - zigbee_reach > 0)
                minY = ap.getY() - zigbee_reach;
            else
                minY = 0;
        }
        else{
            if(ap.getY() + bluetooth_reach < max_reach)
                maxY = ap.getY() + bluetooth_reach;
            else
                maxY = max_reach;
            if(ap.getY() - bluetooth_reach > 0)
                minY = ap.getY() - bluetooth_reach  ;
            else
                minY = 0;
        }

        y = random.nextInt((int) ((maxY - minY) + 1)) + minY;

        device.setX((float) x);
        device.setY((float) y);
      }

      public void setAPPosition(DeviceType deviceType , AP ap) {
        Random random = new Random();
        double angle = random.nextDouble() * 2 * Math.PI;
        double radius, r, x, y;

        radius = max_reach;
        r = radius * Math.sqrt(random.nextDouble());
        x = Math.abs(r * Math.cos(angle));
        y = Math.abs(r * Math.sin(angle));

        ap.setX((float) x);
        ap.setY((float) y);
      }

      /**
       * @return the wifi_max
       */
      public float getWifi_max() {
          return wifi_max;
      }
      /**
       * @return the zigbee_max
       */
      public float getZigbee_max() {
          return zigbee_max;
      }
      /**
       * @return the bluetooth_max
       */
      public float getBluetooth_max() {
          return bluetooth_max;
      }
}