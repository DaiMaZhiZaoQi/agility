package com.hunt.model.dto;

/**
 * @Author: ouyangan
 * @Date : 2016/10/12
 * 页面信息
 */
public class PageInfo {
    private int total;
    private Object rows;
    
    private Object otherRows;

    public PageInfo(int total, Object rows) {
        this.total = total;
        this.rows = rows;
    } 

    @Override
    public String toString() {
        return "PageInfo{" +
                "total=" + total + 
                ", rows=" + rows +
                '}';
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Object getRows() {
        return rows;
    }

    public void setRows(Object rows) {
        this.rows = rows;
    }

	public Object getOtherRows() {
		return otherRows;
	}

	public void setOtherRows(Object otherRows) {
		this.otherRows = otherRows;
	}

    
    
    
    
    
}
