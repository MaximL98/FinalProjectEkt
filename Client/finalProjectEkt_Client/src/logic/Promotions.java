package logic;

import java.sql.Date;

/**
 * Project Name: finalProjectEkt_Client
 * Logic class that contains the details needed to save up for each promotion.
 * @author Maxim Lebedinsky,Nastya chesnov,Raz waiss
 * @version 16/12/2022
 */
public class Promotions {
	/**
	*Promotions logic part.
	*private fields that will contain: products details, discount amount and location of the promotion
	*/
	private String promotionLocation;
	private String promotionName;
	private String promotionDescription;
	private String productId;
	private String discountPercentage;
	private Date startDate;
	private Date endDate;
	/**
	 * Promotions constructor.
	 * @param productsDetails
	 * @param discountAmount
	 * @param promotionLocation
	 */
	public Promotions(String promotionName, String promotionDescription, String productId, String discountPercentage, java.sql.Date startDate, java.sql.Date endDate) {
	    this.promotionName = promotionName;
	    this.promotionDescription = promotionDescription;
	    this.productId = productId;
	    this.discountPercentage = discountPercentage;
	    this.startDate = startDate;
	    this.endDate = endDate;
	}
	/**
	 * setting the location of the promotion
	 * @param promotionLocation
	 */
	public void setPromotionLocation(String promotionLocation) {
		this.promotionLocation = promotionLocation;
	}

	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}

	public String getPromotionDescription() {
		return promotionDescription;
	}

	public void setPromotionDescription(String promotionDescription) {
		this.promotionDescription = promotionDescription;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(String discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "(\"" + promotionName + "\", \"" + promotionDescription + "\")";
		
	}
	//(promotionName, promotionDescription, storeLocation, productID, discountPercentage, startDate, endDate)
	
	
	public static void main(String[] args) {
		Promotions promotion = new Promotions("1","ä","b","c", new Date(0), new Date(1) );
		
		System.out.println(promotion.toString());
		
	}
}
