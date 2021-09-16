package com.cashier.models;

import java.util.List;

public class RequestEntity {
private List<?> objects;
private int count;

public RequestEntity() {}

public List<?> getObjects() {
	return objects;
}
public int getCount() {
	return count;
}
public void setObjects(List<?> objects) {
	this.objects = objects;
}
public void setCount(int count) {
	this.count = count;
}

}
