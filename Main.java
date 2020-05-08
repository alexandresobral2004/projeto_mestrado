
public class Main {
	public static void main(String[] args) {
	  int[] devices_scenario = {7, 10, 12, 15};
    int[] aps_scenario = {2, 4};
    int rep = 32;
    SimulationSetup simulation = new SimulationSetup(rep, aps_scenario, devices_scenario);
    simulation.runSimulation();
	}
}
