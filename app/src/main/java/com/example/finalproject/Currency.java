package com.example.finalproject;

public class Currency {
    private String cFrom;
    private String cTo;
    private long cId;

    public Currency(String cfrom, String cto, long cId){
        this.cFrom=cfrom;
        this.cTo=cto;
        this.cId=cId;
    }

    public void setcFrom(String cFrom) {
        this.cFrom = cFrom;
    }

    public void setcTo(String cTo) {
        this.cTo = cTo;
    }

    public void setcId(long cId) {
        this.cId = cId;
    }

    public String getcFrom(){
        return cFrom;
    }

    public String getcTo(){
        return cTo;
    }

    public long getcId() {
        return cId;
    }
}
