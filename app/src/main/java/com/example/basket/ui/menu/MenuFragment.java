package com.example.basket.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.basket.R;
import com.example.basket.loginFragment.BilFragment;
import com.example.basket.loginFragment.KilFragment;
import com.example.basket.loginFragment.NilFragment;
import com.example.basket.ui.menu.event.EventActivity;
import com.example.basket.ui.menu.myCoupon.MyCouponActivity;
import com.example.basket.ui.menu.myPage.MyPageActivity;
import com.example.basket.ui.menu.myStoreChoice.FavStoreActivity;
import com.example.basket.ui.menu.purchaseHistory.PurchaseHistoryActivity;
import com.example.basket.ui.menu.serviceCenter.ServiceCenterActivity;
import com.example.basket.vo.MemberDTO;

public class MenuFragment extends Fragment {
    private MenuViewModel menuViewModel;
    TextView tv_userNick = null;
    String nickName = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
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
                        intent = new Intent(getActivity(), FavStoreActivity.class);
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
        String mem_Entrance = MemberDTO.getInstance().getMem_Entrance();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        if(mem_Entrance!=null&&mem_Entrance.length()>0){
            if(mem_Entrance.equals("bil")){
                fragmentTransaction.add(BilFragment.getInstance(), BilFragment.TAG);
            } else if(mem_Entrance.equals("nil")){
                fragmentTransaction.add(NilFragment.getInstance(), NilFragment.TAG);
            } else if(mem_Entrance.equals("kil")){
                fragmentTransaction.add(KilFragment.getInstance(), KilFragment.TAG);
            }

            fragmentTransaction.commitAllowingStateLoss();


        }



        menuViewModel =
                ViewModelProviders.of(this).get(MenuViewModel.class);
        View root = inflater.inflate(R.layout.fragment_menu, container, false);
        ArrayAdapter adapter = menuViewModel.getList(root.getContext());
        ListView listView = (ListView) root.findViewById(R.id.menu_list);
        listView.setAdapter(adapter);




        //리스너를 리스트뷰에 매핑
        listView.setOnItemClickListener(itemClickListener);
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

