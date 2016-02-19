/**
 * BosonNLP word segmenter release 0.8.2
 * 玻森中文分词 版本 0.8.2
 */
package org.bosonnlp.analyzer.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public final class BosonNLPWordSegmenter {

    private String TAG_URL;
    private String BOSONNLP_API_TOKEN;
    private int spaceMode;
    private int oovLevel;
    private int t2s;
    private int specialCharConv;

    private List<String> words = new ArrayList<String>();
    private Iterator<String> wordsIter = Collections.emptyIterator();
    private Reader input;

    private ESLogger logger = ESLoggerFactory.getLogger("bosonnlp plugin");

    public BosonNLPWordSegmenter(Reader input, String URL, String BAT, int spaceMode, int oovLevel, int t2s, int specialCharConv)
            throws IOException, JSONException, UnirestException {
        this.input = input;
        this.TAG_URL = URL;
        this.BOSONNLP_API_TOKEN = BAT;
        this.spaceMode = spaceMode;
        this.oovLevel = oovLevel;
        this.t2s = t2s;
        this.specialCharConv = specialCharConv;
    }

    /**
     * Get the input string
     * 
     * @param input
     * @return
     * @throws IOException
     */
    public String getStringText(Reader input) throws IOException {
        StringBuffer target = new StringBuffer();
        try (BufferedReader br = new BufferedReader(input)) {
            String temp;
            while ((temp = br.readLine()) != null) {
                target.append(temp + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return target.toString();
    }

    /**
     * Call BosonNLP word segmenter API via Java library Unirest.
     * 
     * @param target, the text to be processed
     * @throws JSONException
     * @throws UnirestException
     * @throws IOException
     */
    public void segment(String target) throws JSONException, UnirestException, IOException {
        // Clean the word token
        this.words.clear();
        // Get the new word token of target
        String body = new JSONArray(new String[] { target }).toString();
        HttpResponse<JsonNode> jsonResponse = Unirest.post(this.TAG_URL)
                .queryString("space_mode", this.spaceMode)
                .queryString("oov_level", this.oovLevel)
                .queryString("t2s", this.t2s)
                .queryString("special_char_conv", this.specialCharConv)
                .header("Accept", "application/json")
                .header("X-Token", this.BOSONNLP_API_TOKEN).body(body).asJson();

        makeToken(jsonResponse.getBody());
    }

    /**
     * Get the token result from BosonNLP word segmenter.
     * 
     * @param jn
     */
    private void makeToken(JsonNode jn) {
        try {
            // Get Json-array as it encoded before
            JSONArray jaTemp = jn.getArray();
            if (jaTemp.length() > 0) {
                JSONObject jo = jaTemp.getJSONObject(0);
                if (jo != null && jo.has("word")) {
                    JSONArray ja = jo.getJSONArray("word");

                    for (int i = 0; i < ja.length(); i++) {
                        this.words.add(ja.get(i).toString());
                    }
                } else {
                    logger.error("Check the validation of your API TOKEN or internet",
                            new UnirestException(jo.toString()), jo);
                    throw new RuntimeException("Check validation of API TOKEN or internet: " + jo.toString());
                }
            } else {
                logger.info("No string input", jaTemp);
            }
                
        } catch (JSONException e) {
            logger.error("JSONException", e, e);
            throw new RuntimeException("JSONException");
        } finally {
            // Assign to words iterator
            this.wordsIter = this.words.iterator();
        }
    }

    public void reset(Reader input) throws IOException, JSONException, UnirestException {
        // Reset input
        setInput(input);
        String target = getStringText(input);
        // Do segmentation
        segment(target);
    }

    public Reader getInput() {
        return input;
    }

    public void setInput(Reader input) {
        this.input = input;
    }

    public Iterator<String> getWordsIter() {
        return this.wordsIter;
    }
}
