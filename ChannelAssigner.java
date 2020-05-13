import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class ChannelAssigner implements ChannelAssignerInterface {
	private int[] aux;
	private Util util;
	private float elapsedTimeSec = 0.f;
	private  Environment env;
	private DevicesManagerInterface devicesManager;
	private APsManagerInterface accessPointsManager;
	private int[][] graph;
	
	
	
	
	public ChannelAssigner(Environment env, DevicesManagerInterface devicesManager,
			APsManagerInterface accessPointsManager, Util util) {
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
	/*
	 * public ArrayList<Float> channelChooser() { ArrayList<Float> interferences =
	 * new ArrayList<Float>(); ArrayList<AP> bestAPConfig = new ArrayList<AP>();
	 * ArrayList<Device> bestDeviceConfig = new ArrayList<Device>();
	 * 
	 * util.copyAPs(env.getAPs(), bestAPConfig); util.copyDevices(env.getDevices(),
	 * bestDeviceConfig);
	 * 
	 * float globalInterference = Float.MAX_VALUE; float localInterference = 0.0f;
	 * float bestInterference = 0.0f; float interference = 0.0f;
	 * 
	 * long start = System.currentTimeMillis();
	 * 
	 * /* for(int i = 0; i < (env.getDevices().size() * 2); i++) {
	 * util.generateAPsRandomChannels(env.getAPs()); bestInterference =
	 * util.getAPsInterference(env.getAPs());
	 * 
	 * for (AP ap1 : env.getAPs()) { for (AP ap2 : env.getAPs()) { if
	 * (ap1.getType().equals(ap2.getType())) { if (ap1.getId() != ap2.getId()) { int
	 * currentChannel = ap2.getChannel();
	 * 
	 * switch (ap1.getType()) { case WIFI:
	 * 
	 * for (int j = 0; j <= 12; j++) { ap2.setChannel(j); interference =
	 * util.getAPsInterference(env.getAPs());
	 * 
	 * if (interference < bestInterference) { bestInterference = interference;
	 * currentChannel = j; } else { ap2.setChannel(currentChannel); } }
	 * 
	 * break;
	 * 
	 * case ZIGBEE:
	 * 
	 * for (int j = 13; j <= 28; j++) { ap2.setChannel(j); interference =
	 * util.getAPsInterference(env.getAPs());
	 * 
	 * if (interference < bestInterference) { bestInterference = interference;
	 * currentChannel = j; } else { ap2.setChannel(currentChannel); } }
	 * 
	 * break;
	 * 
	 * case BLUETOOTH:
	 */
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
	// }
	// }
	// }
	// }
	// }

	// devicesManager.fillDevicesInfo(env.getDevices());

	/*
	 * localInterference = util.getDevicesInterference(env.getDevices());
	 * interferences.add(localInterference);
	 * 
	 * if(localInterference < globalInterference) { util.copyAPs(env.getAPs(),
	 * bestAPConfig); util.copyDevices(env.getDevices(), bestDeviceConfig);
	 * 
	 * globalInterference = localInterference;
	 */
	// }
	// }

	// long elapsedTimeMillis = System.currentTimeMillis() - start;
//	this.elapsedTimeSec = elapsedTimeMillis / 1000F;

	// accessPointsManager.fillAPsInfo(env.getDevices(), env.getAPs());

	// util.copyAPs(bestAPConfig, env.getAPs());
	// util.copyDevices(bestDeviceConfig, env.getDevices());

//	System.out.println(globalInterference);
	// return null;
	// }
	// Getters and Setters

	public float getElapsedTime() {
		return elapsedTimeSec;
	}

	@Override
	public void setElapsedTime(float runtime) {
		this.elapsedTimeSec = runtime;
	}

	@Override
	public String channelAPsOverllaping() {
		float localInterference = 0.0f;
		long start = System.currentTimeMillis();
		long elapsed = 0;
		float sobreposicao1 = 0; // fator de interferência
		float sobreposicao2 = 0; // fator de interferência
		// generateAPsRandomChannels();
		DecimalFormat df = new DecimalFormat("##.####");
		DecimalFormat df2 = new DecimalFormat("##");
		WriteConsole wc = new WriteConsole();
		ArrayList<AP> APs = this.env.getAPs();
		ArrayList<AP> APs2 = new ArrayList<>();
		ArrayList<Device> device = this.env.getDevices();
		ArrayList<Float> interferences = new ArrayList<Float>();

		// double bestInterference = getAPsInterference();

		for (int i = 0; i < (this.env.getAmountOfDevices()); i++) {
			this.util.generateAPsRandomChannels(APs);
			// for para iterar sobre os AP's

			for (int j = 0; j < APs.size(); j++) {

				for (int k = 0; k < APs.size(); k++) {

					if (APs.get(j).getId() != APs.get(k).getId()) {

						//PEGA A DISTANCIA ANTES DE ENTRAR NO IF	
						float distance2 = this.util.getDistance(APs.get(j), APs.get(k));	

						if (APs.get(j).getType().toString() == "WIFI" && APs.get(k).getType().toString() == "ZIGBEE" && distance2 <= 30) {

							// MÉTODO QUE RETORNA O FATOR DE SOBREPOSIÇÃO
							float interf = this.util.getNormalizedInterference(APs.get(j), APs.get(j).getChannel(),
									APs.get(k), APs.get(k).getChannel());
							float interf2 = 0;
							AP ap2 = null;
							
							//SUBTRAI 13 DO CANAL DO ZIGBEE POR AJUSTE DA TABELA DE SOBREPOSIÇÃO
							int canal_zig_antes = getChannelZigBee(APs.get(k).getChannel()) - 13;
						

							if (interf > 0.0) {

						//	APs2 = 	this.env.changeLocation(APs);
								//this.env.changeLocationDevice(device);
								
								// MÉTODO QUE MUDA O CANAL DO ZIGBEE SE ELE ESTIVER SOBREPOSTO E SETA NO AP2
								ap2 = this.getResolverInterference(APs.get(j), APs.get(k), APs.get(j).getChannel(),APs2.get(k).getChannel());
									//	APs2.get(k).getChannel());
								
								

								// CONVERTE OS CANAIS DA TABELA PRA O FORMATO PADRÃO DO WIFI E ZIGBEE
								// AFIM DE EXIBIR EM TEMPO DE EXECUÇÃO E NO ARQUIVO TESTE.TXT
								int wifi_canal = getChannelWifi(APs2.get(j).getChannel());
								int zigbee_canal = getChannelZigBee(ap2.getChannel());
								
								

								// calcula novamente a sobreposição para ver o que mudou
								interf2 = this.util.getNormalizedInterference(APs.get(j), APs.get(j).getChannel(), ap2,ap2.getChannel());
								// exibe o canal pra ver se houve modificação
								
								
							

								System.out.println("");
								System.out.println("**********************************************************");
								System.out.println("Disp: " + APs2.get(j).getType().toString() + " Canal: " + wifi_canal);
								System.out.println("Disp: " + APs2.get(k).getType().toString() + " Canal: " + zigbee_canal);
								System.out.println("Fator de interf. antes " + df.format(interf));
								System.out.println("Fator de interf. depois " + df.format(interf2));
								System.out.println("Distância: " + this.util.getDistance(APs.get(j), ap2));//CALCULA NOVAMENTE A DISTÂNCIA
								System.out.println("ZigBee antes: " + canal_zig_antes + " ZigBee depois: " + ap2.getChannel());
								System.out.println("**********************************************************");
								System.out.println("");

								
								//PEGA OS APs e adiciona numa lista para tirar a interferencia geral
							//	APs2.add(APs.get(j));
							//	APs2.add(ap2);
								
								
								
								//float interf_geral = this.util.getAPsInterference(APs2);

								long elapsedTimeMillis = System.currentTimeMillis() - start;
								this.elapsedTimeSec = elapsedTimeMillis / 1000F;
								
								//IMPRIME INTERF ENTRE DEVICES
						//		localInterference = this.util.getDevicesInterference(this.env.getDevices());
						//		interferences.add(localInterference);
								

						//		return String.valueOf(localInterference);
								
								
								
								// imprime os resultados no arquivo teste.txt
					   		 return df2.format(APs.get(j).getX()) + "; " + df2.format(APs.get(j).getY()) + "; "
													+ "w"+(APs.get(j).getChannel()+1) + "; \n" + df2.format(ap2.getX()-13) + "; "
														+ df2.format(ap2.getY()) + "; " + "z"+(ap2.getChannel()-13)+";";
										 
									/*	 "\n"
										 +"#################################################################################################################################\n\n"
								 		+ "Id_wf: " + APs.get(j).getId() + " Id_Zb: " + APs.get(k).getId() +
								 		
										 " WF---ZB(ANTES): " + (APs.get(j).getChannel()+1) + "---" + canal_zig_antes+
										 
										 "  WF---ZB(DEPOIS):" + (APs.get(j).getChannel()+1) + "---" + zigbee_canal+
										 
										 " [interf_antes: "+df.format(interf)+
										 
										 " depois: "+df.format(interf2)+ "] "+ 
										 
										 "{Interf_APs: "+ interf_geral + 
										 
										 "} Distance: "+ df.format(distance2) + 
										 
										 " Time_exec: "+ this.elapsedTimeSec;*/
								
								
							
							}
						}
						
						else if (APs.get(j).getType().toString() == "ZIGBEE" && APs.get(k).getType().toString() == "WIFI" && distance2 <= 30) {

							// MÉTODO QUE RETORNA O FATOR DE SOBREPOSIÇÃO
							float interf = this.util.getNormalizedInterference(APs.get(j), APs.get(j).getChannel(),
									APs.get(k), APs.get(k).getChannel());
							float interf2 = 0;
							AP ap2 = null;
							int canal_zig_antes = (APs.get(j).getChannel() - 13);
						

							if (interf > 0.0) {

						//	APs2 = 	this.env.changeLocation(APs);
								//this.env.changeLocationDevice(device);
								
								// MÉTODO QUE MUDA O CANAL DO ZIGBEE SE ELE ESTIVER SOBREPOSTO E SETA NO AP1
							//	ap2 = APs2.get(j);
										
							ap2 = getResolverInterference(APs2.get(j),APs2.get(k), APs2.get(j).getChannel(),APs.get(k).getChannel());
								
							
								
								// PEGA AS COORDENADAS DO ZIGBEE
							//	int Xzb = (int) APs.get(j).getX();
							//	int Yzb = (int) APs.get(j).getY();
								// PEGA AS COORDENADAS DO WIFI
							//	int Xwf = (int) APs.get(k).getX();
							//	int Ywf = (int) APs.get(k).getY();
								

								/// this.graph = new int[Xwf][Ywf];
								// this.graph = new int[Xzb][Yzb];

								// CONVERTE OS CANAIS DA TABELA PRA O FORMATO PADRÃO DO WIFI E ZIGBEE
								// AFIM DE EXIBIR EM TEMPO DE EXECUÇÃO E NO ARQUIVO TESTE.TXT
								int wifi_canal = getChannelWifi(APs.get(k).getChannel());
								int zigbee_canal = getChannelZigBee(ap2.getChannel());
								//SETA OS CANAIS
							//	setChannelWifi(wifi_canal);
							//	setChannelZigBee(zigbee_canal);
								

								// calcula novamente a sobreposição para ver o que mudou
								interf2 = this.util.getNormalizedInterference(APs.get(j), APs.get(j).getChannel(), ap2,ap2.getChannel());
								// exibe o canal pra ver se houve modificação
								
								
								

								System.out.println("");
								System.out.println("**********************************************************");
								System.out.println("Disp: " + APs.get(k).getType().toString() + " Canal: " + wifi_canal);
								System.out.println("Disp: " + APs.get(j).getType().toString() + " Canal: " + zigbee_canal);
								System.out.println("Fator de interf. antes " + df.format(interf));
								System.out.println("Fator de interf. depois " + df.format(interf2));
								System.out.println("Distância: " + this.util.getDistance(APs2.get(k), ap2));
								System.out.println("ZigBee antes: " + canal_zig_antes + " ZigBee depois: " + zigbee_canal);
								System.out.println("**********************************************************");
								System.out.println("");

								
							//	APs2.add(APs.get(j));
							//	APs2.add(ap2);
								
								
								
								//float interf_geral = this.util.getAPsInterference(APs2);
								
								
								
								// imprime os resultados no arquivo teste.txt

								long elapsedTimeMillis = System.currentTimeMillis() - start;
								this.elapsedTimeSec = elapsedTimeMillis / 1000F;
				
								
								
								   return df2.format(APs.get(k).getX()) + "; " + df2.format(APs.get(k).getY()) + "; "
									+"w"+ (APs.get(k).getChannel()+1) + ";\n " + df2.format(ap2.getX()) + "; "
									+ df2.format(ap2.getY()) + "; " + "z"+(ap2.getChannel()-13)+";";
										 
								/*		 "\n"
								 +"#################################################################################################################################"
								 + "\n\n"
						 		+ "Id_wf: " + APs.get(k).getId() + " Id_Zb: " + APs.get(j).getId() +
						 		
								 " WF---ZB(ANTES): " + (APs.get(k).getChannel()+1) + "---" + canal_zig_antes +
								 
								 " WF---ZB(DEPOIS):" + (APs.get(k).getChannel()+1) + "---" + zigbee_canal +
								 
								 " [interf_antes: "+df.format(interf)+
								 
								 " depois: "+df.format(interf2)+ "] "+ 
								 
								 "{Interf_APs: "+ interf_geral + 
								 
								 " Distance: "+ df.format(distance2) + 
								 
								 " Time_exec: "+ this.elapsedTimeSec;*/
						
								
								
							
							}
						
					}
						
				}
					
			}
			devicesManager.fillDevicesInfo(this.env.getDevices());
					
								//IMPRIME INTERF ENTRE DEVICES
								localInterference = this.util.getDevicesInterference(this.env.getDevices());
								interferences.add(localInterference);
							
							//	return String.valueOf(localInterference);
								
		}
			
		
		return null;
		
	}
		
		return null;
  }


	public int getChannelWifi(int x) {
		int a = 0;
		if (x == 0) {
			a = 1;
		} else if (x > 1) {
			a = x + 1;

		}
		return a;

	}
  
	public int getChannelZigBee(int y) {
		int a = y -13;
		return a;
	}

	public int setChannelWifi(int x) {
		int a = x - 1;
		return a;
	}

	public int setChannelZigBee(int y) {
		int a = y + 13;
		return a;
	}
	
	
/*	public void changeDistance(AP ap, AP ap1, float distance) {
		 Random aleatorio = new Random();
		// int valor = aleatorio.nextInt();
		// float interf =  this.util.getDevicesInterference(this.env.getDevices());
		 
		float distance2 = distance;
		
		int[] iteracao = {1,35,70,100,130,160,190,220,250,280,310,340,370,400};

		float x1 = ap.getX();
		float y1 = ap.getY();
		
		float x2 = ap1.getX();
		float y2 = ap1.getY();
		
		while(distance2 < 50) {
			
		
			
			while(x1  < 400.0 && x2 < 400.0 ) {		
				x1+= 2;
				x2+= 2;
				
			}
			while(y1  < 400 && y2 < 400.0 ) {
								
			    y1+=3;	
				y2+=3;
			}
		/*	else {
				
				x1 = this.util.getRandomNumberFrom(10, 400);
				y1 = this.util.getRandomNumberFrom(10, 400);
				
				x2 =  this.util.getRandomNumberFrom(20, 400);
				y2 =  this.util.getRandomNumberFrom(20, 400);
			}*/
				
				
			   
				
		/*	    ap.setX(x1);//incrementa em 3m
				ap.setY(y1);//incrementa em 3m
				
				ap1.setX(x2);//incrementa em 3m
				ap1.setY(y2);//incrementa em 3m
				
				distance2 += this.util.getDistance(ap, ap1);
			
		}
		
		
		
	}*/
	
	
	public void changeDistance(AP ap, AP ap1, float distance) {
		 Random aleatorio = new Random();
		 float x1=0.0f;
		 float x2=0.0f;
		 float y1=0.0f;
		 float y2=0.0f;
		// int valor = aleatorio.nextInt();
		// float interf =  this.util.getDevicesInterference(this.env.getDevices());
		 
		float distance2 = this.util.getDistance(ap, ap1);
		
		//int[] iteracao = {1,35,70,100,130,160,190,220,250,280,310,340,370,400};

		 x1 = ap.getX();
		 y1 = ap.getY();
		
		 x2 = ap1.getX();
		 y2 = ap1.getY();
		
		while(distance2 <= 60) {
			
		
			
			if(x1  < 400.0 && x2 < 400.0 && y1 < 400 && y2 < 400  ) {		
				
				
				
				x1+=2;  //this.util.getRandomNumberFrom(0, 10);
				x2+=3; //this.util.getRandomNumberFrom(0, 10);
				
				y1+= 5;  //this.util.getRandomNumberFrom(0, 1);
				y2+= 1;  //this.util.getRandomNumberFrom(0, 30);
				
			}
			
		/*	else {
				
				x1 = this.util.getRandomNumberFrom(10, 400);
				y1 = this.util.getRandomNumberFrom(10, 400);
				
				x2 =  this.util.getRandomNumberFrom(20, 400);
				y2 =  this.util.getRandomNumberFrom(20, 400);
			}*/
				
				
			   
				
			    ap.setX(x1);//incrementa em 3m
				ap.setY(y1);//incrementa em 3m
				
				ap1.setX(x2);//incrementa em 3m
				ap1.setY(y2);//incrementa em 3m
				
				distance2 += this.util.getDistance(ap, ap1);
			
		}
		
		
		
	}
	
	
	

	/**
	 * TODO Put here a description of what this method does.
	 *
	 * @param ap
	 * @param ap1
	 * @param x
	 * @param y
	 * @return
	 */
	public AP getResolverInterference(AP ap, AP ap1, int x, int y) {
		DecimalFormat df2 = new DecimalFormat("##.####");
		DecimalFormat df3 = new DecimalFormat("##.#");
		int canal_wifi;
		int canal_zigbee;
		// CALCULA A INTERFERÊNCIA DOS AP'S
		float inter_factor = this.util.getNormalizedInterference(ap, x, ap1, y);
		float distance = this.util.getDistance(ap, ap1);
		df2.format(inter_factor);
		df3.format(distance);
		
		
		// Ajustando Canais
		if(ap.getType().toString() == "WIFI") {
			 canal_wifi = x+1;
			
		}
		else {
			 canal_wifi = y+1;
			 
		}
		   
		
		int random = 0;

		 	

			while (inter_factor > 0 ) {
				
				

				  if (canal_wifi >= 1 && canal_wifi <= 4) {

				
					canal_zigbee = this.util.getRandomNumberFrom(8, 15);
					
					// ajusta o canal
				//	canal_zigbee = setChannelZigBee(random);
					if(ap1.getType().toString() == "ZIGBEE") {
						ap1.setChannel(setChannelZigBee(canal_zigbee));
						inter_factor = this.util.getNormalizedInterference(ap, x, ap1, ap1.getChannel());
						if(distance < 35.0) {changeDistance(ap, ap1, distance);}
					}
					else {
						ap.setChannel(setChannelZigBee(canal_zigbee));
						inter_factor = this.util.getNormalizedInterference(ap, ap.getChannel(), ap1, y);
						if(distance < 35.0) {changeDistance(ap, ap1, distance);}
					}
					
					
					

					

				} else if (canal_wifi >= 5 && canal_wifi <= 8) {
					
					canal_zigbee = this.util.getRandomNumberFrom(13, 16);
					// ajusta o canal
					//canal_zigbee = setChannelZigBee(random);
					if(ap1.getType().toString() == "ZIGBEE") {
						ap1.setChannel(setChannelZigBee(canal_zigbee));
						inter_factor = this.util.getNormalizedInterference(ap, x, ap1, ap1.getChannel());
						if(distance < 35.0) {changeDistance(ap, ap1, distance);}
						
					}
					else {
						ap.setChannel(setChannelZigBee(canal_zigbee));
						inter_factor = this.util.getNormalizedInterference(ap, ap.getChannel(), ap1, y);
						if(distance < 35.0) {changeDistance(ap, ap1, distance);}
					}
					
					

					

				} else if (canal_wifi >= 9 && canal_wifi <= 13) {
				
					canal_zigbee = this.util.getRandomNumberFrom(1, 7);
					// ajusta o canal
					//canal_zigbee = setChannelZigBee(random);
					if(ap1.getType().toString() == "ZIGBEE") {
						ap1.setChannel(setChannelZigBee(canal_zigbee));
						inter_factor = this.util.getNormalizedInterference(ap, x, ap1, ap1.getChannel());
						if(distance < 35.0) {changeDistance(ap, ap1, distance);}
					}
					else {
						ap.setChannel(setChannelZigBee(canal_zigbee));
						inter_factor = this.util.getNormalizedInterference(ap, ap.getChannel(), ap1, y);
						if(distance < 35.0) {changeDistance(ap, ap1, distance);}
					}
					
					
					

				}
				
				return (ap1.getType().toString() == "ZIGBEE") ? ap1:ap;
			}
			return (ap1.getType().toString() == "ZIGBEE") ? ap1:ap;
		
	}

	@Override
	public ArrayList<Float> channelChooser() {
		// TODO Auto-generated method stub
		return null;
	}

	
	


}
