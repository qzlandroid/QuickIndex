package cn.jit.quickindex;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import cn.jit.quickindex.bean.Person;
import cn.jit.quickindex.ui.QuickIndexBar;
import cn.jit.quickindex.util.Cheeses;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private QuickIndexBar mIndexBar;
    private TextView mTextView;
    private ArrayList<Person> lists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListener();
    }


    private void initView() {
        mListView = (ListView) findViewById(R.id.lv_main);
        mIndexBar = (QuickIndexBar) findViewById(R.id.bar);
        mTextView = (TextView) findViewById(R.id.tv_center);
    }
    private void initData(){
        for (int i=0;i< Cheeses.NAMES.length;i++){
            lists.add(new Person(Cheeses.NAMES[i]));
        }
        Collections.sort(lists);
        mListView.setAdapter(new MyAdapter());

    }

    private void initListener() {
        //QuickIndexBar触摸监听 提示Toast 改变listView位置
        mIndexBar.setOnLetterUpdateListener(new QuickIndexBar.OnLetterUpdateListener() {
            @Override
            public void onLetterUpdate(String letter) {
                showToast(letter);
                //修改listView位置
                for (int i=0;i<lists.size();i++){
                    String index = lists.get(i).getPinyin().charAt(0)+"";
                    if (TextUtils.equals(letter,index)){
                        mListView.setSelection(i);
                        break;
                    }
                }
            }
        });
    }

    private Handler mHandler = new Handler();

    /**
     * 提示索引Toast 0.5秒后消失
     * @param letter
     */
    private void showToast(String letter) {
        mTextView.setText(letter);
        mTextView.setVisibility(View.VISIBLE);
        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTextView.setVisibility(View.INVISIBLE);
            }
        },500);
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Person getItem(int position) {
            return lists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null){
                convertView = View.inflate(MainActivity.this,R.layout.item_list,null);
                holder = new ViewHolder();
                holder.tv_index = (TextView) convertView.findViewById(R.id.tv_index);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            //如果当前条目与上一个条目拼音首字母相同,则不显示索引
            String str = null;
            String curIndex = getItem(position).getPinyin().charAt(0)+"";
            if (position == 0){
                str = curIndex;
            }else {
                String preIndex = getItem(position-1).getPinyin().charAt(0)+"";
                if (!TextUtils.equals(curIndex,preIndex)){
                    str = curIndex;
                }
            }
            holder.tv_index.setVisibility(str == null ? View.GONE : View.VISIBLE);

            holder.tv_name.setText(getItem(position).getName());
            holder.tv_index.setText(curIndex);
            return convertView;
        }
    }
    class ViewHolder{
        TextView tv_name;
        TextView tv_index;
    }
}
