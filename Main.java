import java.io.IOException;

public class Main {
	//ALEXANDRE
	public static void main(String[] args) throws IOException {
	  int[] devices_scenario = {200};
    int[] aps_scenario = {10};
    int rep = 10;
    SimulationSetup simulation = new SimulationSetup(rep, aps_scenario, devices_scenario);
    simulation.runSimulation();
	}
}
