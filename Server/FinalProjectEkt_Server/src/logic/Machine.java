package logic;

import java.util.ArrayList;
/**
 * Project Name: finalProjectEkt_Server
 * Logic class that contains the details needed to save up for each machine.
 * @author Maxim Lebedinsky
 * @version 16/12/2022
 */
public class Machine {
	/**
	*Machine logic part.
	*private fields that will contain: list of products, stock, threshold level, status and location of the machine
	*/
	private ArrayList<String> products = new ArrayList<>();
	private Integer stock; //if its stock for each product separately, change to ArrayList<Intger>...
	private Integer thresholdLevel;
	/**
	 * field for machine status
	 * True => Done
	 * False => NotDone
	 */
	private boolean status;
	private String location;
	/**
	 * Machine constructor.
	 * @param products
	 * @param stock
	 * @param thresholdLevel
	 * @param location
	 */
	public Machine(ArrayList<String> products, Integer stock, Integer thresholdLevel, String location) {
		super();
		this.products = products;
		this.stock = stock;
		this.thresholdLevel = thresholdLevel;
		this.location = location;
	}
	/**
	 * getting list of products that the machine contain
	 * @return products
	 */
	public ArrayList<String> getProducts() {
		return products;
	}
	/**
	 * setting the products list
	 * @param products
	 */
	public void setProducts(ArrayList<String> products) {
		this.products = products;
	}
	/**
	 * getting the stock of a product
	 * @return stock
	 */
	public Integer getStock() {
		return stock;
	}
	/**
	 * setting the stock of a product
	 * @param stock
	 */
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	/**
	 * getting the Threshold level
	 * @return thresholdLevel
	 */
	public Integer getThresholdLevel() {
		return thresholdLevel;
	}
	/**
	 * setting the threshold level
	 * @param thresholdLevel
	 */
	public void setThresholdLevel(Integer thresholdLevel) {
		this.thresholdLevel = thresholdLevel;
	}
	/**
	 * getting machine location
	 * @return location
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * setting machine location
	 * @param location
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 * checking machine status
	 * @return status (true, false)
	 */
	public boolean isStatus() {
		return status;
	}
	/**
	 * setting machine status (Done, NotDone)
	 * @param status
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}
	/**
	 * toString method, returns machine details
	 */	
	@Override
	public String toString() {
		return "Machine [products=" + products + ", stock=" + stock + ", thresholdLevel=" + thresholdLevel
				+ ", location=" + location + "]";
	}

	
	
	
	
}
