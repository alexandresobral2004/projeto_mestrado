import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.zip.Inflater;

import org.jamesframework.core.problems.objectives.Objective;
import org.jamesframework.core.problems.objectives.evaluations.Evaluation;
import org.jamesframework.core.subset.SubsetSolution;

public class ChannelAssigner implements ChannelAssignerInterface {
	private  int[] aux;
	private  Util util;
	private float elapsedTimeSec = 0.f;
	private final Environment env;
	private final DevicesManagerInterface devicesManager;
	private final APsManagerInterface accessPointsManager;

	public ChannelAssigner(final Environment env, final DevicesManagerInterface devicesManager,
			final APsManagerInterface accessPointsManager, final Util util) {
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
	// this.elapsedTimeSec = elapsedTimeMillis / 1000F;

	// accessPointsManager.fillAPsInfo(env.getDevices(), env.getAPs());

	// util.copyAPs(bestAPConfig, env.getAPs());
	// util.copyDevices(bestDeviceConfig, env.getDevices());

	// System.out.println(globalInterference);
	// return null;
	// }
	// Getters and Setters

	public float getElapsedTime() {
		return elapsedTimeSec;
	}

	@Override
	public void setElapsedTime(final float runtime) {
		this.elapsedTimeSec = runtime;
	}

	/*
	 * public String mitigateInterference() { ArrayList<AP> aps = this.env.getAPs();
	 * this.util.generateAPsRandomChannels(aps); int temp1 = 0; int inc = 0; int
	 * ch_zb, ch_wifi = 0; float interf = 0.0f; int[] canal = { 1, 16, 11, 11, 6,
	 * 22, 1, 18, 6, 23, 11, 17, 1, 19, 11, 17, 1, 20, 6, 24, 11, 26 }; int[]
	 * canalWF = { 1, 6, 11, 1, 6, 11, 1, 6, 11, 1, 6, 11, 1, 6, 13 }; int y = 0;
	 * int w = 0; float global_interf = util.getAPsInterference(env.getAPs()); float
	 * local_interference = 0.0f; float best_interference = 0.0f;
	 * 
	 * float distance2 = 0.0f;
	 * 
	 * while (global_interf > 0.1) {
	 * 
	 * for (int x = 0; x < aps.size(); x++) {
	 * 
	 * for (int z = 0; z < aps.size(); z++) {
	 * 
	 * if (aps.get(x).getType().toString() == "ZIGBEE") {
	 * 
	 * do {
	 * 
	 * if (aps.get((x + inc)).getType().toString() == "WIFI") {
	 * 
	 * aps.get(x + inc).setChannel(canal[y]);// wf aps.get(x).setChannel(canal[y +
	 * 1]);// zb interf = util.getAPsInterference(env.getAPs());
	 * 
	 * if (interf < global_interf) { global_interf = interf; continue; }
	 * 
	 * }
	 * 
	 * y += 2;
	 * 
	 * } while (canal[y + 1] != 26); inc += 1; // inc = 0; y = 0;
	 * 
	 * } else if (aps.get(x).getType().toString() == "WIFI") {
	 * 
	 * do {
	 * 
	 * if (aps.get((x + inc)).getType().toString() == "ZIGBEE") {
	 * 
	 * aps.get(x + inc).setChannel(canal[y]);// wf aps.get(x).setChannel(canal[y +
	 * 1]);// zb interf = util.getAPsInterference(env.getAPs());
	 * 
	 * if (interf < global_interf) { global_interf = interf; continue; }
	 * 
	 * }
	 * 
	 * y += 2; inc += 1; } while (canal[y + 1] != 26); inc += 1; y = 0;
	 * 
	 * } /* else if(aps.get(x).getType().toString() == "WIFI"){
	 * 
	 * do {
	 * 
	 * if (aps.get((x + inc)).getType().toString() == "WIFI") { distance2 =
	 * this.util.getDistance(aps.get(x), aps.get(x + inc)); interf =
	 * this.util.getNormalizedInterference(aps.get(x), aps.get(x).getChannel(),
	 * aps.get(x + inc), aps.get(x + inc).getChannel());
	 * 
	 * if (distance2 < 30 || interf > 0.4) {
	 * 
	 * //ch_wifi = getChannelWifi(aps.get((x + inc)).getChannel()); //ch_zb =
	 * getChannelZigBee(aps.get(x).getChannel());
	 * aps.get(x+inc).setChannel(canalWF[w]);//wf aps.get(x).setChannel(canal[w+1]);
	 * 
	 * 
	 * 
	 * 
	 * }
	 * 
	 * } inc += 1; temp1 += 1; if(w ==13){w=0;}else{w+=2;}
	 * 
	 * } while (temp1 < 5); inc = 0; if(w ==13){w=0;}else{w+=2;}
	 * 
	 * 
	 * 
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 * // if(local_interference < global_interf){ // util.copyAPs(env.getAPs(),
	 * local_interference); // util.copyDevices(env.getDevices(), // global_interf =
	 * local_interference; // local_interference =
	 * util.getDevicesInterference(env.getDevices());
	 * 
	 * // }
	 * 
	 * } devicesManager.fillDevicesInfo(this.env.getDevices()); }
	 * 
	 * return null; }
	 */

	/*
	 * public String channelAPsOverllaping() { float localInterference = 0.0f; long
	 * start = System.currentTimeMillis(); long elapsed = 0; float sobreposicao1 =
	 * 0; // fator de interferência float sobreposicao2 = 0; // fator de
	 * interferência // generateAPsRandomChannels(); DecimalFormat df = new
	 * DecimalFormat("##.####"); DecimalFormat df2 = new DecimalFormat("##");
	 * WriteConsole wc = new WriteConsole(); ArrayList<AP> APs = this.env.getAPs();
	 * ArrayList<AP> APs2 = new ArrayList<>(); ArrayList<Device> device =
	 * this.env.getDevices(); ArrayList<Float> interferences = new
	 * ArrayList<Float>();
	 *
	 * // double bestInterference = getAPsInterference();
	 *
	 * for (int i = 0; i < (this.env.getAmountOfDevices()); i++) {
	 * this.util.generateAPsRandomChannels(APs); // for para iterar sobre os AP's
	 *
	 * for (int j = 0; j < APs.size(); j++) {
	 *
	 * for (int k = 0; k < APs.size(); k++) {
	 *
	 * if (APs.get(j).getId() != APs.get(k).getId()) {
	 *
	 * // PEGA A DISTANCIA ANTES DE ENTRAR NO IF float distance2 =
	 * this.util.getDistance(APs.get(j), APs.get(k));
	 *
	 * if (APs.get(j).getType().toString() == "WIFI" &&
	 * APs.get(k).getType().toString() == "ZIGBEE" && distance2 <= 30) {
	 *
	 * // MÉTODO QUE RETORNA O FATOR DE SOBREPOSIÇÃO float interf =
	 * this.util.getNormalizedInterference(APs.get(j), APs.get(j).getChannel(),
	 * APs.get(k), APs.get(k).getChannel()); float interf2 = 0; AP ap2 = null;
	 *
	 * // SUBTRAI 13 DO CANAL DO ZIGBEE POR AJUSTE DA TABELA DE SOBREPOSIÇÃO int
	 * canal_zig_antes = getChannelZigBee(APs.get(k).getChannel()) - 13;
	 *
	 * if (interf > 0.0) {
	 *
	 * // APs = this.env.changeLocation(APs); //
	 * this.env.changeLocationDevice(device);
	 *
	 * // MÉTODO QUE MUDA O CANAL DO ZIGBEE SE ELE ESTIVER SOBREPOSTO E SETA NO AP2
	 * ap2 = this.getResolverInterference(APs.get(j), APs.get(k),
	 * APs.get(j).getChannel(), APs.get(k).getChannel()); //
	 * APs2.get(k).getChannel());
	 *
	 * // CONVERTE OS CANAIS DA TABELA PRA O FORMATO PADRÃO DO WIFI E ZIGBEE // AFIM
	 * DE EXIBIR EM TEMPO DE EXECUÇÃO E NO ARQUIVO TESTE.TXT int wifi_canal =
	 * getChannelWifi(APs.get(j).getChannel()); int zigbee_canal =
	 * getChannelZigBee(ap2.getChannel());
	 *
	 * // calcula novamente a sobreposição para ver o que mudou interf2 =
	 * this.util.getNormalizedInterference(APs.get(j), APs.get(j).getChannel(), ap2,
	 * ap2.getChannel()); // exibe o canal pra ver se houve modificação
	 *
	 * // PEGA OS APs e adiciona numa lista para tirar a interferencia geral //
	 * APs2.add(APs.get(j)); // APs2.add(ap2);
	 *
	 * // float interf_geral = this.util.getAPsInterference(APs2);
	 *
	 * long elapsedTimeMillis = System.currentTimeMillis() - start;
	 * this.elapsedTimeSec = elapsedTimeMillis / 1000F;
	 *
	 * // IMPRIME INTERF ENTRE DEVICES // localInterference =
	 * this.util.getDevicesInterference(this.env.getDevices()); //
	 * interferences.add(localInterference);
	 *
	 * // return String.valueOf(localInterference);
	 *
	 * // imprime os resultados no arquivo teste.txt return
	 * df2.format(APs.get(j).getX()) + "; " + df2.format(APs.get(j).getY()) + "; " +
	 * "w" + (APs.get(j).getChannel() + 1) + "; \n" + df2.format(ap2.getX() - 13) +
	 * "; " + df2.format(ap2.getY()) + "; " + "z" + (ap2.getChannel() - 13) + ";";
	 *
	 * * "\n" *
	 * +"#################################################################################################################################\n\n"
	 * * + "Id_wf: " + APs.get(j).getId() + " Id_Zb: " + APs.get(k).getId() + * *
	 * " WF---ZB(ANTES): " + (APs.get(j).getChannel()+1) + "---" + canal_zig_antes+
	 * * * "  WF---ZB(DEPOIS):" + (APs.get(j).getChannel()+1) + "---" +
	 * zigbee_canal+ * * " [interf_antes: "+df.format(interf)+ * *
	 * " depois: "+df.format(interf2)+ "] "+ * * "{Interf_APs: "+ interf_geral + * *
	 * "} Distance: "+ df.format(distance2) + * * " Time_exec: "+
	 * this.elapsedTimeSec;
	 * 
	 *
	 * } }
	 *
	 * else if (APs.get(j).getType().toString() == "ZIGBEE" &&
	 * APs.get(k).getType().toString() == "WIFI" && distance2 <= 30) {
	 * 
	 * // MÉTODO QUE RETORNA O FATOR DE SOBREPOSIÇÃO float interf =
	 * this.util.getNormalizedInterference(APs.get(j), APs.get(j).getChannel(),
	 * APs.get(k), APs.get(k).getChannel()); float interf2 = 0; AP ap2 = null; int
	 * canal_zig_antes = (APs.get(j).getChannel() - 13);
	 * 
	 * if (interf > 0.0) {
	 * 
	 * // APs = this.env.changeLocation(APs); //
	 * this.env.changeLocationDevice(device);
	 * 
	 * // MÉTODO QUE MUDA O CANAL DO ZIGBEE SE ELE ESTIVER SOBREPOSTO E SETA NO AP1
	 * // ap2 = APs2.get(j);
	 * 
	 * ap2 = getResolverInterference(APs.get(j), APs.get(k),
	 * APs.get(j).getChannel(), APs.get(k).getChannel());
	 * 
	 * // PEGA AS COORDENADAS DO ZIGBEE // int Xzb = (int) APs.get(j).getX(); // int
	 * Yzb = (int) APs.get(j).getY(); // PEGA AS COORDENADAS DO WIFI // int Xwf =
	 * (int) APs.get(k).getX(); // int Ywf = (int) APs.get(k).getY();
	 * 
	 * /// this.graph = new int[Xwf][Ywf]; // this.graph = new int[Xzb][Yzb];
	 * 
	 * // CONVERTE OS CANAIS DA TABELA PRA O FORMATO PADRÃO DO WIFI E ZIGBEE // AFIM
	 * DE EXIBIR EM TEMPO DE EXECUÇÃO E NO ARQUIVO TESTE.TXT int wifi_canal =
	 * getChannelWifi(APs.get(k).getChannel()); int zigbee_canal =
	 * getChannelZigBee(ap2.getChannel()); // SETA OS CANAIS //
	 * setChannelWifi(wifi_canal); // setChannelZigBee(zigbee_canal);
	 * 
	 * // calcula novamente a sobreposição para ver o que mudou interf2 =
	 * this.util.getNormalizedInterference(APs.get(j), APs.get(j).getChannel(), ap2,
	 * ap2.getChannel()); // exibe o canal pra ver se houve modificação
	 * 
	 * // APs2.add(APs.get(j)); // APs2.add(ap2);
	 * 
	 * // float interf_geral = this.util.getAPsInterference(APs2);
	 * 
	 * // imprime os resultados no arquivo teste.txt
	 * 
	 * long elapsedTimeMillis = System.currentTimeMillis() - start;
	 * this.elapsedTimeSec = elapsedTimeMillis / 1000F;
	 * 
	 * return df2.format(APs.get(k).getX()) + "; " + df2.format(APs.get(k).getY()) +
	 * "; " + "w" + (APs.get(k).getChannel() + 1) + ";\n " + df2.format(ap2.getX())
	 * + "; " + df2.format(ap2.getY()) + "; " + "z" + (ap2.getChannel() - 13) + ";";
	 * 
	 * 
	 * "\n"
	 * +"#################################################################################################################################"
	 * + "\n\n" + "Id_wf: " + APs.get(k).getId() + " Id_Zb: " + APs.get(j).getId() +
	 * 
	 * " WF---ZB(ANTES): " + (APs.get(k).getChannel()+1) + "---" + canal_zig_antes +
	 * 
	 * " WF---ZB(DEPOIS):" + (APs.get(k).getChannel()+1) + "---" + zigbee_canal +
	 * 
	 * " [interf_antes: "+df.format(interf)+
	 * 
	 * " depois: "+df.format(interf2)+ "] "+
	 * 
	 * "{Interf_APs: "+ interf_geral +
	 * 
	 * " Distance: "+ df.format(distance2) +
	 * 
	 * " Time_exec: "+ this.elapsedTimeSec;
	 * 
	 * 
	 * }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * } devicesManager.fillDevicesInfo(this.env.getDevices());
	 * 
	 * // IMPRIME INTERF ENTRE DEVICES localInterference =
	 * this.util.getDevicesInterference(this.env.getDevices());
	 * interferences.add(localInterference);
	 * 
	 * // return String.valueOf(localInterference);
	 * 
	 * }
	 * 
	 * return null;
	 * 
	 * }
	 * 
	 * return null; }
	 */

	/*
	 * public void changeDistance(AP ap, AP ap1, float distance) { Random aleatorio
	 * = new Random(); // int valor = aleatorio.nextInt(); // float interf =
	 * this.util.getDevicesInterference(this.env.getDevices());
	 * 
	 * float distance2 = distance;
	 * 
	 * int[] iteracao = {1,35,70,100,130,160,190,220,250,280,310,340,370,400};
	 * 
	 * float x1 = ap.getX(); float y1 = ap.getY();
	 * 
	 * float x2 = ap1.getX(); float y2 = ap1.getY();
	 * 
	 * while(distance2 < 50) {
	 * 
	 * 
	 * 
	 * while(x1 < 400.0 && x2 < 400.0 ) { x1+= 2; x2+= 2;
	 * 
	 * } while(y1 < 400 && y2 < 400.0 ) {
	 * 
	 * y1+=3; y2+=3; } /* else {
	 * 
	 * x1 = this.util.getRandomNumberFrom(10, 400); y1 =
	 * this.util.getRandomNumberFrom(10, 400);
	 * 
	 * x2 = this.util.getRandomNumberFrom(20, 400); y2 =
	 * this.util.getRandomNumberFrom(20, 400); }
	 */

	/*
	 * ap.setX(x1);//incrementa em 3m ap.setY(y1);//incrementa em 3m
	 * 
	 * ap1.setX(x2);//incrementa em 3m ap1.setY(y2);//incrementa em 3m
	 * 
	 * distance2 += this.util.getDistance(ap, ap1);
	 * 
	 * }
	 * 
	 * 
	 * 
	 * }
	 */

	public ArrayList<AP> changeDistance(final AP ap, final AP ap1, final float distance) {
		final ArrayList<AP> aps = new ArrayList<>();
		final Random aleatorio = new Random();
		float x1 = 0.0f;
		float x2 = 0.0f;
		float y1 = 0.0f;
		float y2 = 0.0f;
		// int valor = aleatorio.nextInt();
		// float interf = this.util.getDevicesInterference(this.env.getDevices());

		float distance2 = this.util.getDistance(ap, ap1);

		// int[] iteracao = {1,35,70,100,130,160,190,220,250,280,310,340,370,400};

		x1 = ap.getX();
		y1 = ap.getY();

		x2 = ap1.getX();
		y2 = ap1.getY();

		while (distance2 <= 35) {

			if (x1 < 400.0 && x2 < 400.0 && y1 < 400 && y2 < 400) {

				x1 += this.util.getRandomNumberFrom(0, 20);
				x2 += this.util.getRandomNumberFrom(0, 10);

				y1 += this.util.getRandomNumberFrom(0, 10);
				y2 += this.util.getRandomNumberFrom(0, 20);

			}

			/*
			 * else {
			 * 
			 * x1 = this.util.getRandomNumberFrom(10, 400); y1 =
			 * this.util.getRandomNumberFrom(10, 400);
			 * 
			 * x2 = this.util.getRandomNumberFrom(20, 400); y2 =
			 * this.util.getRandomNumberFrom(20, 400); }
			 */

			ap.setX(x1);// incrementa em 3m
			ap.setY(y1);// incrementa em 3m

			ap1.setX(x2);// incrementa em 3m
			ap1.setY(y2);// incrementa em 3m

			distance2 += this.util.getDistance(ap, ap1);

		}
		if (ap.getType().toString() == "ZIGBEE") {

			aps.add(ap1);// wf
			aps.add(ap);// zb

		} else {
			aps.add(ap);// wf
			aps.add(ap1);// zb
		}

		return aps;

	}

	public ArrayList<Float> getMenorValor(final ArrayList<Float> lista, final float a, final float b, final float c) {
		if ((a > b && a > c && b > c)) {
			lista.add(a);
			lista.add(b);
			lista.add(c);
		} else if (b > a && b > c && a > c) {

		} else if (c > a && c > b && a > c) {

		}

		return lista;

	}

	public int getChannelWifi(final int x) {
		return x + 1;

	}

	public int getChannelZigBee(final int x) {
		return x - 13;
	}

	public int setChannelWifi(final int a) {
		return a - 1;
	}

	public int setChannelZigBee(final int x) {
		return x + 13;
	}
	
	
	// MÉTODO PARA ITERAR SOBRE OS CANAIS
		/*public ArrayList<Integer> getNextChannelWifi(AP ap, AP ap1) {
			 ArrayList<Integer> ch = new ArrayList<>();
			
			float inter_factor = util.getAPsInterference(env.getAPs());
			float interf_temp = inter_factor;
			int wf=0;
			int zb=0;
			

			for (int z=0;z <=12;z++) {
				for (int y = 0; y <= 15; y++) {

					ap.setChannel(z);//wf
					ap1.setChannel(y+13);//zb
				
					inter_factor = util.getAPsInterference(env.getAPs());

					if (inter_factor < interf_temp) {
						interf_temp = inter_factor;
						
						  wf=z;
						  zb=y+13;
						
					}
				

				}

				
			}
				ch.add(wf);
				ch.add(zb);
				return ch;

		}*/

	// MÉTODO PARA ITERAR SOBRE OS CANAIS
	public  Integer getNextChannelZigbee(AP ap) {
		ArrayList<Integer> ch = new ArrayList<>();
		float inter_factor = util.getAPsInterference(env.getAPs());
		float interf_temp = inter_factor;
		int channel=0;
		int best_ch=ap.getChannel();
	

		

		for (int x = 13; x <= 28; x++) {
			channel = x;

			ap.setChannel(channel);
			
			inter_factor = this.util.getAPsInterference(env.getAPs());

			if (inter_factor < interf_temp) {
				interf_temp = inter_factor;
				best_ch = channel;
				

			}

		}
		return  best_ch;
	}
			

	

	
	
	public Integer getNextChannelWifi(AP ap) {
		 ArrayList<Integer> ch = new ArrayList<>();
		
		float inter_factor = util.getAPsInterference(env.getAPs());
		float interf_temp = inter_factor;
		int channel=0;
		int best_ch=ap.getChannel();
		
		

		for (int z=0;z <=12;z++) {
			
				
				ap.setChannel(z);//wf
			
				inter_factor = util.getAPsInterference(env.getAPs());
				channel = ap.getChannel();

				if (inter_factor < interf_temp) {
					interf_temp = inter_factor;
					
					  best_ch= channel;
					  
					
				}
			

			}
			return best_ch;

			
		}
			

	

	public void getMitigaterInterference() {
		final long start = System.currentTimeMillis();

		 ArrayList<AP> APs = this.env.getAPs();
		/*
		 * DecimalFormat df2 = new DecimalFormat("##.####"); DecimalFormat df3 = new
		 * DecimalFormat("##.#"); ArrayList<AP> aps = new ArrayList<>(); int canal_wifi;
		 */
		

		// CALCULA A INTERFERÊNCIA DOS AP'S
		
		float global_interf = util.getAPsInterference(env.getAPs());
		final ArrayList<Float> interferences = new ArrayList<Float>();

		float best_interf = global_interf;
		float inter_factor1 = 0.0f;
		float inter_factor2 = 0.0f;
		float inter_factor3 = 0.0f;
	

		// float media_interf=global_interf;
		float localInterference = 0.0f;
		// float tempInterf=0.0f;
		 int wf=0;
		 int zb=0;
		 int count=0;
		 int wifi=0;
    	 int zigbee=0; 
		
		for(int u=0;u< env.getDevices().size()*2;u++) {
			util.generateAPsRandomChannels(env.getAPs());
			for (int x = 0; x < APs.size(); x++) {
				for (int y = 0; y < APs.size(); y++) {
					 
				

					if (APs.get(x).getType().toString() == "WIFI" && APs.get(y).getType().toString() == "ZIGBEE") {
						
					

							// TRATAMENTO WIFI
						inter_factor1 = util.getAPsInterference(env.getAPs());
						wifi = APs.get(x).getChannel();//wf
						zigbee = APs.get(y).getChannel();//zb
 					
					while(count <=2) {
							//TESTA INICIALMENTE A INTERF
							
							
							//BUSCA A MENOR INTERF MUDANDO CANAL WIFI
							wf = getNextChannelWifi(APs.get(x));				
							zb = getNextChannelZigbee(APs.get(y));
							
							//ATRIBUI OS CANAIS BUSCADOS NO WIFI E ZIGBEE E TESTA INTERF
							APs.get(x).setChannel(wf);//wf
							APs.get(y).setChannel(zb);//zb	
							inter_factor2 = util.getAPsInterference(env.getAPs());
							
							
						
						count+=1;
 					}
 					count=0;
						
							if ( inter_factor1 < inter_factor2) {
								
								if (inter_factor1 < best_interf) {
									best_interf = inter_factor1;
									//guarda o melhor canal
									int	wf_temp1 = wifi;
									int	zb_temp1 = zigbee;
									
									
									APs.get(x).setChannel(wf_temp1);//wf
									APs.get(y).setChannel(zb_temp1);//zb
							
								}
							


							} else if (inter_factor2 < inter_factor1) {
								if (inter_factor2 < best_interf) {
									best_interf = inter_factor2;
										//guarda o melhor canal
										int	wf_temp2 = wf;
										int	zb_temp2 = zb;
								
								
									APs.get(x).setChannel(wf_temp2);//wf
									APs.get(y).setChannel(zb_temp2);//zb
									
								

								}
								
							}
							else if (inter_factor1 > best_interf && inter_factor1 > inter_factor2) {
								if(inter_factor2 < inter_factor1) {
									int	wf_temp3 = wf;
									int	zb_temp3 = zb;
									APs.get(x).setChannel(wf_temp3);//wf
									APs.get(y).setChannel(zb_temp3);//zb
							
									
								}
							}
							devicesManager.fillDevicesInfo(this.env.getDevices());
					
					}
							
				
					if (APs.get(x).getType().toString() == "ZIGBEE" && APs.get(y).getType().toString() == "WIFI") {
						
						
						
						while(count <=2) {
									inter_factor1 = util.getAPsInterference(env.getAPs());
									 wifi = APs.get(y).getChannel();//wf
									 zigbee = APs.get(x).getChannel();//zb
							
									//BUSCA A MENOR INTERF MUDANDO CANAL
									wf = getNextChannelWifi(APs.get(y));						
									zb = getNextChannelZigbee(APs.get(x));
								

									//ATRIBUI OS CANAIS BUSCADOS E TESTA INTERF
									APs.get(y).setChannel(wf);//wf
									APs.get(x).setChannel(zb);//zb
									inter_factor2 = util.getAPsInterference(env.getAPs());
									devicesManager.fillDevicesInfo(this.env.getDevices());
									
									
								
									
							count+=1;		
						}
						count=0;

									if ( inter_factor1 < inter_factor2) {
										
										if (inter_factor1 < best_interf) {
											best_interf = inter_factor1;
											//guarda o melhor canal
											int	wf_temp1 = wifi;
											int	zb_temp1 = zigbee;
											
											
											APs.get(y).setChannel(wf_temp1);//wf
											APs.get(x).setChannel(zb_temp1);//zb
									
										}
									
			
			
									} else if (inter_factor2 < inter_factor1) {
										if (inter_factor2 < best_interf) {
											best_interf = inter_factor2;
												//guarda o melhor canal
												int	wf_temp2 = wf;
												int	zb_temp2 = zb;
										
										
											APs.get(y).setChannel(wf_temp2);//wf
											APs.get(x).setChannel(zb_temp2);//zb
											
										
			
										}
										
									}
									else if (inter_factor1 > best_interf && inter_factor1 > inter_factor2) {
										if(inter_factor2 < inter_factor1 && inter_factor2 >=best_interf) {
											int	wf_temp3 = wf;
											int	zb_temp3 = zb;
											APs.get(y).setChannel(wf_temp3);//wf
											APs.get(x).setChannel(zb_temp3);//zb
									
											
										}
									}
								

						

								devicesManager.fillDevicesInfo(this.env.getDevices());
								

				
					}
					
						
				}
				best_interf = util.getAPsInterference(env.getAPs());
				devicesManager.fillDevicesInfo(this.env.getDevices());
				localInterference = this.util.getDevicesInterference(this.env.getDevices());
				interferences.add(localInterference);

			

			
				
			}
			
			
		}
			final long elapsedTimeMillis = System.currentTimeMillis() - start;
			this.elapsedTimeSec=elapsedTimeMillis/1000F;System.out.println("Tempo total : "+this.elapsedTimeSec);
	}
	
		



	@Override
	public ArrayList<Float> channelChooser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String channelAPsOverllaping() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
