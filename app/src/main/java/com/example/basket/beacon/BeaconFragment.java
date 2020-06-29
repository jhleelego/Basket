package com.example.basket.beacon;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.List;

public class BeaconFragment extends Fragment implements BeaconConsumer {
    protected static final String TAG = "::MonitoringActivity::";
    protected static final String TAG1 = "::MonitoringActivity::";
    protected static final String TAG2 = "::RangingActivity::";
    private BeaconManager beaconManager;
    Context applicationContext = null;

    public static Fragment getInstance() {
        return LazyHolder.instance;
    }

    private static class LazyHolder {
        private static final BeaconFragment instance = new BeaconFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        Log.i(TAG, "BeaconFragment onAttach()");
        super.onAttach(context);
        applicationContext = getApplicationContext();
       /* if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
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
                Log.i(TAG1, ":::::최소하나의 비콘 발견하였음:::::");
            }
            @Override
            //비콘 Region이 보이지 않을 때 호출됩니다 .
            public void didExitRegion(Region region) {
                Log.i(TAG1, ":::::더이상 비콘을 찾을 수 없음:::::");
            }
            @Override
            //하나 이상의 비콘 Region이 표시 될 때 MonitorNotifier.INSIDE 상태 값으로 호출됩니다 .
            public void didDetermineStateForRegion(int state, Region region) {
                if(state==0){
                    Log.i(TAG1, ":::::비콘이 보이는 상태이다. state : "+state + ":::::");
                } else {
                    Log.i(TAG1, ":::::비콘이 보이지 않는 상태이다. state : "+state +":::::");
                }
            }
        });
        //범위한정 알리미를 추가한다
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            //눈에 보이는 비콘에 대한 mDistance(major 또는 minor와의 거리를 뜻하는)의 추정치를 제공하기 위해 초당 한 번 호출
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                List<Beacon> list = (List<Beacon>)beacons;
                if (beacons.size() > 0) {
                    //디비의 매장의 블루투스관련 컬럼들을 비교하여야할 곳
                    String[] signal_beaconInfo = {beacons.iterator().next().getId1().toString(), beacons.iterator().next().getId2().toString(), beacons.iterator().next().getId3().toString()};
                    //디비 매장블루투스관련 컬럼 들어갈 Array
                    String[] db_beaconInfo = {"e2c56db5-dffb-48d2-b060-d0f5a71096e0", "40001", "45225"};
                    //디비와 매칭되는 beacon에대한 info만 뽑아낸다.
                    if(signal_beaconInfo[0].equals(db_beaconInfo[0])
                            &&signal_beaconInfo[1].equals(db_beaconInfo[1])
                            &&signal_beaconInfo[2].equals(db_beaconInfo[2])){
                        Log.i(TAG2, ":::::The first beacon I see is about "+beacons.iterator().next().getDistance()+" meters away.:::::");
                        Log.i(TAG2, ":::::This :: U U I D :: of beacon   :  "+ signal_beaconInfo[0] + ":::::");
                        Log.i(TAG2, ":::::This ::M a j o r:: of beacon   :  "+ signal_beaconInfo[1] + ":::::");
                        Log.i(TAG2, ":::::This ::M i n o r:: of beacon   :  "+ signal_beaconInfo[2] + ":::::");
                    }
                }
            }
        });
        try {
            //알려주는 BeaconService전달 일치 비콘을 찾고 시작하는 Region개체를 지역에서 비콘을 볼 수있는 동안 추정 mDistance에있는 모든 초 업데이트를 제공합니다.
            beaconManager.startRangingBeaconsInRegion(new Region("C2:02:DD:00:13:DD", null, null, null));
        } catch (RemoteException e) {

        }
    }

    @Override
    public Context getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {}

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {return false;}
}
