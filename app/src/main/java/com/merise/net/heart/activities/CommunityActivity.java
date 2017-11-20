package com.merise.net.heart.activities;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.merise.net.heart.R;
import com.merise.net.heart.adapter.GridviewAdapter;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.bean.Article;
import com.merise.net.heart.bean.CommentsBean;
import com.merise.net.heart.bean.DZUser;
import com.merise.net.heart.bean.ImgsBean;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.CommentFun;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.utils.ITools;
import com.merise.net.heart.utils.SnackbarUtil;
import com.merise.net.heart.view.CustomGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import me.iwf.photopicker.PhotoPreview;

/**
 * Created by Administrator on 2016/10/10.
 */
public class CommunityActivity extends BaseActivity implements XRecyclerView.LoadingListener {
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right)
    Button right;
    @BindView(R.id.recyclerView)
    XRecyclerView recyclerView;
    @BindView(R.id.root)
    RelativeLayout root;
    @BindView(R.id.emptyView)
    TextView emptyView;
//    @BindView(R.id.swipRefreshLayout)
//    SwipeRefreshLayout swipRefreshLayout;
//    String[] imageUrl = new String[]{"http://img.taopic.com/uploads/allimg/110914/8879-11091422541844.jpg", "http://img5.imgtn.bdimg.com/it/u=4246510199,2069483326&fm=21&gp=0.jpg", "http://img4.imgtn.bdimg.com/it/u=3123432318,2547934550&fm=21&gp=0.jpg", "http://img0.imgtn.bdimg.com/it/u=963551012,3660149984&fm=21&gp=0.jpg"};

    private int userID;
    private MyRecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Article> mDatas;

    private static final int REQUESTCODE = 0x02;
    private static final int REFRESH = 0x00;
    private static final int LOADMORE = 0x01;

    private static final int defaultRows = 10;
    private int defaultPage = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_community;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        int left = (int) getResources().getDimension(R.dimen.title_text_padding_left);
        right.setPadding(left, 0, 0, 0);
        titleText.setText(R.string.neighbor_conversation);
        right.setVisibility(View.VISIBLE);
        right.setText(R.string.issue);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mDatas = new ArrayList<>();
        if (app.userInfo != null)
            userID = app.userInfo.getId();
//        swipRefreshLayout.setOnRefreshListener(this);
        recyclerView.setLoadingListener(this);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallGridPulse);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.SemiCircleSpin);
        recyclerView.setEmptyView(emptyView);
