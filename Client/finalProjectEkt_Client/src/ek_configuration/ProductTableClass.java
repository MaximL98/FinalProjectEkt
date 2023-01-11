package ek_configuration;

public class ProductTableClass{
	
	private Integer id;
	private Integer stock;
	private Integer maxStock;
	private String name;
	ProductTableClass(Integer id, String name, Integer stock, Integer maxStock){
		this.setId(id);
		this.setName(name);
		this.setStock(stock);
		this.setMaxStock(maxStock);
	}
	
	public String toString() {
		return ""+getId()+", "+getName()+","+getStock()+","+getMaxStock();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Integer getMaxStock() {
		return maxStock;
	}

	public void setMaxStock(Integer maxStock) {
		this.maxStock = maxStock;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}