package com.example.basket.ui.search;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.example.basket.R;
import com.example.basket.util.VolleyCallback;
import com.example.basket.util.VolleyQueueProvider;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchFragment extends Fragment implements OnMapReadyCallback {
    public static final String TAG = "SearchFragment";
    private double latitude = 0.0;
    private double longitude = 0.0;
    private GoogleMap mMap;
    private Context mContext;

    GoogleApiClient googleApiClient;                                           //LocationManger역할
    FusedLocationProviderClient providerClient;                                //최적의 provider선택역할
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Log.i(TAG, "onLocationResult() : ");
            super.onLocationResult(locationResult);
            Location location = locationResult.getLastLocation();
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.i(TAG, "★★★latitude : " + latitude + ",   longitude : " + longitude);
        }
    };


    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        Log.i(TAG, "context()");
        super.onAttach(context);
        this.mContext = context;

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        String apiKey = getString(R.string.google_api_key);

        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        if (getActivity() != null) {
            Log.i(TAG, "getActivity() : " + getActivity().toString());
        }
        //Places 초기화 여부
        if (!Places.isInitialized()) {
            Places.initialize(getActivity().getApplicationContext(), apiKey);
        }


        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(mContext);      //위치정보객체 생성
        builder.addApi(LocationServices.API);                                         //위치서비스 인증키 넣기
        builder.addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {    //탐색결과여부체크 리스너
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                Toast.makeText(mContext, "위치정보 탐색을 시작합니다.", Toast.LENGTH_SHORT).show();
                //앱에서 서비스(예: 기기의 GPS)를 직접 사용 설정하지않고 필요 수준의 정확도/전력 소비 및 원하는 업데이트 간격을 지정하고 기기는 시스템 설정을 자동으로 적절하게 변경한다.
                //이러한 설정을 LocationRequest 데이터 객체에서 정의
                LocationRequest locationRequest = LocationRequest.create();                     //위치 요청
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);            //정확도
                locationRequest.setInterval(30000);                                             //30초마다
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //퍼미션InspectionActivity에서 퍼미션을 체크하고 진행하지만 한번 더 체크한다
                    return;
                }
                //Looper는 스레드에 대한 메시지 루프를 실행하는 데 사용되는 클래스
                providerClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.i(TAG, "onConnectionSuspended()");
                Toast.makeText(mContext, "위치정보 탐색연결을 잠시 대기합니다.", Toast.LENGTH_SHORT).show();  //연결중단시
            }
        });

        builder.addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {  //연결실패시
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Log.i(TAG, "onConnectionFailed()");
                Toast.makeText(mContext, "위치정보 탐색연결 실패", Toast.LENGTH_SHORT).show();
            }
        });

        googleApiClient = builder.build();                                          //위치정보 관리객체 생성
        googleApiClient.connect();                                                  //위치연결 시도
        providerClient = LocationServices.getFusedLocationProviderClient(mContext); //최적


        //자동완성기능 (Map 상단 SearchBar)
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        //반환 할 장소 데이터의 유형을 지정
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setCountry("KR");

        //응답을 처리하도록 PlaceSelectionListener를 설정하십시오.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.i(TAG, "Place.getName : " + place.getName());
                Log.i(TAG, "place.getLat_Lgt() : " + place.getLatLng());
                Log.i(TAG, "place.getLatLng().latitude : " + place.getLatLng().latitude);
                Log.i(TAG, "place.getLatLng().longitude : " + place.getLatLng().longitude);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude), 16);
                mMap.animateCamera(cameraUpdate);
            }

            @Override
            public void onError(@NotNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return root;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Log.i(TAG, "onMapReady()");

        /********************************************
         *
         *  DB 갔다 와야함
         *
         *  필요정보들 :
         *  sto_code
         *  sto_lat
         *  sto_lng
         *
         *  필요하다면 기타필요정보들 :
         *  영업시간
         *  상세정보 등등
         *
         ********************************************/

        mMap = googleMap;
        String km = "10";
        Map<String, String> requestMap = new HashMap<>();
        if (latitude != 0.0 && longitude != 0.0) {
            requestMap.put("p_lng", Double.toString(latitude));
            requestMap.put("p_lat", Double.toString(longitude));
        } else {
            requestMap.put("p_lng", Double.toString(37.4786733));
            requestMap.put("p_lat", Double.toString(126.8790434));
        }
        requestMap.put("p_km", km);
        VolleyQueueProvider.callbackVolley(new VolleyCallback() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "response : " + response);
                if (response.equals("조회된 매장이 없습니다")) {
                    Toast.makeText(mContext, "반경 " + km + "km 안에 조회된 매장이 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    List<Map<String, Object>> resultList = new Gson().fromJson(response, List.class);
                    if (resultList != null && resultList.size() != 0) {
                        LatLng latLng = null;
                        for (int i = 0; i < resultList.size(); i++) {
                            Map<String, Object> resultMap = resultList.get(i);
                            MarkerOptions markerOptions = new MarkerOptions();
                            if (resultMap.get("LNG") != null && resultMap.get("LAT") != null) {
                                Log.i(TAG, "resultMap.get(\"LNG : " + resultMap.get("LNG").toString());
                                Log.i(TAG, "resultMap.get(\"LAT : " + resultMap.get("LAT").toString());
                                latLng = new LatLng((double) resultMap.get("LNG"), (double) resultMap.get("LAT"));
                                markerOptions.position(latLng);
                            }
                            if (resultMap.get("STO_NAME") != null) {
                                Log.i(TAG, "STO_NAME : " + resultMap.get("STO_NAME").toString());
                                markerOptions.title(resultMap.get("STO_NAME").toString());
                            }
                            if (resultMap.get("STO_ADDR") != null) {
                                Log.i(TAG, "STO_ADDR : " + resultMap.get("STO_ADDR").toString());
                                markerOptions.snippet(resultMap.get("STO_ADDR").toString());
                            }
                            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                @Override
                                public void onInfoWindowClick(Marker arg0) {
                                    if(arg0.getTitle()!=null){
                                        Log.i(TAG, "■■■■■■■■■ arg0.getTitle() : " + arg0.getTitle());
                                        Map<String, String> stoInfoMap = new HashMap<>();
                                        stoInfoMap.put("sto_name", arg0.getTitle());
                                        VolleyQueueProvider.callbackVolley(new VolleyCallback() {
                                            @Override
                                            public void onResponse(String response) {
                                                if(response.length()==0){
                                                    Toast.makeText(mContext, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                                Log.i(TAG, "response : " + response);
                                                Intent intent = new Intent(getActivity(), StoreMapInfoDialog.class);
                                                Log.i(TAG, "■■■■■■■■■■■■■■■■■■■■■■■■■■■ : " + new Gson().toJson(response));
                                                intent.putExtra("storeData", response);
                                                startActivityForResult(intent, 1);
                                            }

                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.i(TAG, "error : " + error.toString());
                                                error.printStackTrace();
                                            }
                                        },"store/sto_info",stoInfoMap);
                                    }
                                }
                            });
                            mMap.addMarker(markerOptions);
                        }
                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i(TAG, "error : " + error.toString());
            }
        }, "store/find_sto", requestMap);
        CameraUpdate cameraUpdate = null;
        if (latitude != 0.0 && longitude != 0.0) {
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16);
        } else {
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(37.4786725, 126.8790373), 16);
        }
        mMap.animateCamera(cameraUpdate);
    }

}