package com.nacid.web;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by georgi.georgiev on 23.09.2015.
 */
public class DeliveryTypeHelper {
    private static Map<Integer, String> DELIVERY_TYPE_ID_TO_NAME = new LinkedHashMap<Integer, String>();
    static {
        DELIVERY_TYPE_ID_TO_NAME.put(0, "На място");
        DELIVERY_TYPE_ID_TO_NAME.put(1, "Международна пратка");
        DELIVERY_TYPE_ID_TO_NAME.put(2, "Вътрешна куриерска пратка");
        DELIVERY_TYPE_ID_TO_NAME.put(3, "По електронна поща");
    }

    public static String getDeliveryTypeName(Integer deliveryTypeId) {
        return DELIVERY_TYPE_ID_TO_NAME.get(deliveryTypeId);
    }

}
