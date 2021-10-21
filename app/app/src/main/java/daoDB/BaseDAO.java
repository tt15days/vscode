package daoDB;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * PostgreSQLを扱う為の基礎クラス.
 * 
 * <br>コンストラクタ後にはinit()を必ず呼ぶ事<br>
 * 継承先でDBの操作を行うメソッド等を実装する<br>
 * 操作後はDisconnectしてDataSource以外の各リソースを閉じる<br>
 */
public class BaseDAO {
	private static DataSource ds;
	
	/**
	 * 継承先から操作する為protectedとする
	 */
	protected Connection con;

	/**
	 * コンストラクタ.
	 * 
	 * 初期化としてメンバをnullにする
	 */
	public BaseDAO() {
		ds = null;
		con = null;
	}

	/**
	 * データベース操作の為の初期化関数.
	 * 
	 * JNDIによる接続方法の為の初期化を行う<br>
	 * (web.xml及びcontext.xmlに設定は保存されている)<br>
	 * Class.forNameでcontext.xmlのdriverClassNameと同じドライバ名を設定する<br>
	 * lookup先の環境変数はcontext.xmlのResource.Name<br>
	 * java:/comp/env/ + リソースの名前<br>
	 * @param dbname 接続するデータベースのリソース名
	 */
	public void init(String dbname) {
		try {
			Class.forName("org.postgresql.Driver");
			if ((ds == null) && (dbname != "") ) {
				InitialContext initCon = new InitialContext();
				ds = (DataSource) initCon.lookup("java:/comp/env/" + dbname);
			}
		} catch (ClassNotFoundException | NamingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * データベースへ接続する.
	 * 
	 * データベースへ接続しコネクションを取得する<br>
	 * @return データベースとの接続に成功すればtrue、失敗したらfalse
	 */
	public Boolean Connect() {
		try {
			con = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (con == null) return false;
		else return true;
	}
	
	/**
	 * データベースから切断する.
	 * 
	 * 必ずデータベースを操作し終わった後に呼び出す事<br>
	 * @throws SQLException 例外は継承先で処理する
	 */
	public void Disconnect() throws SQLException {
		if (con != null) con.close();
	}

}
