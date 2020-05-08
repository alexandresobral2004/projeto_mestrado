import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ChannelAssigner implements ChannelAssignerInterface {
	private int[] aux;
	private Util util;
	private float elapsedTimeSec = 0.f;
	private Environment env;
	private DevicesManagerInterface devicesManager;
	private APsManagerInterface accessPointsManager;
	private int[][] graph;
	GraphColoring gc = new GraphColoring();
	

	public ChannelAssigner(
		Environment env,
		DevicesManagerInterface devicesManager,
		APsManagerInterface accessPointsManager,
		Util util){
			this.env = env;
			this.devicesManager = devicesManager;
			this.accessPointsManager = accessPointsManager;
			this.util = util;

			aux = this.accessPointsManager.initializeAPs(aux, this.env.getAPs());
			this.devicesManager.setDevicesReachablesAPs(aux, this.env.getDevices(), this.env.getAPs());
			this.devicesManager.connectDevicesToAPs(aux, this.env.getDevices());
		}

	// Greedy algorithm to choose the channels with less interference

	@Override
	public ArrayList<Float> channelChooser() {
		ArrayList<Float> interferences = new ArrayList<Float>();
		ArrayList<AP> bestAPConfig = new ArrayList<AP>();
		ArrayList<Device> bestDeviceConfig = new ArrayList<Device>();

		util.copyAPs(env.getAPs(), bestAPConfig);
		util.copyDevices(env.getDevices(), bestDeviceConfig);

		float globalInterference = Float.MAX_VALUE;
		float localInterference = 0.0f;
		float bestInterference = 0.0f;
		float interference = 0.0f;

		long start = System.currentTimeMillis();

		for(int i = 0; i < (env.getDevices().size() * 2); i++) {
			util.generateAPsRandomChannels(env.getAPs());
			bestInterference = util.getAPsInterference(env.getAPs());

			for (AP ap1 : env.getAPs()) {
				for (AP ap2 : env.getAPs()) {
					if (ap1.getType().equals(ap2.getType())) {
						if (ap1.getId() != ap2.getId()) {
							int currentChannel = ap2.getChannel();

							switch (ap1.getType()) {
							case WIFI:

								for (int j = 0; j <= 12; j++) {
									ap2.setChannel(j);
									interference = util.getAPsInterference(env.getAPs());

									if (interference < bestInterference) {
										bestInterference = interference;
										currentChannel = j;
									} else {
										ap2.setChannel(currentChannel);
									}
								}

								break;

							case ZIGBEE:

								for (int j = 13; j <= 28; j++) {
									ap2.setChannel(j);
									interference = util.getAPsInterference(env.getAPs());

									if (interference < bestInterference) {
										bestInterference = interference;
										currentChannel = j;
									} else {
										ap2.setChannel(currentChannel);
									}
								}

								break;

							case BLUETOOTH:
								// for (int j = 29; j <= 65; j++) {
									// ap2.setChannel(j);
									// interference = util.getAPsInterference(env.getAPs());
                //
									// if (interference < bestInterference) {
										// bestInterference = interference;
										// currentChannel = j;
									// } else {
										// ap2.setChannel(currentChannel);
									// }
								// }
								// break;
							}
						}
					}
				}
			}

		devicesManager.fillDevicesInfo(env.getDevices());


		localInterference = util.getDevicesInterference(env.getDevices());
		interferences.add(localInterference);

		if(localInterference < globalInterference) {
			util.copyAPs(env.getAPs(), bestAPConfig);
			util.copyDevices(env.getDevices(), bestDeviceConfig);

			globalInterference = localInterference;
		}
	}

	long elapsedTimeMillis = System.currentTimeMillis() - start;
	this.elapsedTimeSec = elapsedTimeMillis / 1000F;

    accessPointsManager.fillAPsInfo(env.getDevices(), env.getAPs());

    util.copyAPs(bestAPConfig, env.getAPs());
    util.copyDevices(bestDeviceConfig, env.getDevices());

	System.out.println(globalInterference);
	return interferences;
  }
	// Getters and Setters
	@Override
	public float getElapsedTime() {
		return elapsedTimeSec;
	}

	@Override
	public void setElapsedTime(float runtime){
		this.elapsedTimeSec = runtime;
	}
	
	
	@Override
	public String channelAPsOverllaping() {
		  long start = System.currentTimeMillis();
		  long elapsed = 0;
		  float sobreposicao1 = 0; //fator de interferência
		  float sobreposicao2 = 0; //fator de interferência
		 // generateAPsRandomChannels();
		  DecimalFormat df = new DecimalFormat("##.####");
		  WriteConsole wc = new WriteConsole();
		  ArrayList<AP> APs = this.env.getAPs();
		  ArrayList<Device> device = this.env.getDevices();
		 
		   
		  //double bestInterference = getAPsInterference();

		  for(int i = 0; i < (this.env.getAmountOfDevices()); i++) {
		    this.util.generateAPsRandomChannels(APs);
		    //for para iterar sobre os AP's
		   
		    for(int j = 0; j < APs.size(); j++) {

		      for(int k = 0; k < APs.size(); k++) {

		        if (APs.get(j).getId() != APs.get(k).getId()) {
		         
		      

		            if (APs.get(j).getType().toString() == "WIFI" && APs.get(k).getType().toString()  == "ZIGBEE") {
		                
		            	
		            	
		            	
		            	
		              
		              //MÉTODO QUE RETORNA O FATOR DE SOBREPOSIÇÃO
		              float interf = this.util.getNormalizedInterference(APs.get(j), APs.get(j).getChannel(), APs.get(k), APs.get(k).getChannel());
		              float interf2 = 0;
		              AP ap2 = null;
		              int canal_zig_antes = getChannelZigBee(APs.get(k).getChannel()); 
		              
		              
		              int canal_zig_depois = 0;
		              
		             
		              

		              if( interf > 0  ) {
		            	 
		                  
		            	  
		                
		                //MÉTODO QUE MUDA O CANAL DO ZIGBEE SE ELE ESTIVER SOBREPOSTO E SETA NO AP1 
		                 ap2 = getResolverInterference(APs.get(j), APs.get(k), APs.get(j).getChannel(), APs.get(k).getChannel());
		                 
		                 
		           	    //PEGA AS COORDENADAS DO WIFI
		               /*  int Xwf = (int) APs.get(j).getX();
		                 int Ywf = (int) APs.get(j).getY();
		                 //PEGA AS COORDENADAS DO ZIGBEE
		                 int Xzb = (int) APs.get(k).getX();
		                 int Yzb = (int) APs.get(k).getY();
		                 
		                 this.graph = new int[Xwf][Ywf];
		                 this.graph = new int[Xzb][Yzb];*/
		               
		                
		                 
		                 //CONVERTE OS CANAIS DA TABELA PRA O FORMATO PADRÃO DO WIFI E ZIGBEE
		                 //AFIM DE EXIBIR EM TEMPO DE EXECUÇÃO E NO ARQUIVO TESTE.TXT
		                 int wifi_canal=getChannelWifi(APs.get(j).getChannel());
		                 int zigbee_canal = getChannelZigBee(APs.get(k).getChannel());
		                 
		                 //calcula novamente a sobreposição para ver o que mudou
		                 interf2 = this.util.getNormalizedInterference(APs.get(j), APs.get(j).getChannel(), ap2, ap2.getChannel());
		                 //exibe o canal pra ver se houve modificação
		                
		                System.out.println("");
		                System.out.println("**********************************************************");
		                System.out.println("Disp: "+ APs.get(j).getType().toString() + " Canal: "+ wifi_canal);
		                System.out.println("Disp: "+ APs.get(k).getType().toString() + " Canal: "+ zigbee_canal);
		                System.out.println("Fator de interf. antes "+ df.format(interf));
		                System.out.println("Fator de interf. depois "+ df.format(interf2));
		                System.out.println("Distância: "+ this.util.getDistance(APs.get(j), ap2));
		                System.out.println("ZigBee antes: "+canal_zig_antes + " ZigBee depois: " +zigbee_canal);
		                System.out.println("**********************************************************");
		                System.out.println("");
		                
		                
		                //imprime os resultados no arquivo teste.txt

		            	long elapsedTimeMillis = System.currentTimeMillis() - start;
		            	this.elapsedTimeSec = elapsedTimeMillis / 1000F;
		            	
		            	return "\nId_wf: " +  APs.get(j).getId() + " Id_Zb: " +  APs.get(k).getId() + " WF-ZB:" + wifi_canal + "---" + zigbee_canal+" [interf_antes: "+df.format(interf)+
		                        " depois: "+df.format(interf2)+ "] {FS_antes: "+df.format(interf)+" Depois:  "+df.format(interf2)+ 
		                        "} " + "Time_exec: "+ this.elapsedTimeSec;
		            	// return "\nId_wf: " +  APs.get(j).getId() + " Id_Zb: " +  APs.get(k).getId() + " WF-ZB:" + wifi_canal + "---" + zigbee_canal;
		                //return //APs.get(j).getX()+","+APs.get(j).getY();
		                	  // APs.get(k).getX()+","+APs.get(k).getY();
		              }
		            } 
		          }
		        }
		      }
		    }
		  
		  return null;
		}
	
		public void preencheMatrix(int x, int y) {
			
		}
			
		

		  public int getChannelWifi(int x) {
			  int a = 0;
			  if(x == 0) {
			     a = 1;
			 }
			 else if( x > 1) {
				a = x+1;
				  
			 }
			 return a; 
			  
		  }
		  
		  public int getChannelZigBee(int y) {
			  int a = y-13;
			  return a;
		  }
		  
		  public int setChannelWifi(int x) {
			  int a = x+1;
			  return a;
		  }
		  
		  public int setChannelZigBee(int y) {
			  int a = y+13;
			  return a;
		  }
		  
		  public void adjustDistance(AP ap) {
			  
		  }
		  
		  public AP getResolverInterference(AP ap, AP ap1, int x,int y){
			  DecimalFormat df = new DecimalFormat("##.####");
				//CALCULA A INTERFERÊNCIA DOS AP'S
				float inter_factor = this.util.getNormalizedInterference(ap, x, ap1,y);
				float distance = this.util.getDistance(ap, ap1);
				df.format(inter_factor);
				//Ajustando Canais
				int canal_wifi = getChannelWifi(x);
				int canal_zigbee=getChannelZigBee(y);
				
				
				
				int random = 0;
				
				
					while(inter_factor != 0 && distance <= 35) {
						
						
						if( canal_wifi  >= 1 && canal_wifi <=4) {
							
							random = this.util.getRandomNumberFrom(9, 11);
							//ajusta o canal
							canal_zigbee = setChannelZigBee(random);
							ap1.setChannel(canal_zigbee);
							
							inter_factor = this.util.getNormalizedInterference(ap, x, ap1,canal_zigbee);
							
						}
						else if(canal_wifi >= 5 && canal_wifi <=8) {
							random = this.util.getRandomNumberFrom(1, 3);
							//ajusta o canal
							canal_zigbee = setChannelZigBee(random);
							ap1.setChannel(canal_zigbee);
							inter_factor = this.util.getNormalizedInterference(ap, x, ap1,canal_zigbee);
							
							
						}
						else if(canal_wifi >= 9 && canal_wifi <=13) {
							random = this.util.getRandomNumberFrom(5, 7);
							//ajusta o canal
							canal_zigbee = setChannelZigBee(random);
							ap1.setChannel(canal_zigbee);
							inter_factor = this.util.getNormalizedInterference(ap, x, ap1,canal_zigbee);
							
							
						}
						return ap1;
				}
				return ap1;
			}
				

	
	
	
	
}
