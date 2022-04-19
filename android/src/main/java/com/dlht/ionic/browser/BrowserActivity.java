package com.dlht.ionic.browser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BrowserActivity extends AppCompatActivity {
    private static final String LIGHT_BACKGROUND_COLOR = "#f7f7f7";
    private static final String DARK_BACKGROUND_COLOR = "#000000";
    private static final String LIGHT_FRONT_COLOR = "#000000";
    private static final String DARK_FRONT_COLOR = "#ffffff";
    private WebView _webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String theme = intent.getStringExtra("theme");

        if (theme == null || theme.isEmpty()) {
            theme = "light";
        }

        // 进度条
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setMax(100);

        // toolbar
        RelativeLayout toolbar = findViewById(R.id.toolbar);
        // title
        TextView title = findViewById(R.id.textView);
        // close
        ImageButton closeButton = findViewById(R.id.closeButton);
        // more
        ImageButton moreButton = findViewById(R.id.moreButton);

        closeButton.setOnClickListener(view -> finish());
        moreButton.setOnClickListener(view -> {
            showMen(view);
        });

        // 主题样式
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        switch (theme) {
            case "dark":
                // 设置黑色背景，白色字体statusBar,白色
                window.setStatusBarColor(Color.parseColor(DARK_BACKGROUND_COLOR));
                // 设置黑色背景，白色文字
                toolbar.setBackgroundColor(Color.parseColor(DARK_BACKGROUND_COLOR));
                // 标题样式
                title.setTextColor(Color.parseColor(DARK_FRONT_COLOR));
                // 设置两个按钮的颜色
                closeButton.setBackground(getResources().getDrawable(R.drawable.ic_baseline_close_dark_24));
                moreButton.setBackground(getResources().getDrawable(R.drawable.ic_baseline_more_vert_dark_24));
                break;
            default:
                // 设置白色背景，黑色字体statusBar
                window.setStatusBarColor(Color.parseColor(LIGHT_BACKGROUND_COLOR));
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                // 设置白色背景，黑色文字
                toolbar.setBackgroundColor(Color.parseColor(LIGHT_BACKGROUND_COLOR));
                // 标题样式
                title.setTextColor(Color.parseColor(LIGHT_FRONT_COLOR));
                // 设置两个按钮的颜色
                closeButton.setBackground(getResources().getDrawable(R.drawable.ic_baseline_close_24));
                moreButton.setBackground(getResources().getDrawable(R.drawable.ic_baseline_more_vert_24));
                break;
        }

        // webView 相关设置
        WebView webView = findViewById(R.id.webView);
        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 8.0.0; SM-G955U Build/R16NW) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Mobile Safari/537.36");

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(100);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 如果不是http开头的，咱不管
                if (!url.startsWith("http")) {
                    return false;
                } else {
                    view.loadUrl(url);
                    return true;
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setProgress(0);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                String _title = view.getTitle();

                if (_title.length() > 10) {
                    _title = _title.substring(0, 9);
                }

                title.setText(_title);
            }
        });

        _webView = webView;
    }

    /**
     * 监听keyDown事件，阻止页面返回
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && _webView != null && _webView.canGoBack()) {
            _webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 弹出popver
     *
     * @param v 需要弹出popver的元素
     */
    private void showMen(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.inflate(R.menu.pop_menu);
        popup.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.options_open) {
                openBySystem();
            }

            if (menuItem.getItemId() == R.id.options_share) {
                shareToWechat();
            }

            return false;
        });
        popup.show();
    }

    /**
     * 使用系统打开
     */
    private void openBySystem() {
        if (_webView != null) {
            String url = _webView.getUrl();
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    /**
     * 分享到微信
     */
    private void shareToWechat() {
        if (_webView != null) {
            String url = _webView.getUrl();
            String title = _webView.getTitle();
            String thumb = bitmapToBase64(_webView.getFavicon());
            BrowserPlugin.shareToWechat(url, title, thumb);
        }
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}