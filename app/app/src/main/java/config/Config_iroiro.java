package config;

/**
 * 各種設定などを纏めておくクラス.
 * <br>なるべくソースコードの値を直接修正しないように、このクラスに集約して宣言しておく<br>
 * スクレイピングする対象のURLやSQL文などもここに書く<br>
 * 直接参照出来る定数として扱いたいのでpublic static finalとする
 */
public class Config_iroiro {

	/**
	 * 各カテゴリの名前.
	 * <br>配列の順番で{@link #CateURL}とリンクしているので順番を弄らないこと
	 */
	public static final String Category[] = {"すべて", "トップス", "アウター", "ワンピース", "ボトムス"};
	
	/**
	 * スクレイピング対象とするURLの配列.
	 * <br>{@link #Category}と配列のindexがリンクしているので順番を弄らないこと<br>
	 */
	public static final String CateURL[] = {
			"https://www.grail.bz/disp/ranking/001001004/",
			"https://www.grail.bz/disp/ranking/001001004001/",
			"https://www.grail.bz/disp/ranking/001001004002/",
			"https://www.grail.bz/disp/ranking/001001004003/",
			"https://www.grail.bz/disp/ranking/001001004004/"
	};
	
	//jspのファイル名
	/**
	 * サーブレットから呼び出すJSPファイル名.
	 * <br>画面遷移の際に呼び出すjspのファイル名はここに書く<br>
	 * 0:一覧表示用jsp<br>
	 * 1:個別表示用jsp
	 */
	public static final String strJSP[] = {"view1.jsp", "view2.jsp"};
	
	/**
	 * 一覧表示で処理するアイテム数.
	 * <br>ここを変更すると一覧表示で取得するデータ数が変わる<br>
	 * 表示が崩れるかもしれないので4の倍数を推奨する
	 */
	public static final int MaxItem = 20;
	
	/**
	 * 日時表示の時に使用するフォーマット.
	 * <br>{@link java.time.format.DateTimeFormatter}で使用するフォーマット用文字列<br>
	 * 表示したい形式を変えたい時はここを変更する<br>
	 * 表示例2021年10月10日 - 13:35
	 */
	public static final String StrFormatDataTime = "yyyy年MM月dd日 - HH:mm";
	
	/**
	 * DB操作で使うSQLのカラム名の配列.
	 * <br>カラム名の綴り間違い等を防ぐためにここに集約する<br>
	 * インデックスで値を取得するので順番の間違いに気を付ける
	 */
	public static final String strSQLColumns[] = {
			"item_id", "item_name", "pic_url", "price",  "create_time",	"category",	"get_time"};

	/**
	 * テーブルitemsからデータを取得する時に使うSQL文.
	 * <br>SELECT文で全データを取得する為の定数<br>
	 * 日時でソートして取得する ASC/DESC<br>
	 */
	public static final String strSQLGetItems = "SELECT * from items ORDER BY create_time ASC;";
	
	/**
	 * item_idをキーにしてデータを取得する為のSQL文.
	 * <br>index0:テーブルitemsから商品データを取得する為のSQL文<br>
	 * index1:テーブルprice_historyから価格日付データを取得する為のSQL文<br>
	 */
	public static final String strSQLGetItem[] = {
			"SELECT * FROM items WHERE item_id = ?;",
			"SELECT price, get_time FROM price_history WHERE item_id = ?;" };
	
	//DB items更新に使うSQL UPSERT
	//INSERT INTOとON CONFLICTとDO UPDATE SETの3行による構文
	/**
	 * テーブルitemsに商品データを書き込む為のSQL文.
	 * <br>テーブルitemsに商品データを書き込む時に使うSQL文<br>
	 * UPSERTによりプライマリーキーが衝突しても上書き更新されるのでエラーが出ない<br>
	 * ON CONFLICT (xxxx) xxxxが衝突の可能性があるカラム名<br>
	 * DO UPDATE SET xxxx xxxxで上書きするカラム名とデータを指定する
	 */
	public static final String strSQLUpdateItems = "INSERT INTO Items "
			+ "( item_id, item_name, pic_url, price, create_time, category ) VALUES( ?, ?, ?, ?, ?, ? )\n"
			+ "ON CONFLICT (item_id)\n"
			+ "DO UPDATE SET price=?, create_time=?;";
	
	//DB price_historyへの追加に使うSQL
	/**
	 * テーブル price_historyに書き込む為のSQL文.
	 * <br>テーブルprice_historyへ価格と日時を書き込む時に使用するSQL文<br>
	 * serial型でカラム名idをプライマリーキーとして管理しているので<br>
	 * INSEERT文からは省略している
	 */
	public static final String strSQLAddItem = "INSERT INTO price_history "
			+ "(item_id, price, get_time) VALUES(?, ?, ?)";

}
