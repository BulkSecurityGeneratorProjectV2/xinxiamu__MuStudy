package com.didispace.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ScheduledTasks2 {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	

	/**
	 * 每隔5秒执行一次，上一次执行完，隔5秒就执行下一次。
	 */
	 @Scheduled(fixedDelay = 1000 * 6)
	public void reportCurrentTime2() { 
		System.out.println("---c");
		new Thread(){
			public void run() {
				try {
					Thread.sleep(1000 * 10);  
					System.out.println("哦哦");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
	}

}
