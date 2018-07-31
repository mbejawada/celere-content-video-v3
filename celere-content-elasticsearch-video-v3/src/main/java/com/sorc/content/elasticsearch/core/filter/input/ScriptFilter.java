package com.sorc.content.elasticsearch.core.filter.input;

import org.elasticsearch.script.Script;

public class ScriptFilter implements IElasticSearchFilter {

	private Script script;
	
	public ScriptFilter(Script script) {
		this.script = script;
	}

	public Script getScript() {
		return script;
	}

	public void setScript(Script script) {
		this.script = script;
	}
}
