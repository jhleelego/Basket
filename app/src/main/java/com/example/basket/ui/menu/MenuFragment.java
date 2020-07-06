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
import androidx.lifecycle.ViewModelProviders;

import com.example.basket.R;
import com.example.basket.ui.menu.event.EventActivity;
import com.example.basket.ui.menu.myCoupon.MyCouponActivity;
import com.example.basket.ui.menu.myPage.MyPageActivity;
import com.example.basket.ui.menu.myStoreChoice.ChooseStoActivity;
import com.example.basket.ui.menu.purchaseHistory.PurchaseHistoryActivity;
import com.example.basket.ui.menu.serviceCenter.ServiceCenterActivity;

public class MenuFragment extends Fragment {
    private MenuViewModel menuViewModel;
    public TextView tv_memName = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                    if(position==0){startActivity(new Intent(getActivity(), MyPageActivity.class)); return;}
                    if(position==1){startActivity(new Intent(getActivity(), PurchaseHistoryActivity.class)); return;}
                    if(position==2){startActivity(new Intent(getActivity(), MyCouponActivity.class)); return;}
                    if(position==3){startActivity(new Intent(getActivity(), EventActivity.class)); return;}
                    if(position==4){startActivity(new Intent(getActivity(), ChooseStoActivity.class)); return;}
                    if(position==5){startActivity(new Intent(getActivity(), ServiceCenterActivity.class)); return;}
            }
        };
        menuViewModel =
                ViewModelProviders.of(this).get(MenuViewModel.class);
        View root = inflater.inflate(R.layout.fragment_menu, container, false);
        ArrayAdapter adapter = menuViewModel.getList(root.getContext());
        ListView listView = (ListView) root.findViewById(R.id.menu_list);
        listView.setAdapter(adapter);

        //리스너를 리스트뷰에 매핑
        listView.setOnItemClickListener(itemClickListener);
        tv_memName = root.findViewById(R.id.tv_memName);
        if(MemberDTO.getInstance().getMem_name()!=null){
            tv_memName.setText(MemberDTO.getInstance().getMem_name());
        }
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

