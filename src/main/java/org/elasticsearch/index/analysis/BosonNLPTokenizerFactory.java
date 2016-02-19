package org.elasticsearch.index.analysis;

import java.io.IOException;

import org.apache.lucene.analysis.Tokenizer;
import org.bosonnlp.analyzer.lucene.BosonNLPTokenizer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.settings.IndexSettingsService;
import org.json.JSONException;

import com.mashape.unirest.http.exceptions.UnirestException;

public class BosonNLPTokenizerFactory extends AbstractTokenizerFactory {
    private final Settings settings;
    private String BOSONNLP_API_TOKEN;
    private String TAG_URL;
    private int spaceMode;
    private int oovLevel;
    private int t2s;
    private int specialCharConv;

    // The name is associate with this class, which will be
    // called in BinderProcesser
    public static final String NAME = "bosonnlp";

    @Inject
    public BosonNLPTokenizerFactory(Index index, IndexSettingsService indexSettingsService, @Assisted String name, @Assisted Settings settings) {
        super(index, indexSettingsService.getSettings(), name, settings);
        this.settings = settings;
    }

    @Override
    public Tokenizer create() {
        TAG_URL = settings.get("API_URL", "").toString();
        BOSONNLP_API_TOKEN = settings.get("API_TOKEN", "").toString();
        BosonNLPTokenizer BTokenizer = null;
        spaceMode = Integer.parseInt(settings.get("space_mode", "0"));
        oovLevel = Integer.parseInt(settings.get("oov_level", "3"));
        t2s = Integer.parseInt(settings.get("t2s", "0"));
        specialCharConv = Integer.parseInt(settings.get("spechial_char_conv", "0"));

        try {
            BTokenizer = new BosonNLPTokenizer(TAG_URL, BOSONNLP_API_TOKEN, spaceMode, oovLevel, t2s, specialCharConv);
        } catch (IOException | JSONException | UnirestException e) {
            e.printStackTrace();
        }
        return BTokenizer;
    }

}
