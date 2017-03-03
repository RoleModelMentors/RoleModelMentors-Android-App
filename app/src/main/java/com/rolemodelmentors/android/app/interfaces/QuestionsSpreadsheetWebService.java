package com.rolemodelmentors.android.app.interfaces;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by nikhilparanjape on 3/3/17.
 */

public interface QuestionsSpreadsheetWebService {
    @POST("1FAIpQLSeDt4CtKmyifFU0EN_R7IbLmQLvG1qSrwZQ0jRjZbppnxW8Ew/formResponse")
    @FormUrlEncoded
    Call<Void> completeQuestionnaire(
            @Field("entry.1291274605") String mentorName,
            @Field("entry.270357120") String menteeName,
            @Field("entry.597456694") String date,
            @Field("entry.1252845962") String time,
            @Field("entry.1725619977") String notes
    );
}