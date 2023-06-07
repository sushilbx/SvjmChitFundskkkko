/*
package io.realm;


import android.annotation.TargetApi;
import android.os.Build;
import android.util.JsonReader;
import android.util.JsonToken;
import io.realm.ImportFlag;
import io.realm.ProxyUtils;
import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.internal.ColumnInfo;
import io.realm.internal.OsList;
import io.realm.internal.OsObject;
import io.realm.internal.OsObjectSchemaInfo;
import io.realm.internal.OsSchemaInfo;
import io.realm.internal.Property;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.Row;
import io.realm.internal.Table;
import io.realm.internal.android.JsonUtils;
import io.realm.internal.objectstore.OsObjectBuilder;
import io.realm.log.RealmLog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("all")
public class com_converge_converge_dataset_UserinfoRealmProxy extends com.converge.converge.dataset.Userinfo
    implements RealmObjectProxy, com_converge_converge_dataset_UserinfoRealmProxyInterface {

    static final class UserinfoColumnInfo extends ColumnInfo {
        long maxColumnIndexValue;
        long idIndex;
        long user_groupIndex;
        long is_mentorIndex;
        long firstNameIndex;
        long lastNameIndex;
        long mobileNoIndex;
        long emailIndex;
        long intakeIndex;
        long branchIndex;
        long branchNameIndex;
        long paymentStatusIndex;
        long courseIndex;
        long profilePictureIndex;

        UserinfoColumnInfo(OsSchemaInfo schemaInfo) {
            super(13);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("Userinfo");
            this.idIndex = addColumnDetails("id", "id", objectSchemaInfo);
            this.user_groupIndex = addColumnDetails("user_group", "user_group", objectSchemaInfo);
            this.is_mentorIndex = addColumnDetails("is_mentor", "is_mentor", objectSchemaInfo*/

package com.kaas.svjmchitfund.Api;

import android.os.SystemClock;

import com.kaas.svjmchitfund.Constant;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class LogJsonInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        //if (BuildConfig.DEBUG) {
        SystemClock.sleep(700);
        Response response = chain.proceed(request);
        String rawJson = response.body().string();
        Constant.log("TAG", "header ==> " + request.headers());
        if (request.method().compareToIgnoreCase("post") == 0) {
            Constant.log("TAG", "body ==> " + bodyToString(request).replaceAll("&", " & ")
                    .replaceAll("%5B", "[").replaceAll("%5D", "]")
                    .replaceAll("%2F", "/").replaceAll("%22", "\"")
                    .replaceAll("%3A", ":").replaceAll("%20", " ")
                    .replaceAll("%7B", "{").replaceAll("%7D", "}")
                    .replaceAll("%2C", ","));
        }
        Constant.log("TAG1", "call ==> " + request.url());
        Constant.log("TAG", "response Code ==> " + response.code());
        Constant.log("TAG", "response ==> " + rawJson);

        // Re-create the response before returning it because body can be read only once
        return response.newBuilder().body(ResponseBody.create(response.body().contentType(), rawJson)).build();
        /*}else
        {
            Constant.log("TAG","RELEASE");
        }
        return null;*/
    }

    public static String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
