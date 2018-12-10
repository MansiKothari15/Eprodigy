package com.bacancy.eprodigy.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bacancy.eprodigy.Models.ChatPojo;
import com.bacancy.eprodigy.R;
import com.bacancy.eprodigy.utils.SCUtils;

public class ChatContactDetailActivity extends BaseActivity implements View.OnClickListener {
    public static final String CHAT_DATA = "CHAT_DATA";
    TextView tv_label, tv_newMessage, tv_createGroup, tv_back;
    Activity mActivity;

    ImageView img_contact_incoming;
    TextView tv_contact_name_incoming, tv_phone_incoming, tv_time_incoming;
    RelativeLayout rl_contact_incoming;
    //
    ImageView img_contact_outgoing;
    TextView tv_contact_name_outgoing, tv_phone_outgoing, tv_time_outgoing;
    RelativeLayout rl_contact_outgoing;


    public static void StartChatContactDetailActivity(Context context, ChatPojo chatPojo) {
        Intent intent = new Intent(context, ChatContactDetailActivity.class);
        intent.putExtra(CHAT_DATA, chatPojo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_contact_detail);
        mActivity = this;
        init();
        setData();
    }

    private void setData() {
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getParcelable(CHAT_DATA) != null) {
            ChatPojo chatPojo = getIntent().getExtras().getParcelable(CHAT_DATA);

            String formatted_date = SCUtils.formatted_date(chatPojo.getChatTimestamp());
            String name =  chatPojo.getSharedContactSenderName() ;
            String cono =  chatPojo.getSharedContactSenderNumber();
           // String image = chatPojo.isMine() ? chatPojo.getSharedContactSenderImage() : chatPojo.getSharedContactRecvImage();
            
            if (chatPojo.isMine() && (!TextUtils.isEmpty(name)
                    && !TextUtils.isEmpty(cono)))
            {
                rl_contact_outgoing.setVisibility(View.VISIBLE);
                rl_contact_incoming.setVisibility(View.GONE);
                tv_time_outgoing.setText(formatted_date);

/*                Picasso.with(mActivity).load(image)
                        .placeholder(mActivity.getResources().getDrawable(R.mipmap.profile_pic))
                        .error(mActivity.getResources().getDrawable(R.mipmap.profile_pic))
                        .into(img_contact_outgoing);*/

                tv_contact_name_outgoing.setText(name);
                tv_phone_outgoing.setText(cono);
            }
            else if (!chatPojo.isMine() && (!TextUtils.isEmpty(name)
                    && !TextUtils.isEmpty(cono)))
            {

                rl_contact_incoming.setVisibility(View.VISIBLE);
                rl_contact_outgoing.setVisibility(View.GONE);

                    tv_time_incoming.setText(formatted_date);
                    
                    /*Picasso.with(mActivity).load(image)
                            .placeholder(mActivity.getResources().getDrawable(R.mipmap.profile_pic))
                            .error(mActivity.getResources().getDrawable(R.mipmap.profile_pic))
                            .into(img_contact_incoming);*/

                    tv_contact_name_incoming.setText(name);
                    tv_phone_incoming.setText(cono);
                 
            }
            else
            {
                rl_contact_incoming.setVisibility(View.GONE);
                rl_contact_outgoing.setVisibility(View.GONE);
            }
  
        } else {
            rl_contact_incoming.setVisibility(View.GONE);
            rl_contact_outgoing.setVisibility(View.GONE);
            Toast.makeText(activity, "502-Internal error...", Toast.LENGTH_SHORT).show();
        }


    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tv_label = (TextView) findViewById(R.id.tv_label);
        tv_newMessage = (TextView) findViewById(R.id.tv_right);
        tv_createGroup = (TextView) findViewById(R.id.tv_left);
        tv_back = (TextView) findViewById(R.id.tv_back);


        tv_label.setText(getResources().getString(R.string.view_contact_title));
        tv_back.setOnClickListener(this);

        hideCustomToolbar();


        //
        rl_contact_outgoing = (RelativeLayout)findViewById(R.id.rl_contact_outgoing);
        img_contact_outgoing = (ImageView) findViewById(R.id.img_contact_outgoing);
        tv_contact_name_outgoing = (TextView) findViewById(R.id.tv_contact_name_outgoing);
        tv_time_outgoing = (TextView) findViewById(R.id.tv_time_outgoing);
        tv_phone_outgoing = (TextView) findViewById(R.id.tv_phone_outgoing);

        rl_contact_incoming = (RelativeLayout) findViewById(R.id.rl_contact_incoming);
        img_contact_incoming = (ImageView) findViewById(R.id.img_contact_incoming);
        tv_contact_name_incoming = (TextView) findViewById(R.id.tv_contact_name_incoming);
        tv_time_incoming = (TextView) findViewById(R.id.tv_time_incoming);
        tv_phone_incoming = (TextView) findViewById(R.id.tv_phone_incoming);
        

    }

    public void hideCustomToolbar() {
        tv_label.setVisibility(View.VISIBLE);
        tv_newMessage.setVisibility(View.INVISIBLE);

        tv_createGroup.setVisibility(View.INVISIBLE);
        tv_back.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;

        }
    }
}
