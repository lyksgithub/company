package com.genepoint.lbsshow.model;

import java.util.List;

/**
 * @author agint_bin
 * @version 创建时间：2017年5月22日下午10:42:49
 */

public class Buildings {

	private String buildingCode;

	private List<Floors> floors;
	
	private String buildingCodeAuto;

	public List<Floors> getFloors() {
		return floors;
	}

	public void setFloors(List<Floors> floors) {
		this.floors = floors;
	}

	public String getBuildingCode() {
		return buildingCode;
	}

	public void setBuildingCode(String buildingCode) {
		this.buildingCode = buildingCode;
	}

	public String getBuildingCodeAuto() {
		return buildingCodeAuto;
	}

	public void setBuildingCodeAuto(String buildingCodeAuto) {
		this.buildingCodeAuto = buildingCodeAuto;
	}

	@Override
	public String toString() {
		return "Buildings [buildingCode=" + buildingCode + ", floors=" + floors + ", buildingCodeAuto="
				+ buildingCodeAuto + "]";
	}
	
	
}
