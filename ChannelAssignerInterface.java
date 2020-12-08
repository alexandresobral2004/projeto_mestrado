import java.util.ArrayList;

public interface ChannelAssignerInterface{
    public ArrayList<Float> channelChooser();
    public float getElapsedTime();
    public void setElapsedTime(float runtime);
    
    public String channelAPsOverllaping();
    public ArrayList<Float> getMitigaterInterference();
    
    
}