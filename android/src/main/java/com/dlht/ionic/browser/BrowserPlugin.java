package com.dlht.ionic.browser;

import android.content.Intent;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import org.json.JSONException;
import org.json.JSONObject;

@CapacitorPlugin(name = "Browser")
public class BrowserPlugin extends Plugin {
    private static BrowserPlugin plugin;

    @PluginMethod
    public void create(PluginCall call) {
        if (plugin == null) {
            plugin = this;
        }

        String url = call.getString("url");
        Intent intent = new Intent(getContext(), BrowserActivity.class);
        intent.putExtra("url", url);
        getActivity().startActivity(intent);
        call.resolve();
    }

    /**
     * 触发分享到微信事件
     * @param url 访问地址
     * @param title 标题
     * @param thumb 缩略图
     */
    public static void shareToWechat(String url, String title, String thumb) {
        if (plugin != null) {
            JSObject jsonObject = new JSObject();
            jsonObject.put("url", url);
            jsonObject.put("title", title);
            jsonObject.put("thumb", thumb);

            plugin.notifyListeners("shareToWechat", jsonObject);
        }
    }
}
