package logic;
/**
 * Project Name: finalProjectEkt_Client
 * Logic class that contains the details needed to save up for each promotion.
 * @author Maxim Lebedinsky
 * @version 16/12/2022
 */
public class Promotions {
	/**
	*Promotions logic part.
	*private fields that will contain: products details, discount amount and location of the promotion
	*/
	private String productsDetails; //maybe needed ArrayList<String> here...
	private Integer discountAmount; //same here, ArrayList<Integer> ...
	private String promotionLocation;
	/**
	 * Promotions constructor.
	 * @param productsDetails
	 * @param discountAmount
	 * @param promotionLocation
	 */
	public Promotions(String productsDetails, Integer discountAmount, String promotionLocation) {
		this.productsDetails = productsDetails;
		this.discountAmount = discountAmount;
		this.promotionLocation = promotionLocation;
	}
	/**
	 * getting the details of the products
	 * @return productsDetails
	 */
	public String getProductsDetails() {
		return productsDetails;
	}
	/**
	 * setting the details of the products
	 * @param productsDetails
	 */
	public void setProductsDetails(String productsDetails) {
		this.productsDetails = productsDetails;
	}
	/**
	 * getting the discount amount
	 * @return discountAmount
	 */
	public Integer getDiscountAmount() {
		return discountAmount;
	}
	/**
	 * setting the discount amount
	 * @param discountAmount
	 */
	public void setDiscountAmount(Integer discountAmount) {
		this.discountAmount = discountAmount;
	}
	/**
	 * getting location of the promotion
	 * @return promotionLocation
	 */
	public String getPromotionLocation() {
		return promotionLocation;
	}
	/**
	 * setting the location of the promotion
	 * @param promotionLocation
	 */
	public void setPromotionLocation(String promotionLocation) {
		this.promotionLocation = promotionLocation;
	}
	/**
	 * toString method, returns promotions details
	 */	
	@Override
	public String toString() {
		return "Promotions [productsDetails=" + productsDetails + ", discountAmount=" + discountAmount
				+ ", promotionLocation=" + promotionLocation + "]";
	}
	
	
	
}
