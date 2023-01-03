package logic;

import java.io.Serializable;

/**
 * Project Name: finalProjectEkt_Server
 * Logic class that contains the details needed to save up for each order.
 * @author Maxim Lebedinsky
 * @version 16/12/2022
 */
public class Order implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	*Order logic part.
	*private fields that will contain order's: id, total amount (of products), attribute
	*/
	private String orderID;
	private Integer totalAmount;
	private String attribute;
	private int machineID;
	/**
	 * Order constructor.
	 * @param orderID
	 * @param totalAmount
	 * @param attribute
	 */
	public Order(String orderID, Integer totalAmount, String attribute, int machineID) {
		super();
		this.orderID = orderID;
		this.totalAmount = totalAmount;
		this.attribute = attribute;
		setMachineID(machineID);
	}
	/**
	 * getting order id
	 * @return orderID
	 */
	public String getOrderID() {
		return orderID;
	}
	/**
	 * setting order id
	 * @param orderID
	 */
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	/**
	 * getting order total amount
	 * @return totalAmount
	 */
	public Integer getTotalAmount() {
		return totalAmount;
	}
	/**
	 * setting order total amount
	 * @param totalAmount
	 */
	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}
	/**
	 * getting order attribute
	 * @return attribute
	 */
	public String getAttribute() {
		return attribute;
	}
	/**
	 * setting order attribute
	 * @param attribute
	 */
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	/**
	 * toString method, returns order details
	 */	
	@Override
	public String toString() {
		return "Order [orderID=" + orderID + ", totalAmount=" + totalAmount + ", attribute=" + attribute + "]";
	}
	public int getMachineID() {
		return machineID;
	}
	public void setMachineID(int machineID) {
		this.machineID = machineID;
	}
	
	
	
}
