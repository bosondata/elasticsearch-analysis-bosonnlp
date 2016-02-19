/**
 * 玻森数据 中文分词 版本 0.8.2
 *
 */
package org.bosonnlp.analyzer.lucene;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.json.JSONException;

import com.mashape.unirest.http.exceptions.UnirestException;


/**
 * Implementation of Bosonnlp word segmenter 
 * on Lucene Analyzer interface
 */
public final class BosonNLPAnalyzer extends Analyzer{
    
    private int spaceMode = 0;
    private int oovLevel = 3;
    private int t2s = 0;
    private int specialCharConv = 0;
    private String BOSONNLP_API_TOKEN;
    private String TAG_URL;
    
    public BosonNLPAnalyzer(String URL, String BAT){
    	super();
        this.TAG_URL = URL;
    	this.BOSONNLP_API_TOKEN = BAT;
    }
    
    public BosonNLPAnalyzer(String URL, String BAT, int spaceMode, int oovLevel, int t2s, int specialCharConv){
        super();
        this.TAG_URL = URL;
        this.BOSONNLP_API_TOKEN = BAT;
        this.spaceMode = spaceMode;
        this.oovLevel = oovLevel;
        this.t2s = t2s;
        this.specialCharConv = specialCharConv;
    }    
    
    @Override
    protected TokenStreamComponents createComponents(String fieldName){
        Tokenizer BTokenizer = null;
        try {

            BTokenizer = new BosonNLPTokenizer(TAG_URL, BOSONNLP_API_TOKEN, spaceMode, 
                    oovLevel, t2s, specialCharConv);
        } catch (IOException | JSONException | UnirestException e) {
            e.printStackTrace();
        }
        return new TokenStreamComponents(BTokenizer);    
    }
   
}
