package entityControllers;

import java.util.ArrayList;
import java.util.HashMap;

import logic.Product;
import logic.superProduct;

public class OrderController {
	// order related fields

	private static ArrayList<String> CurrentProductCategory = new ArrayList<>();
	// Map that holds the current cart contents of the user
	private static HashMap<String, Integer> currentUserCart = new HashMap<>();

	private static HashMap<String, superProduct> getProductByID = new HashMap<>();
	private static Integer orderNumber = 0; // for now, change later

	private static HashMap<Integer, ArrayList<String>> userOrders = new HashMap<>();
	// ------------------------------------------------------------------------------//
	private static ArrayList<Product> arrayOfAddedProductsToGridpane = new ArrayList<>();

	private static Double orderTotalPrice = new Double(0.0);
	private static HashMap<Product, Double> cartPrice = new HashMap<>();

	private static Integer orderTotalQuantity;
	private static String orderDateReceived;
	private static String orderDeliveryTime;

	private static String orderType = "";
	private static String pickupPlace = "";

	private static String billingDate = null;

	private static String deliveryAddress = "";

	public static ArrayList<String> getCurrentProductCategory() {
		return CurrentProductCategory;
	}

	public static void setCurrentProductCategory(ArrayList<String> currentProductCategory) {
		CurrentProductCategory = currentProductCategory;
	}

	public static HashMap<String, Integer> getCurrentUserCart() {
		return currentUserCart;
	}

	public static void setCurrentUserCart(HashMap<String, Integer> currentUserCart) {
		OrderController.currentUserCart = currentUserCart;
	}

	public static HashMap<String, superProduct> getGetProductByID() {
		return getProductByID;
	}

	public static void setGetProductByID(HashMap<String, superProduct> getProductByID) {
		OrderController.getProductByID = getProductByID;
	}

	public static HashMap<Integer, ArrayList<String>> getUserOrders() {
		return userOrders;
	}

	public static void setUserOrders(HashMap<Integer, ArrayList<String>> userOrders) {
		OrderController.userOrders = userOrders;
	}

	public static Integer getOrderNumber() {
		return orderNumber;
	}

	public static void setOrderNumber(Integer orderNumber) {
		OrderController.orderNumber = orderNumber;
	}

	public static ArrayList<Product> getArrayOfAddedProductsToGridpane() {
		return arrayOfAddedProductsToGridpane;
	}

	public static void setArrayOfAddedProductsToGridpane(ArrayList<Product> arrayOfAddedProductsToGridpane) {
		OrderController.arrayOfAddedProductsToGridpane = arrayOfAddedProductsToGridpane;
	}

	public static Double getOrderTotalPrice() {
		return orderTotalPrice;
	}

	public static void setOrderTotalPrice(Double orderTotalPrice) {
		OrderController.orderTotalPrice = orderTotalPrice;
	}

	public static HashMap<Product, Double> getCartPrice() {
		return cartPrice;
	}

	public static void setCartPrice(HashMap<Product, Double> cartPrice) {
		OrderController.cartPrice = cartPrice;
	}

	public static Integer getOrderTotalQuantity() {
		return orderTotalQuantity;
	}

	public static void setOrderTotalQuantity(Integer orderTotalQuantity) {
		OrderController.orderTotalQuantity = orderTotalQuantity;
	}

	public static String getOrderDateReceived() {
		return orderDateReceived;
	}

	public static void setOrderDateReceived(String orderDateReceived) {
		OrderController.orderDateReceived = orderDateReceived;
	}

	public static String getOrderDeliveryTime() {
		return orderDeliveryTime;
	}

	public static void setOrderDeliveryTime(String orderDeliveryTime) {
		OrderController.orderDeliveryTime = orderDeliveryTime;
	}

	public static String getOrderType() {
		return orderType;
	}

	public static void setOrderType(String orderType) {
		OrderController.orderType = orderType;
	}

	public static String getPickupPlace() {
		return pickupPlace;
	}

	public static void setPickupPlace(String pickupPlace) {
		OrderController.pickupPlace = pickupPlace;
	}

	public static String getBillingDate() {
		return billingDate;
	}

	public static void setBillingDate(String billingDate) {
		OrderController.billingDate = billingDate;
	}

	public static String getDeliveryAddress() {
		return deliveryAddress;
	}

	public static void setDeliveryAddress(String deliveryAddress) {
		OrderController.deliveryAddress = deliveryAddress;
	}

	public static void resetVars() {
		// TODO Auto-generated method stub
		setCurrentProductCategory(new ArrayList<>());
		// Map that holds the current cart contents of the user
		setCurrentUserCart(new HashMap<>());

		setGetProductByID(new HashMap<>());
		setUserOrders(new HashMap<>());
		// ------------------------------------------------------------------------------//
		setArrayOfAddedProductsToGridpane(new ArrayList<>());

		setOrderTotalPrice(new Double(0.0));
		setCartPrice(new HashMap<>());
		setOrderType("");
		setPickupPlace("");

		setBillingDate(null);
	}
}
