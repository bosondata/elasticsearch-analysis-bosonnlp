package org.elasticsearch.plugin.analysis.bosonnlp;

import java.util.Collection;
import java.util.Collections;

import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.index.analysis.BosonNLPAnalysisBinderProcessor;
import org.elasticsearch.indices.analysis.BosonNLPIndicesAnalysisModule;
import org.elasticsearch.plugins.Plugin;

public class AnalysisBosonNLPPlugin extends Plugin {

    private final Settings settings;

    public AnalysisBosonNLPPlugin(Settings settings) {
        this.settings = settings;
    }

    @Override
    public String name() {
        return "analysis-bosonnlp";
    }

    @Override
    public String description() {
        return "BosonNLP analysis plugin for elasticsearch.";
    }

    @Override
    public Collection<Module> nodeModules() {
        return Collections.<Module> singletonList(new BosonNLPIndicesAnalysisModule());
    }

    public void onModule(AnalysisModule module) {
        module.addProcessor(new BosonNLPAnalysisBinderProcessor());
    }

}
