package com.example.finalproject;


public class Currency {
    private String cFrom;
    private String cTo;
    private long cId;

    /**
     * THis constructor builds up the Currency
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
     * this method sets the currency from from the parameter
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
     * @return
     */
    public String getcFrom(){
        return cFrom;
    }
    /**
     * THis method returns a String cTo
     * @return
     */
    public String getcTo(){
        return cTo;
    }
    /**
     * THis method returns a String cId
     * @return
     */
    public long getcId() {
        return cId;
    }
}
