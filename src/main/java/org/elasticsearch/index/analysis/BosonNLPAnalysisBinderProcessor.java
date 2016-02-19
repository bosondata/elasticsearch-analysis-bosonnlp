package org.elasticsearch.index.analysis;

import org.elasticsearch.index.analysis.AnalysisModule.AnalysisBinderProcessor;


public class BosonNLPAnalysisBinderProcessor extends AnalysisBinderProcessor {

    /*
     * It simply adds our analyzer provider class to a list of bindings.
     */
    @Override
    public void processAnalyzers(AnalyzersBindings analyzersBindings) {
        analyzersBindings.processAnalyzer(BosonNLPAnalyzerProvider.NAME, BosonNLPAnalyzerProvider.class);
    }

    @Override
    public void processTokenizers(TokenizersBindings tokenizersBindings) {
        tokenizersBindings.processTokenizer(BosonNLPTokenizerFactory.NAME, BosonNLPTokenizerFactory.class);    
    }
        
}
