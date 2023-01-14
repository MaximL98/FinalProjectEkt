package database;

public enum DatabaseOperation {
	// update this accordingly
	SELECT_PROMOTIONS_TO_ACTIVE, UPDATE_PROMOTION_STATUS,
	INSERT, UPDATE, DELETE, ALTER, SELECT_PROMOTION, DISPLAY, USER_LOGIN, 
	FETCH_PRODUCTS_BY_CATEGORY, FETCH_ORDERS, UPDATE_ORDERS,ADD_PROMOTION, INSERT_PROMOTION_NAMES
	, SELECT, GENERIC_SELECT, FETCH_MACHINES_BY_LOCATION, FETCH_PRODUCTS_IN_MACHINE, UPDATE_PRODUCTS_IN_MACHINE, REMOVE; // my actual select -> I changed the old one to "select promotion" because it wasn't OK
}
