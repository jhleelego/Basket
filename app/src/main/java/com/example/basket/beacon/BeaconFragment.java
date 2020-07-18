package com.example.basket.beacon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.example.basket.util.VolleyCallback;
import com.example.basket.util.VolleyQueueProvider;
import com.example.basket.vo.MemberDTO;
import com.google.gson.Gson;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeaconFragment extends Fragment implements BeaconConsumer {
    protected static final String TAG = "BeaconFragment";
    private String BASKET_BEACON_UUID = "e2c56db5-dffb-48d2-b060-d0f5a71096e0";
    private BeaconManager beaconManager;
    Context applicationContext = null;
    public static List<Map<String, Object>> storeBeaconList = null;
    String last_beacon_UUID = "";
    String last_beacon_major = "";
    String last_beacon_minor = "";
    String now_beacon_UUID = "";
    String now_beacon_major = "";
    String now_beacon_minor = "";
    public int storeOutCount = 0;
    Activity mActivity = null;
    Context mContext = null;

    public static Fragment getInstance() {
        return LazyHolder.instance;
    }

    private static class LazyHolder {
        private static final BeaconFragment instance = new BeaconFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        mActivity = getActivity();
        mContext = getContext();
        Log.i(TAG, "BeaconFragment onAttach()");
        super.onAttach(context);
        applicationContext = getApplicationContext();
        getStoreBeaconInfo();
        /*if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
        }*/
        //이 클래스의 싱글 톤 인스턴스에 대한 접근 자입니다. 컨텍스트가 제공되어야하지만 비 활동 또는
        //비 서비스 클래스에서 사용해야하는 경우 Android 애플리케이션 클래스의 다른 싱글 톤
        //또는 서브 클래스에 연결할 수 있습니다.
        beaconManager = BeaconManager.getInstanceForApplication(context);
        //getBeaconParsers() = 활성 비콘 파서 목록을 가져옵니다.
        //거기에 새로운 비콘파서 형식을 만들어서 .add() 합니다
        //setBeaconLayout(String) = BLE 알림 내에서 0으로 색인화 된 오프셋을 바이트로 지정하는 문자열을 기반으로 비콘 필드 구문 분석 알고리즘을 정의합니다.
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        //Android Activity또는 Service에 바인딩 합니다 BeaconService.
        beaconManager.bind(this);
    }

    private void getStoreBeaconInfo() {
        Map<String, String> requestMap = new HashMap<>();
        VolleyQueueProvider.callbackVolley(new VolleyCallback() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "response : " + response);
                storeBeaconList = new Gson().fromJson(response, List.class);
                for (Map<String, Object> row : storeBeaconList) {
                    row.put("STO_MAJOR", Integer.valueOf((int) ((double) row.get("STO_MAJOR"))));
                    row.put("STO_MINOR", Integer.valueOf((int) ((double) row.get("STO_MINOR"))));
                    row.put("STO_CODE", (int) ((double) row.get("STO_CODE")));
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "error : " + error.toString());
                error.printStackTrace();
            }
        }, "store/sto_beacon", requestMap);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Android Activity또는 Service에 바인딩 을 해제합니다 BeaconService.
        beaconManager.unbind(this);
    }


    @Override
    //비콘 서비스가 실행 중이고 BeaconManager를 통해 명령을 수락 할 준비가되면 호출됩니다.
    public void onBeaconServiceConnect() {
        //모든 범위 알리미를 제거한다.
        beaconManager.removeAllRangeNotifiers();
        //BeaconService비콘 영역을 보거나 멈출 때 마다 호출해야하는 클래스를 지정합니다 .
        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            //하나 이상의 비콘 Region이 표시 될 때 호출됩니다 .
            public void didEnterRegion(Region region) {
                Log.i(TAG, ":::::최소하나의 비콘 발견하였음:::::");
            }

            @Override
            //비콘 Region이 보이지 않을 때 호출됩니다 .
            public void didExitRegion(Region region) {
                Log.i(TAG, ":::::더이상 비콘을 찾을 수 없음:::::");
            }

            @Override
            //하나 이상의 비콘 Region이 표시 될 때 MonitorNotifier.INSIDE 상태 값으로 호출됩니다 .
            public void didDetermineStateForRegion(int state, Region region) {
                if (state == 0) {
                    Log.i(TAG, ":::::비콘이 보이는 상태이다. state : " + state + ":::::");
                } else {
                    Log.i(TAG, ":::::비콘이 보이지 않는 상태이다. state : " + state + ":::::");
                }
            }
        });
        //범위한정 알리미를 추가한다
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            //눈에 보이는 비콘에 대한 mDistance(major 또는 minor와의 거리를 뜻하는)의 추정치를 제공하기 위해 초당 한 번 호출
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                List<Beacon> list = (List<Beacon>) beacons;
                if (beacons.size() > 0) {
                    //디비의 매장의 블루투스관련 컬럼들을 비교하여야할 곳
                    //디비 매장블루투스관련 컬럼 들어갈 Array
                    /*String[] db_beaconInfo = {"e2c56db5-dffb-48d2-b060-d0f5a71096e0", "40001", "45225"};*/
                    //디비와 매칭되는 beacon에대한 info만 뽑아낸다.
                    //SELECT STO_NAME, STO_UUID, STO_MAJOR, STO_MINOR, STO_CODE FROM STORE ORDER BY STO_CODE
                    Log.i(TAG, "■THE SIGNAL BEACON INFO");
                    Log.i(TAG, "■■The FIRST BEACON I SEE IS ABOUT " + beacons.iterator().next().getDistance() + " meters away.");
                    Log.i(TAG, "■■■[UUID : " + beacons.iterator().next().getId1().toString() + "] : [MAJOR : " + beacons.iterator().next().getId2().toString()+ "] : [MINOR : "+ beacons.iterator().next().getId3().toString() + "]");
                    now_beacon_major = beacons.iterator().next().getId2().toString();
                    now_beacon_minor = beacons.iterator().next().getId3().toString();
                    now_beacon_UUID = beacons.iterator().next().getId1().toString();
                    Log.i(TAG, "■■■■now_beacon_major : " + now_beacon_major);
                    Log.i(TAG, "■■■■■BASKET_BEACON : " + BASKET_BEACON_UUID);
                    if (now_beacon_UUID.equals(now_beacon_major) && now_beacon_major.equals(last_beacon_major) && now_beacon_minor.equals(now_beacon_major)) {
                        Log.i(TAG, "■■■■■■현 매장에 진입중");
                        return;
                    } else {
                        Log.i(TAG, "■■■■■■현 매장이 아닌매장에 진입했을 확률이 있음 ");
                        if (storeBeaconList != null && storeBeaconList.size() == 0) {
                            Log.i(TAG, "■■■■■■비콘리스트 0임");
                            return;
                        }
                        if (storeBeaconList == null) {
                            Log.i(TAG, "■■■■■■비콘리스트 로드안됨");
                            return;
                        }
                        if (now_beacon_UUID.equals(BASKET_BEACON_UUID)) {
                            Log.i(TAG, "■■■■■■■바스켓UUID와 현재UUID가 같음 BASKET 매장임");
                            if (last_beacon_major.equals(now_beacon_major)) {
                                Log.i(TAG, "■■■■■■■■MAJOR 같음, 같은매장일 확률 있음 MINOR 비교");
                                if (last_beacon_minor.equals(now_beacon_minor)) {
                                    Log.i(TAG, "■■■■■■■■■MINOR 같음, 같음매장임");
                                    return;
                                } else {
                                    Log.i(TAG, "■■■■■■■■■MINOR가 같지않음, 다른매장임");
                                    if (storeBeaconList != null) {
                                        for (Map<String, Object> row : storeBeaconList) {
                                            if (row.get("STO_MINOR").toString().equals(now_beacon_minor)) {
                                                Log.i(TAG, "■■■■■■■■■■MINOR 매칭됨 새로운매장진입. STO_CODE을 추출 새로운매장확실");
                                                last_beacon_UUID = now_beacon_UUID;
                                                last_beacon_major = now_beacon_major;
                                                last_beacon_minor = now_beacon_minor;
                                                MemberDTO.getInstance().setSto_code((int)row.get("STO_CODE"));
                                                Log.i(TAG, "■■■■■■■■■■■STO_CODE : " + MemberDTO.getInstance().getSto_code());
                                                MemberDTO.getInstance().setSto_name(row.get("STO_NAME").toString());
                                                Toast.makeText(mActivity, MemberDTO.getInstance().getSto_name() + "에 입장하였습니다.", Toast.LENGTH_SHORT).show();
                                                Log.i(TAG, "■■■■■■■■■■■STO_NAME : " + MemberDTO.getInstance().getSto_name());
                                                return;
                                            }
                                        }
                                    }
                                }
                            } else {
                                Log.i(TAG, "■■■■■■■■■MINOR가 같지않음, 다른매장임");
                                if (storeBeaconList != null) {
                                    for (Map<String, Object> row : storeBeaconList) {
                                        if (row.get("STO_MAJOR").toString().equals(now_beacon_major)) {
                                            if (row.get("STO_MINOR").toString().equals(now_beacon_minor)) {
                                                Log.i(TAG, "■■■■■■■■■■MINOR 매칭됨 새로운매장진입. STO_CODE을 추출 새로운매장확실");
                                                last_beacon_UUID = now_beacon_UUID;
                                                last_beacon_major = now_beacon_major;
                                                last_beacon_minor = now_beacon_minor;
                                                MemberDTO.getInstance().setSto_code((int)row.get("STO_CODE"));
                                                Log.i(TAG, "■■■■■■■■■■■STO_CODE : " + MemberDTO.getInstance().getSto_code());
                                                if(row.get("STO_NAME")!=null){
                                                    Log.i(TAG, row.get("STO_NAME").toString());
                                                    MemberDTO.getInstance().setSto_name(row.get("STO_NAME").toString());
                                                }
                                                Log.i(TAG, "■■■■■■■■■■■STO_NAME : " + MemberDTO.getInstance().getSto_name());
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            Log.i(TAG, "■■■■■■■■UUID틀림 BASKET 매장 아님");
                        }
                    }
                }
            }
        });
        try {
            //알려주는 BeaconService전달 일치 비콘을 찾고 시작하는 Region개체를 지역에서 비콘을 볼 수있는 동안 추정 mDistance에있는 모든 초 업데이트를 제공합니다.
            beaconManager.startRangingBeaconsInRegion(new Region("C2:02:DD:00:13:DD", null, null, null));
        } catch (
                RemoteException e) {

        }

    }

    @Override
    public Context getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return false;
    }
}
