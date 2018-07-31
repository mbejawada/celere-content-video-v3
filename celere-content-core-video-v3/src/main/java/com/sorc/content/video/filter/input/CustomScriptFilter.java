package com.sorc.content.video.filter.input;

public class CustomScriptFilter {

	private String scriptType;
	private String scriptField;
	private Long msrp;
	
	public CustomScriptFilter() {
	}

	public String getScriptType() {
		return scriptType;
	}

	public void setScriptType(String scriptType) {
		this.scriptType = scriptType;
	}

	public String getScriptField() {
		return scriptField;
	}

	public void setScriptField(String scriptField) {
		this.scriptField = scriptField;
	}

	public Long getMsrp() {
		return msrp;
	}

	public void setMsrp(Long msrp) {
		this.msrp = msrp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((msrp == null) ? 0 : msrp.hashCode());
		result = prime * result
				+ ((scriptField == null) ? 0 : scriptField.hashCode());
		result = prime * result
				+ ((scriptType == null) ? 0 : scriptType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomScriptFilter other = (CustomScriptFilter) obj;
		if (msrp == null) {
			if (other.msrp != null)
				return false;
		} else if (!msrp.equals(other.msrp))
			return false;
		if (scriptField == null) {
			if (other.scriptField != null)
				return false;
		} else if (!scriptField.equals(other.scriptField))
			return false;
		if (scriptType == null) {
			if (other.scriptType != null)
				return false;
		} else if (!scriptType.equals(other.scriptType))
			return false;
		return true;
	}

}
