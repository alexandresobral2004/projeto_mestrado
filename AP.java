import java.util.ArrayList;

public class AP {
	private int id;
	private DeviceType type;
	private int channel;
	private ArrayList<Device> connectedDevices;
	private ArrayList<Device> reachableDevices;
	private float x;
	private float y;
	private float antennaPowerAP;

	public AP(){}

	public AP(int id, DeviceType type, float antennaPowerAP) {
		super();
		this.id = id;
		this.type = type;
		this.antennaPowerAP = antennaPowerAP;
		this.connectedDevices = new ArrayList<Device>();
		this.reachableDevices = new ArrayList<Device>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public DeviceType getType() {
		return type;
	}

	public void setType(DeviceType type) {
		this.type = type;
	}

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public float getX(){
		return x;
	}

	public void setX(float x){
		this.x = x;
	}

	public float getY(){
		return y;
	}

	public void setY(float y){
		this.y = y;
	}

  	public float getAntennaPowerAP(){
  	    return this.antennaPowerAP;
  	}

  	public void setAntennaPowerAP(float antennaPowerAP){
  	  this.antennaPowerAP = antennaPowerAP;
  	}

	public ArrayList<Device> getConnectedDevices() {
		return connectedDevices;
	}

	public void setConnectedDevices(ArrayList<Device> connectedDevices) {
		this.connectedDevices = connectedDevices;
	}

	public void connectDeviceToAP(Device dev) {
		this.connectedDevices.add(dev);
	}

	public int getAmountOfDevicesConnected() {
		return this.connectedDevices.size();
	}

	/**
	 * @return the reachableDevices
	 */
	public ArrayList<Device> getReachableDevices() {
		return reachableDevices;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AP other = (AP) obj;
		if (id != other.id)
			return false;
		if (type != other.type)
			return false;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}
}
