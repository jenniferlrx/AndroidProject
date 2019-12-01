package com.example.finalproject;

/**
 * This class is for Currency object which includes conveert from field, convert to field, and
 * the id in the database.
 */
public class Currency {
    private String cFrom;
    private String cTo;
    private long cId;

    /**
     * THis constructor builds up the Currency with three parameters
     * @param cfrom
     * @param cto
     * @param cId
     */
    public Currency(String cfrom, String cto, long cId){
        this.cFrom=cfrom;
        this.cTo=cto;
        this.cId=cId;
    }

    /**
     * this method sets the currency from the parameter convert from
     * @param cFrom
     */
    public void setcFrom(String cFrom) {
        this.cFrom = cFrom;
    }

    /**
     * this method sets the currency to from the parameter
     * @param cTo
     */
    public void setcTo(String cTo) {
        this.cTo = cTo;
    }

    /**
     * this method sets the currency ID from the parameter
     * @param cId
     */
    public void setcId(long cId) {
        this.cId = cId;
    }

    /**
     * THis method returns a String cfrom
     * @return a string convert from
     */
    public String getcFrom(){
        return cFrom;
    }
    /**
     * THis method returns a String cTo
     * @return a string convert to
     */
    public String getcTo(){
        return cTo;
    }
    /**
     * THis method returns a String cId
     * @return a database ID
     */
    public long getcId() {
        return cId;
    }
}
