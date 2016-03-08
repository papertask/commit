package com.interview.iso.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.interview.iso.R;
import com.interview.iso.base.MenuItem;
import com.interview.iso.fragments.BaseFragment;
import com.interview.iso.fragments.FunctionSelectFragment;
import com.interview.iso.fragments.HelpFragment;
import com.interview.iso.fragments.ListNameFragment;
import com.interview.iso.fragments.NewQuestionnaireFragment;
import com.interview.iso.fragments.QuestionFragment;
import com.interview.iso.models.Person;
import com.interview.iso.utils.AppData;
import com.interview.iso.utils.Constants;
import com.interview.iso.utils.DBHelper;
import com.interview.iso.utils.DataPreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends CameraActivity implements FragmentManager.OnBackStackChangedListener {

    DrawerLayout mDrawerLayout;
    ListView mMenuListView;
    private ActionBarDrawerToggle mDrawerToggle;
    int mSelectedMenuIndex = -1;
    Toolbar mToolbar;
    TextView mTitle;
    ImageButton mSearch, mTick, mBack;
    TextView[] mMenus;
    int MENU_TIWEN = 0, MENU_LIEBIAO = 1, MENU_FANYI = 2, MENU_BUMEN = 3, MENU_SHIGUAN = 4, MENU_ZHENGCE = 5, MENU_GONGNENG = 6, MENU_BANGZHU = 7;
    RelativeLayout sg_submenu;
    ImageView img_sg_dropdown;
    RelativeLayout zc_submenu;
    ImageView img_zc_dropdown;
    FrameLayout container_cnt_questions;
    TextView txt_cnt_questions;

    public static MainActivity shareActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        shareActivity = this;
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) findViewById(R.id.title);
        mSearch = (ImageButton) findViewById(R.id.button_search);
        mTick = (ImageButton) findViewById(R.id.button_tick);
        mBack = (ImageButton) findViewById(R.id.button_back);
        container_cnt_questions = (FrameLayout) findViewById(R.id.container_dajuanshu);
        txt_cnt_questions = (TextView) findViewById(R.id.txt_dajuanshu);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            // enabling action bar app icon and behaving it as toggle button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            toolbar.setNavigationIcon(R.drawable.icon_nav);
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                    R.string.app_name,
                    R.string.app_name) {

                /** Called when a drawer has settled in a completely closed state. */
                @Override
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                }

                /** Called when a drawer has settled in a completely open state. */
                @Override
                public void onDrawerOpened(View drawerView) {
                    updateCountQuestions();
                    super.onDrawerOpened(drawerView);
                }
            };
            mDrawerLayout.setDrawerListener(mDrawerToggle);
            mDrawerToggle.syncState();
        }

        // Initialize left menu textviews
        mMenus = new TextView[8];
        mMenus[MENU_TIWEN] = (TextView) findViewById(R.id.menu_tiwen);
        mMenus[MENU_LIEBIAO] = (TextView) findViewById(R.id.menu_liebiao);
        mMenus[MENU_FANYI] = (TextView) findViewById(R.id.menu_fanyi);
        mMenus[MENU_BUMEN] = (TextView) findViewById(R.id.menu_bumen);
        mMenus[MENU_SHIGUAN] = (TextView) findViewById(R.id.menu_shiguan);
        mMenus[MENU_ZHENGCE] = (TextView) findViewById(R.id.menu_zhengce);
        mMenus[MENU_GONGNENG] = (TextView) findViewById(R.id.menu_function_select);
        mMenus[MENU_BANGZHU] = (TextView) findViewById(R.id.menu_bangzhu);

        // Initialize submenus
        sg_submenu = (RelativeLayout) findViewById(R.id.container_shiguan_sub);
        zc_submenu = (RelativeLayout) findViewById(R.id.container_zhengce_sub);

        // Initialize dropdown buttons
        img_zc_dropdown = (ImageView) findViewById(R.id.img_zhengce_dropdown);
        img_sg_dropdown = (ImageView) findViewById(R.id.img_shiguan_dropdown);

        updateCountQuestions();
        didSelectMenuItem(new MenuItem(getResources().getString(R.string.addnew_header), "NewQuestionnaireFragment", "add_new", 0));
    }

    public void reset_textbox_color(int num) {
        for (int i = MENU_TIWEN; i <= MENU_BANGZHU; i++) {
            mMenus[i].setTextColor(getResources().getColor(R.color.menuForecolor));
        }

        if (num != -1)
            mMenus[num].setTextColor(getResources().getColor(R.color.colorPrimary));

        if (num != MENU_SHIGUAN && num != MENU_ZHENGCE) {
            sg_submenu.setVisibility(View.GONE);
        }
    }

    public void perform_action(View v) {
        int str_id = v.getId();
        switch (str_id) {
            case R.id.container_tiwen:  // 开始提问
                didSelectMenuItem(new MenuItem(getResources().getString(R.string.addnew_header), "NewQuestionnaireFragment", "add_new", 0));
                break;
            case R.id.container_liebiao:
                didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_question_list), "ListNameFragment", "list_interviewer", 0));
                break;
            case R.id.container_fanyi:
                didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_link_translate), "TranslatorFragment", "translate", 0));
                break;
            case R.id.container_bangzhu:
                didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_about), "HelpFragment", "about", 0));
                break;
            case R.id.container_bumen:
                didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_link_related_section), "SectionFragment", "section", 0));
                break;
            case R.id.container_shiguan:
                reset_textbox_color(MENU_SHIGUAN);
                if (sg_submenu.getVisibility() == View.GONE) {
                    sg_submenu.setVisibility(View.VISIBLE);
                    img_sg_dropdown.setImageResource(R.drawable.menu_sub_open);
                } else {
                    sg_submenu.setVisibility(View.GONE);
                    img_sg_dropdown.setImageResource(R.drawable.menu_sub_close);
                }
                break;
            case R.id.container_zhengce:
                reset_textbox_color(MENU_ZHENGCE);
                if (zc_submenu.getVisibility() == View.GONE) {
                    zc_submenu.setVisibility(View.VISIBLE);
                    img_zc_dropdown.setImageResource(R.drawable.menu_sub_open);
                } else {
                    zc_submenu.setVisibility(View.GONE);
                    img_zc_dropdown.setImageResource(R.drawable.menu_sub_close);
                }
                break;
            case R.id.container_function_select:
                didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_function_select), "FunctionSelectFragment", "function_select", 0));
                break;
            case R.id.container_shiguan_sub_cambodia:
                didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_link_embassy), "CamEmbassyFragment", "submenu_shiguan", 0));
                break;
            case R.id.container_shiguan_sub_laos:
                didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_link_embassy), "LaosEmbassyFragment", "submenu_shiguan", 0));
                break;
            case R.id.container_shiguan_sub_vietnam:
                didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_link_embassy), "VietnamEmbassyFragment", "submenu_shiguan", 0));
                break;
            case R.id.container_shiguan_sub_thai:
                didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_link_embassy), "MyanmarEmbassyFragment", "submenu_shiguan", 0));
                break;
            case R.id.container_zhengce_sub_gongan:
                didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_police_crackdown_guide), "PoliceGuideFragment", "police_guide", 0));
                break;
            case R.id.container_zhengce_sub_minzheng:
                didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_people_rescue_procedure), "PeopleGovFragment", "police_guide", 0));
                break;
            case R.id.container_zhengce_sub_shouhai:
                didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_trafficking_victims_indicators), "PeopleGovFragment", "police_guide", 0));
                break;
            case R.id.container_zhengce_sub_guojidingyi:
                didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_legal_definition), "GovInternationalFragment", "police_guide", 0));
                break;
            case R.id.container_zhengce_sub_guojiajihua:
                didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_anti_trafficking_plan), "GovTraffickingPlanFragment", "police_guide", 0));
                break;
            default:
                didSelectMenuItem(new MenuItem("等一等", "NewQuestionnaireFragment", "", 0));
                break;
        }
        updateCountQuestions();

    }

    public void close_menu(View v) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    public void updateCountQuestions() {
        DBHelper db = new DBHelper( this );

        List<Person> mListPerson = db.getAllPerson();

        if (mListPerson.size() > 0) {
            txt_cnt_questions.setText(Integer.toString(mListPerson.size()));
            container_cnt_questions.setVisibility(View.VISIBLE);
        } else {
            container_cnt_questions.setVisibility(View.GONE);
        }
    }

    public void didSelectMenuItem(MenuItem item) {
        final Fragment fragment = item.fragment(this);
        if (QuestionFragment.mPlayer != null) {
            QuestionFragment.mPlayer.pause();
            QuestionFragment.mPlayer = null;
        }
        if (fragment != null) {
            //getSupportActionBar().setTitle(item.getTitle());
            if (item.identifier.equals("add_new")) {
                mTick.setVisibility(View.VISIBLE);
                mSearch.setVisibility(View.GONE);
                mBack.setVisibility(View.GONE);
                mTick.setBackgroundResource(R.drawable.icon_tick);
                reset_textbox_color(MENU_TIWEN);
                mTick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NewQuestionnaireFragment frAddNew = (NewQuestionnaireFragment) fragment;
                        frAddNew.storeInterviewer();
                    }
                });
                close_menu(null);
            } else if (item.identifier.equals("list_interviewer")) {
                mTick.setVisibility(View.GONE);
                mBack.setVisibility(View.GONE);
                mSearch.setVisibility(View.VISIBLE);
                mSearch.setBackgroundResource(R.drawable.icon_search);
                reset_textbox_color(MENU_LIEBIAO);
                mSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ListNameFragment frListName = (ListNameFragment) fragment;
                        frListName.updateSearch();
                    }
                });
                close_menu(null);
            } else if (item.identifier.equals("section")) {
                mTick.setVisibility(View.GONE);
                mBack.setVisibility(View.GONE);
                mSearch.setVisibility(View.VISIBLE);
                mSearch.setBackgroundResource(R.drawable.icon_search);
                reset_textbox_color(MENU_BUMEN);
                mSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ListNameFragment frListName = (ListNameFragment) fragment;
                        frListName.updateSearch();
                    }
                });
                close_menu(null);
            } else if (item.identifier.equals("question")) {
                mSearch.setVisibility(View.GONE);
                mTick.setVisibility(View.GONE);
                mBack.setVisibility(View.GONE);
                mBack.setBackgroundResource(R.drawable.ic_back);
                mBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //show
                        QuestionFragment frListName = (QuestionFragment) fragment;
                        frListName.doBackQuestion();
                    }
                });
            } else if (item.identifier.equals("function_select")) {
                mTick.setVisibility(View.GONE);
                mBack.setVisibility(View.GONE);
                mSearch.setVisibility(View.VISIBLE);
                mSearch.setBackgroundResource(R.drawable.icon_search);
                reset_textbox_color(MENU_GONGNENG);
                close_menu(null);
            } else if (item.identifier.equals("translate")) {
                reset_textbox_color(MENU_FANYI);
                close_menu(null);
            } else if ( item.identifier.equals("about")) {
                reset_textbox_color(MENU_BANGZHU);
                close_menu(null);
            } else if ( item.identifier.equals("submenu_shiguan")) {
                reset_textbox_color(MENU_SHIGUAN);
                close_menu(null);
            } else if ( item.identifier.equals("police_guide")) {
                reset_textbox_color(MENU_ZHENGCE);
                close_menu(null);
            } else if (item.identifier.equals("people_gov")) {
                reset_textbox_color(MENU_ZHENGCE);
                close_menu(null);
            } else {
                mSearch.setVisibility(View.GONE);
                mTick.setVisibility(View.GONE);
                mBack.setVisibility(View.GONE);
                close_menu(null);
            }
            mTitle.setText(item.getTitle());
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStackImmediate(fragmentManager.getBackStackEntryAt(0).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "root").commit();
            mDrawerToggle.setDrawerIndicatorEnabled(true);
        }
    }

    public void UpdateFragment(String fr) {

        Fragment fragment = MenuItem.getFragment(this, fr);

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate(fragmentManager.getBackStackEntryAt(0).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "root").commit();
    }

    /*
        update action bar custom
     */
    public void updateActionBar(boolean isBackShow) {
        if (isBackShow)
            mBack.setVisibility(View.VISIBLE);
        else
            mBack.setVisibility(View.GONE);
    }

    public void loadWebview(int id) {
        Intent itent = new Intent(this, ChapterViewActivity.class);
        itent.putExtra("id", id);
        startActivity(itent);
    }

    @Override
    public void onBackStackChanged() {
        boolean canback = getSupportFragmentManager().getBackStackEntryCount() > 0;
        mDrawerToggle.setDrawerIndicatorEnabled(!canback);
        BaseFragment currentFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (currentFragment != null) {
            if (BaseFragment.class.isAssignableFrom(currentFragment.getClass())) {
                getSupportActionBar().setTitle(currentFragment.getTitle());
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggle
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
            getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class MenuAdapter extends BaseAdapter {

        private List<MenuItem> menuItems;
        private Context mContext;

        MenuAdapter(Context context, ArrayList<MenuItem> items) {
            mContext = context;
            menuItems = items;

        }

        @Override
        public int getCount() {
            return menuItems != null ? menuItems.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return menuItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            ViewHolder holder;
            MenuItem menuItem = menuItems.get(position);
            if (v == null) {
                holder = new ViewHolder();
                // Inflate the layout according to the view type
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.menu_item, parent, false);
                holder.ivIcon = (ImageView) v.findViewById(R.id.icon);
                holder.tvName = (TextView) v.findViewById(R.id.menu_name);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }
            // holder.ivIcon.setImageResource(menuItem.mIconResource);
            holder.tvName.setText(menuItem.mTitle);
            return v;
        }

        class ViewHolder {
            ImageView ivIcon;
            TextView tvName;
        }
    }


}
