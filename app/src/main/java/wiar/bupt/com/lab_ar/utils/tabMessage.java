package wiar.bupt.com.lab_ar.utils;

import wiar.bupt.com.lab_ar.R;

/**
 * Created by pengfeng on 2017/5/1.
 */

public class tabMessage {
    public static String get(int menuItemId, boolean isReselection) {
        String message = "Content for ";

        switch (menuItemId) {

            case R.id.tab_scan:
                message += "favorites";
                break;

            case R.id.tab_document:
                message += "friends";
                break;

        }

        if (isReselection) {
            message += " WAS RESELECTED! YAY!";
        }

        return message;
    }
}
