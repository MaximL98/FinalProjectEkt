package logic;

public enum OrderType {
	PICKUP, DELIVERY, LOCAL;
	public Integer orderTypeId() {
		if (this.equals(PICKUP))
			return 1;
		if(this.equals(DELIVERY))
			return 2;
		return 3;
	}
//	public static void main(String[] args) {
//		OrderType a,b,c;
//		a =PICKUP;
//		b=DELIVERY;
//		c=LOCAL;
//		System.out.println(a+", "+a.orderTypeId()+"\n"+b+", "+b.orderTypeId()+"\n"+c+", "+c.orderTypeId());
//	}
}
