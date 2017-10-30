package com.chinasofti.message;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MessageFragment extends Fragment {
    private String mTitle;

    public MessageFragment(){}

//    public MessageFragment(String title){
//        this.mTitle=title;
//    }

//    public static MessageFragment getInstance(String title) {
//        MessageFragment sf = new MessageFragment();
//        sf.mTitle = title;
//        return sf;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.message_main_layout, null);
        TextView card_title_tv = (TextView) v.findViewById(R.id.meesage_card_title_tv);
        card_title_tv.setText("消息");
        return v;
    }
}
