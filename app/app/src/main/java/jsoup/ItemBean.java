package jsoup;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JavaBeansモデルの商品データを管理するクラス.
 * 
 * メンバ変数はデータベースに用いるカラム名とする<br>
 * 商品名item_nameにSQLで扱うシングルクォート'が含まれている場合、エラーになるので置換するメソッドを用意する<br>
 * price_lisは{@link Integer}の商品価格が入ったリスト<br>
 * time_listは{@link java.time.LocalDateTime}の価格データを取得した時の日時が入ったリスト<br>
 * price_listとtime_listのインデックスは同期しているものとする
 */
public class ItemBean implements Serializable {
	
	//データベースの設計に合わせて64文字とする postgreSQL@VARCHAR(64)
	private int MAXitemName = 64;

	private String item_id;
	private String item_name;
	private String pic_url;
	private int price;
	private LocalDateTime create_time;
	private int cate;
	
	private ArrayList<Integer> price_list;
	private ArrayList<LocalDateTime> time_list;
	
	public ItemBean() {
		super();
		price_list = new ArrayList<>();
		time_list = new ArrayList<>();
	}

	public String getItem_id() {
		return item_id;
	}

	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getPic_url() {
		return pic_url;
	}

	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}

	public LocalDateTime getCreate_time() {
		return create_time;
	}
	
	public Timestamp getCreate_timeSQL() {
		return Timestamp.valueOf(create_time);
	}

	public void setCreate_time(LocalDateTime create_time) {
		this.create_time = create_time;
	}
	
	public void setCreate_timeSQL(Timestamp create_time) {
		this.create_time = create_time.toLocalDateTime();
	}

	public int getCate() {
		return cate;
	}

	public void setCate(int cate) {
		this.cate = cate;
	}

	public List<Integer> getPrice_list() {
		return price_list;
	}

	public void setPrice_list(ArrayList<Integer> price_list) {
		this.price_list = price_list;
	}

	public List<LocalDateTime> getTime_list() {
		return time_list;
	}

	public void setTime_list(ArrayList<LocalDateTime> time_list) {
		this.time_list = time_list;
	}
	
	//3桁区切りの値段表記として末尾に円を付加する
	public String getPriceYen(int price) {
		return String.format("%,d", price) + "円";
	}
	
	//シングルクォートを置換しMAXitemNameを超える文字を切り捨てる
	public String getItem_NameProc() {
		String s = this.item_name.replaceAll("\'", "\'\'");
		if (s.length() > MAXitemName) {
			s = s.substring(0, MAXitemName-1);
		}
		return s;
	}

}
