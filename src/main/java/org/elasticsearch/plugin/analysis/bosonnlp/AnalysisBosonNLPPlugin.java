package org.elasticsearch.plugin.analysis.bosonnlp;


import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.index.analysis.BosonNLPAnalysisBinderProcessor;
import org.elasticsearch.plugins.AbstractPlugin;


public class AnalysisBosonNLPPlugin extends AbstractPlugin {

    @Override 
    public String name() {
            return "analysis-bosonnlp";
    }

    @Override 
    public String description() {
            return "bosonNLP analysis plugin for elasticsearch.";
    }
   
    public void onModule(AnalysisModule module) {
        module.addProcessor(new BosonNLPAnalysisBinderProcessor());
    }
    
}

