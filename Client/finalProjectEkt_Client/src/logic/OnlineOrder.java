package logic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;

/**
 * Project Name: finalProjectEkt_Client Logic class that contains the details
 * needed to save up for each online order.
 * 
 * @author Maxim Lebedinsky
 * @version 16/12/2022
 */
public class OnlineOrder extends Order {
	/**
	 * OnlineOrder logic part. private fields that will contain online order's:
	 * type(pickup, delivery), Status(in progress, complete, cancelled) and machine
	 * location
	 */
	public enum Type {
		PICKUP("pickup"), DELIVERY("delivery");

		private final String type;

		Type(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}
	}

	public enum Status {
		InProgress("In Progress"), Complete("Complete"), Canceled("Cancelled");

		private final String status;

		Status(String status) {
			this.status = status;
		}

		public String getStatus() {
			return status;
		}
	}

	private String machineLocation;
	private LocalDate dateReceived;
	private Type type;
	private ComboBox<String> statusCombo;
	private LocalDateTime deliveryTime;

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
	public OnlineOrder(String orderID, Integer totalAmount, String attribute, String machineLocation,
			LocalDate dateReceived,LocalDateTime deliveryTime, Type type, Status status) {
		super(orderID, totalAmount, attribute);
		this.machineLocation = machineLocation;
		this.dateReceived = dateReceived;
		this.deliveryTime = deliveryTime;
		this.type = type;
		this.statusCombo = new ComboBox<>(FXCollections.observableArrayList(Status.InProgress.getStatus(), Status.Canceled.getStatus(), Status.Complete.getStatus()));
		this.statusCombo.setValue(status.getStatus());
	}

	/**
	 * getting machine location
	 * 
	 * @return machineLocation
	 */
	public String getMachineLocation() {
		return machineLocation;
	}

	/**
	 * setting machine location
	 * 
	 * @param machineLocation
	 */
	public void setMachineLocation(String machineLocation) {
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
	 * @return the statusCombo
	 */
	public ComboBox<String> getStatusCombo() {
		return statusCombo;
	}

	/**
	 * toString method, returns order details
	 */
	@Override
	public String toString() {
		return "OnlineOrder [machineLocation=" + machineLocation + "]";
	}

}
