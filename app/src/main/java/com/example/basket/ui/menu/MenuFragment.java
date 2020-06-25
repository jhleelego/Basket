package com.example.basket.ui.menu;

import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.basket.R;
import com.example.basket.nil.OAuthFragment;
import com.example.basket.ui.menu.event.EventActivity;
import com.example.basket.ui.menu.myCoupon.MyCouponActivity;
import com.example.basket.ui.menu.myPage.MyPageActivity;
import com.example.basket.ui.menu.myStoreChoice.MyStoreChoiceActivity;
import com.example.basket.ui.menu.purchaseHistory.PurchaseHistoryActivity;
import com.example.basket.ui.menu.serviceCenter.ServiceCenterActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuFragment extends Fragment {

    private FragmentManager fragmentManager;
    private OAuthFragment oAuthFragment;
    private FragmentTransaction transaction;


    private MenuViewModel menuViewModel;
    TextView tv_userNick = null;
    String nickName = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                /*if(position==0){intent = new Intent(getActivity(), MyPageActivity.class);}
                if(position==1){intent = new Intent(getActivity(), PurchaseHistoryActivity.class);}
                if(position==2){intent = new Intent(getActivity(), MyCouponActivity.class);}
                if(position==3){intent = new Intent(getActivity(), EventActivity.class);}
                if(position==4){intent = new Intent(getActivity(), MyStoreChoiceActivity.class);}
                if(position==5){intent = new Intent(getActivity(), ServiceCenterActivity.class);}
                startActivity(intent);
                return;*/
                switch(position){
                    case 0 :
                        intent = new Intent(getActivity(), MyPageActivity.class);
                        startActivity(intent);
                        break;
                    case 1 :
                        intent = new Intent(getActivity(), PurchaseHistoryActivity.class);
                        startActivity(intent);
                        break;
                    case 2 :
                        intent = new Intent(getActivity(), MyCouponActivity.class);
                        startActivity(intent);
                        break;
                    case 3 :
                        intent = new Intent(getActivity(), EventActivity.class);
                        startActivity(intent);
                        break;
                    case 4 :
                        intent = new Intent(getActivity(), MyStoreChoiceActivity.class);
                        startActivity(intent);
                        break;
                    case 5 :
                        intent = new Intent(getActivity(), ServiceCenterActivity.class);
                        startActivity(intent);
                        break;
                    default :
                        break;
                }
            }
        };

        menuViewModel =
                ViewModelProviders.of(this).get(MenuViewModel.class);
        View root = inflater.inflate(R.layout.fragment_menu, container, false);
        ArrayAdapter adapter = menuViewModel.getList(root.getContext());
        ListView listView = (ListView) root.findViewById(R.id.menu_list);
        listView.setAdapter(adapter);


        if(this.getActivity().getIntent().getExtras().getString("nickname")!=null){
            nickName = this.getActivity().getIntent().getExtras().getString("nickname");
            tv_userNick = (TextView) root.findViewById(R.id.userNick);
            tv_userNick.setText(nickName);
        }


        //리스너를 리스트뷰에 매핑
        listView.setOnItemClickListener(itemClickListener);
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

