package com.merise.net.heart.activities;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.bean.ReplyBean;
import com.merise.net.heart.bean.SendMessageBean;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.GouUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangdawang on 2016/10/18 0018.
 */
public class SuggestionActivity extends BaseActivity {

    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.suggest_lv)
    ListView suggestLv;
    @BindView(R.id.id_input_msg)
    EditText idInputMsg;
    @BindView(R.id.send_msg)
    Button sendMsg;
    @BindView(R.id.id_ly_bottom)
    LinearLayout idLyBottom;

    private int currentIndex;

    private List<ReplyBean> replyList = new ArrayList();
    //suggestList包含了replyList
    private List<ReplyBean> suggestList = new ArrayList();
    private Myadapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_suggestion;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleText.setText(R.string.suggestion);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public void initData() {
        suggestList.clear();
        replyList.clear();
        getOpinions();
    }

    /**
     * 获取回复信息
     */
    private void getReplys() {
        AppOperate.getReply(1, 100, (RxAppCompatActivity) this, new Report() {
            @Override
            public void onSucces(Object o) {
                replyList = (List) o;
                for (int i = 0; i < replyList.size(); i++) {
                    replyList.get(i).setType(ReplyBean.Type.INCOMING);
                }
                //找到reply应该呆的位置！！！,指定位置添加
                for (int j = suggestList.size() - 1; j >= 0; j--) {
                    for (int i = 0; i < replyList.size(); i++) {
                        if (replyList.get(i).getPid() == suggestList.get(j).getId()) {
                            suggestList.add(j, replyList.get(i));
                        }
                    }
                }
                Collections.reverse(suggestList);
                adapter = new Myadapter();
                suggestLv.setAdapter(adapter);
            }

            @Override
            public void onError(Object o) {
                TLog.log(TAG, "rerror---" + o.toString());
            }
        });
    }

    /**
     * 获取意见建议
     */
    private void getOpinions() {
        AppOperate.getOpinions(1, 100, (RxAppCompatActivity) this, new Report() {
            @Override
            public void onSucces(Object o) {
                suggestList = (List) o;
                for (int i = suggestList.size() - 1; i >= 0; i--) {
                    suggestList.get(i).setType(ReplyBean.Type.OUTCOMING);
                }
                //意见获取完毕之后获取回复
                getReplys();
            }

            @Override
            public void onError(Object o) {
                TLog.log(TAG, "serror---" + o.toString());
            }
        });
    }

    @Override
    public void initListener() {

    }


    @OnClick({R.id.back, R.id.send_msg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.send_msg:
                String content = idInputMsg.getText().toString().trim();
                if (content.length() == 0) {
                    GouUtils.showTip(SuggestionActivity.this, "内容不能为空！");
                    return;
                }
                sendOpinnion(content);
                break;
        }
    }


    /**
     * listview 的 adapter
     */

    class Myadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return suggestList.size();
        }

        @Override
        public Object getItem(int position) {
            return suggestList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            ReplyBean chatMessage = suggestList.get(position);
            if (chatMessage.getType() == ReplyBean.Type.INCOMING) {
                return 0;// 回复
            }
            return 1;// 建议
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ReplyBean message = suggestList.get(position);
            toHolder holder = null;
            if (convertView == null) {
                // 接收到回复
                if (getItemViewType(position) == 0) {
                    holder = new toHolder();
                    convertView = View.inflate(SuggestionActivity.this,
                            R.layout.answer_listview_item, null);
                    holder.msg = (TextView) convertView.findViewById(R.id.answer_msg);
                    holder.date = (TextView) convertView
                            .findViewById(R.id.answer_date);
                    //接收到意见建议
                } else {
                    holder = new toHolder();
                    convertView = View.inflate(SuggestionActivity.this,
                            R.layout.question_listview_item, null);
                    holder.msg = (TextView) convertView.findViewById(R.id.msg);
                    holder.date = (TextView) convertView
                            .findViewById(R.id.date);
                    holder.del = (ImageButton) convertView
                            .findViewById(R.id.del);
                }
                convertView.setTag(holder);
            } else {
                holder = (toHolder) convertView.getTag();
            }
            //设置回复的数据
            if (getItemViewType(position) == 0) {
                holder.msg.setText(message.getContent());
                holder.date.setText(message.getCreateTime());
            }
            //设置意见建议的数据
            else {
                holder.msg.setText(message.getContent());
                holder.date.setText(message.getCreateTime());
                holder.del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int questionId = message.getId();
                        currentIndex = position;
                        deleteQuestion(questionId);
                    }
                });
            }
            //把复用的总集合返回
            return convertView;
        }
    }

    class toHolder {
        TextView msg;
        TextView date;
        ImageButton del;
    }

    /**
     * 发布问题
     *
     * @param content
     */
    private void sendOpinnion(final String content) {
        AppOperate.sendOpinnion(content, (RxAppCompatActivity) this, new Report() {
            @Override
            public void onSucces(Object o) {
                idInputMsg.setText("");
                SendMessageBean sendMessage = (SendMessageBean) o;
                ReplyBean bean = new ReplyBean();
                TLog.log(TAG, "id----" + sendMessage.getFeedBackID());
                bean.setId(sendMessage.getFeedBackID());
                bean.setType(ReplyBean.Type.OUTCOMING);
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                bean.setCreateTime(dateFormat.format(new Date())
                        .toString());
                bean.setContent(content);
                suggestList.add(bean);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Object o) {
                if (o != null) {
                    TLog.log(TAG, "失败返回数据：" + o.toString());
                } else {
                    TLog.log(TAG, "请检查网络状态！");
                    Toast.makeText(SuggestionActivity.this,
                            "请检查网络状态是否可用！", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    /**
     * 删除问题
     *
     * @param questionId
     */
    private void deleteQuestion(final int questionId) {
        AppOperate.deleteQuestion(questionId, this, new Report() {
            @Override
            public void onSucces(Object o) {
                suggestList.remove(currentIndex);
                for (int i = 0; i < suggestList.size(); i++) {
                    ReplyBean msg = suggestList.get(i);
                    if (msg.getPid() == questionId) {
                        suggestList.remove(msg);
                    }
                }
                TLog.log(TAG, "后时代数据---" + suggestList.size());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Object o) {

            }
        });
    }
}
