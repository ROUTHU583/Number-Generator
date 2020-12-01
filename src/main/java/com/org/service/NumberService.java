package com.org.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.org.dto.Number;
@Service
public class NumberService {
	private List<Number> numberList=new ArrayList<>();
	private UUID id;
	private String status=new String("ERROR");
	
	public UUID addNumber(Number number) {
		id=UUID.randomUUID();
		number.setId(id);
		number.setStatus("IN_PROGRESS");
		numberList.add(number);	
		Runnable r = new Runnable() {
	         public void run() {
	        	 generateResult(number);
	         }
	     };
	     new Thread(r).start();
		
		return id;
	}
	
	public String getStatus(UUID id) {
		if(numberList.isEmpty())
			return "ERROR";
		else {
			for(Number number:numberList) {
				if(number.getId().equals(id)) {
					status=number.getStatus();
				}
			}
			
		}
		return status;
	}
	
	public List<String> get_numlist(UUID id) {
		List<String> numList=new ArrayList<String>();
		for(Number number:numberList) {
			if(number.getId().equals(id)) {
				numList.add(number.getResult().toString().replace("[","").replace("]",""));
			}
				
		}
		return numList;				
	}
	
	
	public UUID addNumberList(List<Number> numList) {
		id=UUID.randomUUID();
		for(Number number:numList) {
		number.setId(id);
		numberList.add(number);
		Runnable r=new Runnable() {
			public void run() {
				generateResult(number);
			}
		};
		new Thread(r).start();
		}
		return id;
	}
	
	public synchronized void generateResult(Number number) {
		List<Integer> numArray=new ArrayList<>();
		Random rand=new Random();
		try {
		for(int i=number.getGoal();i>=0;i=i-number.getStep()) {
			Thread.sleep(rand.nextInt(3000));
			numArray.add(i);
		}
		number.setResult(numArray);
		number.setStatus("SUCCESS");
		}
		catch(Exception ex) {
			number.setStatus("ERROR");
		}
		
	}

}

