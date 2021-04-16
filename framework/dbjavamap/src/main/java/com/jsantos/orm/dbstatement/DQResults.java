package com.jsantos.orm.dbstatement;

import com.jsantos.common.util.ListValues;

/*
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
*/
public class DQResults<T> {

	//@ApiModelProperty("Number of page shown ")
	Integer page;
	//@ApiModelProperty("number of result shown for each page ")
	Integer size;
	//@ApiModelProperty("mumber of total results ")
	Integer total;
	
	//@ApiModelProperty("array of data ")
	 ListValues<T> rawData;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public  ListValues<T> getRawData() {
		return rawData;
	}

	public  void setRawData(ListValues<T> results) {
		this.rawData =  results;
	}
}

