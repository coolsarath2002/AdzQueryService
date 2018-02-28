package com.challenge.api.ads_service.facts;

public enum Country {
	NONE(0,"NONE"),
	AFGHANISTAN(1,"AFGHANISTAN"),
	ALBANIA(2,"ALBANIA"),
	ALGERIA(3,"ALGERIA"),
	ANDORRA(4,"ANDORRA"),
	ANGOLA(5,"ANGOLA"),
	ANTIGUAANDBARBUDA(6,"ANTIGUA AND BARBUDA"),
	ARGENTINA(7,"ARGENTINA"),
	ARMENIA(8,"ARMENIA"),
	ARUBA(9,"ARUBA"),
	AUSTRALIA(10,"AUSTRALIA"),
	AUSTRIA(11,"AUSTRIA"),
	AZERBAIJAN(12,"AZERBAIJAN"),
	JAMAICA(13,"JAMAICA"),
	JAPAN(14,"JAPAN"),
	JORDAN(15,"JORDAN"),
	KAZAKHSTAN(16,"KAZAKHSTAN"),
	KENYA(17,"KENYA"),
	KIRIBATI(18,"KIRIBATI"),
	KUWAIT(19,"KUWAIT"),
	KYRGYZSTAN(20,"KYRGYZSTAN"),
	LAOS(21,"LAOS"),
	LATVIA(22,"LATVIA"),
	LEBANON(23,"LEBANON"),
	LESOTHO(24,"LESOTHO"),
	LIBERIA(25,"LIBERIA"),
	LIBYA(26,"LIBYA"),
	LIECHTENSTEIN(27,"LIECHTENSTEIN"),
	LITHUANIA(28,"LITHUANIA"),
	LUXEMBOURG(29,"LUXEMBOURG"),
	US(30,"US"),
	CANADA(31,"CANADA");
	
	private int id;
	private String value;

	private Country(int id,String value) {
		this.id=id;
		this.value=value;
		
	}

	public int getId() {
		return id;
	}

	public String getValue() {
		return value;
	}

	public static Country get(int id) {
		for(Country c:Country.values()) {
			if(id == c.getId())
				return c;
		}
		
		return NONE;
	}
	
	

}
