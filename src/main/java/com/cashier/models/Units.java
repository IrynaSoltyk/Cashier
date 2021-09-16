package com.cashier.models;

public enum Units {
	KG(1, "kg"), 
	LITRE(2, "litre"), 
	PIECE(3, "piece");
	
	private int id;
	private String name;

    Units(int id , String name) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNamwe() {
        return name;
    }
}

