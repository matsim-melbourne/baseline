package io.github.matsimmelbourne.baseline.run;

import org.matsim.api.core.v01.Scenario;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.network.algorithms.MultimodalNetworkCleaner;
import org.matsim.core.scenario.ScenarioUtils;

import java.util.HashSet;
import java.util.Set;

public class RunBaselineScenario {
    public static void main(String[] args) {
        Config config;
        config = ConfigUtils.loadConfig( "./scenario/config.xml" );

        config.controler().setOverwriteFileSetting( OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists );

        Scenario scenario = ScenarioUtils.loadScenario(config) ;

        // For now the network cleaner must be run on the mm network, to be fixed in future - jafshin Nov27 2020
        Set<String> mode_Set = new HashSet<String>();
        mode_Set.add("car");
        // uncomment this if also simulating bicycles
        //mode_Set.add("bicycle");
        new MultimodalNetworkCleaner(scenario.getNetwork()).run(mode_Set);

        Controler controler = new Controler( scenario ) ;

        controler.run();
    }
}
