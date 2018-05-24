package repositories;

public final class SQLRepository {

	private SQLRepository() {}
	
	public static String getQuery(String query, Object... values)
    {
        if(values == null)
            return query;
        for(Object value : values)
        {
            query = query.replaceFirst("####",String.valueOf(value));
        }
        return query;
    }
	
	public static String max_incrementId                   		= "select max(increment_id) as increment_id from ((select max(increment_id) as increment_id from inventory.orders where increment_id like '####%') UNION (select max(order_id) as increment_id from inventory.newOrdersFlow where increment_id like '####%')) as max_increment;";
	public static String get_orderId_by_incrementId       	 	= "(select order_id from inventory.orders where increment_id = '####') union (select order_id from inventory.newOrdersFlow where increment_id = '####');";
	public static String get_incrementId_productId_uw_item_id 	= "select increment_id, product_id, uw_item_id from inventory.uw_orders where increment_id = '####' and parent_uw = 0;";
}
