package logic;

import java.io.Serializable;

//Class that represents a product in a machine, that includes that product and it's stock in the machine.
public class ProductInMachine implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// the product
	private Product product;
	// the machine that contains the product
	private Machine machine;
	// how much of it is there in the machine.
	private int stock;
	// Rotem added: How much TOTAL is allowed? max_stock?
	private int maxStock;
	
	public ProductInMachine(Product product, Machine machine, int stock, int maxStock) {
		this.product = product;
		this.machine = machine;
		this.stock = stock;
		this.maxStock = maxStock;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public Product getProduct() {
		return product;
	}

	public Machine getMachine() {
		return machine;
	}

	public int getMaxStock() {
		return maxStock;
	}

	public void setMaxStock(int maxStock) {
		this.maxStock = maxStock;
	}

	
}
