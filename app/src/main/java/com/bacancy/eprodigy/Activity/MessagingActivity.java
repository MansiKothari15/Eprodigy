package com.bacancy.eprodigy.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bacancy.eprodigy.Fragment.ChatsFragment;
import com.bacancy.eprodigy.Fragment.SettingsFragment;
import com.bacancy.eprodigy.Fragment.UsersFragment;
import com.bacancy.eprodigy.R;

import java.lang.reflect.Field;

public class MessagingActivity extends BaseActivity{

    private BottomNavigationView mBottomNav;
    private int mSelectedItem;
    TextView tv_label,tv_newMessage,tv_createGroup,tv_back;
    private static final String SELECTED_ITEM = "arg_selected_item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv_label = (TextView)findViewById(R.id.tv_label);
        tv_back = (TextView)findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_newMessage = (TextView)findViewById(R.id.tv_right);
        tv_newMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MessagingActivity.this,NewMessageActivity.class);
                startActivity(i);
            }
        });

        tv_createGroup = (TextView)findViewById(R.id.tv_left);
        tv_createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MessagingActivity.this,CreateGroupActivity.class);
                startActivity(i);
            }
        });

        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.removeShiftMode(mBottomNav);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
        });

        MenuItem selectedItem;
        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            selectedItem = mBottomNav.getMenu().findItem(mSelectedItem);
        } else {
            selectedItem = mBottomNav.getMenu().getItem(0);
            Log.d("selectedItem",selectedItem.toString());
        }
        selectFragment(selectedItem);
    }

    private void selectFragment(MenuItem item) {
        Fragment frag = null;

        switch (item.getItemId()) {
            case R.id.menu_user:
                frag = new UsersFragment();
                item.setChecked(true);
                setCustomToolbar("Users");
                break;
            case R.id.menu_chat:
                frag = new ChatsFragment();
                setCustomToolbar("Chat");
                break;
            case R.id.menu_settings:
                frag = new SettingsFragment();
                setCustomToolbar("Settings");
                hideCustomToolbar();
                break;
            default:
                item.setChecked(false);
        }

        // update selected item
        mSelectedItem = item.getItemId();

        updateToolbarText(item.getTitle());

        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, frag, frag.getTag());
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    static class BottomNavigationViewHelper {
        @SuppressLint("RestrictedApi")
        static void removeShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    item.setShiftingMode(false);
                    // set once again checked value, so view will be updated
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
            } catch (IllegalAccessException e) {
                Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
            }
        }

    }

    private void updateToolbarText(CharSequence text) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(text);
        }
    }

    public void setCustomToolbar(String title){
        tv_label.setText(title);
        tv_createGroup.setVisibility(View.VISIBLE);
        tv_newMessage.setVisibility(View.VISIBLE);
    }

    public void hideCustomToolbar(){
        tv_newMessage.setVisibility(View.INVISIBLE);
        tv_createGroup.setVisibility(View.INVISIBLE);
    }

}
