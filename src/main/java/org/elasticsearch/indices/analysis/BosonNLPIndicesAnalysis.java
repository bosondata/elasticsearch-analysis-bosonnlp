package org.elasticsearch.indices.analysis;

import java.io.IOException;

import org.apache.lucene.analysis.Tokenizer;
import org.bosonnlp.analyzer.lucene.BosonNLPAnalyzer;
import org.bosonnlp.analyzer.lucene.BosonNLPTokenizer;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.analysis.AnalyzerScope;
import org.elasticsearch.index.analysis.PreBuiltAnalyzerProviderFactory;
import org.elasticsearch.index.analysis.PreBuiltTokenizerFactoryFactory;
import org.elasticsearch.index.analysis.TokenizerFactory;
import org.json.JSONException;

import com.mashape.unirest.http.exceptions.UnirestException;


/**
 * Registers indices level analysis components.
 */
public class BosonNLPIndicesAnalysis extends AbstractComponent {

    private String BOSONNLP_API_TOKEN;
    private String TAG_URL;
    private int spaceMode;
    private int oovLevel;
    private int t2s;
    private int specialCharConv;
	
    @Inject    
    public BosonNLPIndicesAnalysis(final Settings settings, IndicesAnalysisService indicesAnalysisService) {
        super(settings);
        // Get all the arguments from settings
        this.TAG_URL = settings.get("API_URL", "").toString();
        this.BOSONNLP_API_TOKEN = settings.get("API_TOKEN", "").toString();
        this.spaceMode = Integer.parseInt(settings.get("space_mode", "0"));
        this.oovLevel = Integer.parseInt(settings.get("oov_level", "3"));
        this.t2s = Integer.parseInt(settings.get("t2s", "0"));
        this.specialCharConv = Integer.parseInt(settings.get("spechial_char_conv", "0"));
        
        // Register the bosonnlp type analyzer
        indicesAnalysisService.analyzerProviderFactories().put("bosonnlp", 
                new PreBuiltAnalyzerProviderFactory("bosonnlp", AnalyzerScope.GLOBAL, 
                        new BosonNLPAnalyzer(TAG_URL, BOSONNLP_API_TOKEN, spaceMode, oovLevel, t2s, specialCharConv)));
        
        // Register the bosonnlp type tokenizer
        indicesAnalysisService.tokenizerFactories().put("bosonnlp", 
                new PreBuiltTokenizerFactoryFactory(new TokenizerFactory(){

                    @Override
                    public String name() {
                        return "bosonnlp";
                    }

                    @Override
                    public Tokenizer create() {
                        BosonNLPTokenizer BToken = null;
                        try {
                            BToken = new BosonNLPTokenizer(TAG_URL, BOSONNLP_API_TOKEN, spaceMode, oovLevel, t2s, specialCharConv);
                        } catch (JSONException | IOException | UnirestException e) {

                            e.printStackTrace();
                        }
                        return BToken;
                    }
        			
                }));
		
    }
	
}
