package io.github.matsimmelbourne.baseline.run;

import ch.sbb.matsim.mobsim.qsim.SBBTransitModule;
import ch.sbb.matsim.routing.pt.raptor.SwissRailRaptorModule;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.NetworkWriter;
import org.matsim.contrib.bicycle.BicycleConfigGroup;
import org.matsim.contrib.bicycle.Bicycles;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.network.algorithms.MultimodalNetworkCleaner;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.vehicles.VehicleType;
import org.matsim.vehicles.VehicleUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RunBaselineScenario {
    private static final Logger LOG = Logger.getLogger(RunBaselineScenario.class);

    private String configFile;
    private String inputDir;

    private Config config;
    private Scenario scenario;
    private Controler controler;
    private Network network;
    private String cleanNetwork;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        RunBaselineScenario rbs = new RunBaselineScenario(args[0], args[1], args[2]);
        rbs.run();
    }

    public RunBaselineScenario(String inputDir, String configName, String cleanNetwork) throws FileNotFoundException, IOException {

            this.configFile = inputDir + "/" + configName; // e.g. "./scenario/v1.1/config.xml"
            this.inputDir = inputDir;
            this.cleanNetwork=cleanNetwork; //true or false
            this.config = ConfigUtils.loadConfig(configFile, new BicycleConfigGroup());
            config.controler().setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists);
            fillConfigWithBicycleStandardValues(config, false);

            this.scenario = ScenarioUtils.loadScenario(config);

            if (cleanNetwork.equals("true")) {
                System.out.println("About to clean the network");
                this.network = this.scenario.getNetwork();
                cleanNetworkForCarBicycle(network);
            }
            this.controler = new Controler(scenario);
        }

    private static void cleanNetworkForCarBicycle(Network network){
        System.out.println("cleaning the network");
        Set<String> mode_Set = new HashSet<String>();
        mode_Set.add("car");
        new MultimodalNetworkCleaner(network).run(mode_Set);
        Set<String> mode_Set2 = new HashSet<String>();
        mode_Set2.add("bicycle");
        new MultimodalNetworkCleaner(network).run(mode_Set2);
        new NetworkWriter(network).write("./networkCleaned.xml.gz");
    }

    private static void fillConfigWithBicycleStandardValues(Config config, boolean considerMotorizedInteraction){

        BicycleConfigGroup bicycleConfigGroup = (BicycleConfigGroup) config.getModules().get(BicycleConfigGroup.GROUP_NAME);
        bicycleConfigGroup.setMarginalUtilityOfInfrastructure_m(-0.04);
        bicycleConfigGroup.setMarginalUtilityOfComfort_m(-0.0002);
        bicycleConfigGroup.setMarginalUtilityOfGradient_m_100m(-0.02);
        List<String> mainModeList = new ArrayList<>();
        mainModeList.add("bicycle");
        mainModeList.add(TransportMode.car);
        config.qsim().setMainModes(mainModeList);
        config.plansCalcRoute().setNetworkModes(mainModeList);
        config.planCalcScore().addModeParams( new PlanCalcScoreConfigGroup.ModeParams("bicycle").setConstant(0. ).setMarginalUtilityOfDistance(-0.0004 ).setMarginalUtilityOfTraveling(-6.0 ).setMonetaryDistanceRate(0. ) );

        if (considerMotorizedInteraction) {
            bicycleConfigGroup.setMotorizedInteraction(considerMotorizedInteraction);
        }
    }


    public void run(){
        Bicycles.addAsOverridingModule(controler);
        controler.addOverridingModule(new SBBTransitModule());
        controler.addOverridingModule(new SwissRailRaptorModule());
        controler.run();
    }

}
