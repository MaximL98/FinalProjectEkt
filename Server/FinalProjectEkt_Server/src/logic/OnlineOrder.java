package logic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;

/**
 * Project Name: finalProjectEkt_Client Logic class that contains the details
 * needed to save up for each online order.
 * 
 * @author Maxim Lebedinsky, Daniel Vardimon
 * @version 26/12/2022
 */
public class OnlineOrder extends Order {
	/**
	 * OnlineOrder logic part. private fields that will contain online order's:
	 * type(pickup, delivery), Status(in progress, complete, cancelled) and machine
	 * location
	 */
	public enum Type {
		Pickup, Delivery;
	}

	public enum Status {
		InProgress, Complete, Cancelled;
		
		@Override
		public String toString() {
			if(this.compareTo(InProgress) == 0)
				return "In Progress";
			return super.toString();
		}
	}
<<<<<<< HEAD

	private String machineLocation;
	private LocalDate dateReceived;
	private Type type;
	private Status status;
	private LocalDateTime deliveryTime;

=======
	
	public enum Status{
		InProgress("inProgress"), Complete("complete"), Canceled("cancelled");
		
		private final String status;
		
		Status(String status) {
			this.status = status;
		}
		public String getStatus() {
			return status;
		}
	}
	
	private int machineLocation;
>>>>>>> refs/remotes/origin/master
	/**
	 * OnlineOrder constructor, inherits fields from Order and own field machine
	 * location
	 * 
	 * @param orderID
	 * @param totalAmount
	 * @param attribute
	 * @param machineLocation
	 * @param dateReceived
	 */
<<<<<<< HEAD
	public OnlineOrder(String orderID, Integer totalAmount, String attribute, String machineLocation,
			LocalDate dateReceived,LocalDateTime deliveryTime, Type type, Status status) {
		super(orderID, totalAmount, attribute);
=======
	public OnlineOrder(String orderID, Integer totalAmount, String attribute, int machineLocation) {
		super(orderID, totalAmount, attribute, machineLocation);
>>>>>>> refs/remotes/origin/master
		this.machineLocation = machineLocation;
		this.dateReceived = dateReceived;
		this.deliveryTime = deliveryTime;
		this.type = type;
		this.setStatus(status);
	}

	/**
	 * getting machine location
	 * 
	 * @return machineLocation
	 */
	public int getMachineLocation() {
		return machineLocation;
	}

	/**
	 * setting machine location
	 * 
	 * @param machineLocation
	 */
	public void setMachineLocation(int machineLocation) {
		this.machineLocation = machineLocation;
	}

	/**
	 * getting date received
	 * 
	 * @return dateReceived
	 */
	public LocalDate getDateReceived() {
		return dateReceived;
	}

	/**
	 * setting date received
	 * 
	 * @param dateReceived
	 */
	public void setDateReceived(LocalDate dateReceived) {
		this.dateReceived = dateReceived;
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
	 * @return the deliveryTime
	 */
	public LocalDateTime getDeliveryTime() {
		return deliveryTime;
	}

	/**
	 * @param deliveryTime the deliveryTime to set
	 */
	public void setDeliveryTime(LocalDateTime deliveryTime) {
		this.deliveryTime = deliveryTime;
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
	 * toString method, returns order details
	 */
	@Override
	public String toString() {
		return "OnlineOrder [machineLocation=" + machineLocation + "]";
	}

}
