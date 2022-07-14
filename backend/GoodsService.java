package backend;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.SQLException;
import java.util.List;

public class GoodsService {
    public DatabaseManager db;

    public GoodsService(DatabaseManager db) {
        this.db = db;
    }

    public JSONObject get_product_by_name(String name) {
        List<Goods> goods = db.get_products_where("name", name);
        JSONObject goods_json = new JSONObject();
        goods_json.put("group_name", goods.get(0).getGroupName());
        goods_json.put("goods_name", name);
        goods_json.put("amount", goods.get(0).getAmount());
        goods_json.put("price", goods.get(0).getPrice());
        goods_json.put("about", goods.get(0).getAbout());
        goods_json.put("producer", goods.get(0).getProducer());
        goods_json.put("goods_id", goods.get(0).getId());
        return goods_json;
    }

    public JSONObject get_all_products(){
        List<Goods> goods = db.get_products();
        JSONArray goods_array = new JSONArray();
        JSONObject goods_json = new JSONObject();
        int i = 0;
        while(!goods.isEmpty()){
            JSONObject good_json = new JSONObject();
            good_json.put("group_name", goods.get(i).getGroupName());
            good_json.put("goods_name", goods.get(i).getName());
            good_json.put("amount", goods.get(i).getAmount());
            good_json.put("price", goods.get(i).getPrice());
            good_json.put("about", goods.get(i).getAbout());
            good_json.put("producer", goods.get(i).getProducer());
            good_json.put("goods_id", goods.get(i).getId());
            goods.remove(i);
            goods_array.add(good_json);
        }
        goods_json.put("result", goods_array);
        return goods_json;
    }

    public JSONObject create_new_product(JSONObject goods_json) throws SQLException {
        String name = (String) goods_json.get("name");
        String group_name = (String) goods_json.get("group_name");
        int amount = Integer.parseInt(String.valueOf(goods_json.get("amount")));
        double price = Double.parseDouble(String.valueOf(goods_json.get("price")));
        String about = (String) goods_json.get("about");
        String producer = (String) goods_json.get("producer");
        JSONObject goods_id_json = new JSONObject();
        goods_id_json.put("goods_id", db.create_product(name, price, (int) amount, group_name, about, producer));
        return goods_id_json;
    }

    public void update_product(String name, JSONObject goods_json) throws SQLException {
        Goods goods = db.get_products_where("name", name).get(0);
        if (goods_json.containsKey("amount")) {
            long amount = (long) goods_json.get("amount");
            if (goods.getAmount() > amount) {
                db.discard_product_by_name((int) (goods.getAmount() - amount), name);
            } else {
                db.add_product_by_name((int) (amount - goods.getAmount()), name);
            }
        }
        if (goods_json.containsKey("price")) {
            double price = Double.parseDouble(String.valueOf(goods_json.get("price")));
            if (goods.getPrice() != price) {
                db.set_product_price_by_name(price, name);
            }
        }
        if (goods_json.containsKey("group_name")) {
            String group_name = (String) goods_json.get("group_name");
            if (goods.getGroupName() != group_name) {
                db.set_product_group_by_name(group_name, name);
            }
        }
        if (goods_json.containsKey("about")) {
            String about = (String) goods_json.get("about");
            if (goods.getAbout() != about) {
                db.set_product_about_by_name(about, name);
            }
        }
        if (goods_json.containsKey("producer")) {
            String producer = (String) goods_json.get("producer");
            if (goods.getProducer() != producer) {
                db.set_product_producer_by_name(producer, name);
            }
        }
    }

    public void delete_product(String name) throws SQLException {
        db.delete_product_by_name(name);
    }

}
