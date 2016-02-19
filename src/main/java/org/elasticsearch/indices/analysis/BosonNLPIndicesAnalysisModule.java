package org.elasticsearch.indices.analysis;

import org.elasticsearch.common.inject.AbstractModule;

public class BosonNLPIndicesAnalysisModule extends AbstractModule{

	@Override
	protected void configure() {
		bind(BosonNLPIndicesAnalysis.class).asEagerSingleton();
		
	}

}
