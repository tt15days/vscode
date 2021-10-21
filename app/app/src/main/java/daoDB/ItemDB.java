package daoDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import config.Config_iroiro;
import jsoup.ItemBean;

/**
 * 主にデータベースを操作する為のクラス.
 * 
 * 共通の設定は{@link config.Config_iroiro}に記述する<br>
 * データは{@link jsoup.ItemBean}を利用する<br>
 */
public class ItemDB extends BaseDAO {
	//接続先DB
	private static final String name = "jdbc/app";

	/**
	 * コンストラクタ.
	 * 
	 * ここで忘れずに{@link BaseDAO#init(String)}を呼ぶ
	 */
	public ItemDB() {
		super();
		init(name);
	}

	/**
	 * テーブルitemsから全データを取得しリストで返す.
	 * 
	 * itemsから日付順にソートしデータを取得する<br>
	 * データを取得するSQL文は{@link config.Config_iroiro#strSQLGetItems}を使用する<br>
	 * @return List
	 */
	public List<ItemBean> GetItems() {
		//ItemBeanのリスト作成
		List<ItemBean> list = new ArrayList<>();

		try {
			//DB接続
			this.Connect();
			//SQL文の作成、実行
			Statement st = con.createStatement();
			ResultSet res = st.executeQuery(Config_iroiro.strSQLGetItems);

			//DBから取得したデータをItemBeanを使用してリストに追加していく
			while (res.next()) {
				ItemBean item = new ItemBean();
				item.setItem_id(res.getString(Config_iroiro.strSQLColumns[0]));
				item.setItem_name(res.getString(Config_iroiro.strSQLColumns[1]));
				item.setPic_url(res.getString(Config_iroiro.strSQLColumns[2]));
				item.setPrice(res.getInt(Config_iroiro.strSQLColumns[3]));
				item.setCreate_timeSQL(res.getTimestamp(Config_iroiro.strSQLColumns[4]));
				item.setCate(res.getInt(Config_iroiro.strSQLColumns[5]));

				list.add(item);
			}

			//明示的にStatementは閉じた方がいいらしい
			st.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				//finallyでDB切断
				this.Disconnect();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		//ItemBeanが入ったリストを返す
		return list;
	}

	/**
	 * テーブルitemsから商品データを1件取得する.
	 * 
	 * 引数の商品IDをitemsから検索し商品データを取得する<br>
	 * 次にprice_historyから価格日時データを取得する<br>
	 * データを取得するSQL文は{@link config.Config_iroiro#strSQLGetItem}を使用する<br>
	 * @param item_id データを取得する商品ID
	 * @return {@link jsoup.ItemBean}
	 */
	public ItemBean GetItem(String item_id) {
		ItemBean item = new ItemBean();

		try {
			this.Connect();
			//SQL itemsからitem_idを検索して商品データ取得
			//各カテゴリの上位20データがDBに入る状態なので
			//お気に入り機能追加したらこれがエラー出る気がする
			
			//SQL文の作成、実行
			PreparedStatement st = con.prepareStatement(Config_iroiro.strSQLGetItem[0]);
			st.setString(1, item_id);
			ResultSet res = st.executeQuery();

			//商品データ1つしかないはずなのでループしないけど
			while (res.next()) {
				item.setItem_id(res.getString(Config_iroiro.strSQLColumns[0]));
				item.setItem_name(res.getString(Config_iroiro.strSQLColumns[1]));
				item.setPic_url(res.getString(Config_iroiro.strSQLColumns[2]));
				item.setPrice(res.getInt(Config_iroiro.strSQLColumns[3]));
				item.setCreate_timeSQL(res.getTimestamp(Config_iroiro.strSQLColumns[4]));
				item.setCate(res.getInt(Config_iroiro.strSQLColumns[5]));
			}

			//SQL price_historyから価格データ取得
			//SQL文の作成、実行
			st = con.prepareStatement(Config_iroiro.strSQLGetItem[1]);
			st.setString(1, item_id);
			res = st.executeQuery();

			//ItemBeanのリスト要素に追加していく
			//毎回リストプロパティをitem.getXXXするのはよくないかも
			while (res.next()) {
				item.getPrice_list().add(res.getInt(Config_iroiro.strSQLColumns[3]));
				item.getTime_list().add(res.getTimestamp(Config_iroiro.strSQLColumns[6]).toLocalDateTime());
			}

			//Statement閉じる
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				//finally句でDisconnectする
				this.Disconnect();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return item;
	}

	/**
	 * テーブルprice_historyへ価格日時データを追加する.
	 * 
	 * テーブルprice_historyへデータを追加する<br>
	 * データを追加するSQL文は{@link config.Config_iroiro#strSQLAddItem}を使用する<br>
	 * @param Item {@link jsoup.ItemBean}型の商品データ
	 * @return テーブルへ追加成功したら1、失敗したらそれ以外が返る
	 */
	public int AddItem(ItemBean Item) {
		int res = -1;

		try {
			this.Connect();
			
			//SQL文の一時作成、実行
			PreparedStatement st = con.prepareStatement(Config_iroiro.strSQLAddItem);
			st.setString(1, Item.getItem_id());
			st.setInt(2, Item.getPrice());
			st.setTimestamp(3, Item.getCreate_timeSQL());

			res = st.executeUpdate(); //成功したら1が返ってくる

			//Statement閉じる
			st.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				//finally句で切断する
				this.Disconnect();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return res;
	}

	/**
	 * テーブルitemsへ商品データを書き込む.
	 * 
	 * itemsへ商品データリストを書き込む<br>
	 * データ書き込み用のSQL文は{@link config.Config_iroiro#strSQLUpdateItems}を使用する<br>
	 * 
	 * @param Items 商品データのリスト
	 * @return データがなければ-1、成功したら書き込んだ件数が返る
	 */
	public int UpdateItems(List<ItemBean> Items) {
		if ((Items == null) || Items.size() == 0)
			return -1;
		int res = 0;

		try {
			this.Connect();
			//SQL文の作成
			PreparedStatement st = con.prepareStatement(Config_iroiro.strSQLUpdateItems);

			int i;
			
			//リストを1個ずつ処理する
			for (ItemBean Item : Items) {
				st.setString(1, Item.getItem_id());
				st.setString(2, Item.getItem_name());
				st.setString(3, Item.getPic_url());
				st.setInt(4, Item.getPrice());
				st.setTimestamp(5, Item.getCreate_timeSQL());
				st.setInt(6, Item.getCate());
				st.setInt(7, Item.getPrice());
				st.setTimestamp(8, Item.getCreate_timeSQL());
				//SQL文の実行
				i = st.executeUpdate();
				res = res + i;	//総数カウントアップ
			}

			//Statement閉じる
			st.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				//finally句でDisconnectする
				this.Disconnect();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		//追加した処理数を返す MaxItemと同じはず
		return res;
	}

}
