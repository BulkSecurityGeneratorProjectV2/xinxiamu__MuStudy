package com.j8.string;

public enum TaskStatus {
	All("ȫ��(�����ڲ�ѯ)"),
	waitDistribution("������");

	private final String value;

	TaskStatus(String value){
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
