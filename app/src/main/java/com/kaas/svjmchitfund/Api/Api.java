package com.kaas.svjmchitfund.Api;


import com.kaas.svjmchitfund.Module.AddCoustmerModel;
import com.kaas.svjmchitfund.Module.BannerModel;
import com.kaas.svjmchitfund.Module.BillModel;
import com.kaas.svjmchitfund.Module.BilllistModel;
import com.kaas.svjmchitfund.Module.BulkimageModel;
import com.kaas.svjmchitfund.Module.ChangePasswordModel;
import com.kaas.svjmchitfund.Module.CoustmerindexModel;
import com.kaas.svjmchitfund.Module.CustomerreportModel;
import com.kaas.svjmchitfund.Module.EditCoustmerModel;
import com.kaas.svjmchitfund.Module.EditbillnioModel;
import com.kaas.svjmchitfund.Module.EditdateModel;
import com.kaas.svjmchitfund.Module.GroupModel;
import com.kaas.svjmchitfund.Module.GrouplistModel;
import com.kaas.svjmchitfund.Module.ListCoustmerModel;
import com.kaas.svjmchitfund.Module.LoguserdModel;
import com.kaas.svjmchitfund.Module.MSGStatus;
import com.kaas.svjmchitfund.Module.MonthlyreportModel;
import com.kaas.svjmchitfund.Module.SessionModel;
import com.kaas.svjmchitfund.Module.SignupModel;
import com.kaas.svjmchitfund.Module.TodayreportModel;
import com.kaas.svjmchitfund.Module.WeeklyReportModel;
import com.kaas.svjmchitfund.Module.YestrdayreportModel;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface Api {


    @FormUrlEncoded
    @POST("register")
    Call<SignupModel> register(
            //    @Header("Authorization") String auth,
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("password") String password,
            @Field("email") String email,
            @Field("password_confirmation") String password_confirmation

    );

    @FormUrlEncoded
    @POST("login")
    Call<SessionModel> login(
            @Field("phone") String phone,
            @Field("password") String password
    );


    @FormUrlEncoded
    @POST("group/create")
    Call<GroupModel> group(
            @Header("Authorization") String auth,
            @Field("name") String name,
            @Field("amount") String amount
    );

    @GET("customer/weekly/report")
    Call<WeeklyReportModel> week(
            @Header("Authorization") String auth
    );

    @GET("customer/index")
    Call<CoustmerindexModel> indexCoustmer(
            @Header("Authorization") String auth

    );





    @GET("group/index")
    Call<GrouplistModel> grouplist(
            @Header("Authorization") String auth

    );


    @GET("loggeduser")
    Call<LoguserdModel> loguser(
            @Header("Authorization") String auth

    );



    @GET("customer/today/report")
    Call<TodayreportModel> customerreport(
            @Header("Authorization") String auth

    );





    @GET("customer/montly/report")
    Call<MonthlyreportModel> monthlyreport(
            @Header("Authorization") String auth

    );




    @FormUrlEncoded
    @POST("customer/import")
    Call<BulkimageModel> customerimage(
            @Header("Authorization") String auth,
            @Field("customer_file") String customer_file

            );


    @FormUrlEncoded
    @POST("billing/create")
    Call<BillModel> billing(
            @Header("Authorization") String auth,
            @Field("customers_id") String customers_id,
              @Field("shop_name") String shop_name,
              @Field("gst_no") String gst_no,
              @Field("account_no") String account_no
       /*     @Field("customers_id") String customers_id,
            @Field("name") String name,
            @Field("place") String place,
            @Field("month") String month,
            @Field("previous") String previous,
            @Field("billings_id") String billings_id,
            @Field("total") String total,
            @Field("bill_date_password") String bill_date_password,
            @Field("amount") String amount*/
    );


    @GET("billing/index")
    Call<BilllistModel> billingindex(
            @Header("Authorization") String auth

    );
    @FormUrlEncoded
    @POST("changepassword")
    Call<ChangePasswordModel> changePassword(
            @Header("Authorization") String auth,
            @Field("password") String password,
            @Field("password_confirmation") String password_confirmation
    );

    @GET("customer/lastday/report")
    Call<BilllistModel> customer(
            @Header("Authorization") String auth

    );





    @GET("banner/index")
    Call<BannerModel> banners(
            @Header("Authorization") String auth
    );



    @GET("customer/reports")
    Call<CustomerreportModel> customersreports(
            @Header("Authorization") String auth
    );


    @GET("customer/yesterday/report")
    Call<YestrdayreportModel> yestradyreport(
            @Header("Authorization") String auth
    );











    @Multipart
    @POST("customer/import")
    Call<MSGStatus> uploadDocuments(@Header("Authorization") String token,
                                    @Part ArrayList<MultipartBody.Part> url
                                 );



    @FormUrlEncoded
    @POST("customer/create")
    Call<AddCoustmerModel> addCoustmer(
            @Header("Authorization") String auth,
            @Field("group_id") String group_id,
            @Field("customers_id") String customers_id,
            @Field("name") String name,
            @Field("mobile") String mobile,
            @Field("place") String place,
            @Field("amount") String amount,
            @Field("route") String route


    );

    @FormUrlEncoded
    @POST("customer/update/{id}")
    Call<ListCoustmerModel> editCoustmer(
            @Header("Authorization") String auth,
            @Path("id") int id,
            @Field("group_id") String group_id,
            @Field("customers_id") String customer_id,
            @Field("name") String name,
            @Field("mobile") String mobile,
            @Field("month") String month,
            @Field("place") String place,
            @Field("installment") String installment,
            @Field("route") String route
    );

    @FormUrlEncoded
    @POST("bill/number/update/{id}")
    Call<EditbillnioModel> editbillno(
            @Header("Authorization") String auth,
            @Path("id") int id,
            @Field("billings_id") String billings_id,
            @Field("bill_no_password") String bill_no_password

            );


    @FormUrlEncoded
    @POST("bill/date/update/{id}")
    Call<EditdateModel> editdate(
            @Header("Authorization") String auth,
            @Path("id") int id,
            @Field("created_at") String created_at,
            @Field("bill_date_password") String date


    );






}


