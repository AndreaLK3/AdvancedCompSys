package com.acertaininventorymanager.client.workloads;

import com.acertaininventorymanager.interfaces.CustomerTransactionManager;

/**
 * 
 * WorkloadConfiguration represents the configuration parameters to be used by
 * Workers class for running the workloads
 * 
 */
public class WorkloadConfiguration {
	
	private int warmUpRuns = 50;
	private int numActualRuns = 100;
	private float percentRarePurchaseInteraction = 10f;
	private float percentFrequentPurchaseInteraction = 65f;
	//15% of the interactions will be region lookups
	private ElementsGenerator bookSetGenerator = null;
	private CustomerTransactionManager ctm;

	public WorkloadConfiguration(CustomerTransactionManager customerTransactionManager) throws Exception {
		// Create a new one so that it is not shared
		bookSetGenerator = new ElementsGenerator();
		ctm = customerTransactionManager;
	}

	

	public float getPercentRareStockManagerInteraction() {
		return percentRarePurchaseInteraction;
	}

	public void setPercentRareStockManagerInteraction(
			float percentRareStockManagerInteraction) {
		this.percentRarePurchaseInteraction = percentRareStockManagerInteraction;
	}

	public float getPercentFrequentStockManagerInteraction() {
		return percentFrequentPurchaseInteraction;
	}

	public void setPercentFrequentStockManagerInteraction(
			float percentFrequentStockManagerInteraction) {
		this.percentFrequentPurchaseInteraction = percentFrequentStockManagerInteraction;
	}

	public int getWarmUpRuns() {
		return warmUpRuns;
	}

	public void setWarmUpRuns(int warmUpRuns) {
		this.warmUpRuns = warmUpRuns;
	}

	public int getNumActualRuns() {
		return numActualRuns;
	}

	public void setNumActualRuns(int numActualRuns) {
		this.numActualRuns = numActualRuns;
	}

	
	public ElementsGenerator getBookSetGenerator() {
		return bookSetGenerator;
	}

	public void setBookSetGenerator(ElementsGenerator bookSetGenerator) {
		this.bookSetGenerator = bookSetGenerator;
	}

	public CustomerTransactionManager getCtm() {
		return ctm;
	}


}