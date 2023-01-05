package database;

public enum DatabaseOperation {
	// update this accordingly
	INSERT, UPDATE, DELETE, ALTER, SELECT_PROMOTION, DISPLAY, USER_LOGIN, FETCH_PRODUCTS_BY_CATEGORY, FETCH_ONLINE_ORDERS, UPDATE_ONLINE_ORDERS,ADD_PROMOTION, INSERT_PROMOTION_NAMES
	, SELECT; // my actual select -> I changed the old one to "select promotion" because it wasn't OK
}
