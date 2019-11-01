package com.example.finalproject;

public class Currency {
    private String cFrom;
    private String cTo;

    public Currency(String cfrom, String cto){
        this.cFrom=cfrom;
        this.cTo=cto;
    }

    public void setcFrom(String cFrom) {
        this.cFrom = cFrom;
    }

    public void setcTo(String cTo) {
        this.cTo = cTo;
    }

    public String getcFrom(){
        return cFrom;
    }

    public String getcTo(){
        return cTo;
    }
}
