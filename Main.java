import java.io.IOException;

public class Main {
	//ALEXANDRE
	public static void main(String[] args) throws IOException {
	  int[] devices_scenario = {300};
    int[] aps_scenario = {15};
    int rep = 20;
    SimulationSetup simulation = new SimulationSetup(rep, aps_scenario, devices_scenario);
    simulation.runSimulation();
	}
}
