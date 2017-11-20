package com.merise.net.heart.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.bumptech.glide.Glide;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.merise.net.heart.R;
import com.merise.net.heart.adapter.GridviewAdapter;
import com.merise.net.heart.api.util.MultipartBody;
import com.merise.net.heart.application.XYApplication;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.bean.Article;
import com.merise.net.heart.bean.CommentsBean;
import com.merise.net.heart.bean.DZUser;
import com.merise.net.heart.bean.FoucsInfo;
import com.merise.net.heart.bean.ImgsBean;
import com.merise.net.heart.listener.OnItemClickListener;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.CommentFun;
import com.merise.net.heart.utils.Const;
import com.merise.net.heart.utils.Constant;
import com.merise.net.heart.utils.ITools;
import com.merise.net.heart.utils.ImageTools;
import com.merise.net.heart.utils.SnackbarUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 作者:xiangyang
 * 日期:2016/10/13
 */
public class SingleUserDetailActivity extends BaseActivity implements OnItemClickListener, XRecyclerView.LoadingListener {

    @BindView(R.id.recyclerView)
    XRecyclerView recyclerView;
    //    @BindView(R.id.swipRefreshLayout)
//    SwipeRefreshLayout swipRefreshLayout;
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.followTV)
    TextView followTV;
    @BindView(R.id.titleRL)
    RelativeLayout titleRL;
    @BindView(R.id.root)
    RelativeLayout root;
    @BindView(R.id.contentFL)
    FrameLayout contentFL;
    private MyRecyclerViewAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<Article> mDatas;
    private static final int REFRESH = 0x00;
    private static final int LOADMORE = 0x01;
    private static final int HEADIMG = 0x02;
    private static final int UPHEADIMG = 0x03;

    private int userID;
    private int autherID;
    private String mobileNum;
    private String defaultSign;
    private String headImgUrl;
    private ArrayList<String> selectedPhoto = new ArrayList<>();
    private List<String> uploadFiles = new ArrayList<>();
    private ViewHolderTop holderTop;
    private String nickName;
    private String sign;
    private FoucsInfo foucsInfo;
    private int defaultPage = 1;
    private static final int defaultRows = 20;
    private String netNickName = null;
    private String netSign = null;
    private int netFans;
    private int netFocus;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_single_user;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        mDatas = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        TLog.log(TAG, "执行个人信息");
        if (app.userInfo != null)
        userID = app.userInfo.getId();
        mobileNum = app.userInfo.getMobile();
        netNickName = mobileNum;
        autherID = getIntent().getIntExtra(Constant.AUTHUSER, -1);
        headImgUrl = getIntent().getStringExtra(Constant.HEADIMGURL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ITools.getActionBarHeight(getBaseContext()));
            params.topMargin = ITools.getStatusHeight(getBaseContext());
            titleRL.setLayoutParams(params);
        }

        recyclerView.setLoadingListener(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(-1)) {
                    TLog.log(TAG, "已经到顶了");
                    titleRL.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition() > 1) {
                    if (newState == recyclerView.SCROLL_STATE_DRAGGING) {
                        titleRL.setVisibility(View.GONE);
                    }
                    if (newState == recyclerView.SCROLL_STATE_SETTLING) {
                        titleRL.setVisibility(View.GONE);
                    }
                    if (newState == recyclerView.SCROLL_STATE_IDLE) {
                        titleRL.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public void initData() {
        getOneUserArticleList(1, defaultRows, autherID, REFRESH);
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
                        adapter.setOnItemClickListener(SingleUserDetailActivity.this);
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
                case HEADIMG:
                    String path = msg.getData().getString("headimg");
                    Glide.with(SingleUserDetailActivity.this)
                            .load(path)
                            .placeholder(R.drawable.ic720)
                            .error(R.drawable.ic720)
                            .centerCrop()
                            .crossFade()
                            .bitmapTransform(new CropCircleTransformation(SingleUserDetailActivity.this))
                            .into(holderTop.headImg);
                    break;
                case UPHEADIMG:
                    //上传头像文件到服务器
                    TLog.log(TAG, "开始上传");
                    String headPath = msg.getData().getString(Const.UPHEADIMG);
                    MultipartBody multipartBody = filesToMultipartBody(headPath);
                    upHeadImage(multipartBody);
                    break;

            }
        }
    };


    /**
     * 获取个人数据
     *
     * @param page
     * @param rows
     * @param userID
     * @param what
     */
    private void getOneUserArticleList(int page, int rows, int userID, final int what) {
        AppOperate.getOneUserArticleList(page, rows, userID, this, new Report() {
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

    /**
     * 获取个人社区交流数据
     *
     * @param userID
     */
    private void getOneUserInfo(int userID) {
        AppOperate.getOneUserInfo(userID, this, new Report() {
            @Override
            public void onSucces(Object o) {
                TLog.log(TAG, "getOneUserInfo成功" + o);
                foucsInfo = (FoucsInfo) o;
                sign = holderTop.feeling.getText().toString().trim();
                netNickName = foucsInfo.getNickName();
                netSign = foucsInfo.getSign();
                spt.saveSharedPreferences(Const.SIGN, netSign);
                spt.saveSharedPreferences(Const.NICKNAME, netNickName);
                netFans = foucsInfo.getFans();
                netFocus = foucsInfo.getFocus();
                XYApplication.foucsInfo = foucsInfo;
                adapter.notifyItemChanged(1);
//                adapter.notifyItemChanged(0);
            }

            @Override
            public void onError(Object o) {

            }
        });
    }

    /*
    接受到选择的照片信息链接
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TLog.log(TAG, "onActivityResult。。。");
        if (resultCode == RESULT_OK && (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                //先保存本地
                compressAndSave2up(photos);
//
            }
        }
    }

    /*
    保存图片然后上传
     */
    private void compressAndSave2up(List<String> files) {
        if (uploadFiles != null) {
            uploadFiles.clear();
        }
        for (String filePath : files) {
            Bitmap bitmap = ImageTools.getImage(filePath, 100, 100, 200);
            String headPath = Environment.getExternalStorageDirectory().getPath()
                    + "/heartCache/cache/" + getName() + ".png";
            TLog.log(TAG, "path-----" + headPath);
            ImageTools.savePhotoToSDCard(bitmap, headPath);
            spt.saveSharedPreferences(Const.NETHEADPATH, headPath);
            uploadFiles.add(headPath);
        }
        //保存好了之后上传
        Message headMessage = new Message();
        Bundle bundle = new Bundle();
        bundle.putString(Const.UPHEADIMG, uploadFiles.get(0));
        headMessage.setData(bundle);
        headMessage.what = UPHEADIMG;
        handler.sendMessage(headMessage);

    }

    /**
     * 上传头像的方法
     *
     * @return
     */
    private void upHeadImage(MultipartBody multipartBody) {
        AppOperate.upHeadImage(multipartBody, this, new Report() {
            @Override
            public void onSucces(Object o) {
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("headimg", uploadFiles.get(0));
                message.setData(bundle);
                message.what = HEADIMG;
                handler.sendMessage(message);
                SnackbarUtil.LongSnackbar(root, "头像修改成功", SnackbarUtil.Confirm).show();
            }

            @Override
            public void onError(Object o) {
                String message = (String) o;
                SnackbarUtil.LongSnackbar(root, message, SnackbarUtil.Confirm).show();
            }
        });
    }

    /**
     * 上传昵称
     *
     * @return
     */
    private void updateUserNickName(final String nickName) {
        TLog.log(TAG, "开始上传");
        AppOperate.updateUserNickName(nickName, this, new Report() {
            @Override
            public void onSucces(Object o) {
                SnackbarUtil.LongSnackbar(root, "昵称修改成功", SnackbarUtil.Confirm).show();
                holderTop.username.setEnabled(false);
                spt.saveSharedPreferences(Const.NICKNAME, nickName);
            }

            @Override
            public void onError(Object o) {
                String message = (String) o;
                SnackbarUtil.LongSnackbar(root, message, SnackbarUtil.Confirm).show();
            }
        });
    }

    /**
     * 上传签名
     *
     * @return
     */
    private void updateUserSign(final String sign) {
        AppOperate.updateUserSign(sign, this, new Report() {
            @Override
            public void onSucces(Object o) {
                SnackbarUtil.LongSnackbar(root, "签名修改成功", SnackbarUtil.Confirm).show();
                holderTop.feeling.setEnabled(false);
                spt.saveSharedPreferences(Const.SIGN, sign);
            }

            @Override
            public void onError(Object o) {
                String message = (String) o;
                SnackbarUtil.LongSnackbar(root, message, SnackbarUtil.Confirm).show();
            }
        });
    }


    /*
       构造请求体
     */
    public static MultipartBody filesToMultipartBody(String filePaths) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        TLog.log(TAG, "构造请求体");
        File file = new File(filePaths);
        // 根据类型及File对象创建RequestBody（okhttp的类）
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
        builder.addFormDataPart("file", file.getName(), requestBody);
        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();
        return multipartBody;
    }

    /**
     * 去一个随机8位的名字
     *
     * @return
     */
    private String getName() {
        StringBuffer str = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            str.append(random.nextInt(10));
        }
        return String.valueOf(str);
    }

    @Override
    public void onItemClick(View v, int position) {
        TLog.log(TAG, "postion:" + position);
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.headImg:
                TLog.log(TAG, "modify_headImg");
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setSelected(selectedPhoto)
                        .start(SingleUserDetailActivity.this, PhotoPicker.REQUEST_CODE);
                break;
            case R.id.modify_name:
                holderTop.username.setEnabled(true);
                holderTop.username.requestFocus();
                holderTop.username.setSelection(holderTop.username.getText().length());
                break;
            case R.id.modify_feeling:
                holderTop.feeling.setEnabled(true);
                holderTop.feeling.requestFocus();
                holderTop.feeling.setSelection(holderTop.feeling.getText().length());
                break;
            case R.id.followTV:
                TLog.log(TAG, "关注");
                if (foucsInfo.getIsFocus().equals("1")) {
                    insertFocus(autherID, 1, position);
                } else if (foucsInfo.getIsFocus().equals("0")) {
                    cancelFocus(autherID, 1, position);
                }
                break;
            case R.id.follow_num:
                TLog.log(TAG, "关注量");
                startActivity(new Intent(this,FansListActivity.class));
                break;
            case R.id.fans_num:
                TLog.log(TAG, "粉丝数量");
                break;
        }
    }

    /**
     * 添加关注
     *
     * @param focuseID
     * @param type
     * @param position
     */
    private void insertFocus(int focuseID, int type, final int position) {
        AppOperate.insertFocus(focuseID, type, this, new Report() {
            @Override
            public void onSucces(Object o) {
                foucsInfo.setIsFocus("0");
                foucsInfo.setFocus(foucsInfo.getFocus() + 1);
                adapter.notifyItemChanged(position);
                followTV.setText(R.string.followed);
                String message = getResources().getString(R.string.follow_success);
                SnackbarUtil.LongSnackbar(root, message, SnackbarUtil.Confirm).show();
            }

            @Override
            public void onError(Object o) {
                SnackbarUtil.LongSnackbar(root, String.valueOf(o), SnackbarUtil.Confirm).show();
            }
        });
    }


    /**
     * 获取粉丝列表
     */
    public void getFansList() {
        AppOperate.getFansList(this, new Report() {
            @Override
            public void onSucces(Object o) {
                TLog.log(TAG, "获取粉丝列表成功" + o.toString());
            }

            @Override
            public void onError(Object o) {
                TLog.log(TAG, "获取粉丝列表失败" + o.toString());
            }
        });
    }


    /**
     * 获取关注列表
     */
    public void getFocusUserList() {
        AppOperate.getFocusUserList(this, new Report() {
            @Override
            public void onSucces(Object o) {
                TLog.log(TAG, "获取关注列表成功" + o.toString());
            }

            @Override
            public void onError(Object o) {
                TLog.log(TAG, "获取关注列表失败" + o.toString());
            }
        });
    }


    /**
     * 取消关注
     *
     * @param focuseID
     * @param type
     * @param position
     */
    private void cancelFocus(int focuseID, int type, final int position) {
        AppOperate.cancelFocus(focuseID, type, this, new Report() {
            @Override
            public void onSucces(Object o) {
                foucsInfo.setIsFocus("1");
                foucsInfo.setFocus(foucsInfo.getFocus() - 1);
                adapter.notifyItemChanged(position);
                followTV.setText(R.string.follow_plus);
                String message = getResources().getString(R.string.cancel_follow);
                SnackbarUtil.LongSnackbar(root, message, SnackbarUtil.Confirm).show();
            }

            @Override
            public void onError(Object o) {
                SnackbarUtil.LongSnackbar(root, String.valueOf(o), SnackbarUtil.Confirm).show();
            }
        });
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
        getOneUserArticleList(1, defaultRows, autherID, REFRESH);
    }

    @Override
    public void onLoadMore() {
        getOneUserArticleList(++defaultPage, defaultRows, autherID, LOADMORE);
    }


    @OnClick({R.id.back, R.id.followTV})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.followTV:
                if (foucsInfo.getIsFocus().equals("1")) {
                    insertFocus(autherID, 1, 1);
                } else if (foucsInfo.getIsFocus().equals("0")) {
                    cancelFocus(autherID, 1, 1);
                }
                break;
        }
    }

    private class MyRecyclerViewAdapter extends RecyclerView.Adapter {
        private OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return 0;
            }
            return 1;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            TLog.log(TAG, "onCreateViewHolder");
            if (viewType == 0) {
                View view = View.inflate(SingleUserDetailActivity.this, R.layout.item_top_single_user, null);
                holderTop = new ViewHolderTop(view);
                ViewHolderTop holderTop = new ViewHolderTop(view);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ITools.getActionBarHeight(getBaseContext()));
                    params.topMargin = ITools.getStatusHeight(getBaseContext());
                    view.findViewById(R.id.titleRL).setLayoutParams(params);
                }
                return holderTop;
            } else {
                View view = View.inflate(SingleUserDetailActivity.this, R.layout.item_community, null);
                CommunityActivity.ViewHolder viewHolder = new CommunityActivity.ViewHolder(view);
                //头像默认加载
                Glide.with(SingleUserDetailActivity.this)
                        .load(spt.readSharedPreferencesString(Const.NETHEADPATH))
                        .placeholder(R.drawable.ic720)
                        .error(R.drawable.ic720)
                        .centerCrop()
                        .crossFade()
                        .bitmapTransform(new CropCircleTransformation(SingleUserDetailActivity.this))
                        .into(holderTop.headImg);

                return viewHolder;
            }
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
//            TLog.log(TAG, "onBindViewHolder");
            if (position == 0) {
                final ViewHolderTop holderTop = ((ViewHolderTop) holder);
                holderTop.setOnItemClickListener(onItemClickListener);
                headImgUrl = headImgUrl.replaceAll("\\\\", "/");
                String fansNum = getResources().getString(R.string.fans_num);
                String followNum = getResources().getString(R.string.follow_num);
                if (foucsInfo != null) {
                    holderTop.fansNum.setText(fansNum + foucsInfo.getFans());
                    holderTop.followNum.setText(followNum + foucsInfo.getFocus());
                    TLog.log(TAG, "focus:" + foucsInfo.getFocus());
                    if (foucsInfo.getIsFocus().equals("1")) {
                        holderTop.followTV.setText(R.string.follow_plus);
                        followTV.setText(R.string.follow_plus);
                    } else if (foucsInfo.getIsFocus().equals("0")) {
                        holderTop.followTV.setText(R.string.followed);
                        followTV.setText(R.string.followed);
                    }
                    //// TODO: 2016/12/12 0012 修改昵称心情周边
                    holderTop.username.setText(spt.readSharedPreferencesString(Const.NICKNAME));
                    holderTop.feeling.setText(spt.readSharedPreferencesString(Const.SIGN));

                } else {
                    getOneUserInfo(autherID);
                    holderTop.fansNum.setText(fansNum);
                    holderTop.followNum.setText(followNum);
                }
                //为position(0)设置监听,实现edittext的外部点击失去焦点功能
                holderTop.forward.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        holderTop.feeling.setEnabled(false);
                        holderTop.forward.setFocusable(true);
                        holderTop.forward.setFocusableInTouchMode(true);
                        //如果触摸外部时，edittext编辑框有焦点就上传
                        TLog.log(TAG, "是否有焦点" + holderTop.username.isFocused());
                        if (holderTop.username.isFocused()) {
                            nickName = holderTop.username.getText().toString().trim();
                            updateUserNickName(nickName);
//                            holderTop.username.setFocusable(false);
                        } else if (holderTop.feeling.isFocused()) {
                            sign = holderTop.feeling.getText().toString().trim();
                            updateUserSign(sign);
//                            holderTop.feeling.setFocusable(false);
                        }
                        holderTop.forward.requestFocus();
                        // 同时关闭软键盘
                        WindowManager.LayoutParams parms = getWindow().getAttributes();
                        TLog.log(TAG, "event---" + event.getAction() + "===== " + "softinputmode---" + parms.softInputMode);
                        if (event.getAction() == MotionEvent.ACTION_UP
                                && parms.softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                            imm.hideSoftInputFromWindow(holderTop.username.getWindowToken(), 0);
                            imm.hideSoftInputFromWindow(holderTop.feeling.getWindowToken(), 0);
                        }
                        return false;
                    }
                });
            }
            /*
            -------------------------------------------------------------------------------------------------------------
             */
            if (position > 0) {
                final CommunityActivity.ViewHolder viewHolder = (CommunityActivity.ViewHolder) holder;
                viewHolder.headImg.setVisibility(View.GONE);
                String url = mDatas.get(position).getFaceImg();
                url = url.replaceAll("\\\\", "/");
                Glide.with(getBaseContext())
                        .load(Constant.TestUrl + url)
                        .placeholder(R.drawable.default_error)
                        .bitmapTransform(new CropCircleTransformation(getBaseContext()))
                        .into(viewHolder.headImg);
                viewHolder.date.setText(mDatas.get(position).getCreateTime());
                viewHolder.content.setText(mDatas.get(position).getContent());

                final List<ImgsBean> images = mDatas.get(position).getImgs();
                List<String> urlList = new ArrayList<>();
                if (images != null) {
                    for (ImgsBean img : images) {
                        urlList.add(img.getUrl());
                    }
                }
                viewHolder.gridView.setNumColumns(urlList.size() >= 3 ? 3 : urlList.size());
                viewHolder.gridView.setAdapter(new GridviewAdapter(urlList, SingleUserDetailActivity.this, false));
                if (mDatas.get(position).getType() == 1) {
                    viewHolder.dosthLl.setVisibility(View.GONE);
                } else {
                    viewHolder.dosthLl.setVisibility(View.VISIBLE);
                    viewHolder.dateDetail.setText(mDatas.get(position).getTime());
                    viewHolder.destinationDetail.setText(mDatas.get(position).getPlace());
                }
                boolean isZan = false;
                final List<DZUser> dzUsers = mDatas.get(position).getDzUsers();
                if (dzUsers != null && dzUsers.size() > 0) {
                    viewHolder.dzNames.setVisibility(View.VISIBLE);
                    String dzNameStr = "";
                    for (DZUser dzUser : dzUsers) {
                        dzNameStr += dzUser.getDzName();
                        if (app.userInfo != null) {
                        if (dzUser.getDzID() == app.userInfo.getId()) {
                            isZan = true;
                            }
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
                        String message = "<a href=\"javascript:void(0)\">" + comment.getName() + ":</a>" + comment.getComment();
                        tv.setText(Html.fromHtml(message));
                        viewHolder.commentContent.addView(tv);
                    }
                }

                //点赞按钮点击事件
                final boolean finalIsZan = isZan;
                viewHolder.zan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!finalIsZan) {
                            AppOperate.insertLaud(mDatas.get(position).getId(), SingleUserDetailActivity.this, new Report() {
                                @Override
                                public void onSucces(Object o) {
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
                                    SnackbarUtil.LongSnackbar(root, message, SnackbarUtil.Confirm).show();
                                }

                                @Override
                                public void onError(Object o) {
                                    SnackbarUtil.LongSnackbar(root, String.valueOf(o), SnackbarUtil.Alert).show();
                                }
                            });
                        } else {
                            AppOperate.deleteLaud(mDatas.get(position).getId(), SingleUserDetailActivity.this, new Report() {
                                @Override
                                public void onSucces(Object o) {
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
                //
                viewHolder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TLog.log(TAG, position + "");
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
                                .start(SingleUserDetailActivity.this);
                    }
                });
                viewHolder.comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CommentFun.inputComment(SingleUserDetailActivity.this, recyclerView, v, new CommentFun.InputCommentListener() {
                            @Override
                            public void onCommitComment(String content) {
                                insertComment(mDatas.get(position).getId(), content, position);
                            }
                        });
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }
    }

    public class ViewHolderTop extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.back)
        Button back;
        @BindView(R.id.headImg)
        ImageView headImg;
        @BindView(R.id.username)
        EditText username;
        @BindView(R.id.modify_name)
        ImageButton modifyName;
        @BindView(R.id.follow_num)
        TextView followNum;
        @BindView(R.id.fans_num)
        TextView fansNum;
        @BindView(R.id.feeling)
        EditText feeling;
        @BindView(R.id.modify_feeling)
        ImageButton modifyFeeling;
        @BindView(R.id.forward)
        LinearLayout forward;
        @BindView(R.id.tv)
        TextView tv;
        @BindView(R.id.followTV)
        TextView followTV;
        private OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        //顶部布局返回的view
        public ViewHolderTop(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            back.setOnClickListener(this);
            headImg.setOnClickListener(this);
            modifyName.setOnClickListener(this);
            modifyFeeling.setOnClickListener(this);
            followTV.setOnClickListener(this);
            fansNum.setOnClickListener(this);
            followNum.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
