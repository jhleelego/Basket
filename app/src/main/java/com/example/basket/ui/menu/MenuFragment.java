package com.example.basket.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.basket.R;
import com.example.basket.ui.menu.favevent.FavCouponActivity;
import com.example.basket.ui.menu.favstore.FavStoreActivity;
import com.example.basket.ui.menu.myPage.MyPageActivity;
import com.example.basket.ui.menu.paymenthistory.PaymentHistoryActivity;
import com.example.basket.ui.menu.serviceCenter.ServiceCenterActivity;
import com.example.basket.vo.MemberDTO;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Locale;

import static com.example.basket.ui.basket.BasketFragment.basketMoney;

public class MenuFragment extends Fragment {
    public static final String TAG = "MenuFragment";
    private MenuViewModel menuViewModel;
    public TextView tv_memName = null;
    public static TextView tv_menu_basketMoney = null;
    public TextView tv_stoName = null;
    private View root = null;

    public static MenuFragment newInstance() {
        return new MenuFragment();
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "■■onCreateView");
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                    if(position==0){startActivity(new Intent(getActivity(), MyPageActivity.class)); return;}
                    if(position==1){startActivity(new Intent(getActivity(), PaymentHistoryActivity.class)); return;}
                    if(position==2){startActivity(new Intent(getActivity(), FavCouponActivity.class)); return;}
                    if(position==3){startActivity(new Intent(getActivity(), FavStoreActivity.class)); return;}
                    if(position==4){startActivity(new Intent(getActivity(), ServiceCenterActivity.class)); return;}
            }
        };
        menuViewModel =
                ViewModelProviders.of(this).get(MenuViewModel.class);
        root = inflater.inflate(R.layout.fragment_menu, container, false);
        ArrayAdapter adapter = menuViewModel.getList(root.getContext());
        tv_menu_basketMoney = root.findViewById(R.id.tv_meun_basketMoney);
        tv_memName = root.findViewById(R.id.tv_memName);
        if(MemberDTO.getInstance().getMem_name()!=null){
            Log.i(TAG, MemberDTO.getInstance().getMem_name());
            tv_memName.setText(MemberDTO.getInstance().getMem_name());
        }
        tv_stoName = root.findViewById(R.id.tv_stoName);
        tv_menu_basketMoney.setText(Currency.getInstance(Locale.KOREA).getSymbol() + new DecimalFormat("###,###").format(basketMoney));
        if(MemberDTO.getInstance().getSto_name()!=null){
            Log.i(TAG, MemberDTO.getInstance().getSto_name());
            tv_stoName.setText(MemberDTO.getInstance().getSto_name());
        }
        ListView listView = (ListView) root.findViewById(R.id.menu_list);
        listView.setAdapter(adapter);

        //리스너를 리스트뷰에 매핑
        listView.setOnItemClickListener(itemClickListener);
        tv_memName = root.findViewById(R.id.tv_memName);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "■■MenuFragment : onStart()");
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "■■MenuFragment : onCreate()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "■■MenuFragment : onPause()");
    }
}

