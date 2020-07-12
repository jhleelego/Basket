package com.example.basket.ui.search;

import android.Manifest;
import android.content.Context;
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

import com.example.basket.R;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class SearchFragment extends Fragment implements OnMapReadyCallback {
    public static final String TAG = "SearchFragment";

    GoogleApiClient googleApiClient;                                           //LocationManger역할
    FusedLocationProviderClient providerClient;                                //최적의 provider선택역할
    LocationCallback locationCallback= new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Log.i(TAG, "onLocationResult() : ");
            super.onLocationResult(locationResult);

            Location location= locationResult.getLastLocation();
            double latitude= location.getLatitude();
            double longitude= location.getLongitude();
            Log.i(TAG, "★★★latitude : " + latitude + ",   longitude : " + longitude);
        }
    };

    private GoogleMap mMap;
    private Context mContext;


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

        Log.i(TAG, "clickBtn()");
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

        Log.i(TAG, "onCreateView()");
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        String apiKey = getString(R.string.google_api_key);

        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        if(getActivity()!=null){
            Log.i(TAG, "getActivity() : " + getActivity().toString());
        }
        //Places 초기화 여부
        if (!Places.isInitialized()) {
            Places.initialize(getActivity().getApplicationContext(), apiKey);
        }

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
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                Log.i(TAG, "place.getLat_Lgt() : " + place.getLatLng());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude), 16);
                mMap.animateCamera(cameraUpdate);
            }
            @Override
            public void onError(@NotNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });


       /* //새 지역 정보 클라이언트 인스턴스를 생성
        PlacesClient placesClient = Places.createClient(mContext);
        //장소 ID를 정의
        String placeId = "INSERT_PLACE_ID_HERE";
        //리턴 할 필드를 지정
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
        //장소 ID 및 필드 배열을 전달하여 요청 객체를 생성
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();
        //응답을 처리 할 리스너를 추가
        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            Log.i(TAG, "Place found: " + place.getName());
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                int statusCode = apiException.getStatusCode();
                // Handle error with given status code.
                Log.e(TAG, "Place not found: " + exception.getMessage());
            }
        });*/

        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return root;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap){
        Log.i(TAG, "onMapReady()");

        /********************************************
         *
         *  DB 갔다 와야함
         *
         *  필요정보들 :
         *  sto_code
         *  sto_lat
         *  sto_lng
         *  sto_name
         *
         *  필요하다면 기타필요정보들 :
         *  영업시간
         *  상세정보 등등
         *
         ********************************************/

        mMap = googleMap;

        LatLng SEOUL = new LatLng(37.585445, 126.954459);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("서울");
        markerOptions.snippet("한국의 수도");
        mMap.addMarker(markerOptions);

        LatLng AMUDAENA1 = new LatLng(37.566465, 126.9345357);
        markerOptions = new MarkerOptions();
        markerOptions.position(AMUDAENA1);
        markerOptions.title("아무대나1");
        markerOptions.snippet("한국의 수도");
        mMap.addMarker(markerOptions);

        LatLng AMUDAENA2 = new LatLng(37.43454356, 126.83459);
        markerOptions = new MarkerOptions();
        markerOptions.position(AMUDAENA2);
        markerOptions.title("아무대나2");
        markerOptions.snippet("한국의 수도");
        mMap.addMarker(markerOptions);

        LatLng AMUDAENA3 = new LatLng( 37.585965, 126.949354);
        markerOptions = new MarkerOptions();
        markerOptions.position(AMUDAENA3);
        markerOptions.title("아무대나2");
        markerOptions.snippet("홍제동1");
        mMap.addMarker(markerOptions);

        LatLng AMUDAENA4 = new LatLng(37.586329, 126.948002);
        markerOptions = new MarkerOptions();
        markerOptions.position(AMUDAENA4);
        markerOptions.title("아무대나3");
        markerOptions.snippet("홍제동2");
        mMap.addMarker(markerOptions);

        LatLng AMUDAENA5 = new LatLng(37.584085, 126.948244);
        markerOptions = new MarkerOptions();
        markerOptions.position(AMUDAENA5);
        markerOptions.title("아무대나4");
        markerOptions.snippet("홍제동3");
        mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(37.478720, 126.878654), 16);
        mMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause()");
        super.onPause();
        if(providerClient!=null){
            providerClient.removeLocationUpdates(locationCallback);    //화면 중단시 업데이트 종료
        }
    }
}