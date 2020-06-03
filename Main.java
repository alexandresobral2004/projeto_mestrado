import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
	  int[] devices_scenario = {20, 30, 40};
    int[] aps_scenario = {5, 10,15};
    int rep = 32;
    SimulationSetup simulation = new SimulationSetup(rep, aps_scenario, devices_scenario);
    simulation.runSimulation();
	}
}
