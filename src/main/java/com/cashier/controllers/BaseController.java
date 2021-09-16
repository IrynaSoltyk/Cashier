package com.cashier.controllers;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

import org.apache.log4j.Logger;

public abstract class BaseController implements Controller{
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

	public Optional<Integer> getInt(Optional input) throws NumberFormatException{

		return input.orElseThrow(() -> new NumberFormatException());
	}
}
