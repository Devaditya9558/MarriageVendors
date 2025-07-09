

package com.example.marriagevendors.RetrofitClient;

  import retrofit2.Retrofit;
  import retrofit2.converter.gson.GsonConverterFactory;

    public class RetrofitClient {
        private static final String BASE_URL = "http://192.168.1.6:4000/api/v1/"; // Change as needed
        private static Retrofit retrofit = null;

        public static Retrofit getRetrofitInstance() {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit; // Fixed: Changed from return null to return retrofit
        }
    }