//        emptyView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getCommunityList(defailtPage,defaultRows,LOADMORE);
//            }
//        });
    }

    @Override
    public void initData() {
        getCommunityList(defaultPage, defaultRows, REFRESH);
    }


    @OnClick({R.id.back, R.id.right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.right:
                gotoActivityForResult(PublishActivity.class, REQUESTCODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TLog.log(TAG, "onActivityResult");
        if (requestCode == REQUESTCODE && resultCode == RESULT_OK) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    getCommunityList(defaultPage, defaultRows, REFRESH);
                }
            });
        }
    }

    /**
     * 新增交流评论
     *
     * @param articleID
     * @param content
     * @param position
     */
    private void insertComment(int articleID, final String content, final int position) {
        AppOperate.insertComment(articleID, content, this, new Report() {
            @Override
            public void onSucces(Object o) {
                CommentsBean comment = new CommentsBean();
                comment.setName(app.userInfo.getName());
                comment.setComment(content);
                List<CommentsBean> commentsBeanList;
                commentsBeanList = mDatas.get(position).getComments();
                if (commentsBeanList == null) {
                    commentsBeanList = new ArrayList<>();
                }
                commentsBeanList.add(comment);
                mDatas.get(position).setComments(commentsBeanList);
                adapter.notifyItemChanged(position + 1);
                String message = getResources().getString(R.string.commment_success);
                SnackbarUtil.LongSnackbar(root, message, SnackbarUtil.Confirm).show();
            }

            @Override
            public void onError(Object o) {
                SnackbarUtil.LongSnackbar(root, String.valueOf(o), SnackbarUtil.Confirm).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        getCommunityList(1, defaultRows, REFRESH);
    }

    @Override
    public void onLoadMore() {
        getCommunityList(++defaultPage, defaultRows, LOADMORE);
    }


    private class MyRecyclerViewAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TLog.log(TAG, "onCreateViewHolder" + viewType);
            View view = View.inflate(CommunityActivity.this, R.layout.item_community, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            TLog.log(TAG, "onBindViewHolder--position:" + position);
            final ViewHolder viewHolder = (ViewHolder) holder;
//            holder.setIsRecyclable(false);   //不可复用
            String url = mDatas.get(position).getFaceImg();
            url = url.replaceAll("\\\\", "/");
            Glide.with(getBaseContext())
                    .load(Constant.TestUrl + url)
                    .placeholder(R.drawable.default_error)
                    .bitmapTransform(new CropCircleTransformation(getBaseContext()))
                    .into(viewHolder.headImg);
            Glide.with(getBaseContext())
                    .load(Constant.TestUrl + url)
                    .asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                }
            });
            viewHolder.date.setText(mDatas.get(position).getCreateTime());
            viewHolder.content.setText(mDatas.get(position).getContent());


            final List<ImgsBean> images = mDatas.get(position).getImgs();

            List<String> urlList = new ArrayList<>();
            if (images != null) {
                for (ImgsBean img : images) {
                    urlList.add(img.getUrl());
                }
            }
//            if (images != null) {
            viewHolder.gridView.setNumColumns(urlList.size() >= 3 ? 3 : urlList.size());
            viewHolder.gridView.setAdapter(new GridviewAdapter(urlList, CommunityActivity.this, false));
//            } else {
//                viewHolder.gridView.setAdapter(null);
//            }

            if (mDatas.get(position).getType() == 1) {
                viewHolder.dosthLl.setVisibility(View.GONE);
            } else {
                viewHolder.dosthLl.setVisibility(View.VISIBLE);
                viewHolder.dateDetail.setText(mDatas.get(position).getTime());
                viewHolder.destinationDetail.setText(mDatas.get(position).getPlace());
            }
//            final String dzNames = mDatas.get(position).getDzNames();
//            if (dzNames != null && dzNames.length() > 0) {
//                viewHolder.dzNames.setVisibility(View.VISIBLE);
//                String str = getResources().getString(R.string.sb_zan);
//                viewHolder.dzNames.setText(dzNames + str);
//            } else {
//                viewHolder.dzNames.setVisibility(View.GONE);
//            }
            boolean isZan = false;
            final List<DZUser> dzUsers = mDatas.get(position).getDzUsers();
            if (dzUsers != null && dzUsers.size() > 0) {
                viewHolder.dzNames.setVisibility(View.VISIBLE);
                String dzNameStr = "";
                for (DZUser dzUser : dzUsers) {
                    dzNameStr += dzUser.getDzName();
                    if (app.userInfo != null)
                        if (dzUser.getDzID() == app.userInfo.getId()) {
                            isZan = true;
                        }
                }
                String str = getResources().getString(R.string.sb_zan);
                viewHolder.dzNames.setText(dzNameStr + str);
            } else {
                viewHolder.dzNames.setVisibility(View.GONE);
            }
            if (isZan) {
                viewHolder.zan.setImageResource(R.drawable.zan_s);
            } else {
                viewHolder.zan.setImageResource(R.drawable.zan);
            }

            String scan = getResources().getString(R.string.scan);
            String times = getResources().getString(R.string.times);
            viewHolder.scanTimes.setText(scan + mDatas.get(position).getHot() + times);
            List<CommentsBean> comments = mDatas.get(position).getComments();
            viewHolder.commentContent.removeAllViews();
            if (comments != null) {
                for (CommentsBean comment : comments) {
                    TextView tv = new TextView(getBaseContext());
                    tv.setTextColor(Color.BLACK);
//                    tv.setBackgroundColor(Color.RED);
                    String message = "<a href=\"javascript:void(0)\">" + comment.getName() + ":</a>" + comment.getComment();
                    tv.setText(Html.fromHtml(message));
                    viewHolder.commentContent.addView(tv);
                }
            }
            final boolean finalIsZan = isZan;
            viewHolder.zan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TLog.log(TAG, "点击size:" + position);
                    if (!app.isLogin) {
                        ITools.showLoginAlert(CommunityActivity.this);
                        return;
                    }
                    if (!finalIsZan) {
                        AppOperate.insertLaud(mDatas.get(position).getParentID(), CommunityActivity.this, new Report() {
                            @Override
                            public void onSucces(Object o) {
//                                String dzNames = mDatas.get(position).getDzNames();
//                                if (dzNames == null || dzNames.length() == 0) {
//                                    mDatas.get(position).setDzNames(app.userInfo.getName());
//                                } else {
//                                    mDatas.get(position).setDzNames(app.userInfo.getName() + "," + dzNames);
//                                }
                                List<DZUser> dzUserList = mDatas.get(position).getDzUsers();
                                if (dzUserList == null) {
                                    dzUserList = new ArrayList<>();
                                }
                                DZUser dzUser = new DZUser();
                                dzUser.setDzID(app.userInfo.getId());
                                dzUser.setDzName(app.userInfo.getName());
                                dzUserList.add(dzUser);
                                mDatas.get(position).setDzUsers(dzUserList);
                                adapter.notifyItemChanged(position + 1);
                                String message = getResources().getString(R.string.zan_success);
//                                String dzIDs = mDatas.get(position).getDzIDs();
//                                if (dzIDs == null || dzIDs.length() == 0) {
//                                    mDatas.get(position).setDzIDs(String.valueOf(userID));
//                                } else {
//                                    mDatas.get(position).setDzIDs(userID + "," + dzIDs);
//                                }
//                                mDatas.get(position).setDzIDs(dzIDs);
                                SnackbarUtil.LongSnackbar(root, message, SnackbarUtil.Confirm).show();
                            }

                            @Override
                            public void onError(Object o) {
                                SnackbarUtil.LongSnackbar(root, String.valueOf(o), SnackbarUtil.Alert).show();
                            }
                        });
                    } else {
                        AppOperate.deleteLaud(mDatas.get(position).getId(), CommunityActivity.this, new Report() {
                            @Override
                            public void onSucces(Object o) {
//                                String dzNames = mDatas.get(position).getDzNames();
//                                if (dzNames != null) {
//                                    String newDzNames = ITools.getNewString(dzNames, app.userInfo.getName());
//                                    mDatas.get(position).setDzNames(newDzNames);
//                                }
//                                String oldDzIDs = mDatas.get(position).getDzIDs();
//                                if (oldDzIDs != null) {
//                                    String newDzIds = ITools.getNewString(oldDzIDs, String.valueOf(userID));
//                                    mDatas.get(position).setDzIDs(newDzIds);
//                                }
                                List<DZUser> dzUserList = mDatas.get(position).getDzUsers();
                                TLog.log(TAG, "size：" + dzUserList.size());
                                for (DZUser dzUser : dzUserList) {
                                    if (dzUser.getDzID() == app.userInfo.getId()) {
                                        dzUserList.remove(dzUser);
                                    }
                                }
                                TLog.log(TAG, "size：" + dzUserList.size());
                                mDatas.get(position).setDzUsers(dzUserList);
                                adapter.notifyItemChanged(position + 1);
                                String message = getResources().getString(R.string.cancel_zan_success);
                                SnackbarUtil.LongSnackbar(root, message, SnackbarUtil.Confirm).show();
                            }

                            @Override
                            public void onError(Object o) {
                                SnackbarUtil.LongSnackbar(root, String.valueOf(o), SnackbarUtil.Alert).show();
                            }
                        });
                    }
                }
            });
            viewHolder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TLog.log(TAG, position + "");
