import java.util.ArrayList;

public class Device {
	private int id;
	private DeviceType type;
	private int channel;
	private ArrayList<AP> reachableAPs;
	private AP connectedAP;
	private float x;
	private float y;
	private float antennaPowerDevice;
	
	public Device(){}
	
	public Device(int id, DeviceType type, float antennaPowerDevice) {
		super();
		this.id = id;
		this.type = type;
		this.antennaPowerDevice = antennaPowerDevice;
		this.reachableAPs = new ArrayList<AP>();
		this.connectedAP = null;
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
	
	public float getAntennaPowerDevice(){
	    return this.antennaPowerDevice;
	}

	public void setAntennaPowerDevice(float antennaPowerDevice){
		this.antennaPowerDevice = antennaPowerDevice;
	}
	
	public ArrayList<AP> getReachableAPs(){
		return reachableAPs;
	}
	
	public void setReachableAPs(ArrayList<AP> reachableAPs){
		this.reachableAPs = reachableAPs;
	}
	
	public void setX(float x){
		this.x = x;
	}
	
	public float getX(){
		return x;
	}
	
	public void setY(float y){
		this.y = y;
	}
	
	public float getY(){
		return y;
	}	
	
	public void addReachableAP(AP ap) {
		this.reachableAPs.add(ap);
	}
	
	public AP getConnectedAP() {
		return connectedAP;
	}
	
	public void setConnectedAP(AP connectedAP) {
		this.connectedAP = connectedAP;
	}
	
	public int getConnectedAPId() {
		return this.connectedAP.getId();
	}
	
	public String toString() {
		return "Device 00" + getId() + " connects to antenna 00" + getConnectedAPId() + 
				" via " + getType().toString() + " channel " + getChannel();
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
		Device other = (Device) obj;
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
