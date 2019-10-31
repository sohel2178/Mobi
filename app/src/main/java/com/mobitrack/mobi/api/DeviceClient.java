package com.mobitrack.mobi.api;

import com.mobitrack.mobi.api.accountModel.AccountGetResponse;
import com.mobitrack.mobi.api.accountModel.AccountPostResponse;
import com.mobitrack.mobi.api.headModel.HeadGetResponse;
import com.mobitrack.mobi.api.headModel.HeadPostResponse;
import com.mobitrack.mobi.api.model.AccountReq;
import com.mobitrack.mobi.api.model.AlartDeleteRequest;
import com.mobitrack.mobi.api.model.AlartRequest;
import com.mobitrack.mobi.api.model.AlartResponse;
import com.mobitrack.mobi.api.model.Fence;
import com.mobitrack.mobi.api.model.FenceR;
import com.mobitrack.mobi.api.model.FenceReply;
import com.mobitrack.mobi.api.model.MonthlyReaponse;
import com.mobitrack.mobi.api.model.MonthlyRequestBody;
import com.mobitrack.mobi.api.model.MyData;
import com.mobitrack.mobi.api.model.ImeiReq;
import com.mobitrack.mobi.api.model.RUser;
import com.mobitrack.mobi.api.model.RequestBody;
import com.mobitrack.mobi.api.transModel.PostTran;
import com.mobitrack.mobi.api.transModel.TranGetResponse;
import com.mobitrack.mobi.api.transModel.TranPostResponse;
import com.mobitrack.mobi.model.Account;
import com.mobitrack.mobi.model.Head;
import com.mobitrack.mobi.model.MonthlyRBody;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DeviceClient {
    //http://localhost:5000/api/locations/getlocationsbyimeidate

    @POST("/api/locations/getlocationsbyimeidate")
    Call<MyData> getLocationData(@Body RequestBody data);

    @POST("/api/locations/monthly")
    Call<MonthlyReaponse> getMonthlyData(@Body MonthlyRequestBody body);

    @POST("/api/pdf")
    Call<ResponseBody> getFile(@Body MonthlyRBody monthlyData);

    @POST("/api/fences/applyfence")
    Call<FenceR> applyFence(@Body Fence data);

    @POST("/api/fences/getfencebyimei")
    Call<FenceR> getMyFence(@Body ImeiReq data);

    @POST("/api/fences/deletefence")
    Call<FenceReply> deleteFence(@Body ImeiReq data);


    @POST("/api/fencealerts/fencealertbyuid")
    Call<AlartResponse> getAllAlart(@Body AlartRequest alartRequest);

    @POST("/api/fencealerts/deletefencealert")
    Call<FenceReply> deleteFenceAlart(@Body AlartDeleteRequest deleteRequest);



    // Account and Transaction Api
    @POST("/api/accounts")
    Call<AccountPostResponse> createAccount(@Body Account account);

    @GET("/api/accounts")
    Call<AccountGetResponse> getAccounts();

    @POST("/api/accounts/get_customer_accounts")
    Call<AccountGetResponse> getCustomerAccounts(@Body AccountReq accountReq);

    @POST("/api/heads/get_customer_heads")
    Call<HeadGetResponse> getCustomerHeads(@Body AccountReq accountReq);

    @POST("/api/heads")
    Call<HeadPostResponse> createHead(@Body Head head);


    @POST("/api/trans")
    Call<TranPostResponse> saveTransaction(@Body PostTran postTransaction);

    @PUT("/api/trans/{id}")
    Call<TranPostResponse> updateTransaction(@Path("id") String id, @Body PostTran postTransaction);

    @DELETE("/api/trans/{id}")
    Call<TranPostResponse> deleteTransaction(@Path("id") String id);

    @POST("/api/trans/get_customer_transaction")
    Call<TranGetResponse> getCustomerTrans(@Body AccountReq accountReq);

}
