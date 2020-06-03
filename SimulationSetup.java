import java.io.IOException;

public class SimulationSetup{
    int[] aps_scenario;
    int[] devices_scenario;
    int rep;
    int rooms;
    CreateTable table = new CreateTable("tabela-w.txt");
    Util util = new Util(table.getW());
    PositionerInterface positioner = new Positioner(util);
    WriteConsole wc = new WriteConsole();

    SimulationSetup(final int rep, final int[] aps_scenario, final int[] devices_scenario){
        this.aps_scenario = aps_scenario;
        this.devices_scenario = devices_scenario;
        this.rep = rep;
    }

    public void runSimulation() throws IOException{
      for(int j = 0; j < devices_scenario.length; j++) {
        for(int k = 0; k < aps_scenario.length; k++) {
            final String path = "../saida100-omnetpp/";
            final String suffix = "-" + devices_scenario[j] + "-" + aps_scenario[k] + ".txt";
            for(int i = 0; i < rep; i++) {
              final APsManagerInterface accessPointManager = new APsManager(aps_scenario[k], positioner);
              final DevicesManagerInterface devicesManager = new DevicesManager(devices_scenario[j], positioner);
              final Environment env = new Environment(devicesManager, accessPointManager);
              final ChannelAssignerInterface assigner = new ChannelAssigner(env,
                                                             devicesManager,
                                                             accessPointManager,
                                                             util);
            
            	    assigner.getMitigaterInterference();
            	/*  if(retorno!=null) {
                  this.wc.salva2(retorno);
                }
                }
                catch (final Exception e) {
                  //TODO: handle exception
                  e.printStackTrace();
                }*/
            		 
            
              
            	   
              env.setInterferences(assigner.channelChooser());
              final OmnetppFiles omnetppFiles = new OmnetppFiles(env, util, assigner, path, suffix);
            

              final String nedFile = "../saida100-omnetpp/network-" + devices_scenario[j] + "-" + aps_scenario[k] + "-" + i + ".ned";
              final String iniFile = "../saida100-omnetpp/parameters-" + devices_scenario[j] + "-" + aps_scenario[k] + "-" + i + ".ini";

              omnetppFiles.printINIFile(iniFile);
              omnetppFiles.printNEDFile(nedFile);
          //    omnetppFiles.writeInterferencesFile("CUSTOM");
           //   omnetppFiles.writeInterferencesFile("RANDOM");
            //  omnetppFiles.writeInterferencesFile("SAME");
              omnetppFiles.writeTimeElapsedFile();
            //  System.out.println("Total amount of interference = " + env.getBestInterferences());
             // System.out.println("Elapsed time = " + assigner.getElapsedTime() + "s");



        
         
        }
        
      }


      }
    }

   
      
    
}