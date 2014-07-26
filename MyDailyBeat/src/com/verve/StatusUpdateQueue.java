package com.verve;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

import android.content.Context;


public class StatusUpdateQueue implements Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4632176905088549674L;
	public Queue<StatusUpdate> mainQueue;
	public Queue<StatusUpdate> mirrorQueue;
	private static Context mContext;
	
	public StatusUpdateQueue(Context context) {
		mContext = context;
		this.mainQueue = new LinkedList<StatusUpdate>();
		this.mirrorQueue = new LinkedList<StatusUpdate>();
	}
	
	public void addDataSetToQueue(StatusUpdate ds) {
		mainQueue.add(ds);
		mirrorQueue.add(ds);
	}
	
	// removes the dataset with the associated key: true if removed, false if not found
	public StatusUpdate removeItemWithKey(long keyVal) {
		LinkedList<StatusUpdate> backup = new LinkedList<StatusUpdate>();
		backup.addAll(mainQueue);
		for (StatusUpdate ds : backup) {
			if (ds.UPDATE_ID == keyVal) {
				mainQueue.remove(ds);
				mirrorQueue.remove(ds);
				return ds;
			}
		}
		
		return null;
	}


}
