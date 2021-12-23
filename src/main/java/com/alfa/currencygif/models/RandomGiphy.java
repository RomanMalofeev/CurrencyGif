package com.alfa.currencygif.models;


import com.fasterxml.jackson.annotation.JsonProperty;


public class RandomGiphy {
    @JsonProperty("data")
    private DataGiphy data;

    @JsonProperty("meta")
    private MetaGiphy meta;

    /**
     * Returns the data.
     * 
     * <p>
     * "data": { ... }
     * 
     * @return The data.
     */
    public DataGiphy getData() {
	return data;
    }

    /**
     * Sets the data.
     * 
     * @param data
     *            the data
     */
    public void setData(DataGiphy data) {
	this.data = data;
    }

    /**
     * Returns the meta data.
     * 
     * <p>
     * "meta": { ... }
     * 
     * @return the meta data.
     */
    public MetaGiphy getMeta() {
	return meta;
    }

    /**
     * Sets the meta data.
     * 
     * @param meta
     *            the meta data
     */
    public void setMeta(MetaGiphy meta) {
	this.meta = meta;
    }

    @Override
    public String toString() {
        return "RandomGiphy{" +
                "data=" + data +
                ", meta=" + meta +
                '}';
    }
}