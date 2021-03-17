import java.util.ArrayList;

// import org.jamesframework.core.problems.objectives.Objective;
// import org.jamesframework.core.problems.objectives.evaluations.Evaluation;
// import org.jamesframework.core.subset.SubsetSolution;

public class ChannelAssigner implements ChannelAssignerInterface {
	private int[] aux;
	private Util util;
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

	public float getElapsedTime() {
		return elapsedTimeSec;
	}

	@Override
	public void setElapsedTime(final float runtime) {
		this.elapsedTimeSec = runtime;
	}

	public ArrayList<Integer> getNextChannel(AP ap, AP ap2) {
		ArrayList<Integer> ch = new ArrayList<>();

		float inter_factor = Float.MAX_VALUE;
		float interf_temp = inter_factor;

		int ch_wifi = 0;
		int ch_zb = 0;
		int best_ch = ap.getChannel();
		int count = 0;

		for (int z = 0; z <= 12; z++) {

			for (int x = 13; x <= 28; x++) {

				ap.setChannel(z);// wf
				ap2.setChannel(x);// zb

				inter_factor = util.getAPsInterference(env.getAPs());

				if (inter_factor < interf_temp) {
					interf_temp = inter_factor;
					ch_wifi = ap.getChannel();
					ch_zb = ap2.getChannel();
					ch.add(ch_wifi);
					ch.add(ch_zb);

				}
			}

		}

		return ch;

	}

	public float getfilterInterference(AP ap, AP ap2) {
		ArrayList<AP> apTemp = new ArrayList<>();
		apTemp.add(ap);
		apTemp.add(ap2);

		float retorno = util.getAPsInterference(apTemp);

		return retorno;

	}

	public Integer getNextChannelWifi(AP ap) {
		float inter_factor = Float.MAX_VALUE;
		float best_interf = this.util.getAPsInterference(env.getAPs());
		int best_ch = ap.getChannel();

		for (int z = 0; z <= 12; z++) {

			ap.setChannel(z);// wf

			inter_factor = util.getAPsInterference(env.getAPs());

			if (inter_factor < best_interf) {
				best_interf = inter_factor;
				best_ch = z;
				break;
			}
//			else {
//				ap.setChannel(best_ch);
//			}

		}

		return best_ch;

	}

	// MÉTODO PARA ITERAR SOBRE OS CANAIS
	public Integer getNextChannelZigbee(AP ap) {
		float inter_factor = Float.MAX_VALUE;
		float best_interf = this.util.getAPsInterference(env.getAPs());
		int best_ch = ap.getChannel();

		for (int x = 13; x <= 28; x++) {

			ap.setChannel(x);

			inter_factor = this.util.getAPsInterference(env.getAPs());

			if (inter_factor < best_interf) {
				best_interf = inter_factor;
				best_ch = x;
				break;

			}

		}

		return best_ch;
	}

	public ArrayList<Float> channelOverlappingAvoidance() {
		final long start = System.currentTimeMillis();

		ArrayList<AP> APs = this.env.getAPs();
		ArrayList<Integer> canal = new ArrayList<>();

		float global_interf = Float.MAX_VALUE;
		final ArrayList<Float> interferences = new ArrayList<Float>();
		ArrayList<AP> bestAPConfig = new ArrayList<AP>();
		ArrayList<Device> bestDeviceConfig = new ArrayList<Device>();
		util.copyAPs(this.env.getAPs(), bestAPConfig);
		util.copyDevices(this.env.getDevices(), bestDeviceConfig);

		float best_interf = 0.0f;
		float localInterference = 0.0f;
		int channel_temp = 0;
		float interf_local = 0;
		float interferencia = 0.0f;

//		for (int u = 0; u < (this.env.getDevices().size()); u++) {
//			util.generateAPsRandomChannels(env.getAPs());
//			best_interf = util.getAPsInterference(this.env.getAPs());

			for (int x = 0; x < APs.size(); x++) {
				for (int y = 0; y < APs.size(); y++) {

					if (APs.get(x).getType().equals(APs.get(y).getType())) {

						if (APs.get(x).getId() != APs.get(y).getId()) {

							interf_local = getfilterInterference(APs.get(x), APs.get(y));

							if (interf_local > 0.0f) {
								channel_temp = APs.get(y).getChannel();

								if (APs.get(y).getType().toString() == "ZIGBEE") {
									int zb = getNextChannelZigbee(APs.get(y));
									APs.get(y).setChannel(zb);
									interferencia = util.getAPsInterference(env.getAPs());

									if (interferencia < best_interf) {
										best_interf = interferencia;
										channel_temp = zb;

									} else {

										APs.get(y).setChannel(channel_temp);
									}

								}

								else if (APs.get(y).getType().toString() == "WIFI") {

									int wf = getNextChannelWifi(APs.get(y));// busca um canal melhor

									APs.get(y).setChannel(wf);// atribui o canal melhor

									interferencia = util.getAPsInterference(env.getAPs());// testa

									if (interferencia < best_interf) {
										best_interf = interferencia;
										channel_temp = wf;

									} else {
										APs.get(y).setChannel(channel_temp);
									}

								}

							}
						}
					}

				}
			}

			devicesManager.fillDevicesInfo(this.env.getDevices());
			localInterference = this.util.getDevicesInterference(this.env.getDevices());
			if (localInterference < global_interf) {
				global_interf = localInterference;
				util.copyAPs(this.env.getAPs(), bestAPConfig);
				util.copyDevices(this.env.getDevices(), bestDeviceConfig);
			}

			interferences.add(global_interf);

		//}

		final long elapsedTimeMillis = System.currentTimeMillis() - start;
		this.elapsedTimeSec = elapsedTimeMillis / 1000F;
		System.out.println("######################################################");
		System.out.println("Tempo total : " + this.elapsedTimeSec);
		System.out.println("######################################################");
		System.out.println("global_interf: " + global_interf);
		accessPointsManager.fillAPsInfo(this.env.getDevices(), this.env.getAPs());

		util.copyAPs(bestAPConfig, this.env.getAPs());
		util.copyDevices(bestDeviceConfig, this.env.getDevices());
		return interferences;

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
