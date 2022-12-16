package logic;
/**
 * Project Name: finalProjectEkt_Client
 * Logic class that contains the details needed to save up for each online order.
 * @author Maxim Lebedinsky
 * @version 16/12/2022
 */
public class OnlineOrder extends Order{
	/**
	*OnlineOrder logic part.
	*private fields that will contain online order's: type(pickup, delivery), Status(in progress, complete, cancelled) and machine location
	*/
	public enum Type{
		PICKUP("pickup"), DELIVERY("delivery");
		private final String type;
		
		Type(String type) {
			this.type = type;
		}
		
		public String getType() {
			return type;
		}
	}
	
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
	
	private String machineLocation;
	/**
	 * OnlineOrder constructor, inherits fields from Order and own field machine location
	 * @param orderID
	 * @param totalAmount
	 * @param attribute
	 * @param machineLocation
	 */
	public OnlineOrder(String orderID, Integer totalAmount, String attribute, String machineLocation) {
		super(orderID, totalAmount, attribute);
		this.machineLocation = machineLocation;
	}
	/**
	 * getting machine location
	 * @return machineLocation
	 */
	public String getMachineLocation() {
		return machineLocation;
	}
	/**
	 * setting machine location
	 * @param machineLocation
	 */
	public void setMachineLocation(String machineLocation) {
		this.machineLocation = machineLocation;
	}
	/**
	 * toString method, returns customer details
	 */
	@Override
	public String toString() {
		return "OnlineOrder [machineLocation=" + machineLocation + "]";
	}
	
	

}
