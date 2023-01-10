package logic;

import java.io.Serializable;
import java.time.*;

/**
 * Project Name: finalProjectEkt_Client Logic class that contains the details
 * needed to save up for each order.
 * 
 * @author Maxim Lebedinsky
 * @version 16/12/2022
 */
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;

	public enum Type {
		Pickup, Delivery;
	}

	public enum Status {
		InProgress("In Progress"), Complete("Complete"), Cancelled("Cancelled"),
		RequestedCancellation("Requested Cancellation");

		String statusString;

		Status(String statusString) {
			this.statusString = statusString;
		}

		@Override
		public String toString() {
			return statusString;
		}
	}
	

	/**
	 * Order logic part. private fields that will contain order entity: ID, total
	 * price, total amount of this order, the machine this order is for, the date
	 * when the order was received, the estimated delivery time for the order.
	 */

	private Integer orderID;
	private Integer totalPrice;
	private Integer totalAmount;
	private Machine machine;
	private LocalDate dateReceived;
	private LocalDateTime deliveryTime;
	private Status status;
	private Type type;
	
	public Order(Integer orderID, Integer totalPrice, Integer totalAmount, Machine machine, LocalDate dateReceived,
			LocalDateTime deliveryTime, Status status, Type type) {
		this.orderID = orderID;
		this.totalPrice = totalPrice;
		this.totalAmount = totalAmount;
		this.machine = machine;
		this.dateReceived = dateReceived;
		this.deliveryTime = deliveryTime;
		this.setStatus(status);
		this.setType(type);
	}
	
	public Integer getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Integer getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Machine getMachine() {
		return machine;
	}

	public LocalDateTime getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(LocalDateTime deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public Integer getOrderID() {
		return orderID;
	}

	public LocalDate getDateReceived() {
		return dateReceived;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * toString method, returns order details
	 */
	@Override
	public String toString() {
		return "Order [orderID=" + orderID + ", totalPrice=" + totalPrice + ", totalAmount=" + totalAmount
				+ ", machine name=" + machine + "dateReceived=" + dateReceived + "deliveryTime="
				+ deliveryTime + "]";
	}

}