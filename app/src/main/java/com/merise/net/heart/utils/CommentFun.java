package com.merise.net.heart.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;


/**
 * 评论相关方法
 */
public class CommentFun {
    public static final int KEY_COMMENT_SOURCE_COMMENT_LIST = -200162;

    /**
     * 弹出评论对话框
     */
    public static void inputComment(final Activity activity, final RecyclerView recyclerView,
                                    final View btnComment,
                                    final InputCommentListener listener) {


        String hint = "评论";
        // 获取评论的位置,不要在CommentDialogListener.onShow()中获取，onShow在输入法弹出后才调用，
        // 此时btnComment所属的父布局可能已经被ListView回收
        final int[] coord = new int[2];
        if (recyclerView != null) {
            btnComment.getLocationOnScreen(coord);
        }

        showInputComment(activity, hint, new CommentDialogListener() {
            @Override
            public void onClickPublish(final Dialog dialog, EditText input, final TextView btn) {
                final String content = input.getText().toString();
//                if (content.trim().equals("")) {
//                    Toast.makeText(activity, "评论不能为空", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                btn.setClickable(false);
                if (listener != null) {
                    listener.onCommitComment(content);
                }
                dialog.dismiss();
//                Toast.makeText(activity, "评论成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onShow(int[] inputViewCoordinatesInScreen) {
                if (recyclerView != null) {
                    // 点击某条评论则这条评论刚好在输入框上面，点击评论按钮则输入框刚好挡住按钮
                    int span = btnComment.getId() == R.id.comment ? 0 : btnComment.getHeight();
                    recyclerView.smoothScrollBy(0, coord[1] + span - inputViewCoordinatesInScreen[1]+150);
                }
            }

            @Override
            public void onDismiss() {

            }
        });

    }

    public static class InputCommentListener {
        //　评论成功时调用
        public void onCommitComment(String content) {

        }
    }


    /**
     * 弹出评论对话框
     */
    private static Dialog showInputComment(Activity activity, CharSequence hint, final CommentDialogListener listener) {
        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.view_input_comment);
        dialog.findViewById(R.id.input_comment_dialog_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onDismiss();
                }
            }
        });
        final EditText input = (EditText) dialog.findViewById(R.id.input_comment);
        input.setHint(hint);
        final TextView btn = (TextView) dialog.findViewById(R.id.btn_publish_comment);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (input.getText().toString().trim().length() <= 0) {
                    btn.setEnabled(false);
                } else {
                    btn.setEnabled(true);
                }
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickPublish(dialog, input, btn);
                }
            }
        });
        btn.setEnabled(false);
        dialog.setCancelable(true);
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    int[] coord = new int[2];
                    dialog.findViewById(R.id.input_comment_container).getLocationOnScreen(coord);
                    // 传入 输入框距离屏幕顶部（不包括状态栏）的长度
                    listener.onShow(coord);
                }
            }
        }, 300);
        return dialog;
    }

    public interface CommentDialogListener {
        void onClickPublish(Dialog dialog, EditText input, TextView btn);

        void onShow(int[] inputViewCoordinatesOnScreen);

        void onDismiss();
    }

}
