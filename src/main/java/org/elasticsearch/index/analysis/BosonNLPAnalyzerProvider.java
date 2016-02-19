package org.elasticsearch.index.analysis;

import org.bosonnlp.analyzer.lucene.BosonNLPAnalyzer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.settings.IndexSettingsService;

public class BosonNLPAnalyzerProvider extends AbstractIndexAnalyzerProvider<BosonNLPAnalyzer> {
    private final BosonNLPAnalyzer analyzer;
    private String BOSONNLP_API_TOKEN;
    private String TAG_URL;    
    private int spaceMode;
    private int oovLevel;
    private int t2s;
    private int specialCharConv;

    /*
     * Name to associate with this class. It will be used in BinderProcesser
     */
    public static final String NAME = "bosonnlp";

    @Inject
    public BosonNLPAnalyzerProvider(Index index, IndexSettingsService indexSettingsService, Environment env, @Assisted String name, @Assisted Settings settings) {

        super(index, indexSettingsService.getSettings(), name, settings);
        this.TAG_URL = settings.get("API_URL", "").toString();
        this.BOSONNLP_API_TOKEN = settings.get("API_TOKEN", "").toString();
        this.spaceMode = Integer.parseInt(settings.get("space_mode", "0"));
        this.oovLevel = Integer.parseInt(settings.get("oov_level", "3"));
        this.t2s = Integer.parseInt(settings.get("t2s", "0"));
        this.specialCharConv = Integer.parseInt(settings.get("spechial_char_conv", "0"));

        this.analyzer = new BosonNLPAnalyzer(TAG_URL, BOSONNLP_API_TOKEN, spaceMode, oovLevel, t2s, specialCharConv);

    }

    @Override
    public BosonNLPAnalyzer get() {
        return this.analyzer;
    }
}
