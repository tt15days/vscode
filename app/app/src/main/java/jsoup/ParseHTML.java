package jsoup;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import config.Config_iroiro;

/**
 * スクレイピング用にJsoupを用いたHTMLパーサクラス.
 * 
 * <br>{@link org.jsoup.Jsoup}ライブラリを使いHTMLをパースする<br>
 * Jsoupの詳細や使い方は省略
 */
public class ParseHTML {
	
	/**
	 * 指定URLのデータをパースして必要なデータを取得しリストで返す.
	 * 
	 * <br>Jsoupのselectメソッドを使い、必要な商品データを1個ずつ取得し{@link jsoup.ItemBean}へ保存する<br>
	 * {@link jsoup.ItemBean}のListにconfig.Config_iroiro#MaxItem}個数だけ商品データを追加する<br>
	 * 
	 * @param TargetURL 対象となるURL
	 * @return List
	 * @throws InterruptedException スルーする
	 */
	public List<ItemBean> parse(String TargetURL) throws InterruptedException {
			Document document;
			
			List<ItemBean> beans = new ArrayList<>();
			List<String> id_list = new ArrayList<>();
			List<String> name_list = new ArrayList<>();
			List<Integer> price_list = new ArrayList<>();
			List<String> pic_list = new ArrayList<>();
			LocalDateTime t;
			
			try {
				document = Jsoup.connect(TargetURL).get();
				//カテゴリのindexを取得する
				List<String> sl = Arrays.asList(Config_iroiro.CateURL);
				int cateNum = sl.indexOf(TargetURL);
				
				//別々のメソッドを作って呼び出す方がわかりやすいかもしれない
				//その場合は名前Get_XXXとか 引数はselectの中の文字列かElementsを渡す感じ
				//返り値が各リスト
				Elements ids = document.select("ul div.card-product-01 a.box-link");
	            for (Element e : ids) {
	                String s = e.attr("href");
	                s = s.substring(6, s.lastIndexOf("/")); //test master data
	                id_list.add(s);
	            }
	            
	            Elements names = document.select("p.txt-name.js-line-clamp");
	            for (Element e : names) {
	            	String s = e.text();
	            	//ここで追加する前に商品名の文字チェックとかしたい
	            	name_list.add(s);
	            }
	            
				Elements prices = document.select("p.txt-price");
	            for (Element e : prices) {
	            	//sに\1,999(50%off)とかが入る
	                String s = e.text(); 
	                //最初の(の位置を検索する
	                int i = s.indexOf("(");
	                //\記号をスキップして価格文字列だけ切り出す
	                s = s.substring(1, s.length());
	                if (i > 0) s = s.substring(0, i-1);
	                //1,999 -> 1999にする
	                s = s.replaceAll(",","");
	                //int型のリストなので変換する
	                price_list.add(Integer.valueOf(s));
	            }
	            
				Elements pics = document.select("img.image-main");
				for (Element e : pics) { 
	                String s = e.attr("src");
	                if ( ( s.indexOf("_u.jpg") > 0 )  ) {
	                	pic_list.add(s);
	                }
	                else {
	                	pic_list.add(s);
	                }
	            }
	            
				//200件取得されるので20件だけデータを取得する
	            for (int i = 0; i < Config_iroiro.MaxItem; i++) {
	            	t = LocalDateTime.now();
	            	ItemBean item  = new ItemBean();
	            	item.setItem_id(id_list.get(i));
	            	item.setItem_name(name_list.get(i));
	            	item.setPrice( price_list.get(i) );
	            	item.setPic_url( pic_list.get(i) );
	            	item.setCreate_time(t);
	            	item.setCate(cateNum);
	            	
	            	beans.add(item);
	            	Thread.sleep(1); //sleepをいれて各アイテムの取得時刻をずらす これで日時順ソート出来る
	            }
	            
			} catch (IOException e) {
				e.printStackTrace();
			}

			return beans;
		}

}
