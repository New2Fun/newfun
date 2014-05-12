package com.example.android.util;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts.Photo;
import android.text.TextUtils;

public class ContactsHelper {

    /**获取库Phon表字段**/
    private static final String[] PHONES_PROJECTION         = new String[] { Phone.DISPLAY_NAME, Phone.NUMBER,
            Photo.PHOTO_ID, Phone.CONTACT_ID               };

    /**联系人显示名称**/
    private static final int      PHONES_DISPLAY_NAME_INDEX = 0;

    /**电话号码**/
    private static final int      PHONES_NUMBER_INDEX       = 1;

    /**头像ID**/
    private static final int      PHONES_PHOTO_ID_INDEX     = 2;

    /**联系人的ID**/
    private static final int      PHONES_CONTACT_ID_INDEX   = 3;

    /**得到手机通讯录联系人信息**/
    public static List<String[]> getPhoneContacts(Context mContext) {
        List<String[]> result = new ArrayList<String[]>();
        ContentResolver resolver = mContext.getContentResolver();

        // 获取手机联系人  
        Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {

                //得到手机号码  
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                //当手机号码为空的或者为空字段 跳过当前循环  
                if (TextUtils.isEmpty(phoneNumber))
                    continue;

                //得到联系人名称  
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);

                //得到联系人ID  
                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);

                //得到联系人头像ID  
                Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
                String[] row = new String[] { contactName, phoneNumber };
                result.add(row);
            }

            phoneCursor.close();
        }

        return result;
    }

    /**得到手机SIM卡联系人人信息**/
    public static List<String[]> getSIMContacts(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();
        // 获取Sims卡联系人  
        Uri uri = Uri.parse("content://icc/adn");
        Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null, null);
        List<String[]> result = new ArrayList<String[]>();
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {

                // 得到手机号码  
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                // 当手机号码为空的或者为空字段 跳过当前循环  
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                // 得到联系人名称  
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);

                String[] row = new String[] { contactName, phoneNumber };
                result.add(row);
            }

            phoneCursor.close();
        }

        return result;
    }

}