//                    TLog.log(TAG, pictureUrl.get(position));
                    ArrayList<String> pictureUrl = new ArrayList<>();
                    if (images != null) {
                        for (ImgsBean imgsBean : images) {
                            pictureUrl.add(Constant.TestUrl + "/" + imgsBean.getUrl());
                        }
                    }
                    PhotoPreview.builder()
                            .setPhotos(pictureUrl)
                            .setCurrentItem(position)
                            .setShowDeleteButton(false)
                            .start(CommunityActivity.this);
                }
            });
            viewHolder.comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!app.isLogin) {
                        ITools.showLoginAlert(CommunityActivity.this);
                        return;
                    }
                    CommentFun.inputComment(CommunityActivity.this, recyclerView, v, new CommentFun.InputCommentListener() {
                        @Override
                        public void onCommitComment(String content) {
                            insertComment(mDatas.get(position).getId(), content, position);
                        }
                    });
                }
            });
            viewHolder.headImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TLog.log(TAG, position + "++++++++++++++");
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constant.AUTHUSER, mDatas.get(position).getAuthor());
                    bundle.putString(Constant.HEADIMGURL, mDatas.get(position).getFaceImg());
                    gotoActivity(SingleUserDetailActivity.class, false, bundle);
                }
            });
            viewHolder.mainRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TLog.log(TAG, position + "");
                }
            });
        }

        @Override
        public int getItemViewType(int position) {
            TLog.log(TAG, "getItemViewType--position:" + position);
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH:
                    defaultPage = 1;
                    if (adapter == null) {
                        adapter = new MyRecyclerViewAdapter();
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                        recyclerView.refreshComplete();
                    }
                    break;
                case LOADMORE:
                    adapter.notifyDataSetChanged();
                    recyclerView.loadMoreComplete();
                    break;
            }
        }
    };


    private void getCommunityList(int page, int rows, final int what) {
        AppOperate.getCommunityList(page, rows, this, new Report() {
            @Override
            public void onSucces(Object o) {
                if (what == REFRESH) {
                    mDatas.clear();
                    mDatas.addAll((List<Article>) o);
                } else {
                    List<Article> articles = (List<Article>) o;
                    if (articles.size() == 0) {
                        recyclerView.setNoMore(true);
                        adapter.notifyDataSetChanged();
                        return;
                    }
                    /**
                     * 判断最新加载的数据当中是否存在列表中已经存在的数据，若存在就不需在向数据集里面添加了
                     */
                    List<Integer> ids = new ArrayList<>();
                    for (Article article : mDatas) {
                        ids.add(article.getId());
                    }
                    for (Article article : articles) {
                        if (!ids.contains(article.getId())) {
                            mDatas.add(article);
                        }
                    }
                }
                handler.sendEmptyMessage(what);
            }

            @Override
            public void onError(Object o) {
                recyclerView.reset();
                SnackbarUtil.LongSnackbar(root, (String) o, SnackbarUtil.Alert).show();
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.headImg)
        ImageView headImg;
        @BindView(R.id.nickName)
        TextView nickName;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.mainRl)
        RelativeLayout mainRl;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.gridView)
        CustomGridView gridView;
        @BindView(R.id.dateTitle)
        TextView dateTitle;
        @BindView(R.id.dateDetail)
        TextView dateDetail;
        @BindView(R.id.destinationTitle)
        TextView destinationTitle;
        @BindView(R.id.destinationDetail)
        TextView destinationDetail;
        @BindView(R.id.dosthLl)
        LinearLayout dosthLl;
        @BindView(R.id.scanTimes)
        TextView scanTimes;
        @BindView(R.id.zan)
        ImageButton zan;
        @BindView(R.id.dzNames)
        TextView dzNames;
        @BindView(R.id.comment)
        ImageButton comment;
        @BindView(R.id.commentContent)
        LinearLayout commentContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
