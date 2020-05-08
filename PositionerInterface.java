public interface PositionerInterface {
    public void setDevicePosition(DeviceType type, Device device, AP ap);
    public void setAPPosition(DeviceType type, AP ap);
    public float getWifi_max();
    public float getZigbee_max();
    public float getBluetooth_max();
}