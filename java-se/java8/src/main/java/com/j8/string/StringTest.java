package com.j8.string;

public class StringTest {

	public static void main(String[] args) {
		// �ַ����Ƚ�
		String name = null;
		// û���ж�name�Ƿ�Ϊnull�����쳣
		// try {
		// if (name.equals("����")) {
		//
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// ��������������Ӳ������ж�name�Ƿ�Ϊnull
		if ("����".equals(name)) {
			System.out.println("---name=" + "����");
		} else {
			System.out.println("---name���ǣ�" + "����");
		}

		// ö�ٺ��ַ����Ƚ�
		TaskStatus taskStatus = null;
		TaskStatus taskStatus2 = TaskStatus.All;
		// �����ж�null,�쳣
//		if (taskStatus.equals("All")) {
//
//		}
		// ����������ж�null
		if (("All").equals(taskStatus)) {
			System.out.println("---taskStatus=All");
		} else {
			System.out.println("-----taskStatus!=All");
		}
	}

}
