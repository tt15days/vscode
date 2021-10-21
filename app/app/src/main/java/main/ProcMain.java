package main;

import java.util.ArrayList;
import java.util.List;

import daoDB.ItemDB;
import jsoup.ItemBean;
import jsoup.ParseHTML;

public class ProcMain {
	private ItemDB db;
	private ParseHTML parser;
	
	public ProcMain() {
		super();
		db = new ItemDB();
	}
	
	public List<ItemBean> parse(String str) {
		List<ItemBean> resList = new ArrayList<>();
		parser = new ParseHTML();
		
		try {
			resList = parser.parse(str);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("データ取得数 : " + resList.size());
		
		return resList;
	}
	
	public int SetDBItems(List<ItemBean> items) {
		//DAOを呼んでDB itemsに書き込み
		int res = db.UpdateItems(items);
		System.out.println("DB items output : " + res);
		
		//DAOを呼んでDB price_historyに書き込み
		for (ItemBean item : items) {
			res = db.AddItem(item); 
		}
		
		return res;
	}
	
	public ItemBean GetItemDetail(String id) {
		return db.GetItem(id);
	}

}
