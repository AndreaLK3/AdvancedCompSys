package com.acertaininventorymanager.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.acertaininventorymanager.business.Customer;
import com.acertaininventorymanager.business.CustomerTransactionsHandler;
import com.acertaininventorymanager.interfaces.CustomerTransactionManager;
import com.acertaininventorymanager.utils.EmptyRegionException;
import com.acertaininventorymanager.utils.InventoryManagerException;
import com.acertaininventorymanager.utils.NonPositiveIntegerException;
import com.acertaininventorymanager.business.ItemPurchase;
import com.acertaininventorymanager.business.RegionTotal;

public class CTMbasicTests {
	
	public final static int NUM_OF_IDM=5;
	public final static int NUM_OF_CUSTOMERS = 20;
	public final static int ITEMS_PER_CUSTOMER = 10;
	public final static Set<Integer> REGIONS = new HashSet<Integer>(Arrays.asList(1, 2, 3));
	public final static int RANDOMINT_BOUND = 1000;
	
	Random randGen = new Random();
	
	private CustomerTransactionManager ctm;
	Set<Customer> customers;
	private boolean localTest = true;


	/**Initialization before every test: 
	 * we create an instance of the CustomerTransactionManager,
	 * we create a random set of customers (n: we have a specified, limited number of regions),
	 * and we execute a random set of purchases. */
	@Before
	public void setUp() throws Exception {
		customers = createSetOfCustomers();
		ctm = new CustomerTransactionsHandler(NUM_OF_IDM, customers);
		Set<ItemPurchase> purchases = createSetOfItemPurchases(customers);
		ctm.processOrders(purchases);
		
	}

	/**Helper function: creates a set of customers, with cID in [0,999] and 
	 * belonging to one region chosen at random.*/
	private Set<Customer> createSetOfCustomers(){
		Set<Customer> setOfCustomers = new HashSet<>();
		
		for (int i=1; i<=NUM_OF_CUSTOMERS; i++){
			int cId = randGen.nextInt(1000)+1;
			int cReg = randGen.nextInt(REGIONS.size())+1;
			Customer c = new Customer(cId, cReg);
			setOfCustomers.add(c);
		}
		return setOfCustomers;
	}
	
	/**Helper function: given a set of customers, creates a set of item purchases.
	 * The customerID belongs to one of the customers, while the purchase data
	 * (orderID, itemID, quantity, unit price) are chosen at random.*/
	private Set<ItemPurchase> createSetOfItemPurchases(Set<Customer> customers){
		Set<ItemPurchase> setOfPurchases = new HashSet<>();
		
		for (Customer c : customers){
			for (int i=1; i<=ITEMS_PER_CUSTOMER; i++){
				int orderID = randGen.nextInt(RANDOMINT_BOUND)+1;
				int itemID = randGen.nextInt(RANDOMINT_BOUND)+1;
				int quantity = randGen.nextInt(RANDOMINT_BOUND)+1;
				int price = randGen.nextInt(RANDOMINT_BOUND)+1;
				
				setOfPurchases.add(new ItemPurchase(orderID, c.getCustomerId(), itemID, quantity, price));
			}
		}
		return setOfPurchases;
	}
	
	

	/**This is the main test that checks the core functionality of CustomerTransactionManager.
	 * We pick a customer at random, and we create a purchase of nOfUnits * unitPrice.+
	 * After the purchase, the totalValueBought for the region of the customer
	 * must have increased of nOfUnits * unitPrice.**/
	@Test
	public void testProcessOrders() {
		List<RegionTotal> oldRegionTotals = new ArrayList<>();
		try {
			oldRegionTotals = ctm.getTotalsForRegions(REGIONS);
		} catch (InventoryManagerException e) {
			e.printStackTrace();
			fail();
		}
		
		Customer aCustomer = customers.stream().findAny().get();
		int cRegID = aCustomer.getRegionId();
		ItemPurchase purchase1 = createRandomItemPurchase(aCustomer);
		int pricePerUnit = purchase1.getUnitPrice();
		int numberOfUnits = purchase1.getQuantity();
		Set<ItemPurchase> itPurchases = new HashSet<>(Arrays.asList(purchase1));
		
		try {
			ctm.processOrders(itPurchases);
			List<RegionTotal> newRegionTotals = ctm.getTotalsForRegions(REGIONS);
			RegionTotal oldTotal = getRegionTotal(cRegID, oldRegionTotals);
			RegionTotal newTotal = getRegionTotal(cRegID, newRegionTotals);
			
			assert(newTotal.getTotalValueBought() - oldTotal.getTotalValueBought() == pricePerUnit * numberOfUnits);
			
		} catch (InventoryManagerException e) {
			e.printStackTrace();
			fail();
		}
		
	}
	
	/**Helper function.*/
	private ItemPurchase createRandomItemPurchase(Customer aCustomer){
		int cID = aCustomer.getCustomerId();
		int cReg = aCustomer.getRegionId();
		int orderID = randGen.nextInt(RANDOMINT_BOUND)+1, itemID = randGen.nextInt(RANDOMINT_BOUND)+1;
		int quantity =  randGen.nextInt(RANDOMINT_BOUND)+1, unitPrice = randGen.nextInt(RANDOMINT_BOUND)+1;
		ItemPurchase aPurchase = new ItemPurchase(orderID, cID, itemID, quantity, unitPrice);
		return aPurchase;
	}
	
	/**Helper function.*/
	private RegionTotal getRegionTotal(Integer regID, List<RegionTotal> regionTotals){
		RegionTotal result = null;
		for (RegionTotal regTot : regionTotals){
			if (regTot.getRegionId()==regID)
				result = regTot;
		}
		return result;
	}
	
	
	

}
